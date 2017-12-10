package com.blockchain;

import org.junit.Test;
import sun.misc.BASE64Encoder;

import java.io.UnsupportedEncodingException;
import java.security.*;

/**
 * Created by denis on 12/10/2017.
 */
public class KeysTest {

    @Test
    public void testKeyCreate() throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException, SignatureException {
        KeyPairGenerator keyPairGen = null; // abstrakcyjna klasa
        keyPairGen = KeyPairGenerator.getInstance("RSA");
        keyPairGen.initialize(2048); // rozmiar klucza
        KeyPair keyPair = keyPairGen.genKeyPair();
        PublicKey pubKey = keyPair.getPublic();
        PrivateKey privKey = keyPair.getPrivate();

        System.out.println(pubKey);
        System.out.println(privKey);


        String message = "Ala ma kota";
        byte[] messageData = message.getBytes("UTF8");
        Signature sig = Signature.getInstance("SHA1WithRSA");
        sig.initSign(privKey);
        sig.update(messageData);
        byte[] signatureBytes = sig.sign();
        System.out.println("Singature:" + new BASE64Encoder().encode(signatureBytes));


//        Signature sig = Signature.getInstance("SHA1WithRSA");
        sig.initVerify(pubKey);
        String message2 = "Ala ma kota";
        byte[] messageData2 = message2.getBytes("UTF8");
        sig.update(messageData2);
        System.out.println(sig.verify(signatureBytes));
    }

}
