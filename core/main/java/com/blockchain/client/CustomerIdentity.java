package com.blockchain.client;

import java.io.Serializable;
import java.security.PublicKey;

/**
 * Created by denis on 12/12/2017.
 */
public class CustomerIdentity implements Serializable {

    final private String name;
    final private PublicKey publicKey;

    public CustomerIdentity(String name, PublicKey publicKey) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CustomerIdentity that = (CustomerIdentity) o;

        return publicKey.equals(that.publicKey);
    }

    @Override
    public int hashCode() {
        return publicKey.hashCode();
    }
}
