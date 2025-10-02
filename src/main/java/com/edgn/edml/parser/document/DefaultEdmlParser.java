package com.edgn.edml.parser.document;

import com.edgn.edml.dom.components.attributes.TagAttribute;
import com.edgn.edml.core.component.EdmlComponent;
import com.edgn.edml.core.component.registry.ComponentRegistry;
import com.edgn.edml.core.component.registry.EdmlComponentRegistry;
import com.edgn.edml.exceptions.ComponentCreationException;
import com.edgn.edml.exceptions.EdmlParsingException;
import com.edgn.edml.exceptions.LexerException;
import com.edgn.edml.parser.tokenizer.EdmlToken;
import com.edgn.edml.parser.tokenizer.TokenType;
import com.edgn.edml.parser.tokenizer.IEdmlTokenizer;
import com.edgn.edml.parser.tokenizer.EdmlTokenizer;
import com.edgn.edml.parser.tokenizer.TokenIterator;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public final class DefaultEdmlParser implements EdmlParser {
    private final IEdmlTokenizer tokenizer;
    private final ComponentRegistry registry;

    public DefaultEdmlParser() {
        this.registry = EdmlComponentRegistry.getInstance();
        this.tokenizer = new EdmlTokenizer(registry);
    }

    public DefaultEdmlParser(ComponentRegistry registry) {
        this.registry = registry;
        this.tokenizer = new EdmlTokenizer(registry);
    }

    @Override
    public EdmlComponent parseDocument(String edgnContent) throws EdmlParsingException {
        try {
            List<EdmlToken> tokens = tokenizer.tokenize(edgnContent);
            return parseTokens(new TokenIterator(tokens));
        } catch (LexerException | ComponentCreationException e) {
            throw new EdmlParsingException("Failed to parse EDGN document: " + e.getMessage(), e);
        }
    }

    @Override
    public EdmlComponent parseResource(String resourcePath) throws EdmlParsingException {
        try {
            String content = loadResource(resourcePath);
            return parseDocument(content);
        } catch (IOException e) {
            throw new EdmlParsingException("Failed to load resource: " + resourcePath, e);
        }
    }

    private EdmlComponent parseTokens(TokenIterator tokens) throws EdmlParsingException, ComponentCreationException {
        if (!tokens.hasNext()) {
            throw new EdmlParsingException("Empty document");
        }

        EdmlToken openToken = tokens.next();
        if (openToken.type() != TokenType.OPEN_TAG) {
            throw new EdmlParsingException("Expected opening tag, got: " + openToken.type());
        }

        EdmlComponent component = registry.createComponent(openToken.value());
        parseAttributes(tokens, component);

        if (!tokens.hasNext() || tokens.next().type() != TokenType.TAG_END) {
            throw new EdmlParsingException("Expected '>' after tag and attributes");
        }

        parseChildren(tokens, component, openToken.value());
        return component;
    }

    private void parseAttributes(TokenIterator tokens, EdmlComponent component) throws EdmlParsingException {
        while (tokens.hasNext() && tokens.peek().type() == TokenType.ATTRIBUTE_NAME) {
            EdmlToken attrName = tokens.next();

            if (!tokens.hasNext() || tokens.next().type() != TokenType.EQUALS) {
                throw new EdmlParsingException("Expected '=' after attribute name: " + attrName.value());
            }

            if (!tokens.hasNext() || tokens.peek().type() != TokenType.ATTRIBUTE_VALUE) {
                throw new EdmlParsingException("Expected attribute value after '='");
            }

            EdmlToken attrValue = tokens.next();
            component.applyAttribute(attrName.value(), attrValue.value());
        }
    }
    private void parseChildren(TokenIterator tokens, EdmlComponent parent, String expectedClosingTag)
            throws EdmlParsingException, ComponentCreationException {

        StringBuilder currentTextContent = new StringBuilder();

        while (tokens.hasNext()) {
            EdmlToken token = tokens.peek();

            switch (token.type()) {
                case OPEN_TAG -> {
                    saveAccumulatedText(parent, currentTextContent);

                    EdmlComponent child = parseTokens(tokens);
                    parent.addChild(child);
                }
                case CLOSE_TAG -> {
                    saveAccumulatedText(parent, currentTextContent);

                    tokens.next();
                    if (!token.value().equals(expectedClosingTag)) {
                        throw new EdmlParsingException("Mismatched closing tag. Expected: " +
                                expectedClosingTag + ", got: " + token.value());
                    }
                    return;
                }
                case TEXT_CONTENT -> {
                    tokens.next();
                    currentTextContent.append(token.value()).append(" ");
                }
                case TAG_END, WHITESPACE -> tokens.next();
                default -> throw new EdmlParsingException("Unexpected token: " + token.type() +
                        " with value: '" + token.value() + "' while parsing children of: " + expectedClosingTag);
            }
        }

        throw new EdmlParsingException("Unclosed tag: " + expectedClosingTag);
    }

    private void saveAccumulatedText(EdmlComponent parent, StringBuilder textContent) {
        if (!textContent.isEmpty()) {
            String newText = textContent.toString().trim();
            if (!newText.isEmpty()) {
                String existingText = parent.getAttribute(TagAttribute.DATA_TEXT.getProperty(), "");
                String combinedText = existingText.isEmpty() ? newText : existingText + " " + newText;
                parent.applyAttribute(TagAttribute.DATA_TEXT.getProperty(), combinedText);
            }
            textContent.setLength(0);
        }
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
