package com.enigma.components;

import com.enigma.core.EnigmaConfiguration;
import com.enigma.exception.InvalidConfigurationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class RotorTest {
    
    @Test
    void rotorShouldInitializeWithValidParameters() {
        assertDoesNotThrow(() -> {
            new Rotor(42L, 0, 5);
        });
    }

    @Test
    void rotorShouldResetToStartPosition() {
        Rotor rotor = new Rotor(42L, 5, 10);
        rotor.rotate(); // Move from initial position
        rotor.rotate();
        rotor.reset();
        assertEquals(5, rotor.getPosition());
    }

    @Test
    void rotorShouldAdvanceNextAtNotchPosition() {
        Rotor rotor = new Rotor(42L, 0, 1);
        assertFalse(rotor.shouldAdvanceNext());
        rotor.rotate(); // Position 1 (notch position)
        assertTrue(rotor.shouldAdvanceNext());
        rotor.rotate(); // Position 2
        assertFalse(rotor.shouldAdvanceNext());
    }

    @Test
    void transformationShouldBeReversible() {
        Rotor rotor = new Rotor(42L, 0, 5);
        for (char c = EnigmaConfiguration.CHARSET_START; 
             c <= EnigmaConfiguration.CHARSET_END; c++) {
            char transformed = rotor.transform(c);
            char restored = rotor.transformReverse(transformed);
            assertEquals(c, restored, 
                "Character should be restored after forward and reverse transformation");
        }
    }

    @Test
    void rotationShouldChangeTransformation() {
        Rotor rotor = new Rotor(42L, 0, 5);
        char input = 'A';
        char first = rotor.transform(input);
        rotor.rotate();
        char second = rotor.transform(input);
        assertNotEquals(first, second, 
            "Transformation should change after rotation");
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, EnigmaConfiguration.CHARSET_SIZE + 1})
    void invalidPositionsShouldThrow(int position) {
        assertThrows(InvalidConfigurationException.class, () -> {
            new Rotor(42L, position, 5);
        });
    }

    @Test
    void nonEncryptableCharactersShouldPassThrough() {
        Rotor rotor = new Rotor(42L, 0, 5);
        char nonEncryptable = '\u0000';
        assertEquals(nonEncryptable, rotor.transform(nonEncryptable));
    }

    @Test
    void fullRotationShouldReturnToInitialState() {
        Rotor rotor = new Rotor(42L, 0, 5);
        char input = 'A';
        char initial = rotor.transform(input);
        
        // Complete one full rotation
        for (int i = 0; i < EnigmaConfiguration.CHARSET_SIZE; i++) {
            rotor.rotate();
        }
        
        assertEquals(initial, rotor.transform(input),
            "Transformation should be the same after full rotation");
    }

    @Test
    void differentSeedsShouldProduceDifferentTransformations() {
        Rotor rotor1 = new Rotor(42L, 0, 5);
        Rotor rotor2 = new Rotor(43L, 0, 5);
        
        char input = 'A';
        assertNotEquals(rotor1.transform(input), rotor2.transform(input),
            "Different seeds should produce different transformations");
    }

    @Test
    void sameSeedShouldProduceSameTransformations() {
        Rotor rotor1 = new Rotor(42L, 0, 5);
        Rotor rotor2 = new Rotor(42L, 0, 5);
        
        char input = 'A';
        assertEquals(rotor1.transform(input), rotor2.transform(input),
            "Same seeds should produce identical transformations");
    }
}
