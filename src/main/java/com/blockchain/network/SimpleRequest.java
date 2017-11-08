package com.blockchain.network;

/**
 * Created by denis on 11/7/2017.
 */
public class SimpleRequest implements Request {

    private final RequestType messageType;

    public SimpleRequest(RequestType messageType) {
        this.messageType = messageType;
    }

    @Override
    public RequestType getType() {
        return messageType;
    }

    @Override
    public String toString() {
        return messageType.toString();
    }
}
