package com.edgn.prog.exceptions;

public class CssParsingException extends Exception {
    public CssParsingException(String message) {
        super(message);
    }
    
    public CssParsingException(String message, Throwable cause) {
        super(message, cause);
    }
}
