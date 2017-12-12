package com.blockchain;

import com.blockchain.client.Block;
import com.blockchain.client.Transaction;
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

        KeyPair clientB = KeysUtil.generateKeys();

        LinkedList<Transaction> input = new LinkedList<>();
        input.add(new Transaction("A", "B", 10));

        LinkedList<Transaction> output = new LinkedList<>();
        input.add(new Transaction("B", "1", 1));
        input.add(new Transaction("B", "B", 9));

        Block block = new Block(0, "hash".getBytes(), input, output);
        Assert.assertTrue(block.sign(clientB.getPrivate()));
        block.calcNonce();
        Assert.assertTrue(block.isValid());
        Assert.assertTrue(block.verify(clientB.getPublic()));

        System.out.println(String.format("Time: %.4fsec", (System.currentTimeMillis() - start)/1000.0));

        System.out.println(block.getNonce());
        HashUtil.printHash(block.getHash());
    }


}
