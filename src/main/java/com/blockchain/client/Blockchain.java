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

    public Blockchain(Customer initialCustomer, int initialAmount) {
        LinkedList<Transaction> input = new LinkedList<>();
        LinkedList<Transaction> output = new LinkedList<>();
        CustomerIdentity initialCustomerIdentity = initialCustomer.getIdentity();
        output.add(new Transaction(new TransactionId(0, 0, initialCustomerIdentity), initialCustomerIdentity, initialAmount));
        Block block = Block.create(0, "hash".getBytes(), input, output);
        block.sign(initialCustomer.getPrivateKey());
        block.calcNonce();
        add(block);
    }

    private Blockchain add(Block block) {
        checkNotNull(block, "empty block can't be added");
        checkArgument(
                blocks.size() == 0 || block.getId() - blocks.getLast().getId() == 1,
                "added id = " + block.getId() + " after last id"
        );
        checkArgument(block.isValid(), "block should be valid");

        blocks.add(block);

        block.getInputTransactions().forEach(tr -> inputTransactions.put(tr.getId(), tr));
        block.getOutputTransactions().forEach(tr -> outputTransactions.put(tr.getId(), tr));

        return this;
    }

    synchronized public void submitBlock(Block block) throws InvalidBlock {
        if (!Arrays.equals(block.getPrevHash(), blocks.getLast().getHash())) {
            throw new InvalidBlock("Block not last");
        }
        if (!block.isValid()) {
            throw new InvalidBlock("Invalid nonce for block");
        }
        add(block);
    }

    public Block createBlock(Customer fromCustomer, CustomerIdentity toCustomerId, int amount) {
        CustomerIdentity fromCustomerId = fromCustomer.getIdentity();
        List<Transaction> input = incomeTransactionsFor(fromCustomerId);
        int fromBalance = sumAmountForTransactions(input);

        checkArgument(fromBalance > amount, String.format("Insufficient funds %d < %d", fromBalance, amount));

        final Block lastBlock = blocks.getLast();
        int blockId = lastBlock.getId() + 1;

        LinkedList<Transaction> output = new LinkedList<>();
        int transferedAmount = 0;
        for (Transaction transaction : input) {
            int shouldTransfer = amount - transferedAmount;
            if (shouldTransfer >= transaction.getAmount()) {
                output.add(new Transaction(
                        new TransactionId(blockId, output.size(), toCustomerId),
                        toCustomerId,
                        transaction.getAmount()
                ));
                transferedAmount += transaction.getAmount();
            } else {
                // transfer on receiver's account
                output.add(new Transaction(
                        new TransactionId(blockId, output.size(), toCustomerId),
                        toCustomerId,
                        shouldTransfer
                ));

                // transfer rest on account of sender
                output.add(new Transaction(
                        new TransactionId(blockId, output.size(), fromCustomerId),
                        transaction.getClientId(),
                        fromBalance - amount
                ));
                break;
            }
        }
        Block block = Block.create(blockId, lastBlock.getHash(), input, output);
        block.sign(fromCustomer.getPrivateKey());
        return block;
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

    public List<Transaction> incomeTransactionsFor(CustomerIdentity customer) {
        return outputTransactions.values().stream()
                .filter(
                        tr -> tr.getClientId().equals(customer)                // exists in input
                        && !inputTransactions.containsKey(tr.getId()) // and doesn exist in output
                )
                .collect(Collectors.toList());
    }

    public int balanceFor(CustomerIdentity customer) {
        return sumAmountForTransactions(incomeTransactionsFor(customer));
    }

    private int sumAmountForTransactions(List<Transaction> transactions) {
        return transactions.stream().mapToInt(cl -> cl.getAmount()).sum();
    }
}
