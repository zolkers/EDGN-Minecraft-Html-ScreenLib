package com.edgn.prog.tokenizer;

import com.edgn.prog.tokenizer.enums.TokenType;

public record EdgnToken(TokenType type, String value, Position position) {}
