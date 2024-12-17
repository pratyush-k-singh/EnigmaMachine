package com.enigma.components;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Implements the reflector (Umkehrwalze) component of the Enigma machine.
 * The reflector ensures the reciprocal nature of the encryption by reflecting
 * the signal back through the rotors in reverse order.
 */
public class Reflector extends EnigmaComponent {
    private static final Logger logger = LogManager.getLogger(Reflector.class);

    public Reflector(long seed) {
        super("Reflector", seed);
        validateReflectorWiring();
        logWiringTable();
        logger.info("Reflector initialized with {} character pairs", wiring.size() / 2);
    }
    
    /**
     * Validates that the reflector wiring is properly reciprocal.
     * Each character must map to a different character, and if A maps to B,
     * then B must map to A.
     */
    private void validateReflectorWiring() {
        for (var entry : wiring.entrySet()) {
            char a = entry.getKey();
            char b = entry.getValue();
            
            if (a == b) {
                logger.warn("Reflector has self-mapping for character: {}", a);
            }
            
            if (!wiring.containsKey(b) || wiring.get(b) != a) {
                logger.error("Invalid reflector wiring: {} -> {} but {} -> {}", 
                           a, b, b, wiring.get(b));
                throw new IllegalStateException(
                    "Reflector wiring is not properly reciprocal");
            }
        }
    }
    
    /**
     * Custom implementation of transform that includes detailed logging
     * and ensures the reciprocal nature of the reflector.
     */
    @Override
    public char transform(char c) {
        char result = super.transform(c);
        if (result == c) {
            logger.warn("Reflector encountered self-mapping for character: {}", c);
        } else {
            logger.trace("Reflector mapped {} <-> {}", c, result);
        }
        return result;
    }
    
    /**
     * Returns a string representation of the current reflector mappings.
     */
    public String getMappingTable() {
        StringBuilder table = new StringBuilder("Reflector Mappings:\n");
        wiring.entrySet().stream()
            .filter(e -> e.getKey() <= e.getValue()) // Avoid duplicate pairs
            .sorted(Map.Entry.comparingByKey())
            .forEach(e -> table.append(String.format("%c <-> %c%n", 
                                                   e.getKey(), e.getValue())));
        return table.toString();
    }
}
