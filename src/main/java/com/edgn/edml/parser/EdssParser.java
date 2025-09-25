package com.edgn.edml.parser;

import com.edgn.edml.component.edss.EdssRule;
import com.edgn.edml.exceptions.EdssParsingException;

import java.util.List;

public interface EdssParser {
    List<EdssRule> parse(String cssContent) throws EdssParsingException;
}