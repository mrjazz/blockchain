package com.blockchain.client;

import com.blockchain.network.Receiver;


public interface Client {

    void transactionSend(Client to, int amount);
    String getClientId();

}
