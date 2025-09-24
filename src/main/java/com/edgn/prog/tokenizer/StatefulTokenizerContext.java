package com.edgn.prog.tokenizer;

import com.edgn.prog.tokenizer.enums.TokenizerState;

public final class StatefulTokenizerContext {
    private final String input;
    private int position = 0;
    private Position currentPos = new Position(1, 1, 0);
    private TokenizerState state = TokenizerState.TEXT;
    
    public StatefulTokenizerContext(String input) {
        this.input = input;
    }
    
    public char peek() {
        return isAtEnd() ? '\0' : input.charAt(position);
    }
    
    public char peekNext() {
        return (position + 1 >= input.length()) ? '\0' : input.charAt(position + 1);
    }
    
    public char advance() {
        if (isAtEnd()) return '\0';
        char ch = input.charAt(position++);
        currentPos = currentPos.advance(ch);
        return ch;
    }
    
    public boolean isAtEnd() {
        return position >= input.length();
    }
    
    public Position getCurrentPosition() {
        return currentPos;
    }
    
    public TokenizerState getState() {
        return state;
    }
    
    public void setState(TokenizerState newState) {
        this.state = newState;
    }
    
    public String getDebugInfo() {
        int start = Math.max(0, position - 10);
        int end = Math.min(input.length(), position + 10);
        return String.format("Position: %d, State: %s, Context: '...%s[%c]%s...'", 
            position, state, 
            input.substring(start, position),
            isAtEnd() ? '?' : input.charAt(position),
            isAtEnd() ? "" : input.substring(position + 1, end));
    }
}