package com.enigma.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "enigma")
@Data
public class EnigmaProperties {
    private CharsetConfig charset = new CharsetConfig();
    private RotorConfig rotors = new RotorConfig();

    @Data
    public static class CharsetConfig {
        public static final char START = '\u0020';  // Space character
        public static final char END = '\u007E';    // Tilde character
        private char start = START;
        private char end = END;
    }

    @Data
    public static class RotorConfig {
        private int minRotors = 2;
        private int maxRotors = 12;
        private long defaultSeed = 42L;
    }
}
