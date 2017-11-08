package com.blockchain.network;


import java.util.function.Consumer;

public interface Network {

    void sendMessage(Receiver from, Receiver to, Request message, Consumer<Response> response);
    void broadcastMessage(Receiver from, Request message, Consumer<Response> response);
    void addReceiver(Handler handler);
    void removeReceiver(Handler handler);

    Receiver getReceiverByClient(Handler handler);
    Receiver getNewId();
    int getReceiversCount();

}
