package com.blockchain.network;

import com.blockchain.client.Block;
import com.blockchain.client.Transaction;

/**
 * Created by denis on 11/9/2017.
 */
public class BlockRequest implements Request {

    private RequestType requestType;
    private final Block block;

    public BlockRequest(
            RequestType requestType,
            Block block
    ) {
        this.requestType = requestType;
        this.block = block;
    }

    public Block getBlock() {
        return block;
    }

    @Override
    public RequestType getType() {
        return requestType;
    }

//    public static TransactionRequest of(RequestType requestType, TransactionRequest request) {
//        return new TransactionRequest(
//                requestType,
//                request.getId(),
//                request.getClientId(),
//                request.getAmount(),
//                request.getTimestamp(),
//                request.getPrevHash(),
//                request.getNonce()
//        );
//    }

    @Override
    public String toString() {
        return "block";
//        return String.format("%s -> %s (%s$); %s; %s", fromId, toId, amount, requestType, timestamp);
    }

//    public Transaction getTransaction() {
//        return new Transaction(fromId, toId, amount, timestamp, prevHash, nonce);
//    }

}