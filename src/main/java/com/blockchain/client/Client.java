package com.blockchain.client;

import com.blockchain.network.Receiver;


public interface Client {

    void transactionSend(Client to, int amount);

    String getClientId();

//    void startTransaction(Receiver from, Receiver to, );

//    private final String id;
//    private Network network;
//    private LinkedBlockingDeque<Transaction> transactions = new LinkedBlockingDeque<>();
//
//    public Client(Network network, String id) {
//        this.id = id;
//    }
//
//    public String getId() {
//        return id;
//    }
//
//    public void processLastTransaction() {
//
//    }
//
//    @Override
//    public void run() {
////        while (true) {
////            env.getInputTransactions().getLast();
////        }
//    }

}