package com.blockchain.client;

/**
 * Created by denis on 1/2/2018.
 */
public class InvalidBlock extends Exception {

    public InvalidBlock(String message) {
        super(message);
    }
}
