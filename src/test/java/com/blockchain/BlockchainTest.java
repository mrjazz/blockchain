package com.blockchain;

import com.blockchain.client.*;
import com.blockchain.util.HashUtil;
import com.blockchain.util.KeysUtil;
import org.assertj.core.util.Preconditions;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;

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
        input.add(new Transaction(clientA, clientB, initAmount));

        LinkedList<Transaction> output = new LinkedList<>();
        output.add(new Transaction(clientB, clientA, transferedAmount));
        output.add(new Transaction(clientB, clientB, initAmount - transferedAmount));

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
        Assert.assertTrue(block.verify(keysB.getPublic()));
    }

    private Block createEmptyBlock(int amount) {
        LinkedList<Transaction> input = new LinkedList<>();
        LinkedList<Transaction> output = new LinkedList<>();
        output.add(new Transaction(null, clientA, amount));

        Block block = Block.create(0, "hash".getBytes(), input, output);
        Assert.assertNotNull(block);
        Assert.assertTrue(block.sign(keysB.getPrivate()));

        block.calcNonce();
        return block;
    }

}
