package com.blockchain.web.websockets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;

@Component
public class ScheduledTasks {

    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Autowired
    private Emulator emulator;

    @Autowired
    private SimpMessageSendingOperations messagingTemplate;


    @Scheduled(fixedRate = 5000)
    public void reportCurrentTime() {
        emulator.dumpBalances();
        emulator.run();
//        log.info("The time is now {}", dateFormat.format(new Date()));
        messagingTemplate.convertAndSend("/topic/customers", emulator.getCustomerProfiles());
        messagingTemplate.convertAndSend("/topic/block", emulator.getLatestBlock());
    }

}