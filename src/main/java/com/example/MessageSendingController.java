package com.example;

import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessageSendingController {

    private final QueueMessagingTemplate queueMessagingTemplate;

    public MessageSendingController(final QueueMessagingTemplate queueMessagingTemplate) {

        this.queueMessagingTemplate = queueMessagingTemplate;
    }

    @GetMapping("/send")
    public ResponseEntity<String> get(@RequestParam("message") String message) {

        String payload = StringUtils.isEmpty(message) ? "a test message" : message;

        System.out.println("SENDING MESSAGE: " + payload);

        GenericMessage<String> genericMessage = new GenericMessage<>(payload);

        queueMessagingTemplate.send(genericMessage);

        return ResponseEntity.ok("MESSAGE SENT: " + message);
    }


}
