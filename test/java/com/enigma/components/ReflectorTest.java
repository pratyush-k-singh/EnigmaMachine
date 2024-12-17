package com.enigma.components;

import com.enigma.core.EnigmaConfiguration;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ReflectorTest {

    @Test
    void reflectorShouldInitializeWithValidSeed() {
        assertDoesNotThrow(() -> new Reflector(42L));
    }

    @Test
    void transformationShouldBeSymmetric() {
        Reflector reflector = new Reflector(42L);
        for (char c = EnigmaConfiguration.CHARSET_START; 
             c <= EnigmaConfiguration.CHARSET_END; c++) {
            char transformed = reflector.transform(c);
            char restored = reflector.transform(transformed);
            assertEquals(c, restored,
                "Character should be restored after double transformation");
        }
    }

    @Test
    void noCharacterShouldMapToItself() {
        Reflector reflector = new Reflector(42L);
        for (char c = EnigmaConfiguration.CHARSET_START; 
             c <= EnigmaConfiguration.CHARSET_END; c++) {
            assertNotEquals(c, reflector.transform(c),
                "Reflector should not map a character to itself");
        }
    }

    @Test
    void allCharactersShouldBeTransformed() {
        Reflector reflector = new Reflector(42L);
        Set<Character> transformedChars = new HashSet<>();
        
        for (char c = EnigmaConfiguration.CHARSET_START; 
             c <= EnigmaConfiguration.CHARSET_END; c++) {
            transformedChars.add(reflector.transform(c));
        }
        
        assertEquals(EnigmaConfiguration.CHARSET_SIZE, transformedChars.size(),
            "All characters should be mapped uniquely");
    }

    @Test
    void nonEncryptableCharactersShouldPassThrough() {
        Reflector reflector = new Reflector(42L);
        char nonEncryptable = '\u0000';
        assertEquals(nonEncryptable, reflector.transform(nonEncryptable));
    }

    @Test
    void differentSeedsShouldProduceDifferentTransformations() {
        Reflector reflector1 = new Reflector(42L);
        Reflector reflector2 = new Reflector(43L);
        
        boolean foundDifference = false;
        for (char c = EnigmaConfiguration.CHARSET_START; 
             c <= EnigmaConfiguration.CHARSET_END; c++) {
            if (reflector1.transform(c) != reflector2.transform(c)) {
                foundDifference = true;
                break;
            }
        }
        
        assertTrue(foundDifference,
            "Different seeds should produce different transformations");
    }

    @Test
    void sameSeedShouldProduceSameTransformations() {
        Reflector reflector1 = new Reflector(42L);
        Reflector reflector2 = new Reflector(42L);
        
        for (char c = EnigmaConfiguration.CHARSET_START; 
             c <= EnigmaConfiguration.CHARSET_END; c++) {
            assertEquals(reflector1.transform(c), reflector2.transform(c),
                "Same seeds should produce identical transformations");
        }
    }

    @Test
    void mappingTableShouldBeComplete() {
        Reflector reflector = new Reflector(42L);
        String table = reflector.getMappingTable();
        
        assertFalse(table.isEmpty(), "Mapping table should not be empty");
        assertTrue(table.startsWith("Reflector Mappings:"),
            "Mapping table should have proper header");
    }
}
