package com.blockchain.sandbox.network;

import com.blockchain.network.Request;
import com.blockchain.network.RequestType;

/**
 * Created by denis on 11/7/2017.
 */
public class SandboxMessage implements Request {

    private final String message;
    private final RequestType messageType;

    public SandboxMessage(RequestType messageType, String message) {
        this.messageType = messageType;
        this.message = message;
    }

    @Override
    public RequestType getType() {
        return messageType;
    }

    public String getMessage() {
        return message;
    }
}
