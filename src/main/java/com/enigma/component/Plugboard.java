package com.enigma.component;

import com.enigma.config.EnigmaProperties;
import com.enigma.util.SecureRandom;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class Plugboard {
    private final Map<Character, Character> connections;
    private final long seed;

    public Plugboard(long seed, EnigmaProperties properties) {
        this.seed = seed;
        this.connections = initializeConnections(properties);
    }

    private Map<Character, Character> initializeConnections(EnigmaProperties properties) {
        Map<Character, Character> map = new HashMap<>();
        SecureRandom random = new SecureRandom(seed);
        
        // Initialize character set
        char[] chars = new char[properties.getCharset().getEnd() - properties.getCharset().getStart() + 1];
        for (int i = 0; i < chars.length; i++) {
            chars[i] = (char) (properties.getCharset().getStart() + i);
        }
        
        // Create random pairings
        for (int i = 0; i < chars.length; i++) {
            if (!map.containsKey(chars[i])) {
                int j = random.nextInt(chars.length);
                while (map.containsKey(chars[j]) || i == j) {
                    j = random.nextInt(chars.length);
                }
                map.put(chars[i], chars[j]);
                map.put(chars[j], chars[i]);
            }
        }
        
        return map;
    }

    public char transform(char c) {
        return connections.getOrDefault(c, c);
    }

    public String getConnectionsTable() {
        StringBuilder table = new StringBuilder("Plugboard Connections:\n");
        connections.forEach((k, v) -> {
            if (k < v) {  // Only show each pair once
                table.append(String.format("%c <-> %c\n", k, v));
            }
        });
        return table.toString();
    }
}
