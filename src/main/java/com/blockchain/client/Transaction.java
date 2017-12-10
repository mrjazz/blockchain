package com.blockchain.client;

import com.blockchain.util.HashUtil;

/**
 * Created by denis on 11/4/2017.
 */
public class Transaction {

    final private String fromId;
    final private String toId;
    final private int amount;
    final private byte[] signature;

    public Transaction(String fromId, String toId, int amount, byte[] signature) {
        this.fromId = fromId;
        this.toId = toId;
        this.amount = amount;
        this.signature = signature;
    }

    public String getFromId() {
        return fromId;
    }

    public String getToId() {
        return toId;
    }

    public int getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return String.format("%s -> %s; %sPTC", fromId, toId, amount);
    }
}
