package com.blockchain.client;

import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.function.Consumer;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by denis on 12/12/2017.
 */
public class Blockchain implements Iterable<Block> {

    private final LinkedBlockingDeque<Block> blocks = new LinkedBlockingDeque<Block>();

    public Blockchain add(Block block) {
        checkNotNull(block, "empty block can't be added");
        checkArgument(
                blocks.size() == 0 || block.getId() - blocks.getLast().getId() == 1,
                "added id = " + block.getId() + " after last id"
        );
        checkArgument(block.isValid(), "block should be valid");

        blocks.add(block);
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

    public List<Transaction> searchInputTransactions(ClientIdentity client) {
        // TODO : implement search input transactions
        return null;
    }
}
