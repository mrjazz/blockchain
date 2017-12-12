package com.blockchain.network;

import com.blockchain.client.Blockchain;

/**
 * Created by denis on 11/7/2017.
 */
public class SyncResponse extends SimpleResponse {

    private final Blockchain blocks;

    public SyncResponse(ResponseType responseType, Blockchain blocks) {
        super(responseType);
        this.blocks = blocks;
    }

    public Blockchain getBlocks() {
        return blocks;
    }
}
