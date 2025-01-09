package com.enigma.monitoring;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class EnigmaMetrics {
    private final Counter encryptionCounter;
    private final Counter decryptionCounter;
    private final Counter errorCounter;

    public EnigmaMetrics(MeterRegistry registry) {
        this.encryptionCounter = Counter.builder("enigma.operations")
            .tag("type", "encryption")
            .description("Number of encryption operations")
            .register(registry);
            
        this.decryptionCounter = Counter.builder("enigma.operations")
            .tag("type", "decryption")
            .description("Number of decryption operations")
            .register(registry);
            
        this.errorCounter = Counter.builder("enigma.errors")
            .description("Number of operation errors")
            .register(registry);
    }

    public void incrementEncryption() {
        encryptionCounter.increment();
    }

    public void incrementDecryption() {
        decryptionCounter.increment();
    }

    public void incrementError() {
        errorCounter.increment();
    }
}