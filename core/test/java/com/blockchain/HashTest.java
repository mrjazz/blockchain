package com.blockchain;

import com.blockchain.client.*;
import com.blockchain.util.KeysUtil;
import org.junit.Assert;
import org.junit.Test;

import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;


public class HashTest {

    private static final int COMPLEXITY = 2;
    private static final Configuration configuration = new Configuration(COMPLEXITY);
    private Hashing hashing = new Hashing(COMPLEXITY);

    @Test
    public void hashCalculationTest() {
        final String value = "test";
        long start = System.currentTimeMillis();
        int nonce = hashing.calcNonce(value);
        String source = nonce + value;
        Assert.assertTrue(hashing.isValid(source));
        System.out.println(String.format("Time: %.4fsec", (System.currentTimeMillis() - start)/1000.0));
        hashing.printHash(hashing.hash(source));
    }

    @Test
    public void initTransactionTest() throws NoSuchAlgorithmException {
        long start = System.currentTimeMillis();

        KeyPair keysA = KeysUtil.generateKeys();
        KeyPair keysB = KeysUtil.generateKeys();
        CustomerIdentity clientA = new CustomerIdentity("A", keysA.getPublic());
        CustomerIdentity clientB = new CustomerIdentity("B", keysB.getPublic());

        LinkedList<Transaction> input = new LinkedList<>();
        input.add(new Transaction(null, clientB, 10));

        LinkedList<Transaction> output = new LinkedList<>();
        output.add(new Transaction(new TransactionId(0, 0, clientB), clientA, 1));
        output.add(new Transaction(new TransactionId(0, 0, clientB), clientB, 9));

        Block block = Block.create(configuration, 0, "hash".getBytes(), input, output);
        Assert.assertNotNull(block);
        Assert.assertTrue(block.sign(keysB.getPrivate()));
        block.calcNonce();
        Assert.assertTrue(block.isValid());
        Assert.assertTrue(block.verify(keysB.getPublic()));

        System.out.println(String.format("Time: %.4fsec", (System.currentTimeMillis() - start)/1000.0));

        System.out.println(block.getNonce());
        hashing.printHash(block.getHash());
    }


}
