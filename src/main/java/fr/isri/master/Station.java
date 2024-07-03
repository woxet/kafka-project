package fr.isri.master;

import org.eclipse.microprofile.reactive.messaging.*;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;


import java.util.Random;

import io.smallrye.mutiny.Multi;
import java.time.Duration;

@ApplicationScoped
public class Station {

    @Inject
    @Channel("celsius")
    Emitter<Double> emitter;
    
    private Random random = new Random();

    /**
     * Genere un temperature aleatoire entre 0 et 35 °C
     * Dans le topic celsius-out
     **/
    @Outgoing("celsius-out")
    public Multi<Double> generate() {
        // Stream de temperature celsius random
        // Emet dans celsius-out chaques secondes
        return Multi.createFrom().ticks().every(Duration.ofSeconds(1))
            .map(x -> random.nextDouble()*35);
    }

    /**
     * Consomme dans le topic celsius-in, convertit en fahrenheit, et poste dans le topic fahrenheit.
     **/
    @Incoming("celsius-in")
    @Outgoing("fahrenheit-out")
    public Message<Double> celsiusToFahrenheit(Message<Double> celsius) {
        return celsius.withPayload((celsius.getPayload() * 9/5) + 32);
    }

    /**
     * Consomme et affiche la temperature en fahrenheit
     **/
    @Incoming("fahrenheit-in")
    public void sink(Double fahrenheit) {
        System.out.println(">> " + fahrenheit + " °F");
    }
}
