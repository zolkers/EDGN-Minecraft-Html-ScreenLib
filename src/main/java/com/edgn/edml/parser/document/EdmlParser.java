package com.edgn.edml.parser.document;

import com.edgn.edml.core.component.EdmlComponent;
import com.edgn.edml.exceptions.EdmlParsingException;

public interface EdmlParser {
    EdmlComponent parseDocument(String edgnContent) throws EdmlParsingException;
    EdmlComponent parseResource(String resourcePath) throws EdmlParsingException;
}