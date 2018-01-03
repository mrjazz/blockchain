package com.blockchain;

import com.blockchain.client.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;

/**
 * Created by denis on 12/12/2017.
 */
public class BlockchainTest {

    private static final Configuration configuration = new Configuration(2);

    private Customer customerA;
    private Customer customerB;

    @Before
    public void init() throws NoSuchAlgorithmException {
        customerA = Customer.create("A");
        customerB = Customer.create("B");
    }

    private Block createBlock(
            int id,
            byte[] prevHash,
            Customer customerFrom,
            CustomerIdentity clientTo,
            int initAmount,
            int transferedAmount
    ) {
        CustomerIdentity clientFrom = customerFrom.getIdentity();

        LinkedList<Transaction> input = new LinkedList<>();
        input.add(new Transaction(new TransactionId(0, 0, customerA.getIdentity()), clientFrom, initAmount));

        LinkedList<Transaction> output = new LinkedList<>();
        output.add(new Transaction(new TransactionId(1, 0, clientFrom), clientTo, transferedAmount));
        output.add(new Transaction(new TransactionId(1, 1, clientFrom), clientFrom, initAmount - transferedAmount));

        Block block = Block.create(configuration, id, prevHash, input, output);
        Assert.assertNotNull(block);
        Assert.assertTrue(block.sign(customerFrom.getPrivateKey()));
        block.calcNonce();
        Assert.assertTrue(block.isValid());
        Assert.assertTrue(block.verify(customerFrom.getPublicKey()));

        return block;
    }

    @Test
    public void testEmptyBlockCreation() {
        Block block = createEmptyBlock(100, customerA.getIdentity());
        Assert.assertTrue(block.isValid());
        Assert.assertTrue(block.verify(customerA.getPublicKey()));
    }

    private Block createEmptyBlock(int amount, CustomerIdentity client) {
        LinkedList<Transaction> input = new LinkedList<>();
        LinkedList<Transaction> output = new LinkedList<>();
        output.add(new Transaction(new TransactionId(0, 0, client), client, amount));

        Block block = Block.create(configuration, 0, "hash".getBytes(), input, output);
        Assert.assertNotNull(block);
        Assert.assertTrue(block.sign(customerA.getPrivateKey()));

        block.calcNonce();
        return block;
    }

    @Test
    public void doTransfer2() {
        CustomerIdentity customerIdA = this.customerA.getIdentity();
        CustomerIdentity customerIdB = this.customerB.getIdentity();

        Blockchain blockchain = new Blockchain(configuration, customerA, 100);
        Assert.assertEquals(100, blockchain.balanceFor(customerIdA));

        Block block = blockchain.createBlock(configuration, customerA, customerIdB, 10);

        Assert.assertFalse("Block should be invalid without nonce", addBlockInBlockchain(blockchain, block));
        block.calcNonce();
        Assert.assertTrue("Block should be valid without nonce", addBlockInBlockchain(blockchain, block));

        Assert.assertEquals(90, blockchain.balanceFor(customerIdA));
        Assert.assertEquals(10, blockchain.balanceFor(customerIdB));

        block = blockchain.createBlock(configuration, customerB, customerIdA, 5);

        Assert.assertFalse("Block should be invalid without nonce", addBlockInBlockchain(blockchain, block));
        block.calcNonce();
        Assert.assertTrue("Block should be valid without nonce", addBlockInBlockchain(blockchain, block));

        Assert.assertEquals(95, blockchain.balanceFor(customerIdA));
        Assert.assertEquals(5, blockchain.balanceFor(customerIdB));
    }

    private boolean addBlockInBlockchain(Blockchain blockchain, Block block) {
        try {
            blockchain.submitBlock(block);
            return true;
        } catch (InvalidBlock invalidBlock) {
            // System.out.println(invalidBlock.getMessage());
            return false;
        }
    }

}