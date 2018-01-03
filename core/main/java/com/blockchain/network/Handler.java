package com.blockchain.network;

/**
 * Created by denis on 11/7/2017.
 */
public interface Handler {

    Response onReceive(Receiver from, Request message);

}
