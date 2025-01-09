package com.enigma.component;

import com.enigma.config.EnigmaProperties;
import com.enigma.util.SecureRandom;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class Reflector {
    private final Map<Character, Character> mappings;
    private final long seed;

    public Reflector(long seed, EnigmaProperties properties) {
        this.seed = seed;
        this.mappings = initializeMappings(properties);
    }

    private Map<Character, Character> initializeMappings(EnigmaProperties properties) {
        Map<Character, Character> map = new HashMap<>();
        SecureRandom random = new SecureRandom(seed);
        
        // Initialize character set
        char[] chars = new char[properties.getCharset().getEnd() - properties.getCharset().getStart() + 1];
        for (int i = 0; i < chars.length; i++) {
            chars[i] = (char) (properties.getCharset().getStart() + i);
        }
        
        // Create random reflections (ensuring no character maps to itself)
        boolean[] used = new boolean[chars.length];
        for (int i = 0; i < chars.length; i++) {
            if (!used[i]) {
                int j = random.nextInt(chars.length);
                while (j == i || used[j]) {
                    j = random.nextInt(chars.length);
                }
                map.put(chars[i], chars[j]);
                map.put(chars[j], chars[i]);
                used[i] = used[j] = true;
            }
        }
        
        return map;
    }

    public char transform(char c) {
        return mappings.getOrDefault(c, c);
    }

    public String getMappingTable() {
        StringBuilder table = new StringBuilder("Reflector Mappings:\n");
        mappings.forEach((k, v) -> {
            if (k < v) {  // Only show each pair once
                table.append(String.format("%c <-> %c\n", k, v));
            }
        });
        return table.toString();
    }
}
