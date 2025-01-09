package com.enigma.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EncryptionRequest {
    @NotBlank(message = "Message cannot be empty")
    private String message;
    
    @NotNull(message = "Configuration ID must be provided")
    private Long configId;
}
