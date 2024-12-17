package com.enigma.core;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EnigmaMachineTest {
    private EnigmaMachine enigma;
    private static List<String> testMessages;

    @BeforeAll
    static void loadTestData() throws IOException {
        testMessages = Files.readAllLines(
            Path.of("src/test/resources/test-messages.txt"))
            .stream()
            .filter(line -> !line.startsWith("#") && !line.isBlank())
            .toList();
    }

    @BeforeEach
    void setUp() {
        EnigmaConfiguration config = new EnigmaConfiguration.Builder()
            .plugboardSeed(123456L)
            .reflectorSeed(789012L)
            .addRotor(345678L, 0, 5)
            .addRotor(901234L, 0, 10)
            .addRotor(567890L, 0, 15)
            .build();
        enigma = new EnigmaMachine(config);
    }

    @Test
    void encryptionShouldBeReversible() {
        testMessages.forEach(message -> {
            String encrypted = enigma.encrypt(message);
            assertNotEquals(message, encrypted, "Encryption should change the message");
            
            enigma.reset();
            String decrypted = enigma.decrypt(encrypted);
            assertEquals(message, decrypted, "Decryption should restore the original message");
        });
    }

    @Test
    void sameSeedShouldProduceSameResult() {
        EnigmaMachine enigma2 = new EnigmaMachine(
            new EnigmaConfiguration.Builder()
                .plugboardSeed(123456L)
                .reflectorSeed(789012L)
                .addRotor(345678L, 0, 5)
                .addRotor(901234L, 0, 10)
                .addRotor(567890L, 0, 15)
                .build()
        );

        testMessages.forEach(message -> {
            String encrypted1 = enigma.encrypt(message);
            enigma.reset();
            String encrypted2 = enigma2.encrypt(message);
            assertEquals(encrypted1, encrypted2, 
                "Same configuration should produce same encryption");
        });
    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 42L, 999999L})
    void differentSeedsShouldProduceDifferentResults(long seed) {
        EnigmaMachine enigma2 = new EnigmaMachine(
            new EnigmaConfiguration.Builder()
                .plugboardSeed(seed)
                .reflectorSeed(789012L)
                .addRotor(345678L, 0, 5)
                .addRotor(901234L, 0, 10)
                .addRotor(567890L, 0, 15)
                .build()
        );

        testMessages.forEach(message -> {
            String encrypted1 = enigma.encrypt(message);
            enigma.reset();
            String encrypted2 = enigma2.encrypt(message);
            assertNotEquals(encrypted1, encrypted2, 
                "Different configurations should produce different encryption");
        });
    }

    @Test
    void nonEncryptableCharactersShouldRemainUnchanged() {
        String message = "Hello\n\tWorld!";  // Contains non-encryptable characters
        String encrypted = enigma.encrypt(message);
        assertTrue(encrypted.contains("\n"), "Newline should remain unchanged");
        assertTrue(encrypted.contains("\t"), "Tab should remain unchanged");
    }

    @Test
    void emptyStringShouldRemainEmpty() {
        assertEquals("", enigma.encrypt(""), "Empty string should encrypt to empty string");
    }

    @Test
    void longMessageShouldWork() {
        String longMessage = "a".repeat(10000);
        String encrypted = enigma.encrypt(longMessage);
        enigma.reset();
        String decrypted = enigma.decrypt(encrypted);
        assertEquals(longMessage, decrypted, "Long message should be handled correctly");
    }
}
