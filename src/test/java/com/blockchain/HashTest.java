package com.blockchain;

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

}