package com.edgn.edml.parser.tokenizer;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

public final class TokenIterator {
    private final List<EdmlToken> tokens;
    private int position = 0;

    public TokenIterator(List<EdmlToken> tokens) {
        this.tokens = tokens.stream()
                .filter(token -> token.type() != TokenType.EOF)
                .collect(Collectors.toList());
    }

    public boolean hasNext() {
        return position < tokens.size();
    }

    public EdmlToken next() {
        if (!hasNext()) {
            throw new NoSuchElementException("No more tokens at position " + position + "/" + tokens.size());
        }
        return tokens.get(position++);
    }

    public EdmlToken peek() {
        if (!hasNext()) {
            throw new NoSuchElementException("No more tokens to peek at position " + position + "/" + tokens.size());
        }
        return tokens.get(position);
    }
}