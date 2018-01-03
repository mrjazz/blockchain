package com.blockchain;

import com.blockchain.util.KeysUtil;
import org.junit.Assert;
import org.junit.Test;
import sun.misc.BASE64Encoder;

import java.io.UnsupportedEncodingException;
import java.security.*;

/**
 * Created by denis on 12/10/2017.
 */
public class KeysTest {

    @Test
    public void testKeysUtil() throws NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException, SignatureException {
        KeyPair keyPair = KeysUtil.generateKeys();

        Assert.assertNotNull(keyPair);
        Assert.assertNotNull(keyPair.getPrivate());
        Assert.assertNotNull(keyPair.getPublic());

        final String TEST_MESSAGE = "Test message";
        byte[] signature = KeysUtil.sign(TEST_MESSAGE, keyPair.getPrivate());
        Assert.assertNotNull(signature);

        Assert.assertTrue(KeysUtil.verify(TEST_MESSAGE, signature, keyPair.getPublic()));
    }

}