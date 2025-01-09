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
class PlugboardTest {
    @Mock
    private EnigmaProperties properties;
    
    @Mock
    private EnigmaProperties.CharsetConfig charsetConfig;
    
    private Plugboard plugboard;

    @BeforeEach
    void setUp() {
        when(properties.getCharset()).thenReturn(charsetConfig);
        // Important: Mock these methods to return the actual values
        when(charsetConfig.getStart()).thenReturn(EnigmaProperties.CharsetConfig.START);
        when(charsetConfig.getEnd()).thenReturn(EnigmaProperties.CharsetConfig.END);
        
        plugboard = new Plugboard(42L, properties);
    }

    @Test
    void transformationShouldBeSymmetric() {
        char testChar = 'A';
        char transformed = plugboard.transform(testChar);
        char restored = plugboard.transform(transformed);
        assertEquals(testChar, restored);
    }

    @Test
    void nonEncryptableCharactersShouldPassThrough() {
        char nonEncryptable = '\u0000';
        assertEquals(nonEncryptable, plugboard.transform(nonEncryptable),
            "Non-encryptable character should pass through unchanged");
    }

    @Test
    void differentSeedsShouldProduceDifferentTransformations() {
        Plugboard plugboard2 = new Plugboard(43L, properties);
        
        boolean foundDifference = false;
        for (char c = properties.getCharset().getStart(); 
             c <= properties.getCharset().getEnd(); c++) {
            if (plugboard.transform(c) != plugboard2.transform(c)) {
                foundDifference = true;
                break;
            }
        }
        
        assertTrue(foundDifference,
            "Different seeds should produce different transformations");
    }
}
