package com.blockchain;

import com.blockchain.client.Block;
import com.blockchain.client.ClientIdentity;
import com.blockchain.client.Transaction;
import com.blockchain.client.TransactionId;
import com.blockchain.util.HashUtil;
import com.blockchain.util.KeysUtil;
import org.junit.Assert;
import org.junit.Test;

import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;


public class HashTest {

    @Test
    public void hashCalculationTest() {
        final String value = "test";
        long start = System.currentTimeMillis();
        int nonce = HashUtil.calcNonce(value);
        String source = nonce + value;
        Assert.assertTrue(HashUtil.isValid(source));
        System.out.println(String.format("Time: %.4fsec", (System.currentTimeMillis() - start)/1000.0));
        HashUtil.printHash(HashUtil.hash(source));
    }

    @Test
    public void initTransactionTest() throws NoSuchAlgorithmException {
        long start = System.currentTimeMillis();

        KeyPair keysA = KeysUtil.generateKeys();
        KeyPair keysB = KeysUtil.generateKeys();
        ClientIdentity clientA = new ClientIdentity("A", keysA.getPublic());
        ClientIdentity clientB = new ClientIdentity("B", keysB.getPublic());

        LinkedList<Transaction> input = new LinkedList<>();
        input.add(new Transaction(null, clientB, 10));

        LinkedList<Transaction> output = new LinkedList<>();
        output.add(new Transaction(new TransactionId(0, 0, clientB), clientA, 1));
        output.add(new Transaction(new TransactionId(0, 0, clientB), clientB, 9));

        Block block = Block.create(0, "hash".getBytes(), input, output);
        Assert.assertNotNull(block);
        Assert.assertTrue(block.sign(keysB.getPrivate()));
        block.calcNonce();
        Assert.assertTrue(block.isValid());
        Assert.assertTrue(block.verify(keysB.getPublic()));

        System.out.println(String.format("Time: %.4fsec", (System.currentTimeMillis() - start)/1000.0));

        System.out.println(block.getNonce());
        HashUtil.printHash(block.getHash());
    }


}
