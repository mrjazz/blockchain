package com.blockchain.sandbox.network;

import com.blockchain.network.Receiver;

/**
 * Created by denis on 11/7/2017.
 */
public class SandboxReceiver implements Receiver {

    private final int id;

    public SandboxReceiver(int id) {
        this.id = id;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && ((SandboxReceiver)obj).getId() == getId();
    }

    @Override
    public int hashCode() {
        return getId();
    }

    @Override
    public String toString() {
        return String.valueOf(id);
    }

}
