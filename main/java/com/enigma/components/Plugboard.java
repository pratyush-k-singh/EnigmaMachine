package com.enigma.components;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Implements the plugboard (Steckerbrett) component of the Enigma machine.
 * The plugboard performs character substitutions before and after the rotor transformations.
 */
public class Plugboard extends EnigmaComponent {
    private static final Logger logger = LogManager.getLogger(Plugboard.class);

    public Plugboard(long seed) {
        super("Plugboard", seed);
        logWiringTable();
        logger.info("Plugboard initialized with {} character pairs", wiring.size() / 2);
    }
    
    /**
     * Custom implementation of transform that includes detailed logging.
     */
    @Override
    public char transform(char c) {
        char result = super.transform(c);
        if (result != c) {
            logger.trace("Plugboard swapped {} <-> {}", c, result);
        }
        return result;
    }
    
    /**
     * Returns a string representation of the current plugboard connections.
     */
    public String getConnectionsTable() {
        StringBuilder table = new StringBuilder("Plugboard Connections:\n");
        wiring.entrySet().stream()
            .filter(e -> e.getKey() <= e.getValue()) // Avoid duplicate pairs
            .sorted(Map.Entry.comparingByKey())
            .forEach(e -> table.append(String.format("%c <-> %c%n", 
                                                   e.getKey(), e.getValue())));
        return table.toString();
    }
}
