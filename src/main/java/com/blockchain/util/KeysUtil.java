package com.blockchain.util;

import sun.misc.BASE64Encoder;

import java.io.UnsupportedEncodingException;
import java.security.*;

/**
 * Created by denis on 12/12/2017.
 */
public class KeysUtil {

    public static KeyPair generateKeys() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        keyPairGen.initialize(2048);
        return keyPairGen.genKeyPair();
    }

    public static byte[] sign(String message, PrivateKey privateKey) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        byte[] messageData = message.getBytes("UTF8");
        Signature sig = Signature.getInstance("SHA1WithRSA");
        sig.initSign(privateKey);
        sig.update(messageData);
        byte[] signature = sig.sign();
        return signature;
//        return new BASE64Encoder().encode(signature);
    }

    public static boolean verify(String message, byte[] signature, PublicKey pubKey) throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException, SignatureException {
        Signature sig = Signature.getInstance("SHA1WithRSA");
        sig.initVerify(pubKey);
        byte[] messageData = message.getBytes("UTF8");
        sig.update(messageData);
        return sig.verify(signature);
    }

}
