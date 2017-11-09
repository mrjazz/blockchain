package com.blockchain.network;

/**
 * Created by denis on 11/7/2017.
 */
public enum RequestType {

    PING,
    VOTE_FOR_LEADER,
    TERMINATE,
    START_TRANSACTION,
    DO_WORK,
    DONE_WORK,
    VERIFY_WORK,
    FINISH_TRANSACTION

}
