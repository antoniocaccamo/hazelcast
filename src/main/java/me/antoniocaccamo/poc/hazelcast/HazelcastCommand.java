package me.antoniocaccamo.poc.hazelcast;

import io.micronaut.configuration.picocli.PicocliRunner;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;


@Command(
        name = "hazelcast", 
        description = "...",
        subcommands={   
            HazelcastConsumerCommand.class    ,
            HazelcastProducerCommand.class
        }, 
        mixinStandardHelpOptions = true
)        
public class HazelcastCommand implements Runnable {

    @Option(names = {"-v", "--verbose"}, description = "...")
    boolean verbose;

    public static void main(String[] args) throws Exception {
        PicocliRunner.run(HazelcastCommand.class, args);
    }

    public void run() {
        // business logic here
        if (verbose) {
            System.out.println("Hi!");
        }

        CommandLine.usage(this, System.out);


    }    

    public static class Constants {
        public static final String Topic = "hazelcast-topic-name";
    }
}


