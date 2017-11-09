package com.blockchain.sandbox.network;

import com.blockchain.client.Client;
import com.blockchain.network.*;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

/**
 * Created by denis on 11/7/2017.
 */
public class SandboxNetwork implements Network {

    private AtomicInteger ids = new AtomicInteger(0);
    private Map<Integer, Handler> handlers;
    private StringBuilder logHistory = new StringBuilder();

    public SandboxNetwork() {
        this.handlers = new HashMap<>();
    }

    @Override
    public void sendMessage(Receiver from, Receiver to, Request message, Consumer<Response> callback) {
//        if (to == null || to.equals(from)) {
//            throw new RuntimeException(String.format("Message is wrong: %s -> %s; %s", from, to, message));
//        }

        String logMessage = String.format("%s -> %s; %s\n", from, to, message);
        if (!message.getType().equals(RequestType.PING)) {
            log(logMessage);
        }

        Response response = getResponse(from, to, message, callback);
        logMessage = String.format("%s <- %s; %s\n", to, from, response);
        if (!message.getType().equals(RequestType.PING)) {
            log(logMessage);
        }

        callback.accept(response);
    }

    private void log(String logMessage) {
        logHistory.append(logMessage);
        System.out.print(logMessage);
    }

    private Response getResponse(Receiver from, Receiver to, Request message, Consumer<Response> response) {
        if (to == null) {
            return new SimpleResponse(ResponseType.TIMEOUT);
        }

        Handler handler = handlers.get(to.getId());
        if (handler == null) {
            return new SimpleResponse(ResponseType.TIMEOUT);
        }

        return handler.onReceive(from, message);
    }

    @Override
    public void broadcastMessage(Receiver from, Request message, Consumer<Response> response) {
        for (Handler handler : handlers.values()) {
            Receiver receiver = getReceiverByClient(handler);
            if (!receiver.equals(from)) {
                sendMessage(from, receiver, message, response);
            }
        }
    }

    @Override
    public void broadcastMessageAll(Receiver from, Request message, Consumer<Response> response) {
        broadcastMessage(from, message, response);
        sendMessage(null, from, message, response);
    }

    @Override
    public Receiver getReceiverByClient(Handler handler) {
        for (int i = 0; i < handlers.size(); i++) {
            if (handlers.get(i).equals(handler)) {
                return new SandboxReceiver(i);
            }
        }
        return null;
    }

//    @Override
//    public Receiver getReceiverByClientId(String clientId) {
//        for (int i = 0; i < handlers.size(); i++) {
//            if (((Client)handlers).getClientId().equals(clientId)) {
//                return new SandboxReceiver(i);
//            }
//        }
//        return null;
//    }

    @Override
    public void addReceiver(Handler handler) {
        handlers.put(ids.getAndIncrement(), handler);
    }

    @Override
    public void removeReceiver(Handler handler) {
        handlers.remove(handler);
    }

    @Override
    public Receiver getNewId() {
        return new SandboxReceiver(handlers.size());
    }

    @Override
    public int getReceiversCount() {
        return handlers.size();
    }

    public String getLogs() {
        return logHistory.toString();
    }
}
