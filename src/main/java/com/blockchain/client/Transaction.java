package com.blockchain.client;

import com.blockchain.util.HashUtil;

import java.nio.charset.StandardCharsets;

/**
 * Created by denis on 11/4/2017.
 */
public class Transaction {

    private String fromId;
    private String toId;
    private int amount;
    private long timestamp;
    private int nonce;
    private byte[] prevHash;

    public Transaction(String fromId, String toId, int amount, long timestamp, byte[] prevHash, int nonce) {
        this.fromId = fromId;
        this.toId = toId;
        this.amount = amount;
        this.timestamp = timestamp;
        this.prevHash = prevHash;
        this.nonce = nonce;
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

    public long getTimestamp() {
        return timestamp;
    }

    public int getNonce() {
        return nonce;
    }

    public byte[] getHash() {
        return HashUtil.hash(nonce + serialize());
    }

    public boolean isValid() {
        return HashUtil.isValid(nonce + serialize());
    }

    public byte[] getPrevHash() {
        return prevHash;
    }

    private String serialize() {
        return prevHash + fromId + toId + amount + timestamp;
    }

    public void calcNonce() {
        nonce = HashUtil.calcNonce(serialize());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Transaction) {
            return ((Transaction)obj).serialize().equals(serialize());
        }
        return false;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + prevHash.hashCode();
        result = 31 * result + fromId.hashCode();
        result = 31 * result + toId.hashCode();
        result = 31 * result + amount;
        result = (int) (31 * result + timestamp);
        return result;
    }

    @Override
    public String toString() {
        return String.format("%s -> %s; %s$", fromId, toId, amount);
    }
}
