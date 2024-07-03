package fr.isri.master;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import io.quarkus.test.junit.QuarkusTest;

import org.eclipse.microprofile.reactive.messaging.Message;
import org.junit.jupiter.api.Test;

import jakarta.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
class MyMessagingApplicationTest {

    @Inject
    MyMessagingApplication application;

    @Test
    void test() {
        assertEquals(32.0, application.celsiusToFahrenheit(Message.of(0.0)).getPayload());
        assertEquals(77.0, application.celsiusToFahrenheit(Message.of(25.0)).getPayload());
    }
}
