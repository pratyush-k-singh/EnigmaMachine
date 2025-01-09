# Modern Enigma Machine Implementation

This project is a modern implementation of the Enigma machine using Spring Boot. It provides a REST API for encryption and decryption operations, along with configuration management and persistence.

## Features

- **Spring Boot-based implementation** with a configurable rotor system  
- **REST API** for encryption/decryption operations  
- **Database persistence** for machine configurations  
- **Comprehensive test suite** for unit and integration testing  
- **Monitoring and metrics support** for application performance  
- **Security configuration** for secure operations  

## Prerequisites

- **Java** 17 or higher  
- **Maven** 3.6 or higher  
- **Git**

## Building and Running

### Build
```bash
git clone https://github.com/yourusername/enigma.git
cd enigma
mvn clean install
```

### Run
```bash
# Using Maven
mvn spring-boot:run

# Or using the JAR file
java -jar target/enigma-machine-1.0.0.jar
```

## API Usage

### Create Configuration
```bash
curl -X POST http://localhost:8080/api/v1/enigma/config \
  -H "Content-Type: application/json" \
  -d '{
    "plugboardSeed": 123456,
    "reflectorSeed": 789012,
    "rotorConfigurations": [
      {"seed": 345678, "startPosition": 0, "notchPosition": 5},
      {"seed": 901234, "startPosition": 0, "notchPosition": 10}
    ]
  }'
```

### Encrypt Message
```bash
curl -X POST http://localhost:8080/api/v1/enigma/encrypt \
  -H "Content-Type: application/json" \
  -d '{
    "message": "Hello, World!",
    "configId": 1
  }'
```

### Decrypt Message
```bash
curl -X POST http://localhost:8080/api/v1/enigma/decrypt \
  -H "Content-Type: application/json" \
  -d '{
    "message": "encrypted_text_here",
    "configId": 1
  }'
```

## Configuration

The application can be configured via `application.yml`:

```yaml
enigma:
  charset:
    start: ' '  # Space character
    end: '~'    # Tilde character
  rotors:
    min-rotors: 2
    max-rotors: 12
    default-seed: 42
```

## Testing

```bash
# Run unit tests
mvn test

# Run integration tests
mvn verify
```

## Monitoring

The application exposes metrics at `/actuator/metrics`, including:

- `enigma.operations` - Encryption/decryption operation counts  
- `enigma.errors` - Error counts  

## Development Tools

- **H2 Database Console**: http://localhost:8080/h2-console  
  - JDBC URL: `jdbc:h2:mem:enigmadb`  
  - Username: `sa`  
  - Password: `password`

## License

This project is licensed under the MIT License. See the `LICENSE` file for details.
