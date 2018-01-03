package com.blockchain.network;

/**
 * Created by denis on 11/7/2017.
 */
public enum ResponseType {

    TIMEOUT,
    PONG,
    TERMINATED,
    ACCEPT_LEADER,
    TRANSACTION_STARTED,
    VERIFYING_WORK,
    VERIFIED_WORK,
    COMMIT_TRANSACTION,
    ROLLBACK_TRANSACTION

}
