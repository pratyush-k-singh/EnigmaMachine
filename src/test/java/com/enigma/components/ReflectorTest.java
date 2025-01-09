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
class ReflectorTest {
    @Mock
    private EnigmaProperties properties;
    
    @Mock
    private EnigmaProperties.CharsetConfig charsetConfig;
    
    private Reflector reflector;

    @BeforeEach
    void setUp() {
        when(properties.getCharset()).thenReturn(charsetConfig);
        when(charsetConfig.getStart()).thenReturn('\u0020');
        when(charsetConfig.getEnd()).thenReturn('\u007E');
        
        reflector = new Reflector(42L, properties);
    }

    @Test
    void transformationShouldBeSymmetric() {
        for (char c = properties.getCharset().getStart(); 
             c <= properties.getCharset().getEnd(); c++) {
            char transformed = reflector.transform(c);
            char restored = reflector.transform(transformed);
            assertEquals(c, restored,
                "Character should be restored after double transformation");
        }
    }

    @Test
    void noCharacterShouldMapToItself() {
        for (char c = properties.getCharset().getStart(); 
             c <= properties.getCharset().getEnd(); c++) {
            assertNotEquals(c, reflector.transform(c),
                "Reflector should not map a character to itself");
        }
    }

    @Test
    void mappingTableShouldBeComplete() {
        String table = reflector.getMappingTable();
        assertFalse(table.isEmpty(), "Mapping table should not be empty");
        assertTrue(table.startsWith("Reflector Mappings:"),
            "Mapping table should have proper header");
    }
}
