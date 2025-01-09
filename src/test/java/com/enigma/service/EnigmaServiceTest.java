package com.enigma.service;

import com.enigma.config.EnigmaProperties;
import com.enigma.persistence.entity.MachineConfig;
import com.enigma.persistence.repository.MachineConfigRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class EnigmaServiceTest {
    @Mock
    private MachineConfigRepository configRepository;
    
    @Mock
    private EnigmaProperties properties;
    
    @Mock
    private EnigmaProperties.RotorConfig rotorConfig;
    
    @Mock
    private EnigmaProperties.CharsetConfig charsetConfig;
    
    private EnigmaService enigmaService;

    @BeforeEach
    void setUp() {
        when(properties.getRotors()).thenReturn(rotorConfig);
        when(properties.getCharset()).thenReturn(charsetConfig);
        when(rotorConfig.getMinRotors()).thenReturn(2);
        when(rotorConfig.getMaxRotors()).thenReturn(12);
        when(charsetConfig.getStart()).thenReturn(EnigmaProperties.CharsetConfig.START);
        when(charsetConfig.getEnd()).thenReturn(EnigmaProperties.CharsetConfig.END);
        
        enigmaService = new EnigmaService(configRepository, properties);
    }

    @Test
    void encryptionShouldBeReversible() {
        MachineConfig config = createTestConfig();
        when(configRepository.findById(1L)).thenReturn(Optional.of(config));
        
        String message = "Test Message";
        String encrypted = enigmaService.encrypt(message, 1L);
        String decrypted = enigmaService.decrypt(encrypted, 1L);
        
        assertEquals(message, decrypted);
    }

    private MachineConfig createTestConfig() {
        MachineConfig config = new MachineConfig();
        config.setPlugboardSeed(123456L);
        config.setReflectorSeed(789012L);
        // Add rotor configurations...
        return config;
    }
}