package com.blockchain.sandbox.client;

import com.blockchain.client.Transaction;
import com.blockchain.client.Client;
import com.blockchain.network.*;
import com.blockchain.sandbox.handler.CommitTransactionHandler;
import com.blockchain.sandbox.handler.VerifyTransactionHandler;
import com.blockchain.sandbox.handler.VoteForLeaderHandler;
import com.blockchain.util.HashUtil;

import java.util.Arrays;
import java.util.Collection;
import java.util.Random;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.function.Consumer;

/**
 * Created by denis on 11/7/2017.
 */
public class SandboxClient implements Client, Runnable, Handler {

    private final Network network;
    private final LinkedBlockingDeque<Transaction> transactions;

    private Receiver me;
    private boolean terminated = false;
    private final String clientId;

    public SandboxClient(String clientId, Network network, Collection<Transaction> trLog) {
        this.network = network;
        this.clientId = clientId;
        this.transactions = new LinkedBlockingDeque<>();
        if (trLog != null) {
            for (Transaction transaction : trLog) {
                transactions.add(transaction);
            }
        }
        network.addReceiver(this);
        me = network.getReceiverByClient(this);
    }

    @Override
    public void run() {
        while (!terminated) {
            try {
                // fixed delay make sense only for test purpose, in reality have to be random value
                int delay = this.getId() * 200;
                // System.out.println(String.format("%s wait %s", getId(), delay));
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // TODO : add timeout for inProgressLeaderVote
            ping();
        }
    }

    private void ping() {
        network.broadcastMessage(me, new SimpleRequest(RequestType.PING), (response) -> {
            if (!response.getType().equals(ResponseType.PONG)) {
                throw new RuntimeException("Invalid response for Ping request");
            }
        });
    }

//    private void voteForLeadership() {
//        Consumer<Response> inProgressLeaderVote = new VoteForLeaderHandler(network.getReceiversCount(), (result) -> {
//            if (result) {
//                System.out.println("Leader selected: " + me);
//                leader = me;
//            }
//        });
//        network.broadcastMessage(me, new SimpleRequest(RequestType.VOTE_FOR_LEADER), inProgressLeaderVote);
//    }

//    @Override
//    public Response onReceive(Receiver receiver, Request message) {
//        Transaction transaction;
//        switch (message.getType()) {
//            case PING:
//                return new SimpleResponse(ResponseType.PONG);
//            case TERMINATE:
//                terminated = true;
//                network.removeReceiver(this);
//                return new SimpleResponse(ResponseType.TERMINATED);
//            case VOTE_FOR_LEADER:
//                if (!me.equals(leader)) {
//                    leader = receiver;
//                    return new SimpleResponse(ResponseType.ACCEPT_LEADER);
//                }
//                return new SimpleResponse(ResponseType.TIMEOUT);
//            case START_TRANSACTION:
//                if (me.equals(leader)) {
//                    network.broadcastMessageAll(
//                            receiver,
//                            TransactionRequest.of(RequestType.DO_WORK, (TransactionRequest) message),
//                            response -> {
//                                //
//                            }
//                    );
//                    return new SimpleResponse(ResponseType.TRANSACTION_STARTED);
//                }
//            case DO_WORK:
//                doWork((TransactionRequest) message);
//                return new SimpleResponse(ResponseType.DOING_WORK);
//            case DONE_WORK:
//                transaction = ((TransactionRequest) message).getTransaction();
//                if (validTransaction(transaction)) {
//                    verifyTransaction(transaction);
//                    return new SimpleResponse(ResponseType.DOING_WORK);
//                }
//                return new SimpleResponse(ResponseType.TIMEOUT);
//            case VERIFY_WORK:
//                transaction = ((TransactionRequest) message).getTransaction();
//                if (validTransaction(transaction)) {
//                    commits.add(transaction);
//                    return new SimpleResponse(ResponseType.VERIFIED_WORK);
//                }
//                return new SimpleResponse(ResponseType.TIMEOUT);
//            case FINISH_TRANSACTION:
//                transaction = ((TransactionRequest) message).getTransaction();
//                if (validTransaction(transaction) && commits.contains(transaction)) {
//                    transactions.add(transaction);
//                    commits.remove(transaction);
//                    return new SimpleResponse(ResponseType.COMMIT_TRANSACTION);
//                }
//                return new SimpleResponse(ResponseType.ROLLBACK_TRANSACTION);
//            default:
//                System.out.println("Unknown message type: " + message);
//        }
//        return new SimpleResponse(ResponseType.TIMEOUT);
//    }

    @Override
    public Response onReceive(Receiver receiver, Request message) {
        switch (message.getType()) {
            case PING:
                return new SimpleResponse(ResponseType.PONG);
            case TERMINATE:
                terminated = true;
                network.removeReceiver(this);
                return new SimpleResponse(ResponseType.TERMINATED);
            case START_TRANSACTION:
                doWork(receiver, ((TransactionRequest) message).getTransaction());
                return new SimpleResponse(ResponseType.TRANSACTION_STARTED);
            case VERIFY_WORK:
                if (validTransaction(((TransactionRequest) message).getTransaction())) {
                    return new SimpleResponse(ResponseType.VERIFIED_WORK);
                }
                return new SimpleResponse(ResponseType.TIMEOUT);
            case FINISH_TRANSACTION:
                Transaction transaction = ((TransactionRequest) message).getTransaction();
                if (validTransaction(transaction)) {
                    transactions.add(transaction);
                    return new SimpleResponse(ResponseType.COMMIT_TRANSACTION);
                }
                return new SimpleResponse(ResponseType.ROLLBACK_TRANSACTION);

            default:
                System.out.println("Unknown message type: " + message);
        }
        return new SimpleResponse(ResponseType.TIMEOUT);
    }

    private boolean validTransaction(Transaction transaction) {
        return transaction.isValid() && Arrays.equals(transactions.getLast().getHash(), transaction.getPrevHash());
    }

    private void commitTransaction(Transaction transaction) {
        if (validTransaction(transaction)) {
            transactions.add(transaction);
            network.broadcastMessage(
                    me,
                    new TransactionRequest(RequestType.FINISH_TRANSACTION, transaction),
                    response -> {}
            );
        } else {
            System.out.println("Invalid transaction " + transaction);
        }
    }

//    private void commitTransaction(Transaction transaction) {
//        Consumer<Response> commitTransaction = new CommitTransactionHandler(network.getReceiversCount(), (result) -> {
//            if (result && commits.contains(transaction)) {
//                System.out.println("Committed");
//                transactions.add(transaction);
//            }
//            commits.remove(transaction);
//        });
//        network.broadcastMessage(
//                me,
//                new TransactionRequest(RequestType.FINISH_TRANSACTION, transaction),
//                commitTransaction
//        );
//    }

    private void verifyWork(Receiver to, Transaction transaction) {
        System.out.println("VERIFY_TRANSACTION - " + getClientId());
        Consumer<Response> verifyTransaction = new VerifyTransactionHandler(network.getReceiversCount(), (result) -> {
            if (result) {
                System.out.println("Verified - " + getClientId());
                commitTransaction(transaction);
            }
        });
        network.broadcastMessage(me, new TransactionRequest(RequestType.VERIFY_WORK, transaction), verifyTransaction);
    }

    private void doWork(Receiver to, Transaction transaction) {
        (new Thread(() -> {
            System.out.println("Mining started...");
            transaction.calcNonce();
            System.out.println("Mining done...");
            if (validTransaction(transaction)) {
                verifyWork(to, transaction);
            }
        })).start();
    }

    public int getId() {
        return me.getId();
    }

    @Override
    public void transactionSend(Client to, int amount) {
        Transaction transaction = new Transaction(
                this.getClientId(),
                to.getClientId(),
                amount,
                System.currentTimeMillis(),
                transactions.getLast().getHash(),
                0
        );
        network.broadcastMessageAll(
                me,
                new TransactionRequest(
                        RequestType.START_TRANSACTION,
                        transaction
                ),
                response -> System.out.println(response)
        );
    }

    @Override
    public String getClientId() {
        return clientId;
    }

    @Override
    public String toString() {
        return clientId;
    }

    public int getBalance() {
        int balance = 0;
        for (Transaction transaction : transactions) {
            if (transaction.getFromId().equals(clientId)) {
                balance -= transaction.getAmount();
            } else if (transaction.getToId().equals(clientId)) {
                balance += transaction.getAmount();
            }
        }
        return balance;
    }

    public void dumpTransactions() {
        for(Transaction transaction : transactions) {
            System.out.println(transaction);
        }
    }
}
