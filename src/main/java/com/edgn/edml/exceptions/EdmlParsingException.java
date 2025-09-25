package com.edgn.edml.exceptions;

public class EdmlParsingException extends Exception {
    public EdmlParsingException(String message) {
        super(message);
    }
    
    public EdmlParsingException(String message, Throwable cause) {
        super(message, cause);
    }
}