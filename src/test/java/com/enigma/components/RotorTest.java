package com.enigma.component;

import com.enigma.config.EnigmaProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RotorTest {
    
    @Mock
    private EnigmaProperties properties;
    
    @Mock
    private EnigmaProperties.CharsetConfig charsetConfig;
    
    private Rotor rotor;
    private static final int CHARSET_SIZE = EnigmaProperties.CharsetConfig.END - 
                                          EnigmaProperties.CharsetConfig.START + 1;

    @BeforeEach
    void setUp() {
        when(properties.getCharset()).thenReturn(charsetConfig);
        when(charsetConfig.getStart()).thenReturn(EnigmaProperties.CharsetConfig.START);
        when(charsetConfig.getEnd()).thenReturn(EnigmaProperties.CharsetConfig.END);
        
        rotor = new Rotor(42L, 0, 5, properties);
    }

    @Test
    void transformationShouldBeReversible() {
        char testChar = 'A';
        char transformed = rotor.transform(testChar);
        char restored = rotor.transformReverse(transformed);
        assertEquals(testChar, restored);
    }

    @Test
    void fullRotationShouldReturnToInitialState() {
        char input = 'A';
        char initial = rotor.transform(input);
        
        for (int i = 0; i < CHARSET_SIZE; i++) {
            rotor.rotate();
        }
        
        assertEquals(initial, rotor.transform(input),
            "Transformation should be the same after full rotation");
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, CHARSET_SIZE + 1})
    void invalidPositionsShouldThrow(int position) {
        assertThrows(InvalidConfigurationException.class, () -> {
            new Rotor(42L, position, 5, properties);
        });
    }

    @Test
    void nonEncryptableCharactersShouldPassThrough() {
        char nonEncryptable = '\u0000';
        assertEquals(nonEncryptable, rotor.transform(nonEncryptable));
    }
}
