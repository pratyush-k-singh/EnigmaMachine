package com.enigma.core;

import com.enigma.config.EnigmaProperties;
import com.enigma.exception.InvalidConfigurationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EnigmaConfigurationTest {
    @Mock
    private EnigmaProperties properties;
    
    @Mock
    private EnigmaProperties.RotorConfig rotorConfig;

    @BeforeEach
    void setUp() {
        when(properties.getRotors()).thenReturn(rotorConfig);
        when(rotorConfig.getMinRotors()).thenReturn(2);
        when(rotorConfig.getMaxRotors()).thenReturn(12);
    }

    @Test
    void validConfigurationShouldBuild() {
        assertDoesNotThrow(() -> {
            new EnigmaConfiguration.Builder(properties)
                .plugboardSeed(123456L)
                .reflectorSeed(789012L)
                .addRotor(345678L, 0, 5)
                .addRotor(901234L, 0, 10)
                .build();
        });
    }

    @Test
    void configurationWithoutRotorsShouldThrow() {
        Exception exception = assertThrows(InvalidConfigurationException.class, () -> {
            new EnigmaConfiguration.Builder(properties)
                .plugboardSeed(123456L)
                .reflectorSeed(789012L)
                .build();
        });
        assertTrue(exception.getMessage().contains("rotors"));
    }

    @Test
    void singleRotorShouldThrow() {
        Exception exception = assertThrows(InvalidConfigurationException.class, () -> {
            new EnigmaConfiguration.Builder(properties)
                .plugboardSeed(123456L)
                .reflectorSeed(789012L)
                .addRotor(345678L)
                .build();
        });
        assertTrue(exception.getMessage().contains("rotors"));
    }

    @Test
    void invalidSeedsShouldThrow() {
        assertThrows(InvalidConfigurationException.class, () -> {
            new EnigmaConfiguration.Builder(properties)
                .plugboardSeed(-1L)
                .reflectorSeed(789012L)
                .addRotor(345678L)
                .addRotor(901234L)
                .build();
        });
    }
}
