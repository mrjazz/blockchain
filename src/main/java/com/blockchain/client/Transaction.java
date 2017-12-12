package com.blockchain.client;

import com.blockchain.util.HashUtil;

import java.io.Serializable;

/**
 * Created by denis on 11/4/2017.
 */
public class Transaction implements Serializable {

    final private ClientIdentity fromId;
    final private ClientIdentity toId;
    final private int amount;

    public Transaction(ClientIdentity fromId, ClientIdentity toId, int amount) {
        this.fromId = fromId;
        this.toId = toId;
        this.amount = amount;
    }

    public ClientIdentity getFromId() {
        return fromId;
    }

    public ClientIdentity getToId() {
        return toId;
    }

    public int getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return String.format("%s -> %s; %sPTC", fromId.getName(), toId.getName(), amount);
    }
}
