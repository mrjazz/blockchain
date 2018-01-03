package com.blockchain.web;

import com.blockchain.client.Configuration;
import com.blockchain.client.Customer;
import com.blockchain.sandbox.client.SandboxClient;
import com.blockchain.sandbox.network.SandboxNetwork;

import java.util.Arrays;
import java.util.Random;

/**
 * Created by denis on 1/3/2018.
 */
public class Emulator extends Thread {

    private Configuration config = new Configuration(2);

    private Customer customerA = SandboxClient.INITIAL_CUSTOMER;
    private Customer customerB = Customer.create("B");
    private Customer customerC = Customer.create("C");

    private SandboxClient[] clients;

    private SandboxNetwork network = new SandboxNetwork();

    public void init() {
        SandboxClient clientA = new SandboxClient(config, customerA, network);
        SandboxClient clientB = new SandboxClient(config, customerB, network);
        SandboxClient clientC = new SandboxClient(config, customerC, network);

        clients = new SandboxClient[]{clientA, clientB, clientC};

        Thread threadA = new Thread(clientA);
        Thread threadB = new Thread(clientB);
        Thread threadC = new Thread(clientC);

        threadA.start();
        threadB.start();
        threadC.start();
    }

    private SandboxClient customerWithPositiveBalance() {
        return Arrays.stream(clients)
                .filter(c -> c.getBalance() > 0)
                .findFirst()
                .get();
    }

    public void run() {
        try {
            for (int i = 1; i < 10; i++) {
                SandboxClient from = customerWithPositiveBalance();
                SandboxClient to = anyClientExcept(from);
                int amount = randomBelow(from.getBalance());
                System.out.println(String.format("Transfer: %s -> %s (%d)", from, to, amount));
                from.transfer(to.getCustomer().getIdentity(), amount);
                Thread.sleep(8000);
                for (int j = 0; j < clients.length; j++) {
                    System.out.println(clients[j] + " = " + clients[j].getBalance());
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private SandboxClient anyClientExcept(SandboxClient customer) {
        SandboxClient result;
        do {
            result = clients[randomBelow(clients.length)];
        } while (customer.equals(result));
        return result;
    }

    private int randomBelow(int max) {
        return (new Random()).nextInt(max);
    }

    public static void main(String[] args) {
        Emulator thread = new Emulator();
        thread.init();
        thread.start();
    }

}