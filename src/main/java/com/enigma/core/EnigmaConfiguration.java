package com.enigma.core;

import com.enigma.component.Rotor;
import com.enigma.config.EnigmaProperties;
import com.enigma.exception.InvalidConfigurationException;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "enigma.machine")
@Getter
public class EnigmaConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(EnigmaConfiguration.class);

    private final long plugboardSeed;
    private final long reflectorSeed;
    private final List<Rotor> rotors;
    private final EnigmaProperties properties;

    private EnigmaConfiguration(Builder builder) {
        this.plugboardSeed = builder.plugboardSeed;
        this.reflectorSeed = builder.reflectorSeed;
        this.rotors = Collections.unmodifiableList(new ArrayList<>(builder.rotors));
        this.properties = builder.properties;
        validateConfiguration();
        logger.info("Created Enigma configuration with {} rotors", rotors.size());
    }

    private void validateConfiguration() {
        if (rotors.size() < properties.getRotors().getMinRotors()) {
            throw new InvalidConfigurationException(
                String.format("At least %d rotors are required", properties.getRotors().getMinRotors()));
        }
        if (rotors.size() > properties.getRotors().getMaxRotors()) {
            throw new InvalidConfigurationException(
                String.format("Maximum of %d rotors allowed", properties.getRotors().getMaxRotors()));
        }
        if (plugboardSeed <= 0 || reflectorSeed <= 0) {
            throw new InvalidConfigurationException("Seeds must be positive non-zero values");
        }
    }

    public static class Builder {
        private long plugboardSeed = 42L;
        private long reflectorSeed = 42L;
        private final List<Rotor> rotors = new ArrayList<>();
        private final EnigmaProperties properties;

        public Builder(EnigmaProperties properties) {
            this.properties = properties;
        }

        public Builder plugboardSeed(long seed) {
            this.plugboardSeed = seed;
            return this;
        }

        public Builder reflectorSeed(long seed) {
            this.reflectorSeed = seed;
            return this;
        }

        public Builder addRotor(long seed, int startPosition, int notchPosition) {
            rotors.add(new Rotor(seed, startPosition, notchPosition, properties));
            return this;
        }

        public Builder addRotor(long seed) {
            return addRotor(seed, 0, 0);
        }

        public EnigmaConfiguration build() {
            return new EnigmaConfiguration(this);
        }
    }
}
