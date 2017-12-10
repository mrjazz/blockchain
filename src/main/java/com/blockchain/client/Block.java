package com.blockchain.client;

import com.blockchain.util.HashUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by denis on 12/10/2017.
 */
public class Block {

    private int id;

    private BlockData blockData;

    private long timestamp;
    private int nonce;
    private byte[] prevHash;
    private byte[] hash;

    public byte[] getHash() {
        return HashUtil.hash(nonce + serialize());
    }

    public boolean isValid() {
        return HashUtil.isValid(nonce + serialize());
    }

    public byte[] getPrevHash() {
        return prevHash;
    }

    private String serialize() {
        return prevHash + fromId + toId + amount + timestamp;
    }

    public void calcNonce() {
        nonce = HashUtil.calcNonce(serialize());
    }


//
//    @Override
//    public int hashCode() {
//        int result = 17;
//        result = 31 * result + prevHash.hashCode();
//        result = 31 * result + fromId.hashCode();
//        result = 31 * result + toId.hashCode();
//        result = 31 * result + amount;
//        result = (int) (31 * result + timestamp);
//        return result;
//    }


}

