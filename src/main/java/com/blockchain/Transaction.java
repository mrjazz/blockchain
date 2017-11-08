package com.blockchain;

import com.blockchain.client.Client;

/**
 * Created by denis on 11/4/2017.
 */
public class Transaction {

    private Client from;
    private Client to;
    private int timestamp;
    private String nonce;

    public Transaction(Client from, Client to, int timestamp, String nonce) {
        this.from = from;
        this.to = to;
        this.timestamp = timestamp;
        this.nonce = nonce;
    }

    public Client getFrom() {
        return from;
    }

    public Client getTo() {
        return to;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public String getNonce() {
        return nonce;
    }
}
