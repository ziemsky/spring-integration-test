 package com.example;

 import com.amazonaws.services.sqs.AmazonSQSAsync;
 import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
 import org.springframework.context.annotation.Bean;
 import org.springframework.context.annotation.Configuration;
 import org.springframework.integration.annotation.ServiceActivator;
 import org.springframework.integration.aws.inbound.SqsMessageDrivenChannelAdapter;
 import org.springframework.integration.aws.outbound.SqsMessageHandler;
 import org.springframework.integration.channel.QueueChannel;
 import org.springframework.integration.core.MessageProducer;
 import org.springframework.messaging.MessageHandler;
 import org.springframework.messaging.PollableChannel;
 import org.springframework.scheduling.annotation.EnableAsync;
 import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableAsync
@EnableScheduling
public class AppConfig {
    private static final String QUEUE_NAME = "my-aws-sqs-queue-name";


    // INPUT ------------------------------------------------------

//    @Bean
//    public IntegrationFlow sqsQueueReadingFlow() {
//        return IntegrationFlows
//            .from(subscribableChannel())
//            .handle(mySqsMessageHandler())
//            .get();
//    }

    @Bean
    public SqsMessageReader mySqsMessageHandler() {
        return new SqsMessageReader(inputChannel());
    }

    @Bean
    public PollableChannel inputChannel() {
        return new QueueChannel();
    }

    @Bean
    public MessageProducer sqsMessageDrivenChannelAdapter(final AmazonSQSAsync amazonSqs) {
        SqsMessageDrivenChannelAdapter adapter = new SqsMessageDrivenChannelAdapter(amazonSqs, QUEUE_NAME);
        adapter.setOutputChannel(inputChannel());
        return adapter;
    }


    // OUTPUT ----------------------------------------------------
    // these beans allow triggering sending a message
    // to send a message make GET request to http://localhost:8080/send?message=blah

    @Bean
    public QueueMessagingTemplate queueMessagingTemplate(final AmazonSQSAsync amazonSqs) {
        QueueMessagingTemplate queueMessagingTemplate = new QueueMessagingTemplate(amazonSqs);
        queueMessagingTemplate.setDefaultDestinationName(QUEUE_NAME);
        return queueMessagingTemplate;
    }

    @Bean
    @ServiceActivator(inputChannel = QUEUE_NAME)
    public MessageHandler sqsMessageHandler(final QueueMessagingTemplate queueMessagingTemplate) {
        return new SqsMessageHandler(queueMessagingTemplate);
    }

    @Bean
    public MessageSendingController myController(QueueMessagingTemplate queueMessagingTemplate) {
        return new MessageSendingController(queueMessagingTemplate);
    }
}
