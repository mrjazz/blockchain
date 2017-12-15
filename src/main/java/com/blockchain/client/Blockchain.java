package com.blockchain.client;

import java.util.*;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by denis on 12/12/2017.
 */
public class Blockchain implements Iterable<Block> {

    private final LinkedBlockingDeque<Block> blocks = new LinkedBlockingDeque<Block>();
    private final HashMap<TransactionId, Transaction> inputTransactions = new HashMap<>();
    private final HashMap<TransactionId, Transaction> outputTransactions = new HashMap<>();

    public Blockchain add(Block block) {
        checkNotNull(block, "empty block can't be added");
        checkArgument(
                blocks.size() == 0 || block.getId() - blocks.getLast().getId() == 1,
                "added id = " + block.getId() + " after last id"
        );
        checkArgument(block.isValid(), "block should be valid");

        blocks.add(block);

        block.getInputTransactions().forEach(tr -> inputTransactions.put(tr.getFromTxId(), tr));
        block.getOutputTransactions().forEach(tr -> outputTransactions.put(tr.getFromTxId(), tr));

        return this;
    }

    public Block getLast() {
        return blocks.getLast();
    }

    @Override
    public Iterator<Block> iterator() {
        return blocks.iterator();
    }

    @Override
    public void forEach(Consumer<? super Block> action) {
        blocks.forEach(action);
    }

    @Override
    public Spliterator<Block> spliterator() {
        return blocks.spliterator();
    }

    public List<Transaction> incomeTransactionsFor(ClientIdentity client) {
        return outputTransactions.values().stream()
                .filter(
                        tr -> tr.getClientId().equals(client)                // exists in input
                        && !inputTransactions.containsKey(tr.getFromTxId()) // and doesn exist in output
                )
                .collect(Collectors.toList());
    }

}
