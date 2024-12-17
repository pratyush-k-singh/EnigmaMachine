package com.enigma.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Instant;

/**
 * A predictable random number generator for the Enigma machine.
 * Uses a linear congruential generator (LCG) algorithm with carefully chosen
 * parameters to ensure a good distribution and long period.
 */
public class SecureRandom {
    private static final Logger logger = LogManager.getLogger(SecureRandom.class);
    
    // Parameters from Donald Knuth's MMIX
    private static final long MULTIPLIER = 6364136223846793005L;
    private static final long ADDEND = 1442695040888963407L;
    private static final long MASK = (1L << 63) - 1;
    
    private long seed;
    private long sequence;
    
    /**
     * Creates a new SecureRandom with a specified seed.
     * The same seed will always produce the same sequence of numbers.
     */
    public SecureRandom(long seed) {
        this.seed = scrambleSeed(seed);
        this.sequence = 0;
        logger.debug("Initialized SecureRandom with seed: {}", seed);
    }
    
    /**
     * Creates a new SecureRandom with a seed based on the current time.
     */
    public SecureRandom() {
        this(Instant.now().toEpochMilli());
    }
    
    /**
     * Scrambles the initial seed to avoid poor bit patterns.
     */
    private long scrambleSeed(long seed) {
        long scrambled = seed;
        scrambled ^= scrambled >>> 32;
        scrambled *= MULTIPLIER;
        scrambled ^= scrambled >>> 32;
        scrambled *= MULTIPLIER;
        return scrambled & MASK;
    }
    
    /**
     * Generates the next random number in the sequence.
     */
    protected long next() {
        seed = (seed * MULTIPLIER + ADDEND) & MASK;
        sequence++;
        logger.trace("Generated random number {} in sequence {}", seed, sequence);
        return seed;
    }
    
    /**
     * Returns a random integer between 0 (inclusive) and bound (exclusive).
     */
    public int nextInt(int bound) {
        if (bound <= 0) {
            throw new IllegalArgumentException("bound must be positive");
        }
        
        long result = next();
        int r = (int) (bound * (result >>> 1) / (1L << 62));
        logger.trace("Generated random integer {} in range [0, {})", r, bound);
        return r;
    }
    
    /**
     * Returns a random integer between min (inclusive) and max (exclusive).
     */
    public int nextIntBetween(int min, int max) {
        if (max <= min) {
            throw new IllegalArgumentException("max must be greater than min");
        }
        return min + nextInt(max - min);
    }
    
    /**
     * Returns the current sequence number.
     */
    public long getSequence() {
        return sequence;
    }
    
    /**
     * Returns the current internal state.
     */
    public long getState() {
        return seed;
    }
}
