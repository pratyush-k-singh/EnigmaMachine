package com.enigma.core;

import com.enigma.exception.InvalidConfigurationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class EnigmaConfigurationTest {

    @Test
    void validConfigurationShouldBuild() {
        assertDoesNotThrow(() -> {
            new EnigmaConfiguration.Builder()
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
            new EnigmaConfiguration.Builder()
                .plugboardSeed(123456L)
                .reflectorSeed(789012L)
                .build();
        });
        assertTrue(exception.getMessage().contains("rotors"));
    }

    @Test
    void singleRotorShouldThrow() {
        Exception exception = assertThrows(InvalidConfigurationException.class, () -> {
            new EnigmaConfiguration.Builder()
                .plugboardSeed(123456L)
                .reflectorSeed(789012L)
                .addRotor(345678L)
                .build();
        });
        assertTrue(exception.getMessage().contains("rotors"));
    }

    @ParameterizedTest
    @ValueSource(ints = {13, 14, 15, 20})
    void tooManyRotorsShouldThrow(int rotorCount) {
        EnigmaConfiguration.Builder builder = new EnigmaConfiguration.Builder()
            .plugboardSeed(123456L)
            .reflectorSeed(789012L);
            
        for (int i = 0; i < rotorCount; i++) {
            builder.addRotor(i + 1);
        }
        
        assertThrows(InvalidConfigurationException.class, builder::build);
    }

    @Test
    void invalidSeedsShouldThrow() {
        assertThrows(InvalidConfigurationException.class, () -> {
            new EnigmaConfiguration.Builder()
                .plugboardSeed(-1)
                .reflectorSeed(789012L)
                .addRotor(345678L)
                .addRotor(901234L)
                .build();
        });

        assertThrows(InvalidConfigurationException.class, () -> {
            new EnigmaConfiguration.Builder()
                .plugboardSeed(123456L)
                .reflectorSeed(0)
                .addRotor(345678L)
                .addRotor(901234L)
                .build();
        });
    }

    @Test
    void configurationShouldBeImmutable() {
        EnigmaConfiguration config = new EnigmaConfiguration.Builder()
            .plugboardSeed(123456L)
            .reflectorSeed(789012L)
            .addRotor(345678L)
            .addRotor(901234L)
            .build();

        assertThrows(UnsupportedOperationException.class, () -> {
            config.getRotors().add(null);
        });
    }
}
