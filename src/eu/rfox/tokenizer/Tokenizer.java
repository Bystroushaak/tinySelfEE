package eu.rfox.tokenizer;

import java.util.ArrayList;


public class Tokenizer {
    private int last_lineno = 1;
    private int start_char_index;
    private int current_char_index;
    private int line_start = 0;

    private final String source;
    private ArrayList<Token> tokens = new ArrayList<Token>();

    public Tokenizer(String source) {
        this.source = source;
    }

    public ArrayList<Token> tokenize() throws TokenizerException {
        while (!this.isAtEnd()) {
            start_char_index = current_char_index;
            scanToken();
        }
        tokens.add(new Token("", TokenType.EOF, last_lineno));
        return tokens;
    }

    private boolean isAtEnd() {
        return current_char_index >= source.length();
    }

    private char advance() {
        current_char_index++;
        char c = source.charAt(current_char_index - 1);
        if (c == '\n') {
            last_lineno++;
            line_start = current_char_index;
        }

        return c;
    }

    private void scanToken() throws UnterminatedStringException {
        char c = advance();
        switch (c) {
            case '(':
                addToken(TokenType.OBJ_START);
                return;
            case ')':
                addToken(TokenType.OBJ_END);
                return;
            case '[':
                addToken(TokenType.BLOCK_START);
                return;
            case ']':
                addToken(TokenType.BLOCK_END);
                return;
            case '|':
                addToken(TokenType.SEPARATOR);
                return;
            case ';':
                addToken(TokenType.CASCADE);
                return;
            case '.':
                addToken(TokenType.END_OF_EXPR);
                return;
            case '^':
                addToken(TokenType.RETURN);
                return;
            case '=':
                addToken(TokenType.ASSIGNMENT);
                return;
            case '"':
                consumeString(TokenType.DOUBLE_Q_STRING);
                return;
            case '\'':
                consumeString(TokenType.SINGLE_Q_STRING);
                return;
        }

        if (c == '<' && peek() == '-') {
            advance();
            addToken(TokenType.RW_ASSIGNMENT);
            return;
        }
    }

    private Token addToken(TokenType type) {
        String text = source.substring(start_char_index, current_char_index);
        return addToken(type, text);
    }

    private Token addToken(TokenType type, String content) {
        Token token = new Token(content, type, last_lineno, start_char_index - line_start,
                current_char_index - line_start);
        tokens.add(token);
        return token;
    }

    private char peek() {
        if (isAtEnd()) return '\0';
        return source.charAt(current_char_index);
    }

    private void consumeString(TokenType token_type) throws UnterminatedStringException {
        char end_char = token_type == TokenType.DOUBLE_Q_STRING ? '"' : '\'';

        while (peek() != end_char && !isAtEnd()) {
            advance();

            if (peek() == '\\') {
                advance();
                advance();
            }
        }

        if (isAtEnd()) {
            throw new UnterminatedStringException(addToken(token_type));
        }

        advance();
        String content = source.substring(start_char_index + 1, current_char_index - 1);
        addToken(token_type, content);
    }
}
