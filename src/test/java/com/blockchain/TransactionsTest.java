package com.blockchain;

import com.blockchain.client.Transaction;
import com.blockchain.network.RequestType;
import com.blockchain.network.SimpleRequest;
import com.blockchain.sandbox.client.SandboxClient;
import com.blockchain.sandbox.network.SandboxNetwork;
import com.blockchain.sandbox.network.SandboxReceiver;
import org.junit.Assert;
import org.junit.Test;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;

/**
 * Created by denis on 11/4/2017.
 */
public class TransactionsTest {

    @Test
    public void testLeaderElection() throws InterruptedException {
        SandboxNetwork network = new SandboxNetwork();
        SandboxClient clientA = new SandboxClient("A", network, null);
        SandboxClient clientB = new SandboxClient("B", network, null);
        SandboxClient clientC = new SandboxClient("C", network, null);

        Thread threadA = new Thread(clientA);
        Thread threadB = new Thread(clientB);
        Thread threadC = new Thread(clientC);

        threadA.start();
        threadB.start();
        threadC.start();

        System.out.println("Started...");

        Thread.sleep(1000);
        network.broadcastMessageAll(new SandboxReceiver(1), new SimpleRequest(RequestType.TERMINATE), (response) -> {});
//        network.sendMessage(null, new SandboxReceiver(1), new SimpleRequest(RequestType.TERMINATE), (response) -> {});

        threadA.join();
        threadB.join();
        threadC.join();

        System.out.println("Finished...");

        String expectedLog = "0 -> 1; VOTE_FOR_LEADER\n" +
                "1 <- 0; ACCEPT_LEADER\n" +
                "0 -> 2; VOTE_FOR_LEADER\n" +
                "2 <- 0; ACCEPT_LEADER\n" +
                "1 -> 0; PING\n" +
                "0 <- 1; PONG";

        Assert.assertEquals(expectedLog, network.getLogs().substring(0, 117));
    }

    @Test
    public void testLeaderReElection() throws InterruptedException {
        SandboxNetwork network = new SandboxNetwork();
        SandboxClient clientA = new SandboxClient("A", network, null);
        SandboxClient clientB = new SandboxClient("B", network, null);
        SandboxClient clientC = new SandboxClient("C", network, null);

        Thread threadA = new Thread(clientA);
        Thread threadB = new Thread(clientB);
        Thread threadC = new Thread(clientC);

        threadA.start();
        threadB.start();
        threadC.start();

        System.out.println("Started...");

        Thread.sleep(1000);

        SandboxClient clientD = new SandboxClient("D", network, null);
        Thread threadD = new Thread(clientD);
        threadD.start();

        network.sendMessage(null, new SandboxReceiver(0), new SimpleRequest(RequestType.TERMINATE), (response) -> {});

        Thread.sleep(1000);

        network.broadcastMessageAll(new SandboxReceiver(1), new SimpleRequest(RequestType.TERMINATE), (response) -> {});
//        network.sendMessage(null, new SandboxReceiver(1), new SimpleRequest(RequestType.TERMINATE), (response) -> {});

        threadA.join();
        threadB.join();
        threadC.join();

        System.out.println("Finished...");
    }

    @Test
    public void testTransaction() throws InterruptedException {
        ArrayList<Transaction> initTransactions = new ArrayList<>();
        initTransactions.add(new Transaction("A", "B", 10, 0, "hash".getBytes(), 1073814698));

        SandboxNetwork network = new SandboxNetwork();
        SandboxClient clientA = new SandboxClient("A", network, initTransactions);
        SandboxClient clientB = new SandboxClient("B", network, initTransactions);
        SandboxClient clientC = new SandboxClient("C", network, initTransactions);

        Thread threadA = new Thread(clientA);
        Thread threadB = new Thread(clientB);
        Thread threadC = new Thread(clientC);

        threadA.start();
        threadB.start();
        threadC.start();

        System.out.println("Started...");

//        Thread.sleep(1000);
//
//        clientB.transactionSend(clientA, 1);
//
//        Thread.sleep(1000);
//
//        clientB.transactionSend(clientA, 2);

        Thread.sleep(1000);

        System.out.println("A: " + clientA.getBalance());
        System.out.println("B: " + clientB.getBalance());
        System.out.println("C: " + clientC.getBalance());

        System.out.println("ClientA:");
        clientA.dumpTransactions();
        System.out.println("ClientB:");
        clientB.dumpTransactions();

        network.broadcastMessageAll(new SandboxReceiver(1), new SimpleRequest(RequestType.TERMINATE), (response) -> {});

        threadA.join();
        threadB.join();
        threadC.join();

        System.out.println("Finished...");
    }

    @Test
    public void transactionsTest() {
        ArrayList<Transaction> initTransactions = new ArrayList<>();
        Transaction t1 = new Transaction("A", "B", 10, 0, "hash".getBytes(), 1073814698);
        Transaction t2 = new Transaction("B", "A", 1, 0, "hash".getBytes(), 2049080861);
        initTransactions.add(t1);
        initTransactions.add(t2);

        SandboxNetwork network = new SandboxNetwork();
        SandboxClient clientA = new SandboxClient("A", network, initTransactions);
        SandboxClient clientB = new SandboxClient("B", network, initTransactions);
        Assert.assertEquals(-9, clientA.getBalance());
        Assert.assertEquals(9, clientB.getBalance());
    }

}