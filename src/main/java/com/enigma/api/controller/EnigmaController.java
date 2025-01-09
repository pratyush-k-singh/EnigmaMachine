package com.enigma.api.controller;

import com.enigma.api.dto.EncryptionRequest;
import com.enigma.api.dto.EncryptionResponse;
import com.enigma.service.EnigmaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/enigma")
@Validated
public class EnigmaController {
    private final EnigmaService enigmaService;

    public EnigmaController(EnigmaService enigmaService) {
        this.enigmaService = enigmaService;
    }

    @PostMapping("/encrypt")
    public ResponseEntity<EncryptionResponse> encrypt(
            @Valid @RequestBody EncryptionRequest request) {
        String encrypted = enigmaService.encrypt(request.getMessage(), request.getConfigId());
        return ResponseEntity.ok(new EncryptionResponse(encrypted));
    }

    @PostMapping("/decrypt")
    public ResponseEntity<EncryptionResponse> decrypt(
            @Valid @RequestBody EncryptionRequest request) {
        String decrypted = enigmaService.decrypt(request.getMessage(), request.getConfigId());
        return ResponseEntity.ok(new EncryptionResponse(decrypted));
    }
}
