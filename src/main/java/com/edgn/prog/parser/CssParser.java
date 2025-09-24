package com.edgn.prog.parser;

import com.edgn.prog.component.css.CssRule;
import com.edgn.prog.exceptions.CssParsingException;

import java.util.List;

public interface CssParser {
    List<CssRule> parse(String cssContent) throws CssParsingException;
}