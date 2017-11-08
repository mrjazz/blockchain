package com.blockchain.sandbox.client;

import com.blockchain.Transaction;
import com.blockchain.client.Client;
import com.blockchain.network.*;
import com.blockchain.sandbox.handler.VoteForLeaderHandler;

import java.util.Collection;
import java.util.Random;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.function.Consumer;

/**
 * Created by denis on 11/7/2017.
 */
public class SandboxClient implements Client, Runnable, Handler {

    private final Network network;
    private final Collection<Transaction> transactions;
    private Random random;

    private Receiver leader;
    private Receiver me;
    private boolean terminated = false;

    public SandboxClient(Network network) {
        this.network = network;
        this.transactions = new LinkedBlockingDeque<>();

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
            pingLeader();
        }
    }

    private void pingLeader() {
        if (me.equals(leader)) {
            return;
        }

        if (leader == null) {
            voteForLeadership();
        } else {
            network.sendMessage(me, leader, new SimpleRequest(RequestType.PING), (response) -> {
                if (response.getType().equals(ResponseType.TIMEOUT)) {
                    voteForLeadership();
                } else if (!response.getType().equals(ResponseType.PONG)) {
                    throw new RuntimeException("Invalid response for Ping request");
                }
            });
        }
    }

    private void voteForLeadership() {
        // not thread safe code
        Consumer<Response> inProgressLeaderVote = new VoteForLeaderHandler(network.getReceiversCount(), (result) -> {
            if (result) {
                System.out.println("Leader selected: " + me);
                leader = me;
            }
        });
        network.broadcastMessage(me, new SimpleRequest(RequestType.VOTE_FOR_LEADER), inProgressLeaderVote);
    }

    @Override
    public Response onReceive(Receiver receiver, Request message) {
        switch (message.getType()) {
            case PING:
                return new SimpleResponse(ResponseType.PONG);
            case TERMINATE:
                terminated = true;
                network.removeReceiver(this);
                return new SimpleResponse(ResponseType.TERMINATED);
            case VOTE_FOR_LEADER:
                if (leader != me) {
                    leader = receiver;
                    return new SimpleResponse(ResponseType.ACCEPT_LEADER);
                }
                return new SimpleResponse(ResponseType.TIMEOUT);
            default:
                //
        }
        return new SimpleResponse(ResponseType.TIMEOUT);
    }

    public int getId() {
        return me.getId();
    }

    public boolean isLeader() {
        return me.equals(leader);
    }
}
