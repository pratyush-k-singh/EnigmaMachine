# Modern Enigma Machine Implementation

A modern implementation of the Enigma machine in Java, supporting the full printable ASCII character set. This implementation maintains the core concepts of the original Enigma machine while extending its capabilities for modern use.

## Features

- Support for printable ASCII characters (space through tilde)
- Configurable number of rotors (2-12)
- Customizable rotor positions and notch points
- Deterministic encryption/decryption using seed-based randomization
- Full logging support
- Comprehensive test suite
- Thread-safe implementation

## Requirements

- Java 17 or higher
- Maven 3.6 or higher

## Building the Project

Clone the repository and build using Maven:

```bash
git clone https://github.com/yourusername/modern-enigma.git
cd modern-enigma
mvn clean install
```

## Usage

### Basic Usage

```java
import com.enigma.core.EnigmaConfiguration;
import com.enigma.core.EnigmaMachine;

public class Example {
    public static void main(String[] args) {
        // Create a configuration with 3 rotors
        EnigmaConfiguration config = new EnigmaConfiguration.Builder()
            .plugboardSeed(123456L)
            .reflectorSeed(789012L)
            .addRotor(345678L, 0, 5)  // First rotor: seed, start position, notch position
            .addRotor(901234L, 0, 10) // Second rotor
            .addRotor(567890L, 0, 15) // Third rotor
            .build();

        // Create the machine
        EnigmaMachine enigma = new EnigmaMachine(config);

        // Encrypt a message
        String message = "Hello, World!";
        String encrypted = enigma.encrypt(message);
        System.out.println("Encrypted: " + encrypted);

        // Reset the machine to initial state
        enigma.reset();

        // Decrypt the message
        String decrypted = enigma.decrypt(encrypted);
        System.out.println("Decrypted: " + decrypted);
    }
}
```

### Advanced Configuration

```java
// Create a configuration with custom settings
EnigmaConfiguration config = new EnigmaConfiguration.Builder()
    .plugboardSeed(System.currentTimeMillis()) // Random seed based on time
    .reflectorSeed(UUID.randomUUID().getLeastSignificantBits()) // Random seed
    .addRotor(345678L, 5, 10)  // Custom start and notch positions
    .addRotor(901234L, 3, 15)
    .addRotor(567890L, 7, 20)
    .addRotor(111111L, 2, 25)  // Additional rotor
    .build();
```

## Configuration Parameters

### Rotor Configuration
- `seed`: Determines the internal wiring of the rotor
- `startPosition`: Initial position of the rotor (0-94)
- `notchPosition`: Position at which the next rotor advances (0-94)

### Machine Configuration
- `plugboardSeed`: Determines the plugboard wiring
- `reflectorSeed`: Determines the reflector wiring
- Number of rotors: Minimum 2, Maximum 12

## Security Considerations

This implementation:
- Uses deterministic randomization for reproducibility
- Supports a larger character set than the original
- Maintains the reciprocal nature of the original Enigma
- Is NOT intended for actual cryptographic security

## Testing

Run the test suite using Maven:

```bash
mvn test
```

View test coverage report:

```bash
mvn jacoco:report
```

The coverage report will be available at `target/site/jacoco/index.html`

## Logging

Logging is configured in `src/main/resources/log4j2.xml`. By default:
- INFO and above messages go to console
- DEBUG and above messages go to `logs/enigma.log`
- File logging uses rotation (10MB max file size)

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -am 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Create a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Acknowledgments

- Based on the original Enigma machine design
- Uses modern Java features and best practices
- Inspired by various open-source Enigma implementations