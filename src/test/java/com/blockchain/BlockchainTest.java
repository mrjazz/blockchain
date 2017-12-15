package com.blockchain;

import com.blockchain.client.*;
import com.blockchain.util.KeysUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by denis on 12/12/2017.
 */
public class BlockchainTest {

    private KeyPair keysA;
    private KeyPair keysB;

    private ClientIdentity clientA;
    private ClientIdentity clientB;

    @Before
    public void init() throws NoSuchAlgorithmException {
        keysA = KeysUtil.generateKeys();
        keysB = KeysUtil.generateKeys();
        clientA = new ClientIdentity("A", keysA.getPublic());
        clientB = new ClientIdentity("B", keysB.getPublic());
    }

    @Test
    public void testBlockchain() throws NoSuchAlgorithmException {
        Blockchain blockchain = new Blockchain();
        Block block1 = createEmptyBlock(100);
        Block block2 = createBlock(1, block1.getHash(), clientA, clientB, 100, 10);
        Block block3 = createBlock(2, block1.getHash(), clientB, clientA, 10, 2);

        blockchain.add(block1).add(block2).add(block3);
    }

    private Block createBlock(
            int id,
            byte[] prevHash,
            ClientIdentity clientFrom,
            ClientIdentity clientTo,
            int initAmount,
            int transferedAmount
    ) {
        LinkedList<Transaction> input = new LinkedList<>();
        input.add(new Transaction(new TransactionId(-1, -1, clientA), clientFrom, initAmount));

        LinkedList<Transaction> output = new LinkedList<>();
        output.add(new Transaction(new TransactionId(1, 0, clientFrom), clientTo, transferedAmount));
        output.add(new Transaction(new TransactionId(1, 1, clientFrom), clientFrom, initAmount - transferedAmount));

        Block block = Block.create(id, prevHash, input, output);
        Assert.assertNotNull(block);
        Assert.assertTrue(block.sign(keysB.getPrivate()));
        block.calcNonce();
        Assert.assertTrue(block.isValid());
        Assert.assertTrue(block.verify(keysB.getPublic()));

        return block;
    }

    @Test
    public void testEmptyBlockCreation() {
        Block block = createEmptyBlock(100);
        Assert.assertTrue(block.isValid());
        Assert.assertTrue(block.verify(keysA.getPublic()));
    }

    private Block createEmptyBlock(int amount) {
        LinkedList<Transaction> input = new LinkedList<>();
        LinkedList<Transaction> output = new LinkedList<>();
        output.add(new Transaction(new TransactionId(-1, -1, clientA), clientA, amount));

        Block block = Block.create(0, "hash".getBytes(), input, output);
        Assert.assertNotNull(block);
        Assert.assertTrue(block.sign(keysA.getPrivate()));

        block.calcNonce();
        return block;
    }

    @Test
    public void searchBlockTransactions() {
        Block block0 = createEmptyBlock(100);
        Block block1 = createBlock(1, "hash".getBytes(), clientA, clientB, 100, 10);

        Blockchain blockchain = new Blockchain();
        blockchain.add(block0);
        blockchain.add(block1);

        List<Transaction> trIncome1 = blockchain.incomeTransactionsFor(clientA);
        Assert.assertNotNull(trIncome1);
        Assert.assertEquals(1, trIncome1.size());
        Assert.assertEquals(90, trIncome1.get(0).getAmount());

        List<Transaction> trIncome2 = blockchain.incomeTransactionsFor(clientB);
        Assert.assertNotNull(trIncome2);
        Assert.assertEquals(1, trIncome2.size());
        Assert.assertEquals(10, trIncome2.get(0).getAmount());

//        int balanceA = blockchain.balanceFor(clientA);
//        Assert.assertEquals(90, balanceA);
    }

}
