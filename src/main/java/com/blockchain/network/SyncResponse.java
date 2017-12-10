package com.blockchain.network;

import com.blockchain.client.Transaction;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Created by denis on 11/7/2017.
 */
public class SyncResponse extends SimpleResponse {

    private final LinkedBlockingDeque<Transaction> transactions;

    public SyncResponse(ResponseType responseType, LinkedBlockingDeque<Transaction> transactions) {
        super(responseType);
        this.transactions = transactions;
    }

    public LinkedBlockingDeque<Transaction> getTransactions() {
        return transactions;
    }
}
