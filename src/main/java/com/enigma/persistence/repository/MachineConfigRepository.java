package com.enigma.persistence.repository;

import com.enigma.persistence.entity.MachineConfig;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MachineConfigRepository extends JpaRepository<MachineConfig, Long> {
}