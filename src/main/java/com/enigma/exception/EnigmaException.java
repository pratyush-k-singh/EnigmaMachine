package com.enigma.exception;

public class EnigmaException extends RuntimeException {
    public EnigmaException(String message) {
        super(message);
    }

    public EnigmaException(String message, Throwable cause) {
        super(message, cause);
    }
}