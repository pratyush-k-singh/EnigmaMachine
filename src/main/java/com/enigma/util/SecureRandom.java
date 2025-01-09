package com.enigma.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class SecureRandom {
    private static final Logger logger = LoggerFactory.getLogger(SecureRandom.class);
    
    private static final long MULTIPLIER = 6364136223846793005L;
    private static final long ADDEND = 1442695040888963407L;
    private static final long MASK = (1L << 63) - 1;
    
    private long seed;
    private long sequence;
    
    public SecureRandom(long seed) {
        this.seed = scrambleSeed(seed);
        this.sequence = 0;
        logger.debug("Initialized SecureRandom with seed: {}", seed);
    }
    
    public SecureRandom() {
        this(Instant.now().toEpochMilli());
    }
    
    private long scrambleSeed(long seed) {
        long scrambled = seed;
        scrambled ^= scrambled >>> 32;
        scrambled *= MULTIPLIER;
        scrambled ^= scrambled >>> 32;
        scrambled *= MULTIPLIER;
        return scrambled & MASK;
    }
    
    protected long next() {
        seed = (seed * MULTIPLIER + ADDEND) & MASK;
        sequence++;
        logger.trace("Generated random number {} in sequence {}", seed, sequence);
        return seed;
    }
    
    public int nextInt(int bound) {
        if (bound <= 0) {
            throw new IllegalArgumentException("bound must be positive");
        }
        
        long result = next();
        int r = (int) (bound * (result >>> 1) / (1L << 62));
        logger.trace("Generated random integer {} in range [0, {})", r, bound);
        return r;
    }
    
    public int nextIntBetween(int min, int max) {
        if (max <= min) {
            throw new IllegalArgumentException("max must be greater than min");
        }
        return min + nextInt(max - min + 1);
    }
    
    public long getSequence() {
        return sequence;
    }
    
    public long getState() {
        return seed;
    }

    // Added for Spring Boot integration
    public void setSeed(long seed) {
        this.seed = scrambleSeed(seed);
        this.sequence = 0;
        logger.debug("Reset SecureRandom with new seed: {}", seed);
    }
}
