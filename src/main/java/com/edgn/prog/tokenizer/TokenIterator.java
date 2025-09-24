package com.edgn.prog.tokenizer;

import com.edgn.prog.tokenizer.enums.TokenType;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

public final class TokenIterator {
    private final List<EdgnToken> tokens;
    private int position = 0;

    public TokenIterator(List<EdgnToken> tokens) {
        this.tokens = tokens.stream()
                .filter(token -> token.type() != TokenType.EOF)
                .collect(Collectors.toList());
    }

    public boolean hasNext() {
        return position < tokens.size();
    }

    public EdgnToken next() {
        if (!hasNext()) {
            throw new NoSuchElementException("No more tokens at position " + position + "/" + tokens.size());
        }
        return tokens.get(position++);
    }

    public EdgnToken peek() {
        if (!hasNext()) {
            throw new NoSuchElementException("No more tokens to peek at position " + position + "/" + tokens.size());
        }
        return tokens.get(position);
    }
}