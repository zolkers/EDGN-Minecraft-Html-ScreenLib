package com.edgn.edml.exceptions;

import com.edgn.edml.parser.tokenizer.Position;

public class LexerException extends Exception {
    private final Position position;
    
    public LexerException(String message, Position position) {
        super(String.format("Lexer error at line %d, column %d: %s", 
                          position.line(), position.column(), message));
        this.position = position;
    }
    
    public Position getPosition() {
        return position;
    }
}