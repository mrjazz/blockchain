package com.blockchain.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by denis on 11/5/2017.
 */
public class HashUtil {

    public static final int COMPLEXITY = 2;

    public static int calcNonce(String body) {
        Integer nonce = Math.toIntExact(Math.round(Math.random() * Integer.MAX_VALUE));
        while (!isValid(nonce.toString() + body)) {
            nonce++;
            if (nonce >= Integer.MAX_VALUE) {
                nonce = 0;
            }
        }

        // printHash(hash(nonce.toString() + body));

        return nonce;
    }

    public static byte[] hash(String s) {
        try {
            byte[] bytesOfMessage = s.getBytes("UTF-8");
            MessageDigest md = MessageDigest.getInstance("MD5");
            return md.digest(bytesOfMessage);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean isValid(byte[] hash) {
        if (hash != null) {
            int counter = 0;
            for (int i = 0; i < hash.length; i++) {
                if (hash[i] != 0) break;
                counter++;
            }
            return counter == COMPLEXITY;
        }
        return false;
    }

    public static boolean isValid(String s) {
        return isValid(hash(s));
    }

    public static void printHash(byte[] hash) {
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < hash.length; i++) {
            output.append(String.format("%02X", hash[i]));
        }
        System.out.println(String.format("Hash: %s", output.toString()));
    }

}
