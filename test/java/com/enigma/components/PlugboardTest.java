package com.enigma.components;

import com.enigma.core.EnigmaConfiguration;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class PlugboardTest {

    @Test
    void plugboardShouldInitializeWithValidSeed() {
        assertDoesNotThrow(() -> new Plugboard(42L));
    }

    @Test
    void transformationShouldBeSymmetric() {
        Plugboard plugboard = new Plugboard(42L);
        for (char c = EnigmaConfiguration.CHARSET_START; 
             c <= EnigmaConfiguration.CHARSET_END; c++) {
            char transformed = plugboard.transform(c);
            char restored = plugboard.transform(transformed);
            assertEquals(c, restored,
                "Character should be restored after double transformation");
        }
    }

    @Test
    void allCharactersShouldBeTransformed() {
        Plugboard plugboard = new Plugboard(42L);
        Set<Character> inputs = new HashSet<>();
        Set<Character> outputs = new HashSet<>();
        
        for (char c = EnigmaConfiguration.CHARSET_START; 
             c <= EnigmaConfiguration.CHARSET_END; c++) {
            inputs.add(c);
            outputs.add(plugboard.transform(c));
        }
        
        assertEquals(inputs.size(), outputs.size(),
            "All characters should be mapped uniquely");
    }

    @Test
    void nonEncryptableCharactersShouldPassThrough() {
        Plugboard plugboard = new Plugboard(42L);
        char nonEncryptable = '\u0000';
        assertEquals(nonEncryptable, plugboard.transform(nonEncryptable));
    }

    @Test
    void differentSeedsShouldProduceDifferentTransformations() {
        Plugboard plugboard1 = new Plugboard(42L);
        Plugboard plugboard2 = new Plugboard(43L);
        
        boolean foundDifference = false;
        for (char c = EnigmaConfiguration.CHARSET_START; 
             c <= EnigmaConfiguration.CHARSET_END; c++) {
            if (plugboard1.transform(c) != plugboard2.transform(c)) {
                foundDifference = true;
                break;
            }
        }
        
        assertTrue(foundDifference,
            "Different seeds should produce different transformations");
    }

    @Test
    void sameSeedShouldProduceSameTransformations() {
        Plugboard plugboard1 = new Plugboard(42L);
        Plugboard plugboard2 = new Plugboard(42L);
        
        for (char c = EnigmaConfiguration.CHARSET_START; 
             c <= EnigmaConfiguration.CHARSET_END; c++) {
            assertEquals(plugboard1.transform(c), plugboard2.transform(c),
                "Same seeds should produce identical transformations");
        }
    }

    @Test
    void connectionTableShouldBeComplete() {
        Plugboard plugboard = new Plugboard(42L);
        String table = plugboard.getConnectionsTable();
        
        assertFalse(table.isEmpty(), "Connection table should not be empty");
        assertTrue(table.startsWith("Plugboard Connections:"),
            "Connection table should have proper header");
    }
}
