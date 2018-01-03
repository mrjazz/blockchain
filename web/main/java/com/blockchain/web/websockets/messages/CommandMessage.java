package com.blockchain.web.websockets.messages;

/**
 * Created by denis on 1/3/2018.
 */
public class CommandMessage {

    private String name;

    public CommandMessage() {
    }

    public CommandMessage(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
