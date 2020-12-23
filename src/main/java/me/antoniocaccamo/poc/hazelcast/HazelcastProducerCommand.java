package me.antoniocaccamo.poc.hazelcast;

import com.hazelcast.topic.ITopic;
import io.micronaut.configuration.picocli.PicocliRunner;
import io.micronaut.context.ApplicationContext;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;

import lombok.SneakyThrows;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static picocli.CommandLine.Command;

@Slf4j
@Command(name = "producer") 
public class HazelcastProducerCommand implements Runnable {

    @SneakyThrows
    public void run() {
        // business logic here
        log.info("Hi from producer!");
        Runtime.getRuntime()
                .addShutdownHook( new Thread(() -> HazelcastClient.shutdownAll()));
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


        for (; ; ) {
            Thread.sleep(1000);
        }
    }
    
}
