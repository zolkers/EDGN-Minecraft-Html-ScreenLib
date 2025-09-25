package com.edgn.edml.parser;

import com.edgn.edml.component.html.EdmlComponent;
import com.edgn.edml.exceptions.EdmlParsingException;

public interface EdmlParser {
    EdmlComponent parseDocument(String edgnContent) throws EdmlParsingException;
    EdmlComponent parseResource(String resourcePath) throws EdmlParsingException;
}