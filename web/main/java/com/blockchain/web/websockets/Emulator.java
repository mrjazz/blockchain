package com.blockchain.web.websockets;

import com.blockchain.client.Configuration;
import com.blockchain.client.Customer;
import com.blockchain.sandbox.client.SandboxClient;
import com.blockchain.sandbox.network.SandboxNetwork;
import com.blockchain.web.model.BlockDetails;
import com.blockchain.web.websockets.messages.CustomerProfile;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Created by denis on 1/3/2018.
 */
@Service
public class Emulator {

    private Configuration config = new Configuration(2);

    private Customer customerA = SandboxClient.INITIAL_CUSTOMER;
    private Customer customerB = Customer.create("B");
    private Customer customerC = Customer.create("C");

    private SandboxClient[] clients;

    private SandboxNetwork network = new SandboxNetwork();

    @PostConstruct
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

    public BlockDetails getLatestBlock() {
        return BlockDetails.of(clients[0].getLatestBlock());
    }

    private SandboxClient customerWithPositiveBalance() {
        SandboxClient result;
        do {
            result = clients[randomBelow(clients.length)];
        } while (result.getBalance() <= 1);
        return result;
    }

    public void run() {
        SandboxClient from = customerWithPositiveBalance();
        SandboxClient to = anyClientExcept(from);
        int amount = randomBelow(from.getBalance());
        System.out.println(String.format("Transfer: %s -> %s (%d)", from, to, amount));
        from.transfer(to.getCustomer().getIdentity(), amount);
    }

    public void dumpBalances() {
        for (int j = 0; j < clients.length; j++) {
            System.out.println(clients[j] + " = " + clients[j].getBalance());
        }
    }

    public List<CustomerProfile> getCustomerProfiles() {
        return Arrays.stream(clients)
                .map(c -> new CustomerProfile(c.getCustomer().getName(), c.getBalance()))
                .collect(Collectors.toList());
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

}
