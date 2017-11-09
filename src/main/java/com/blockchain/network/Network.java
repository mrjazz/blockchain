package com.blockchain.network;


import java.util.function.Consumer;

public interface Network {

    void sendMessage(Receiver from, Receiver to, Request message, Consumer<Response> response);
    void broadcastMessage(Receiver from, Request message, Consumer<Response> response);
    void addReceiver(Handler handler);
    void removeReceiver(Handler handler);

    void broadcastMessageAll(Receiver from, Request message, Consumer<Response> response);

    Receiver getReceiverByClient(Handler handler);
//    Receiver getReceiverByClientId(String clientId);
    Receiver getNewId();
    int getReceiversCount();

}
