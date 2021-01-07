package me.antoniocaccamo.poc.hazelcast;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.topic.ITopic;
import com.hazelcast.topic.Message;
import com.hazelcast.topic.MessageListener;
import lombok.extern.slf4j.Slf4j;
import picocli.CommandLine.Command;

import java.time.LocalDateTime;
import java.util.concurrent.Callable;

@Slf4j
@Command(name = "consumer") 
public class HazelcastConsumerCommand implements Callable<Void> {

    @Override
    public Void call() throws Exception {
        // business logic here

        log.info("Hi from consumer!");

        Runtime.getRuntime().addShutdownHook( new Thread(() -> {
                log.info("shutting down consumer");
                HazelcastClient.shutdownAll();
            })
        );

        HazelcastInstance client = HazelcastClient.newHazelcastClient();
        ITopic topic = client.getTopic(HazelcastCommand.Constants.Topic);
        topic.addMessageListener( new HazelcastCommandMessageListener() );

        return null;
    }

    private static class HazelcastCommandMessageListener implements MessageListener<LocalDateTime> {

        @Override
        public void onMessage(Message<LocalDateTime> message) {
            log.info("@ {} received : {}", message.getPublishTime(),  message.getMessageObject());
        }
    }
    
}
