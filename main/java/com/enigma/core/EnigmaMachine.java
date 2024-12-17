package com.enigma.core;

import com.enigma.components.Plugboard;
import com.enigma.components.Reflector;
import com.enigma.components.Rotor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Main class implementing the Enigma machine functionality.
 * This modernized version supports an extended character set and
 * configurable number of rotors.
 */
public class EnigmaMachine {
    private static final Logger logger = LogManager.getLogger(EnigmaMachine.class);
    
    private final EnigmaConfiguration config;
    private final Plugboard plugboard;
    private final Reflector reflector;
    private final List<Rotor> rotors;
    
    public EnigmaMachine(EnigmaConfiguration config) {
        logger.debug("Initializing Enigma machine with configuration: {}", config);
        this.config = config;
        this.plugboard = new Plugboard(config.getPlugboardSeed());
        this.reflector = new Reflector(config.getReflectorSeed());
        this.rotors = new ArrayList<>(config.getRotors());
    }
    
    public void reset() {
        logger.debug("Resetting Enigma machine state");
        rotors.forEach(Rotor::reset);
    }
    
    public String encrypt(String message) {
        logger.debug("Encrypting message of length: {}", message.length());
        reset();
        StringBuilder result = new StringBuilder();
        
        for (char c : message.toCharArray()) {
            result.append(encryptChar(c));
        }
        
        return result.toString();
    }
    
    public String decrypt(String message) {
        // Due to the Enigma machine's reciprocal nature, 
        // encryption and decryption are the same operation
        logger.debug("Decrypting message of length: {}", message.length());
        return encrypt(message);
    }
    
    private char encryptChar(char c) {
        if (!isValidCharacter(c)) {
            return c;
        }
        
        // Initial plugboard transformation
        char current = plugboard.transform(c);
        logger.trace("After plugboard (forward): {}", current);
        
        // Forward through rotors
        for (Rotor rotor : rotors) {
            current = rotor.transform(current);
            logger.trace("After rotor {} (forward): {}", rotor.getId(), current);
        }
        
        // Reflector
        current = reflector.transform(current);
        logger.trace("After reflector: {}", current);
        
        // Backward through rotors
        for (int i = rotors.size() - 1; i >= 0; i--) {
            current = rotors.get(i).transformReverse(current);
            logger.trace("After rotor {} (reverse): {}", rotors.get(i).getId(), current);
        }
        
        // Final plugboard transformation
        current = plugboard.transform(current);
        logger.trace("After plugboard (reverse): {}", current);
        
        // Advance rotors
        advanceRotors();
        
        return current;
    }
    
    private void advanceRotors() {
        boolean shouldAdvanceNext = true;
        for (Rotor rotor : rotors) {
            if (shouldAdvanceNext) {
                rotor.rotate();
                shouldAdvanceNext = rotor.shouldAdvanceNext();
            } else {
                break;
            }
        }
    }
    
    private boolean isValidCharacter(char c) {
        return c >= EnigmaConfiguration.CHARSET_START && 
               c <= EnigmaConfiguration.CHARSET_END;
    }
}
