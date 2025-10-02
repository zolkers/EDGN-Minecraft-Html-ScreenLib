package com.edgn.edml.parser.style;

import com.edgn.edml.dom.styling.EdssRule;
import com.edgn.edml.exceptions.EdssParsingException;

import java.util.List;

public interface EdssParser {
    List<EdssRule> parse(String cssContent) throws EdssParsingException;
}