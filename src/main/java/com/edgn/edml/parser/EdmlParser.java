package com.edgn.edml.parser;

import com.edgn.edml.component.edml.component.EdmlComponent;
import com.edgn.edml.exceptions.EdmlParsingException;

public interface EdmlParser {
    EdmlComponent parseDocument(String edgnContent) throws EdmlParsingException;
    EdmlComponent parseResource(String resourcePath) throws EdmlParsingException;
}