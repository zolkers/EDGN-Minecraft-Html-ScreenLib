package com.edgn.edml.tokenizer.enums;

public enum TokenType {
    OPEN_TAG,
    CLOSE_TAG,
    SELF_CLOSING_TAG,
    TAG_END,
    ATTRIBUTE_NAME,
    ATTRIBUTE_VALUE,
    EQUALS,
    TEXT_CONTENT,
    WHITESPACE,
    EOF
}