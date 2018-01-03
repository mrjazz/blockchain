package com.blockchain.sandbox.client;

import com.blockchain.client.*;
import com.blockchain.network.*;
import com.blockchain.sandbox.handler.VerifyTransactionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.Consumer;


/**
 * Created by denis on 11/7/2017.
 */
public class SandboxClient implements Client, Runnable, Handler {

    private static Logger logger = LoggerFactory.getLogger(SandboxClient.class);

    private final Network network;
    private final Blockchain blocks;

    private Receiver me;
    private boolean terminated = false;
    private final Customer customer;

    private Block transactionBlock;
    private long transactionStart;

    private final Configuration configuration;

    public static final Customer INITIAL_CUSTOMER = Customer.create("A");
    private static final int INITIAL_AMOUNT = 100;
    private static final int TRANSACTION_TIMEOUT = 200;


    public SandboxClient(Configuration configuration, Customer customer, Network network) {
        this.configuration = configuration;
        this.network = network;
        this.customer = customer;
        this.blocks = new Blockchain(configuration, INITIAL_CUSTOMER, INITIAL_AMOUNT);
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
                return doVerifyWork(((BlockRequest) message).getBlock());
            case FINISH_TRANSACTION:
                return doFinishTransaction(((BlockRequest) message).getBlock());

            default:
                logger.error("Unknown message type:", message);
        }
        return new SimpleResponse(ResponseType.TIMEOUT);
    }

    private Response doFinishTransaction(Block block) {
        try {
            logger.debug("FINISH: {}; {}", customer.toString(), block.toString());
            blocks.submitBlock(transactionBlock);
            logger.debug("SUBMITED: {}; {}", customer.toString(), block.toString());
            return new SimpleResponse(ResponseType.COMMIT_TRANSACTION);
        } catch (InvalidBlock invalidBlock) {
            logger.debug(invalidBlock.getMessage());
            return new SimpleResponse(ResponseType.ROLLBACK_TRANSACTION);
        }
    }

    private boolean validateTransaction(Block block) {
        if (transactionBlock != null && System.currentTimeMillis() < transactionStart + TRANSACTION_TIMEOUT) {
            return false;
        }

        try {
            blocks.validateBlock(block);
        } catch (InvalidBlock invalidBlock) {
            invalidBlock.printStackTrace();
            return false;
        }

        return true;
    }

    private synchronized Response doVerifyWork(Block block) {
        if (!validateTransaction(block)) {
            return new SimpleResponse(ResponseType.TIMEOUT);
        }

        transactionStart = System.currentTimeMillis();
        transactionBlock = block;

        return new SimpleResponse(ResponseType.VERIFIED_WORK);
    }

    private boolean validBlock(Block block) {
        return block.isValid() && Arrays.equals(blocks.getLast().getHash(), block.getPrevHash());
    }

    private void verifyWork(Block block) {
        logger.debug("VERIFY_TRANSACTION PoW for " + getCustomer());
        Consumer<Response> verifyTransaction = new VerifyTransactionHandler(network.getReceiversCount(), (result) -> {
            if (result) {
                logger.debug("PoW verified for " + getCustomer().toString());
                network.broadcastMessageAll(
                        me,
                        new BlockRequest(RequestType.FINISH_TRANSACTION, block),
                        response -> {}
                );
            }
        });
        network.broadcastMessageAll(me, new BlockRequest(RequestType.VERIFY_WORK, block), verifyTransaction);
    }

    private void doWork(Receiver to, Block block) {
        (new Thread(() -> {
            logger.debug(customer + "; Mining started");
            block.calcNonce();
            logger.debug(customer + "; Mining done");
            try {
                blocks.validateBlock(block);
                verifyWork(block);
            } catch (InvalidBlock invalidBlock) {
                logger.debug(invalidBlock.getMessage());
            }
        })).start();
    }

    public int getId() {
        return me.getId();
    }

    public Customer getCustomer() {
        return customer;
    }

    @Override
    public void transfer(CustomerIdentity customerIdentity, int amount) {
        Block block = blocks.createBlock(configuration, customer, customerIdentity, amount);
        network.broadcastMessageAll(me, new BlockRequest(RequestType.START_TRANSACTION, block), (response) -> {

        });
    }

    @Override
    public String toString() {
        return customer.toString();
    }

    public int getBalance() {
        return blocks.balanceFor(customer.getIdentity());
    }

    public void dumpTransactions() {
        // TODO :
        for(Block block : blocks) {
            System.out.println(block);
        }
    }
}
