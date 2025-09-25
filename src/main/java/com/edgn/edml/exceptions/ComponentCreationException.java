package com.edgn.edml.exceptions;

public class ComponentCreationException extends Exception {
    public ComponentCreationException(String message) {
        super(message);
    }
    
    public ComponentCreationException(String message, Throwable cause) {
        super(message, cause);
    }
}