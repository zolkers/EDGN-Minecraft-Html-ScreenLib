package com.edgn.prog.tokenizer;

import com.edgn.prog.exceptions.LexerException;

import java.util.List;

public interface EdgnTokenizer {
    List<EdgnToken> tokenize(String input) throws LexerException;
}