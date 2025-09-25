package com.edgn.edml.exceptions;

public class EdssParsingException extends Exception {
    public EdssParsingException(String message) {
        super(message);
    }
    
    public EdssParsingException(String message, Throwable cause) {
        super(message, cause);
    }
}
