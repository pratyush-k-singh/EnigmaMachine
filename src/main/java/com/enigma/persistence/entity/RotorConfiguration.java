package com.enigma.persistence.entity;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class RotorConfiguration {
    private Long seed;
    private Integer startPosition;
    private Integer notchPosition;
}
