package com.blockchain.client;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.function.Consumer;

/**
 * Created by denis on 12/12/2017.
 */
public class Blockchain implements Iterable<Block> {

    private final LinkedBlockingDeque<Block> blocks = new LinkedBlockingDeque<Block>();

    public void add(Block block) {
        blocks.add(block);
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
}
