package com.enigma;

import com.enigma.core.EnigmaConfiguration;
import com.enigma.core.EnigmaMachine;

public class Main {
    public static void main(String[] args) {
        // Create configuration
        EnigmaConfiguration config = new EnigmaConfiguration.Builder()
            .plugboardSeed(123456L)
            .reflectorSeed(789012L)
            .addRotor(345678L, 0, 5)
            .addRotor(901234L, 0, 10)
            .addRotor(567890L, 0, 15)
            .build();

        // Create machine
        EnigmaMachine enigma = new EnigmaMachine(config);

        // Test message
        String message = "Hello, World!";
        System.out.println("Original: " + message);
        
        String encrypted = enigma.encrypt(message);
        System.out.println("Encrypted: " + encrypted);
        
        enigma.reset();
        String decrypted = enigma.decrypt(encrypted);
        System.out.println("Decrypted: " + decrypted);
    }
}