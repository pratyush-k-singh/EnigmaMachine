package com.enigma.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class SecureRandomTest {

    @Test
    void sameSeedShouldProduceSameSequence() {
        SecureRandom random1 = new SecureRandom(42L);
        SecureRandom random2 = new SecureRandom(42L);
        
        for (int i = 0; i < 1000; i++) {
            assertEquals(random1.nextInt(100), random2.nextInt(100),
                "Same seed should produce identical sequence");
        }
    }

    @Test
    void differentSeedsShouldProduceDifferentSequences() {
        SecureRandom random1 = new SecureRandom(42L);
        SecureRandom random2 = new SecureRandom(43L);
        
        boolean foundDifference = false;
        for (int i = 0; i < 1000; i++) {
            if (random1.nextInt(100) != random2.nextInt(100)) {
                foundDifference = true;
                break;
            }
        }
        
        assertTrue(foundDifference,
            "Different seeds should produce different sequences");
    }

    @Test
    void shouldGenerateUniformDistribution() {
        SecureRandom random = new SecureRandom(42L);
        int[] buckets = new int[10];
        int iterations = 100000;
        
        for (int i = 0; i < iterations; i++) {
            buckets[random.nextInt(10)]++;
        }
        
        // Check that each bucket has roughly the expected number of values
        double expectedPerBucket = iterations / 10.0;
        double tolerance = expectedPerBucket * 0.1; // 10% tolerance
        
        for (int count : buckets) {
            assertTrue(Math.abs(count - expectedPerBucket) < tolerance,
                "Distribution should be roughly uniform");
        }
    }

    @Test
    void shouldGenerateNumbersInRange() {
        SecureRandom random = new SecureRandom();
        int bound = 100;
        Set<Integer> generated = new HashSet<>();
        
        for (int i = 0; i < 10000; i++) {
            int num = random.nextInt(bound);
            assertTrue(num >= 0 && num < bound,
                "Generated numbers should be within bounds");
            generated.add(num);
        }
        
        // Should have generated most numbers in the range
        assertTrue(generated.size() > bound * 0.95,
            "Should generate most numbers in the range");
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 0})
    void invalidBoundsShouldThrow(int bound) {
        SecureRandom random = new SecureRandom();
        assertThrows(IllegalArgumentException.class, () -> random.nextInt(bound));
    }

    @Test
    void nextIntBetweenShouldRespectBounds() {
        SecureRandom random = new SecureRandom();
        int min = 10;
        int max = 20;
        
        for (int i = 0; i < 1000; i++) {
            int num = random.nextIntBetween(min, max);
            assertTrue(num >= min && num <= max,
                "Generated numbers should be within specified bounds");
        }
    }

    @Test
    void invalidRangeShouldThrow() {
        SecureRandom random = new SecureRandom();
        assertThrows(IllegalArgumentException.class, 
            () -> random.nextIntBetween(20, 10));
    }

    @Test
    void sequenceShouldAdvance() {
        SecureRandom random = new SecureRandom(42L);
        long initialSequence = random.getSequence();
        random.nextInt(100);
        assertTrue(random.getSequence() > initialSequence,
            "Sequence number should increment");
    }

    @Test
    void stateShouldChange() {
        SecureRandom random = new SecureRandom(42L);
        long initialState = random.getState();
        random.nextInt(100);
        assertNotEquals(initialState, random.getState(),
            "Internal state should change after generation");
    }
}
