package com.edgn.prog.exceptions;

public class EdgnParsingException extends Exception {
    public EdgnParsingException(String message) {
        super(message);
    }
    
    public EdgnParsingException(String message, Throwable cause) {
        super(message, cause);
    }
}