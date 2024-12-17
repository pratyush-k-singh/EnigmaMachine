package com.enigma.exception;

/**
 * Exception thrown when invalid configuration parameters are provided to the Enigma machine.
 * This includes invalid rotor positions, invalid seeds, or invalid number of rotors.
 */
public class InvalidConfigurationException extends EnigmaException {
    
    /**
     * Constructs a new InvalidConfigurationException with the specified message.
     *
     * @param message The detailed error message
     */
    public InvalidConfigurationException(String message) {
        super(message);
    }

    /**
     * Constructs a new InvalidConfigurationException with the specified message and cause.
     *
     * @param message The detailed error message
     * @param cause The cause of this exception
     */
    public InvalidConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }
    
    /**
     * Creates an exception for an invalid rotor position.
     *
     * @param position The invalid position value
     * @param minValue The minimum allowed value
     * @param maxValue The maximum allowed value
     * @return A new InvalidConfigurationException with a detailed message
     */
    public static InvalidConfigurationException invalidRotorPosition(
            int position, int minValue, int maxValue) {
        return new InvalidConfigurationException(
            String.format("Invalid rotor position %d. Must be between %d and %d",
                        position, minValue, maxValue));
    }
    
    /**
     * Creates an exception for an invalid number of rotors.
     *
     * @param count The actual number of rotors
     * @param required The minimum required number of rotors
     * @return A new InvalidConfigurationException with a detailed message
     */
    public static InvalidConfigurationException invalidRotorCount(
            int count, int required) {
        return new InvalidConfigurationException(
            String.format("Invalid number of rotors: %d. Minimum required: %d",
                        count, required));
    }
    
    /**
     * Creates an exception for an invalid seed value.
     *
     * @param componentName The name of the component (e.g., "plugboard", "rotor")
     * @return A new InvalidConfigurationException with a detailed message
     */
    public static InvalidConfigurationException invalidSeed(String componentName) {
        return new InvalidConfigurationException(
            String.format("Invalid seed for %s. Must be a positive non-zero value",
                        componentName));
    }
}
