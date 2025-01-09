package com.enigma.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "enigma_configurations")
@Data
public class MachineConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long plugboardSeed;
    private Long reflectorSeed;

    @ElementCollection
    @CollectionTable(name = "rotor_configurations")
    private List<RotorConfiguration> rotorConfigurations = new ArrayList<>();
}
