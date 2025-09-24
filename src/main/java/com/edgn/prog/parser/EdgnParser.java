package com.edgn.prog.parser;

import com.edgn.prog.component.html.EdgnComponent;
import com.edgn.prog.exceptions.EdgnParsingException;

public interface EdgnParser {
    EdgnComponent parseDocument(String edgnContent) throws EdgnParsingException;
    EdgnComponent parseResource(String resourcePath) throws EdgnParsingException;
}