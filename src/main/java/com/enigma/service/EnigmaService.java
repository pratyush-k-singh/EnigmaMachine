package com.enigma.service;

import com.enigma.config.EnigmaProperties;
import com.enigma.core.EnigmaConfiguration;
import com.enigma.core.EnigmaMachine;
import com.enigma.exception.ResourceNotFoundException;
import com.enigma.persistence.entity.MachineConfig;
import com.enigma.persistence.repository.MachineConfigRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class EnigmaService {
    private final MachineConfigRepository configRepository;
    private final EnigmaProperties properties;

    public EnigmaService(MachineConfigRepository configRepository, EnigmaProperties properties) {
        this.configRepository = configRepository;
        this.properties = properties;
    }

    public String encrypt(String message, Long configId) {
        MachineConfig config = configRepository.findById(configId)
            .orElseThrow(() -> new ResourceNotFoundException("Configuration not found"));
            
        EnigmaMachine machine = createMachine(config);
        return machine.encrypt(message);
    }

    public String decrypt(String message, Long configId) {
        MachineConfig config = configRepository.findById(configId)
            .orElseThrow(() -> new ResourceNotFoundException("Configuration not found"));
            
        EnigmaMachine machine = createMachine(config);
        return machine.decrypt(message);
    }

    private EnigmaMachine createMachine(MachineConfig config) {
        EnigmaConfiguration.Builder builder = new EnigmaConfiguration.Builder(properties)
            .plugboardSeed(config.getPlugboardSeed())
            .reflectorSeed(config.getReflectorSeed());
            
        config.getRotorConfigurations().forEach(rotor -> 
            builder.addRotor(rotor.getSeed(), rotor.getStartPosition(), rotor.getNotchPosition())
        );
        
        return new EnigmaMachine(builder.build());
    }
}
