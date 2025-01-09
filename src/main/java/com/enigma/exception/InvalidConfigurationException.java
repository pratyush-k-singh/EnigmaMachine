package com.enigma.exception;

public class InvalidConfigurationException extends EnigmaException {
    
    public InvalidConfigurationException(String message) {
        super(message);
    }

    public InvalidConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public static InvalidConfigurationException invalidRotorPosition(
            int position, int minValue, int maxValue) {
        return new InvalidConfigurationException(
            String.format("Invalid rotor position %d. Must be between %d and %d",
                        position, minValue, maxValue));
    }
    
    public static InvalidConfigurationException invalidRotorCount(
            int count, int required) {
        return new InvalidConfigurationException(
            String.format("Invalid number of rotors: %d. Minimum required: %d",
                        count, required));
    }
    
    public static InvalidConfigurationException invalidSeed(String componentName) {
        return new InvalidConfigurationException(
            String.format("Invalid seed for %s. Must be a positive non-zero value",
                        componentName));
    }
}