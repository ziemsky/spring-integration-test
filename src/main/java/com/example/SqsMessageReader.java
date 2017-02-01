package com.example;


import org.springframework.messaging.Message;
import org.springframework.messaging.PollableChannel;
import org.springframework.scheduling.annotation.Scheduled;

public class SqsMessageReader {


    private final PollableChannel pollableChannel;

    public SqsMessageReader(final PollableChannel pollableChannel) {

        this.pollableChannel = pollableChannel;
    }

    @Scheduled(fixedRate=3000)
    public void myScheduledMethod() {

        System.out.println("READING MESSAGE");

        Message<?> receive = pollableChannel.receive();

        System.out.println("RECEIVED MESSAGE: " + receive.getPayload());
    }

}
