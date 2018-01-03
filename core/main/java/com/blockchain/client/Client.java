package com.blockchain.client;

public interface Client {

    Customer getCustomer();
    void transfer(CustomerIdentity customerIdentity, int amount);

}
