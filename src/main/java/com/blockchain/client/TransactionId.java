package com.blockchain.client;

import java.io.Serializable;

/**
 * Created by denis on 12/15/2017.
 */
public class TransactionId implements Serializable {

    final private int blockId;
    final private int transactionOutId;
    final private ClientIdentity client;

    public TransactionId(int blockId, int transactionOutId, ClientIdentity client) {
        this.blockId = blockId;
        this.transactionOutId = transactionOutId;
        this.client = client;
    }

    public ClientIdentity getClient() {
        return client;
    }

    public int getBlockId() {
        return blockId;
    }

    public int getTransactionOutId() {
        return transactionOutId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TransactionId that = (TransactionId) o;

        if (blockId != that.blockId) return false;
        return transactionOutId == that.transactionOutId;

    }

    @Override
    public int hashCode() {
        int result = blockId;
        result = 31 * result + transactionOutId;
        return result;
    }

    @Override
    public String toString() {
        return String.format("Tr(%d, %d)", blockId, transactionOutId);
    }
}
