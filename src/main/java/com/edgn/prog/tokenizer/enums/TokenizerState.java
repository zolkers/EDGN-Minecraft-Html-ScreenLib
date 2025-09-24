package com.edgn.prog.tokenizer.enums;

public enum TokenizerState {
    TEXT,           // Parsing text content between tags
    TAG_OPENING,    // Just found '<', determining tag type
    TAG_NAME,       // Reading tag name after '<'
    ATTRIBUTES,     // Reading attributes inside a tag
    TAG_CONTENT     // After '>', back to reading content
}