package com.blockchain.client;

import com.blockchain.util.HashUtil;

import java.io.Serializable;

/**
 * Created by denis on 11/4/2017.
 */
public class Transaction implements Serializable {

    final private String fromId;
    final private String toId;
    final private int amount;

    public Transaction(String fromId, String toId, int amount) {
        this.fromId = fromId;
        this.toId = toId;
        this.amount = amount;
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
