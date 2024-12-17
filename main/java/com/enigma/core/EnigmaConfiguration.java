package com.enigma.core;

import com.enigma.components.Rotor;
import com.enigma.exception.InvalidConfigurationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

/**
 * Configuration class for the EnigmaMachine.
 * Uses the Builder pattern to create immutable configurations.
 */
public class EnigmaConfiguration {
    private static final Logger logger = LogManager.getLogger(EnigmaConfiguration.class);

    // Character set configuration
    public static final char CHARSET_START = '\u0020';  // Space character
    public static final char CHARSET_END = '\u007E';    // Tilde character
    public static final int CHARSET_SIZE = CHARSET_END - CHARSET_START + 1;
    
    // Rotor configuration
    public static final int MIN_ROTOR_INDEX = 0;
    public static final int MAX_ROTOR_INDEX = CHARSET_SIZE - 1;
    public static final int DEFAULT_START_POSITION = 0;
    public static final int DEFAULT_NOTCH_POSITION = 0;
    
    // Default values
    public static final long DEFAULT_SEED = 42L;
    public static final int MIN_ROTORS = 2;
    public static final int MAX_ROTORS = 12;  // Reasonable upper limit

    // Configuration properties
    private final long plugboardSeed;
    private final long reflectorSeed;
    private final List<Rotor> rotors;
    
    private EnigmaConfiguration(Builder builder) {
        this.plugboardSeed = builder.plugboardSeed;
        this.reflectorSeed = builder.reflectorSeed;
        this.rotors = Collections.unmodifiableList(new ArrayList<>(builder.rotors));
        validateConfiguration();
        logger.info("Created Enigma configuration with {} rotors", rotors.size());
    }

    private void validateConfiguration() {
        if (rotors.size() < MIN_ROTORS) {
            throw new InvalidConfigurationException(
                String.format("At least %d rotors are required", MIN_ROTORS));
        }
        if (rotors.size() > MAX_ROTORS) {
            throw new InvalidConfigurationException(
                String.format("Maximum of %d rotors allowed", MAX_ROTORS));
        }
        if (plugboardSeed <= 0 || reflectorSeed <= 0) {
            throw new InvalidConfigurationException(
                "Seeds must be positive non-zero values");
        }
    }

    // Getters
    public long getPlugboardSeed() { return plugboardSeed; }
    public long getReflectorSeed() { return reflectorSeed; }
    public List<Rotor> getRotors() { return rotors; }

    @Override
    public String toString() {
        return String.format("EnigmaConfiguration[rotors=%d, plugboardSeed=%d, reflectorSeed=%d]",
                           rotors.size(), plugboardSeed, reflectorSeed);
    }

    /**
     * Builder class for creating EnigmaConfiguration instances.
     */
    public static class Builder {
        private long plugboardSeed = DEFAULT_SEED;
        private long reflectorSeed = DEFAULT_SEED;
        private final List<Rotor> rotors = new ArrayList<>();
        
        public Builder plugboardSeed(long seed) {
            this.plugboardSeed = seed;
            return this;
        }
        
        public Builder reflectorSeed(long seed) {
            this.reflectorSeed = seed;
            return this;
        }
        
        public Builder addRotor(long seed, int startPosition, int notchPosition) {
            rotors.add(new Rotor(seed, startPosition, notchPosition));
            return this;
        }
        
        public Builder addRotor(long seed) {
            return addRotor(seed, DEFAULT_START_POSITION, DEFAULT_NOTCH_POSITION);
        }
        
        public EnigmaConfiguration build() {
            return new EnigmaConfiguration(this);
        }
    }
}
