package com.blockchain.network;

/**
 * Created by denis on 11/7/2017.
 */
public class SimpleResponse implements Response {

    private final ResponseType responseType;

    public SimpleResponse(ResponseType responseType) {
        this.responseType = responseType;
    }

    @Override
    public ResponseType getType() {
        return responseType;
    }

    @Override
    public String toString() {
        return responseType.toString();
    }
}
