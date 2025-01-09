package com.enigma.core;

import com.enigma.config.EnigmaProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EnigmaMachineTest {
    private EnigmaMachine enigma;
    
    @Mock
    private EnigmaProperties properties;
    
    @Mock
    private EnigmaProperties.RotorConfig rotorConfig;
    
    @Mock
    private EnigmaProperties.CharsetConfig charsetConfig;

    @BeforeEach
    void setUp() {
        when(properties.getRotors()).thenReturn(rotorConfig);
        when(properties.getCharset()).thenReturn(charsetConfig);
        when(rotorConfig.getMinRotors()).thenReturn(2);
        when(rotorConfig.getMaxRotors()).thenReturn(12);
        when(charsetConfig.getStart()).thenReturn(EnigmaProperties.CharsetConfig.START);
        when(charsetConfig.getEnd()).thenReturn(EnigmaProperties.CharsetConfig.END);

        EnigmaConfiguration config = new EnigmaConfiguration.Builder(properties)
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
        String message = "Hello, World!";
        String encrypted = enigma.encrypt(message);
        assertNotEquals(message, encrypted);
        
        enigma.reset();
        String decrypted = enigma.decrypt(encrypted);
        assertEquals(message, decrypted);
    }

    @Test
    void sameSeedShouldProduceSameResult() {
        EnigmaMachine enigma2 = new EnigmaMachine(
            new EnigmaConfiguration.Builder(properties)
                .plugboardSeed(123456L)
                .reflectorSeed(789012L)
                .addRotor(345678L, 0, 5)
                .addRotor(901234L, 0, 10)
                .addRotor(567890L, 0, 15)
                .build()
        );

        String message = "Test Message";
        String encrypted1 = enigma.encrypt(message);
        String encrypted2 = enigma2.encrypt(message);
        assertEquals(encrypted1, encrypted2);
    }

    @Test
    void nonEncryptableCharactersShouldRemainUnchanged() {
        String message = "Hello\n\tWorld!";
        String encrypted = enigma.encrypt(message);
        assertTrue(encrypted.contains("\n"));
        assertTrue(encrypted.contains("\t"));
    }

    @Test
    void emptyStringShouldRemainEmpty() {
        assertEquals("", enigma.encrypt(""));
    }
}
