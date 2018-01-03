package com.blockchain.client;

import java.io.Serializable;

/**
 * Created by denis on 11/4/2017.
 */
public class Transaction implements Serializable {

    final private TransactionId id;
    final private CustomerIdentity clientId;
    final private int amount;

    public Transaction(TransactionId id, CustomerIdentity toId, int amount) {
        this.id = id;
        this.clientId = toId;
        this.amount = amount;
    }

    public TransactionId getId() {
        return id;
    }

    public CustomerIdentity getClientId() {
        return clientId;
    }

    public int getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return String.format("%s -> %s; %sPTC", id.toString(), clientId.getName(), amount);
    }
}
