package com.enigma.component;

import com.enigma.config.EnigmaProperties;
import com.enigma.config.EnigmaProperties.CharsetConfig;
import com.enigma.exception.InvalidConfigurationException;
import com.enigma.util.SecureRandom;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class Rotor {
    private final String id;
    private final int[] forwardMapping;
    private final int[] reverseMapping;
    private final int startPosition;
    private final int notchPosition;
    private int currentPosition;

    public Rotor(long seed, int startPosition, int notchPosition, EnigmaProperties properties) {
        this.id = UUID.randomUUID().toString();
        this.startPosition = validatePosition(startPosition, properties);
        this.notchPosition = validatePosition(notchPosition, properties);
        this.currentPosition = startPosition;
        
        int size = CharsetConfig.END - CharsetConfig.START + 1;
        this.forwardMapping = new int[size];
        this.reverseMapping = new int[size];
        
        initializeMappings(seed, size);
    }

    private int validatePosition(int position, EnigmaProperties properties) {
        int size = CharsetConfig.END - CharsetConfig.START + 1;
        if (position < 0 || position >= size) {
            throw InvalidConfigurationException.invalidRotorPosition(
                position, 0, size - 1);
        }
        return position;
    }

    private void initializeMappings(long seed, int size) {
        SecureRandom random = new SecureRandom(seed);
        boolean[] used = new boolean[size];
        
        // Create random permutation
        for (int i = 0; i < size; i++) {
            int j = random.nextInt(size);
            while (used[j]) {
                j = random.nextInt(size);
            }
            forwardMapping[i] = j;
            reverseMapping[j] = i;
            used[j] = true;
        }
    }

    public char transform(char c) {
        if (c < EnigmaProperties.CharsetConfig.START || c > EnigmaProperties.CharsetConfig.END) {
            return c;
        }
        
        int pos = c - EnigmaProperties.CharsetConfig.START;
        int shifted = (pos + currentPosition) % forwardMapping.length;
        int mapped = forwardMapping[shifted];
        int unshifted = (mapped - currentPosition + forwardMapping.length) % forwardMapping.length;
        
        return (char) (EnigmaProperties.CharsetConfig.START + unshifted);
    }

    public char transformReverse(char c) {
        if (c < EnigmaProperties.CharsetConfig.START || c > EnigmaProperties.CharsetConfig.END) {
            return c;
        }
        
        int pos = c - EnigmaProperties.CharsetConfig.START;
        int shifted = (pos + currentPosition) % reverseMapping.length;
        int mapped = reverseMapping[shifted];
        int unshifted = (mapped - currentPosition + reverseMapping.length) % reverseMapping.length;
        
        return (char) (EnigmaProperties.CharsetConfig.START + unshifted);
    }

    public void rotate() {
        currentPosition = (currentPosition + 1) % forwardMapping.length;
    }

    public void reset() {
        currentPosition = startPosition;
    }

    public boolean shouldAdvanceNext() {
        return currentPosition == notchPosition;
    }

    public String getId() {
        return id;
    }

    public int getPosition() {
        return currentPosition;
    }
}
