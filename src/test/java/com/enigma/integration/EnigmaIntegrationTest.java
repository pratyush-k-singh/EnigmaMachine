package com.enigma.integration;

import com.enigma.api.dto.EncryptionRequest;
import com.enigma.persistence.entity.MachineConfig;
import com.enigma.persistence.entity.RotorConfiguration;
import com.enigma.persistence.repository.MachineConfigRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class EnigmaIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Autowired
    private MachineConfigRepository configRepository;
    
    private Long configId;

    @BeforeEach
    void setUp() {
        // Create and save a test configuration
        MachineConfig config = new MachineConfig();
        config.setPlugboardSeed(123456L);
        config.setReflectorSeed(789012L);
        
        RotorConfiguration rotor1 = new RotorConfiguration();
        rotor1.setSeed(345678L);
        rotor1.setStartPosition(0);
        rotor1.setNotchPosition(5);
        
        RotorConfiguration rotor2 = new RotorConfiguration();
        rotor2.setSeed(901234L);
        rotor2.setStartPosition(0);
        rotor2.setNotchPosition(10);
        
        config.setRotorConfigurations(Arrays.asList(rotor1, rotor2));
        
        configId = configRepository.save(config).getId();
    }

    @Test
    void fullEncryptionDecryptionCycle() throws Exception {
        // Create encryption request
        EncryptionRequest request = new EncryptionRequest();
        request.setMessage("Hello, World!");
        request.setConfigId(configId);
        
        // Encrypt
        String encryptResult = mockMvc.perform(post("/api/v1/enigma/encrypt")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").exists())
                .andReturn()
                .getResponse()
                .getContentAsString();
        
        // Extract encrypted message
        String encrypted = objectMapper.readTree(encryptResult)
                                    .get("result")
                                    .asText();
        
        // Create decryption request
        request.setMessage(encrypted);
        
        // Decrypt
        mockMvc.perform(post("/api/v1/enigma/decrypt")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value("Hello, World!"));
    }
}
