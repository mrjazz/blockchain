package com.blockchain;

import com.blockchain.client.Configuration;
import com.blockchain.client.Customer;
import com.blockchain.client.Transaction;
import com.blockchain.network.RequestType;
import com.blockchain.network.SimpleRequest;
import com.blockchain.sandbox.client.SandboxClient;
import com.blockchain.sandbox.network.SandboxNetwork;
import com.blockchain.sandbox.network.SandboxReceiver;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;

/**
 * Created by denis on 11/4/2017.
 */
public class TransactionsTest {

    private static final Configuration config = new Configuration(2);
    private Customer customerA = SandboxClient.INITIAL_CUSTOMER;
    private Customer customerB = Customer.create("B");
    private Customer customerC = Customer.create("C");

    @Test
    public void testTransfer() throws InterruptedException {
        SandboxNetwork network = new SandboxNetwork();
        SandboxClient clientA = new SandboxClient(config, customerA, network);
        SandboxClient clientB = new SandboxClient(config, customerB, network);
        SandboxClient clientC = new SandboxClient(config, customerC, network);

        Thread threadA = new Thread(clientA);
        Thread threadB = new Thread(clientB);
        Thread threadC = new Thread(clientC);

        threadA.start();
        threadB.start();
        threadC.start();

        System.out.println("Started...");

        Assert.assertEquals(100, clientA.getBalance());
        clientA.transfer(customerB.getIdentity(), 10);

        Thread.sleep(8000);
        Assert.assertEquals("A didn't transfer to B", 90, clientA.getBalance());
        Assert.assertEquals("B didn't receive from A", 10, clientB.getBalance());

        network.broadcastMessageAll(new SandboxReceiver(1), new SimpleRequest(RequestType.TERMINATE), (response) -> {});

        Thread.sleep(1000);

        threadA.join();
        threadB.join();
        threadC.join();

        System.out.println("Finished...");
    }

    @Ignore
    @Test
    public void testTransaction() throws InterruptedException {
        // TODO : fix test
//        ArrayList<Transaction> initTransactions = new ArrayList<>();
//        initTransactions.add(new Transaction("A", "B", 10, 0, "hash".getBytes(), 1073814698));
//
//        SandboxNetwork network = new SandboxNetwork();
//        SandboxClient clientA = new SandboxClient("A", network, initTransactions);
//        SandboxClient clientB = new SandboxClient("B", network, initTransactions);
//        SandboxClient clientC = new SandboxClient("C", network, initTransactions);
//
//        Thread threadA = new Thread(clientA);
//        Thread threadB = new Thread(clientB);
//        Thread threadC = new Thread(clientC);
//
//        threadA.start();
//        threadB.start();
//        threadC.start();
//
//        System.out.println("Started...");
//
//        Thread.sleep(1000);
//
//        System.out.println("A: " + clientA.getBalance());
//        System.out.println("B: " + clientB.getBalance());
//        System.out.println("C: " + clientC.getBalance());
//
//        System.out.println("ClientA:");
//        clientA.dumpTransactions();
//        System.out.println("ClientB:");
//        clientB.dumpTransactions();
//
//        network.broadcastMessageAll(new SandboxReceiver(1), new SimpleRequest(RequestType.TERMINATE), (response) -> {});
//
//        threadA.join();
//        threadB.join();
//        threadC.join();
//
//        System.out.println("Finished...");
    }

    @Ignore
    @Test
    public void transactionsTest() {
        // TODO : fix test
//        ArrayList<Transaction> initTransactions = new ArrayList<>();
//        Transaction t1 = new Transaction("A", "B", 10, 0, "hash".getBytes(), 1073814698);
//        Transaction t2 = new Transaction("B", "A", 1, 0, "hash".getBytes(), 2049080861);
//        initTransactions.add(t1);
//        initTransactions.add(t2);
//
//        SandboxNetwork network = new SandboxNetwork();
//        SandboxClient clientA = new SandboxClient("A", network, initTransactions);
//        SandboxClient clientB = new SandboxClient("B", network, initTransactions);
//        Assert.assertEquals(-9, clientA.getBalance());
//        Assert.assertEquals(9, clientB.getBalance());
    }


}