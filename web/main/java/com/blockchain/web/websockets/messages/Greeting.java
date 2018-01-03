package com.blockchain.web.websockets.messages;

/**
 * Created by denis on 1/3/2018.
 */
public class Greeting {

    private String content;

    public Greeting() {
    }

    public Greeting(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

}