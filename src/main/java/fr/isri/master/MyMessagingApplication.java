package fr.isri.master;

import org.eclipse.microprofile.reactive.messaging.*;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;


import java.util.Random;

import io.smallrye.mutiny.Multi;
import java.time.Duration;

@ApplicationScoped
public class MyMessagingApplication {

    @Inject
    @Channel("celsius-out")
    Emitter<Double> emitter;
    
    private Random random = new Random();

    /**
     * Sends message to the "words-out" channel, can be used from a JAX-RS resource or any bean of your application.
     * Messages are sent to the broker.
     **/
    @Outgoing("celsius-out")
    public Multi<Double> generate() {
        // Build an infinite stream of random prices
        // It emits a price every second
        return Multi.createFrom().ticks().every(Duration.ofMillis(1000))
            .map(x -> random.nextDouble()*35);
    }

    /**
     * Consume the message from the "words-in" channel, uppercase it and send it to the uppercase channel.
     * Messages come from the broker.
     **/
    @Incoming("celsius-in")
    @Outgoing("fahrenheit-out")
    public Message<Double> celsiusToFahrenheit(Message<Double> celsius) {
        return celsius.withPayload((celsius.getPayload() * 9/5) + 32);
    }

    /**
     * Consume the uppercase channel (in-memory) and print the messages.
     **/
    @Incoming("fahrenheit-in")
    public void sink(Double fahrenheit) {
        System.out.println(">> " + fahrenheit);
    }
}
