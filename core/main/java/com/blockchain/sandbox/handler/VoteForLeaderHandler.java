package com.blockchain.sandbox.handler;

import com.blockchain.network.Response;
import com.blockchain.network.ResponseType;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

/**
 * Created by denis on 11/7/2017.
 */
public class VoteForLeaderHandler implements Consumer<Response> {

    private final int acceptorsCount;
    private final int minAcceptors;
    private final Consumer<Boolean> callback;
    private AtomicInteger accepted = new AtomicInteger(0);
    private AtomicInteger completed = new AtomicInteger(0);

    public VoteForLeaderHandler(int acceptorsCount, Consumer<Boolean> callback) {
        this.acceptorsCount = acceptorsCount;
        this.callback = callback;
        minAcceptors = (int) Math.ceil(acceptorsCount / 2.0) - 1; // don't include myself
    }

    @Override
    public void accept(Response response) {
        if (response.getType().equals(ResponseType.ACCEPT_LEADER)) {
            accepted.getAndIncrement();
            if (accepted.get() > minAcceptors) {
                callback.accept(true);
                return;
            }
        }
        completed.getAndIncrement();
        if (completed.get() == acceptorsCount) {
            callback.accept(false);
        }
    }

}
