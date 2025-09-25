package com.edgn.edml.tokenizer;

public record Position(int line, int column, int offset) {
    public Position advance(char ch) {
        return ch == '\n' 
            ? new Position(line + 1, 1, offset + 1)
            : new Position(line, column + 1, offset + 1);
    }
}
