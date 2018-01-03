package com.blockchain.client;

/**
 * Created by denis on 1/3/2018.
 */
public class Configuration {

    private final int complexity;

    public Configuration(int complexity) {
        this.complexity = complexity;
    }

    public int getComplexity() {
        return complexity;
    }
}
