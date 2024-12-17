package com.enigma.exception;

/**
 * Base exception class for all Enigma-related exceptions.
 */
public class EnigmaException extends RuntimeException {
    public EnigmaException(String message) {
        super(message);
    }

    public EnigmaException(String message, Throwable cause) {
        super(message, cause);
    }
}
