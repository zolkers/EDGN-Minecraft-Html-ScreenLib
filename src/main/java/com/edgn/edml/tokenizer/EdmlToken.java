package com.edgn.edml.tokenizer;

import com.edgn.edml.tokenizer.enums.TokenType;

public record EdmlToken(TokenType type, String value, Position position) {}
