package com.edgn.edml.tokenizer;

import com.edgn.edml.component.html.ComponentRegistry;
import com.edgn.edml.exceptions.LexerException;
import com.edgn.edml.tokenizer.enums.TokenType;
import com.edgn.edml.tokenizer.enums.TokenizerState;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public record EdmlTokenizer(ComponentRegistry registry) implements IEdmlTokenizer {
    public EdmlTokenizer(ComponentRegistry registry) {
        this.registry = Objects.requireNonNull(registry);
    }

    @Override
    public List<EdmlToken> tokenize(String input) throws LexerException {
        if (input == null || input.isBlank()) {
            return List.of(new EdmlToken(TokenType.EOF, "", new Position(1, 1, 0)));
        }

        List<EdmlToken> tokens = new ArrayList<>();
        TokenizerContext context = new TokenizerContext(input);

        while (!context.isAtEnd()) {
            EdmlToken token = nextToken(context);
            if (token != null) {
                tokens.add(token);
            }
        }

        tokens.add(new EdmlToken(TokenType.EOF, "", context.getCurrentPosition()));

        return tokens;
    }

    private EdmlToken nextToken(TokenizerContext context) throws LexerException {
        skipWhitespace(context);

        if (context.isAtEnd()) {
            return null;
        }

        try {
            return switch (context.getState()) {
                case TEXT -> handleTextState(context);
                case TAG_OPENING -> handleTagOpeningState(context);
                case TAG_NAME -> handleTagNameState(context);
                case ATTRIBUTES -> handleAttributesState(context);
                case TAG_CONTENT -> handleTagContentState(context);
            };
        } catch (Exception e) {
            throw new LexerException("Tokenization error at " + context.getDebugInfo() + ": " + e.getMessage(), context.getCurrentPosition());
        }
    }

    private EdmlToken handleTextState(TokenizerContext context) throws LexerException {
        if (context.peek() == '<') {
            context.setState(TokenizerState.TAG_OPENING);
            return handleTagOpeningState(context);
        }

        Position startPos = context.getCurrentPosition();
        StringBuilder content = new StringBuilder();

        while (!context.isAtEnd() && context.peek() != '<') {
            content.append(context.advance());
        }

        String text = content.toString().trim();
        if (text.isEmpty()) {
            return null;
        }

        return new EdmlToken(TokenType.TEXT_CONTENT, text, startPos);
    }

    private EdmlToken handleTagOpeningState(TokenizerContext context) throws LexerException {
        Position startPos = context.getCurrentPosition();
        context.advance(); // consume '<'

        if (context.peek() == '/') {
            // Closing tag: </div>
            context.advance(); // consume '/'
            String tagName = readTagName(context);

            if (!registry.isTagRegistered(tagName)) {
                throw new LexerException("Unknown closing tag: " + tagName, startPos);
            }

            context.setState(TokenizerState.TAG_NAME); // Expect '>' next
            return new EdmlToken(TokenType.CLOSE_TAG, tagName, startPos);
        } else {
            // Opening tag: <div
            String tagName = readTagName(context);

            if (!registry.isTagRegistered(tagName)) {
                throw new LexerException("Unknown opening tag: " + tagName, startPos);
            }

            context.setState(TokenizerState.ATTRIBUTES); // Expect attributes or '>' next
            return new EdmlToken(TokenType.OPEN_TAG, tagName, startPos);
        }
    }

    private EdmlToken handleTagNameState(TokenizerContext context) throws LexerException {
        skipWhitespace(context);

        if (context.peek() == '>') {
            Position pos = context.getCurrentPosition();
            context.advance(); // consume '>'
            context.setState(TokenizerState.TEXT);
            return new EdmlToken(TokenType.TAG_END, ">", pos);
        }

        throw new LexerException("Expected '>' after closing tag name", context.getCurrentPosition());
    }

    private EdmlToken handleAttributesState(TokenizerContext context) throws LexerException {
        skipWhitespace(context);

        if (context.isAtEnd()) {
            throw new LexerException("Unexpected end of input while parsing attributes", context.getCurrentPosition());
        }

        char current = context.peek();
        Position startPos = context.getCurrentPosition();

        if (current == '>') {
            context.advance(); // consume '>'
            context.setState(TokenizerState.TEXT);
            return new EdmlToken(TokenType.TAG_END, ">", startPos);
        }

        if (current == '/') {
            // Self-closing tag: <br/>
            if (context.peekNext() == '>') {
                context.advance(); // consume '/'
                context.advance(); // consume '>'
                context.setState(TokenizerState.TEXT);
                return new EdmlToken(TokenType.SELF_CLOSING_TAG, "/>", startPos);
            }
        }

        if (current == '=') {
            context.advance(); // consume '='
            return new EdmlToken(TokenType.EQUALS, "=", startPos);
        }

        if (current == '"' || current == '\'') {
            return handleQuotedString(context);
        }

        // Must be an attribute name
        String attributeName = readAttributeName(context);

        if (attributeName.isEmpty()) {
            throw new LexerException("Expected attribute name", startPos);
        }

        return new EdmlToken(TokenType.ATTRIBUTE_NAME, attributeName, startPos);
    }

    private EdmlToken handleTagContentState(TokenizerContext context) {
        // This state transitions immediately to TEXT
        context.setState(TokenizerState.TEXT);
        return null; // Will be handled in next iteration
    }

    private EdmlToken handleQuotedString(TokenizerContext context) throws LexerException {
        Position startPos = context.getCurrentPosition();
        char quote = context.advance(); // consume opening quote
        StringBuilder value = new StringBuilder();

        while (!context.isAtEnd() && context.peek() != quote) {
            char ch = context.advance();
            if (ch == '\\' && !context.isAtEnd()) {
                char escaped = context.advance();
                value.append(switch (escaped) {
                    case 'n' -> '\n';
                    case 't' -> '\t';
                    case 'r' -> '\r';
                    case '\\' -> '\\';
                    case '"' -> '"';
                    case '\'' -> '\'';
                    default -> escaped;
                });
            } else {
                value.append(ch);
            }
        }

        if (context.isAtEnd()) {
            throw new LexerException("Unterminated string starting with " + quote, startPos);
        }

        context.advance(); // consume closing quote
        return new EdmlToken(TokenType.ATTRIBUTE_VALUE, value.toString(), startPos);
    }

    private String readTagName(TokenizerContext context) throws LexerException {
        StringBuilder name = new StringBuilder();

        while (!context.isAtEnd() && isTagNameChar(context.peek())) {
            name.append(context.advance());
        }

        if (name.isEmpty()) {
            throw new LexerException("Expected tag name", context.getCurrentPosition());
        }

        return name.toString();
    }

    private String readAttributeName(TokenizerContext context) {
        StringBuilder name = new StringBuilder();

        while (!context.isAtEnd() && isAttributeNameChar(context.peek())) {
            name.append(context.advance());
        }

        return name.toString();
    }

    private void skipWhitespace(TokenizerContext context) {
        while (!context.isAtEnd() && Character.isWhitespace(context.peek())) {
            context.advance();
        }
    }

    private static boolean isTagNameChar(char c) {
        return Character.isLetterOrDigit(c) || c == '-' || c == '_';
    }

    private static boolean isAttributeNameChar(char c) {
        return Character.isLetterOrDigit(c) || c == '-' || c == '_' || c == ':';
    }
}