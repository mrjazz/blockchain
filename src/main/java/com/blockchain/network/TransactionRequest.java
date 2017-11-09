package com.blockchain.network;

import com.blockchain.client.Transaction;

/**
 * Created by denis on 11/9/2017.
 */
public class TransactionRequest implements Request {

    private RequestType requestType;
    private final String fromId;
    private final String toId;
    private final int amount;
    private final int nonce;
    private final long timestamp;
    private final byte[] prevHash;

    public TransactionRequest(
            RequestType requestType,
            String fromId,
            String toId,
            int amount,
            long timestamp,
            byte[] prevHash,
            int nonce
    ) {
        this.requestType = requestType;
        this.fromId = fromId;
        this.toId = toId;
        this.amount = amount;
        this.timestamp = timestamp;
        this.prevHash = prevHash;
        this.nonce = nonce;
    }

    public TransactionRequest(
            RequestType requestType,
            Transaction transaction
    ) {
        this.requestType = requestType;
        this.fromId = transaction.getFromId();
        this.toId = transaction.getToId();
        this.amount = transaction.getAmount();
        this.timestamp = transaction.getTimestamp();
        this.prevHash = transaction.getPrevHash();
        this.nonce = transaction.getNonce();
    }

    public String getFromId() {
        return fromId;
    }

    public String getToId() {
        return toId;
    }

    public int getNonce() {
        return nonce;
    }

    public int getAmount() {
        return amount;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public byte[] getPrevHash() {
        return prevHash;
    }

    @Override
    public RequestType getType() {
        return requestType;
    }

    public static TransactionRequest of(RequestType requestType, TransactionRequest request) {
        return new TransactionRequest(
                requestType,
                request.getFromId(),
                request.getToId(),
                request.getAmount(),
                request.getTimestamp(),
                request.getPrevHash(),
                request.getNonce()
        );
    }

    @Override
    public String toString() {
        return String.format("%s -> %s (%s$); %s; %s", fromId, toId, amount, requestType, timestamp);
    }

    public Transaction getTransaction() {
        return new Transaction(fromId, toId, amount, timestamp, prevHash, nonce);
    }

}