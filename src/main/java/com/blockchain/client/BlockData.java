package com.blockchain.client;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by denis on 12/10/2017.
 */
public class BlockData implements Serializable {

    final private List<Transaction> inputs;
    final private List<Transaction> outputs;

    public BlockData(List<Transaction> inputs, List<Transaction> outputs) {
        this.inputs = inputs;
        this.outputs = outputs;
    }

    public List<Transaction> getInputs() {
        return inputs;
    }

    public List<Transaction> getOutputs() {
        return outputs;
    }

}
