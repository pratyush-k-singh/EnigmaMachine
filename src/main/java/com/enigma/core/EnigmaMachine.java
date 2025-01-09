package com.enigma.core;

import com.enigma.component.Plugboard;
import com.enigma.component.Reflector;
import com.enigma.component.Rotor;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@Component
public class EnigmaMachine {
    private static final Logger logger = LoggerFactory.getLogger(EnigmaMachine.class);
    
    private final Plugboard plugboard;
    private final Reflector reflector;
    private final List<Rotor> rotors;

    public EnigmaMachine(EnigmaConfiguration config) {
        this.plugboard = new Plugboard(config.getPlugboardSeed(), config.getProperties());
        this.reflector = new Reflector(config.getReflectorSeed(), config.getProperties());
        this.rotors = new ArrayList<>(config.getRotors());
        
        logger.info("Initialized Enigma machine with {} rotors", rotors.size());
    }

    public void reset() {
        logger.debug("Resetting Enigma machine state");
        rotors.forEach(Rotor::reset);
    }

    public String encrypt(String message) {
        logger.debug("Encrypting message of length: {}", message.length());
        reset();
        return process(message);
    }

    public String decrypt(String message) {
        logger.debug("Decrypting message of length: {}", message.length());
        reset();
        return process(message);
    }

    private String process(String message) {
        StringBuilder result = new StringBuilder();
        
        for (char c : message.toCharArray()) {
            result.append(processChar(c));
            advanceRotors();
        }
        
        return result.toString();
    }

    private char processChar(char c) {
        // Forward path
        char current = plugboard.transform(c);
        logger.trace("After plugboard (forward): {}", current);
        
        for (Rotor rotor : rotors) {
            current = rotor.transform(current);
            logger.trace("After rotor {} (forward): {}", rotor.getId(), current);
        }
        
        // Reflector
        current = reflector.transform(current);
        logger.trace("After reflector: {}", current);
        
        // Backward path
        for (int i = rotors.size() - 1; i >= 0; i--) {
            current = rotors.get(i).transformReverse(current);
            logger.trace("After rotor {} (reverse): {}", rotors.get(i).getId(), current);
        }
        
        // Final plugboard
        current = plugboard.transform(current);
        logger.trace("After plugboard (reverse): {}", current);
        
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
}
