package com.blockchain;

import com.blockchain.network.RequestType;
import com.blockchain.network.SimpleRequest;
import com.blockchain.sandbox.client.SandboxClient;
import com.blockchain.sandbox.network.SandboxNetwork;
import com.blockchain.sandbox.network.SandboxReceiver;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by denis on 11/4/2017.
 */
public class RaftTest {

    @Test
    public void testLeaderElection() throws InterruptedException {
        SandboxNetwork network = new SandboxNetwork();
        SandboxClient clientA = new SandboxClient(network);
        SandboxClient clientB = new SandboxClient(network);
        SandboxClient clientC = new SandboxClient(network);

        Thread threadA = new Thread(clientA);
        Thread threadB = new Thread(clientB);
        Thread threadC = new Thread(clientC);

        threadA.start();
        threadB.start();
        threadC.start();

        System.out.println("Started...");

        Thread.sleep(1000);
        network.broadcastMessage(new SandboxReceiver(1), new SimpleRequest(RequestType.TERMINATE), (response) -> {});
        network.sendMessage(null, new SandboxReceiver(1), new SimpleRequest(RequestType.TERMINATE), (response) -> {});

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
        SandboxClient clientA = new SandboxClient(network);
        SandboxClient clientB = new SandboxClient(network);
        SandboxClient clientC = new SandboxClient(network);

        Thread threadA = new Thread(clientA);
        Thread threadB = new Thread(clientB);
        Thread threadC = new Thread(clientC);

        threadA.start();
        threadB.start();
        threadC.start();

        System.out.println("Started...");

        Thread.sleep(1000);

        SandboxClient clientD = new SandboxClient(network);
        Thread threadD = new Thread(clientD);
        threadD.start();

        network.sendMessage(null, new SandboxReceiver(0), new SimpleRequest(RequestType.TERMINATE), (response) -> {});

        Thread.sleep(1000);

        Assert.assertTrue(clientD.isLeader());

        network.broadcastMessage(new SandboxReceiver(1), new SimpleRequest(RequestType.TERMINATE), (response) -> {});
        network.sendMessage(null, new SandboxReceiver(1), new SimpleRequest(RequestType.TERMINATE), (response) -> {});

        threadA.join();
        threadB.join();
        threadC.join();

        System.out.println("Finished...");


    }


}

