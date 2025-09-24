package com.edgn.prog.parser;

import com.edgn.prog.component.html.EdgnComponent;
import com.edgn.prog.component.html.ComponentRegistry;
import com.edgn.prog.component.html.EdgnComponentRegistry;
import com.edgn.prog.exceptions.ComponentCreationException;
import com.edgn.prog.exceptions.EdgnParsingException;
import com.edgn.prog.exceptions.LexerException;
import com.edgn.prog.tokenizer.EdgnToken;
import com.edgn.prog.tokenizer.enums.TokenType;
import com.edgn.prog.tokenizer.EdgnTokenizer;
import com.edgn.prog.tokenizer.HtmlEdgnTokenizer;
import com.edgn.prog.tokenizer.TokenIterator;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public final class DefaultEdgnParser implements EdgnParser {
    private final EdgnTokenizer tokenizer;
    private final ComponentRegistry registry;

    public DefaultEdgnParser() {
        this.registry = EdgnComponentRegistry.getInstance();
        this.tokenizer = new HtmlEdgnTokenizer(registry);
    }

    @Override
    public EdgnComponent parseDocument(String edgnContent) throws EdgnParsingException {
        try {
            List<EdgnToken> tokens = tokenizer.tokenize(edgnContent);
            return parseTokens(new TokenIterator(tokens));
        } catch (LexerException | ComponentCreationException e) {
            throw new EdgnParsingException("Failed to parse EDGN document: " + e.getMessage(), e);
        }
    }

    @Override
    public EdgnComponent parseResource(String resourcePath) throws EdgnParsingException {
        try {
            String content = loadResource(resourcePath);
            return parseDocument(content);
        } catch (IOException e) {
            throw new EdgnParsingException("Failed to load resource: " + resourcePath, e);
        }
    }

    private EdgnComponent parseTokens(TokenIterator tokens) throws EdgnParsingException, ComponentCreationException {
        if (!tokens.hasNext()) {
            throw new EdgnParsingException("Empty document");
        }

        EdgnToken openToken = tokens.next();
        if (openToken.type() != TokenType.OPEN_TAG) {
            throw new EdgnParsingException("Expected opening tag, got: " + openToken.type());
        }

        EdgnComponent component = registry.createComponent(openToken.value());
        parseAttributes(tokens, component);

        if (!tokens.hasNext() || tokens.next().type() != TokenType.TAG_END) {
            throw new EdgnParsingException("Expected '>' after tag and attributes");
        }

        parseChildren(tokens, component, openToken.value());
        return component;
    }

    private void parseAttributes(TokenIterator tokens, EdgnComponent component) throws EdgnParsingException {
        while (tokens.hasNext() && tokens.peek().type() == TokenType.ATTRIBUTE_NAME) {
            EdgnToken attrName = tokens.next();

            if (!tokens.hasNext() || tokens.next().type() != TokenType.EQUALS) {
                throw new EdgnParsingException("Expected '=' after attribute name: " + attrName.value());
            }

            if (!tokens.hasNext() || tokens.peek().type() != TokenType.ATTRIBUTE_VALUE) {
                throw new EdgnParsingException("Expected attribute value after '='");
            }

            EdgnToken attrValue = tokens.next();
            component.applyAttribute(attrName.value(), attrValue.value());
        }
    }

    private void parseChildren(TokenIterator tokens, EdgnComponent parent, String expectedClosingTag)
            throws EdgnParsingException, ComponentCreationException {

        StringBuilder textContent = new StringBuilder();

        while (tokens.hasNext()) {
            EdgnToken token = tokens.peek();

            switch (token.type()) {
                case OPEN_TAG -> {
                    if (!textContent.isEmpty()) {
                        parent.applyAttribute("data-text", textContent.toString().trim());
                        textContent.setLength(0);
                    }

                    EdgnComponent child = parseTokens(tokens);
                    parent.addChild(child);
                }
                case CLOSE_TAG -> {
                    if (!textContent.isEmpty()) {
                        parent.applyAttribute("data-text", textContent.toString().trim());
                    }

                    tokens.next();
                    if (!token.value().equals(expectedClosingTag)) {
                        throw new EdgnParsingException("Mismatched closing tag. Expected: " +
                                expectedClosingTag + ", got: " + token.value());
                    }
                    return;
                }
                case TEXT_CONTENT -> {
                    tokens.next();
                    textContent.append(token.value()).append(" ");
                }
                case TAG_END, WHITESPACE -> tokens.next();
                default -> throw new EdgnParsingException("Unexpected token: " + token.type() +
                        " with value: '" + token.value() + "' while parsing children of: " + expectedClosingTag);
            }
        }

        throw new EdgnParsingException("Unclosed tag: " + expectedClosingTag);
    }

    private String loadResource(String resourcePath) throws IOException {
        try (var inputStream = getClass().getResourceAsStream(resourcePath)) {
            if (inputStream == null) {
                throw new IOException("Resource not found: " + resourcePath);
            }
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        }
    }
}