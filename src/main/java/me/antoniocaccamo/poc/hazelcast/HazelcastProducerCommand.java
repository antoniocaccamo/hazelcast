package me.antoniocaccamo.poc.hazelcast;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.topic.ITopic;
import lombok.extern.slf4j.Slf4j;
import picocli.CommandLine.Command;

import java.time.LocalDateTime;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Slf4j
@Command(name = "producer") 
public class HazelcastProducerCommand implements Callable<Void> {

    @Override
    public Void call() throws Exception {
        // business logic here
        log.info("Hi from producer!");
        Runtime.getRuntime().addShutdownHook( new Thread(() -> {
                    log.info("shutting down producer");
                    HazelcastClient.shutdownAll();
                })
        );
        HazelcastInstance client = HazelcastClient.newHazelcastClient();
        ITopic topic = client.getTopic(HazelcastCommand.Constants.Topic);
        Executors.newSingleThreadScheduledExecutor()
            .scheduleWithFixedDelay(() -> {
                        log.info("publishing..");
                        topic.publish(LocalDateTime.now());
                    },
                    1000 ,
                    400,
                    TimeUnit.MILLISECONDS
            );

        return null;
    }
}
