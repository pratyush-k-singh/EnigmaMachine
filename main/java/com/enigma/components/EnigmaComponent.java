package com.enigma.components;

import com.enigma.core.EnigmaConfiguration;
import com.enigma.util.SecureRandom;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

/**
 * Abstract base class for Enigma machine components (plugboard, reflector, and rotors).
 * Provides common functionality for character mapping and transformation.
 */
public abstract class EnigmaComponent {
    private static final Logger logger = LogManager.getLogger(EnigmaComponent.class);
    
    protected final SecureRandom random;
    protected final Map<Character, Character> wiring;
    protected final String componentId;
    
    protected EnigmaComponent(String componentId, long seed) {
        this.componentId = componentId;
        this.random = new SecureRandom(seed);
        this.wiring = generateWiring();
        logger.debug("Initialized {} with seed {}", componentId, seed);
    }
    
    /**
     * Generates a bidirectional mapping of characters based on the component's random seed.
     * Each character maps to exactly one other character, creating a reciprocal relationship.
     */
    protected Map<Character, Character> generateWiring() {
        List<Character> charset = new ArrayList<>();
        for (char c = EnigmaConfiguration.CHARSET_START; 
             c <= EnigmaConfiguration.CHARSET_END; c++) {
            charset.add(c);
        }
        
        Map<Character, Character> mapping = new HashMap<>();
        List<Character> available = new ArrayList<>(charset);
        
        while (!available.isEmpty()) {
            char a = available.remove(random.nextInt(available.size()));
            if (available.isEmpty()) {
                mapping.put(a, a); // Self-map the last character if needed
                logger.trace("{}: Self-mapping character {}", componentId, a);
                break;
            }
            char b = available.remove(random.nextInt(available.size()));
            mapping.put(a, b);
            mapping.put(b, a);
            logger.trace("{}: Mapped {} <-> {}", componentId, a, b);
        }
        
        return Collections.unmodifiableMap(mapping);
    }
    
    /**
     * Transforms a character through this component's wiring.
     * @param c The input character
     * @return The transformed character
     */
    public char transform(char c) {
        if (!isValidCharacter(c)) {
            return c;
        }
        char result = wiring.getOrDefault(c, c);
        logger.trace("{}: Transform {} -> {}", componentId, c, result);
        return result;
    }
    
    protected boolean isValidCharacter(char c) {
        return c >= EnigmaConfiguration.CHARSET_START && 
               c <= EnigmaConfiguration.CHARSET_END;
    }
    
    public String getId() {
        return componentId;
    }
    
    protected void logWiringTable() {
        if (logger.isDebugEnabled()) {
            StringBuilder table = new StringBuilder()
                .append("\nWiring table for ")
                .append(componentId)
                .append(":\n");
            wiring.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(e -> table
                    .append(String.format("%c -> %c%n", e.getKey(), e.getValue())));
            logger.debug(table.toString());
        }
    }
}
