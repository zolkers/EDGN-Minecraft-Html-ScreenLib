package com.edgn.edml.parser.tokenizer;

public record EdmlToken(TokenType type, String value, Position position) {}
