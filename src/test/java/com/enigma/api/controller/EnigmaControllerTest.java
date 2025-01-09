package com.enigma.api.controller;

import com.enigma.api.dto.EncryptionRequest;
import com.enigma.service.EnigmaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(EnigmaController.class)
class EnigmaControllerTest {
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private EnigmaService enigmaService;
    
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldEncryptMessage() throws Exception {
        // Given
        EncryptionRequest request = new EncryptionRequest();
        request.setMessage("Hello World");
        request.setConfigId(1L);
        
        when(enigmaService.encrypt(any(), anyLong()))
            .thenReturn("Encrypted Message");

        // When/Then
        mockMvc.perform(post("/api/v1/enigma/encrypt")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value("Encrypted Message"));
    }
}
