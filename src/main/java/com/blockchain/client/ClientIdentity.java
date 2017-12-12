package com.blockchain.client;

import java.io.Serializable;
import java.security.PublicKey;

/**
 * Created by denis on 12/12/2017.
 */
public class ClientIdentity implements Serializable {

    final private String name;
    final private PublicKey publicKey;

    public ClientIdentity(String name, PublicKey publicKey) {
        this.name = name;
        this.publicKey = publicKey;
    }

    public String getName() {
        return name;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    @Override
    public String toString() {
        return "@" + name;
    }
}
