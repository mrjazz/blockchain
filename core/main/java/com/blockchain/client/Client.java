package com.blockchain.client;

public interface Client {

    Customer getCustomer();
    Block getLatestBlock();
    void transfer(CustomerIdentity customerIdentity, int amount);

}
