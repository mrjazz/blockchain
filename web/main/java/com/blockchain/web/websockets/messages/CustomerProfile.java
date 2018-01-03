package com.blockchain.web.websockets.messages;

/**
 * Created by denis on 1/4/2018.
 */
public class CustomerProfile {

    String name;
    int amount;

    public CustomerProfile() {}

    public CustomerProfile(String name, int amount) {
        this.name = name;
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public int getAmount() {
        return amount;
    }
}
