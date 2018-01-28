package com.blockchain.web.model;

import com.blockchain.client.Block;
import com.blockchain.client.Hashing;

/**
 * Created by denis on 1/21/2018.
 */
public class BlockDetails {

    private int id;
    private long timestamp;
    private String prevHash;
    private String hash;
    private int nonce;

    private BlockDetails() {
    }

    public int getId() {
        return id;
    }

    public BlockDetails setId(int id) {
        this.id = id;
        return this;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public BlockDetails setTimestamp(long timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public String getPrevHash() {
        return prevHash;
    }

    public BlockDetails setPrevHash(String prevHash) {
        this.prevHash = prevHash;
        return this;
    }

    public String getHash() {
        return hash;
    }

    public BlockDetails setHash(String hash) {
        this.hash = hash;
        return this;
    }

    public int getNonce() {
        return nonce;
    }

    public BlockDetails setNonce(int nonce) {
        this.nonce = nonce;
        return this;
    }

    public static BlockDetails of(Block block) {
        Hashing hashing = new Hashing(2);
        return (new BlockDetails())
                .setId(block.getId())
                .setNonce(block.getNonce())
                .setTimestamp(block.getTimestamp())
                .setPrevHash(hashing.hashToString(block.getPrevHash()))
                .setHash(hashing.hashToString(block.getHash()));
    }

}
