package me.antoniocaccamo.poc.hazelcast;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.topic.ITopic;
import com.hazelcast.topic.Message;
import com.hazelcast.topic.MessageListener;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import picocli.CommandLine.Command;

import java.time.LocalDateTime;

@Slf4j
@Command(name = "consumer") 
public class HazelcastConsumerCommand implements Runnable{



    @SneakyThrows
    public void run() {
        // business logic here
        
        log.info("Hi from consumer!");

        Runtime.getRuntime()
                .addShutdownHook( new Thread(() -> HazelcastClient.shutdownAll()));
        
        HazelcastInstance client = HazelcastClient.newHazelcastClient();
        ITopic topic = client.getTopic(HazelcastCommand.Constants.Topic);
        topic.addMessageListener( new HazelcastCommandMessageListener() );

        for (; ; ) {
            Thread.sleep(1000);
        }

    }
    
    private static class HazelcastCommandMessageListener implements MessageListener<LocalDateTime> {


        @Override
        public void onMessage(Message<LocalDateTime> message) {
            log.info("@ {} received : {}", message.getPublishTime(),  message.getMessageObject());
        }
    }
    
}
