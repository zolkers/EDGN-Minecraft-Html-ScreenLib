package com.edgn.edml.parser.tokenizer;

import com.edgn.edml.exceptions.LexerException;

import java.util.List;

public interface IEdmlTokenizer {
    List<EdmlToken> tokenize(String input) throws LexerException;
}