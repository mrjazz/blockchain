package com.blockchain.web.websockets;

import com.blockchain.web.websockets.messages.Greeting;
import com.blockchain.web.websockets.messages.CommandMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

/**
 * Created by denis on 1/3/2018.
 */
@Controller
public class TestController {

    @MessageMapping("/hello")
    @SendTo("/topic/command")
    public Greeting greeting(CommandMessage message) throws Exception {
        return new Greeting("Hello, " + message.getName() + "!");
    }

}