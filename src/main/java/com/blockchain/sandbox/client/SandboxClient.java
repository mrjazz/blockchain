package com.blockchain.sandbox.client;

import com.blockchain.client.*;
import com.blockchain.network.*;
import com.blockchain.sandbox.handler.VerifyTransactionHandler;

import java.util.*;
import java.util.function.Consumer;


/**
 * Created by denis on 11/7/2017.
 */
public class SandboxClient implements Client, Runnable, Handler {

    private final Network network;
    private final Blockchain blocks = new Blockchain();

    private Receiver me;
    private boolean terminated = false;
    private final String clientId;

    public SandboxClient(String clientId, Network network, Collection<Block> trLog) {
        this.network = network;
        this.clientId = clientId;
        if (trLog != null) {
            for (Block block : trLog) {
                blocks.add(block);
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

    @Override
    public Response onReceive(Receiver receiver, Request message) {
        switch (message.getType()) {
            case PING:
                return new SimpleResponse(ResponseType.PONG);
            case SYNC_BLOCKS:
                return new SyncResponse(ResponseType.TERMINATED, blocks);
            case TERMINATE:
                terminated = true;
                network.removeReceiver(this);
                return new SimpleResponse(ResponseType.TERMINATED);
            case START_TRANSACTION:
                doWork(receiver, ((BlockRequest) message).getBlock());
                return new SimpleResponse(ResponseType.TRANSACTION_STARTED);
            case VERIFY_WORK:
                if (validBlock(((BlockRequest) message).getBlock())) {
                    return new SimpleResponse(ResponseType.VERIFIED_WORK);
                }
                return new SimpleResponse(ResponseType.TIMEOUT);
            case FINISH_TRANSACTION:
//                Transaction transaction = ((TransactionRequest) message).getTransaction();
//                if (validBlock(transaction)) {
//                    blocks.add(transaction);
//                    return new SimpleResponse(ResponseType.COMMIT_TRANSACTION);
//                }
                return new SimpleResponse(ResponseType.ROLLBACK_TRANSACTION);

            default:
                System.out.println("Unknown message type: " + message);
        }
        return new SimpleResponse(ResponseType.TIMEOUT);
    }

    private boolean validBlock(Block block) {
        return block.isValid() && Arrays.equals(blocks.getLast().getHash(), block.getPrevHash());
    }

    private void commitTransaction(Block block) {
        if (validBlock(block)) {
            blocks.add(block);
            network.broadcastMessage(
                    me,
                    new BlockRequest(RequestType.FINISH_TRANSACTION, block),
                    response -> {}
            );
        } else {
            System.out.println("Invalid block " + block);
        }
    }

    private void verifyWork(Receiver to, Block block) {
        System.out.println("VERIFY_TRANSACTION - " + getClientId());
        Consumer<Response> verifyTransaction = new VerifyTransactionHandler(network.getReceiversCount(), (result) -> {
            if (result) {
                System.out.println("Verified - " + getClientId());
                commitTransaction(block);
            }
        });
        network.broadcastMessage(me, new BlockRequest(RequestType.VERIFY_WORK, block), verifyTransaction);
    }

    private void doWork(Receiver to, Block block) {
        (new Thread(() -> {
            System.out.println("Mining started...");
            block.calcNonce();
            System.out.println("Mining done...");
            if (validBlock(block)) {
                verifyWork(to, block);
            }
        })).start();
    }

    public int getId() {
        return me.getId();
    }

    @Override
    public void transactionSend(Client to, int amount) {
        // TODO : initialize block correctly
        Block transaction = new Block(
//                this.getClientId(),
//                to.getClientId(),
//                amount,
//                System.currentTimeMillis(),
//                blocks.getLast().getHash(),
//                0
        );
        network.broadcastMessageAll(
                me,
                new BlockRequest(
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
        // TODO : implement balance calculation
//        for (Block block : blocks) {
//            if (block.getFromId().equals(clientId)) {
//                balance -= block.getAmount();
//            } else if (block.getToId().equals(clientId)) {
//                balance += block.getAmount();
//            }
//        }
        return balance;
    }

    public void dumpTransactions() {
        // TODO :
        for(Block block : blocks) {
            System.out.println(block);
        }
    }
}
