package com.blockchain.client;

import java.io.Serializable;

/**
 * Created by denis on 11/4/2017.
 */
public class Transaction implements Serializable {

    final private TransactionId fromTxId;
    final private ClientIdentity clientId;
    final private int amount;

    public Transaction(TransactionId fromTxId, ClientIdentity toId, int amount) {
        this.fromTxId = fromTxId;
        this.clientId = toId;
        this.amount = amount;
    }

    public TransactionId getFromTxId() {
        return fromTxId;
    }

    public ClientIdentity getClientId() {
        return clientId;
    }

    public int getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return String.format("%s -> %s; %sPTC", fromTxId.toString(), clientId.getName(), amount);
    }
}
