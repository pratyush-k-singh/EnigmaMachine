package com.enigma.validation;

import com.enigma.config.EnigmaProperties;
import com.enigma.exception.InvalidConfigurationException;
import com.enigma.persistence.entity.MachineConfig;
import org.springframework.stereotype.Component;

@Component
public class ConfigurationValidator {
    private final EnigmaProperties properties;

    public ConfigurationValidator(EnigmaProperties properties) {
        this.properties = properties;
    }

    public void validateConfiguration(MachineConfig config) {
        if (config == null) {
            throw new InvalidConfigurationException("Configuration cannot be null");
        }

        if (config.getPlugboardSeed() <= 0) {
            throw new InvalidConfigurationException("Plugboard seed must be positive");
        }

        if (config.getReflectorSeed() <= 0) {
            throw new InvalidConfigurationException("Reflector seed must be positive");
        }

        if (config.getRotorConfigurations() == null || 
            config.getRotorConfigurations().isEmpty()) {
            throw new InvalidConfigurationException("Rotor configurations cannot be empty");
        }

        int rotorCount = config.getRotorConfigurations().size();
        if (rotorCount < properties.getRotors().getMinRotors() || 
            rotorCount > properties.getRotors().getMaxRotors()) {
            throw new InvalidConfigurationException(
                String.format("Number of rotors must be between %d and %d",
                    properties.getRotors().getMinRotors(),
                    properties.getRotors().getMaxRotors()));
        }

        config.getRotorConfigurations().forEach(rotor -> {
            if (rotor.getSeed() <= 0) {
                throw new InvalidConfigurationException("Rotor seed must be positive");
            }
        });
    }
}
