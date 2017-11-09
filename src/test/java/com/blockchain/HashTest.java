package com.blockchain;

import com.blockchain.client.Transaction;
import com.blockchain.network.TransactionRequest;
import com.blockchain.util.HashUtil;
import org.junit.Assert;
import org.junit.Test;


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
    public void transactionHashTest() {
        long start = System.currentTimeMillis();
        Transaction transaction = new Transaction("A", "B", 10, 1, "hash".getBytes(), 0);

        transaction.calcNonce();
        System.out.println(transaction.getNonce());

        Assert.assertTrue(transaction.isValid());
        HashUtil.printHash(transaction.getHash());
        System.out.println(String.format("Time: %.4fsec", (System.currentTimeMillis() - start)/1000.0));
    }

    @Test
    public void initTransactionTest() {
        Transaction transaction = new Transaction("A", "B", 10, 0, "hash".getBytes(), 1073814698);
        transaction.calcNonce();
        Assert.assertTrue(transaction.isValid());
        System.out.println(transaction.getNonce());
    }


}