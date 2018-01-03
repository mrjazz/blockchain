package com.blockchain.client;

import com.blockchain.util.KeysUtil;

import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * Created by denis on 12/15/2017.
 */
public class Customer {

    final private KeyPair keys;
    final private String name;

    private Customer(String name, KeyPair keys) {
        this.name = name;
        this.keys = keys;
    }

    public CustomerIdentity getIdentity() {
        return new CustomerIdentity(name, keys.getPublic());
    }

    public PrivateKey getPrivateKey() {
        return keys.getPrivate();
    }

    public static Customer create(String name) {
        try {
            KeyPair keys = KeysUtil.generateKeys();
            return new Customer(name, keys);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public PublicKey getPublicKey() {
        return keys.getPublic();
    }

    @Override
    public String toString() {
        return "Customer{name=" + name + "}";
    }
}
