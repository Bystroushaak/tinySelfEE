package eu.rfox.oplang.tokenizer;

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

        // handle case of == which would have been caught in the switch next
        if (c == '=' && (peek() == '=' || isOperatorCharacter(peek()))) {
            consumeOperator();
            return;
        }

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
            case '#':
                consumeComment();
                return;
            case ' ':
            case '\r':
            case '\t':
            case '\n':
                return;
        }

        if (c == '<' && peek() == '-') {
            advance();
            addToken(TokenType.RW_ASSIGNMENT);
            return;
        }

        if (isDigit(c)) {
            if (peek() == 'x' || peek() == 'X') {
                consumeHexNumber();
                return;
            } else {
                consumeNumber();
                return;
            }
        }

        if (c == ':' && isLowAlpha(peek())) {
            consumeArgument();
            return;
        }

        if (isOperatorCharacter(c)) {
            consumeOperator();
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

    private char peekTwo() {
        current_char_index++;
        char c = peek();
        current_char_index--;

        return c;
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

    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    private void consumeHexNumber() {
        advance();
        advance();

        while (isHexNum(peek())) {
            advance();
        }

        addToken(TokenType.NUMBER_HEX);
    }

    private boolean isHexNum(char c) {
        return isDigit(c) || (c >= 'A' && c <= 'F') || (c >= 'A' && c <= 'F');
    }

    private void consumeNumber() {
        boolean float_number = false;
        while (isDigit(peek()) || (!float_number && (peek() == '.' && isDigit(peekTwo())))) {
            if (advance() == '.') {
                float_number = true;
            }
        }

        if (float_number) {
            addToken(TokenType.NUMBER_FLOAT);
        } else {
            addToken(TokenType.NUMBER);
        }
    }

    private void consumeComment() {
        while (peek() != '\n' && peek() != '\0') {
            advance();
        }

        addToken(TokenType.COMMENT);
    }

    private boolean isLowAlpha(char c) {
        return c >= 'a' && c <= 'z';
    }

    private boolean isBigAlpha(char c) {
        return c >= 'A' && c <= 'Z';
    }

    private boolean isAlphaNumUnderscore(char c) {
        return isLowAlpha(c) || isBigAlpha(c) || isDigit(c) || c == '_';
    }

    private void consumeArgument() {
        start_char_index++;  // ignore : at the beginning
        advance();

        while (isAlphaNumUnderscore(peek())) {
            advance();
        }

        addToken(TokenType.ARGUMENT);
    }

    private boolean isOperatorCharacter(char c) {
        switch (c) {
            case '!':
            case '@':
            case '$':
            case '%':
            case '&':
            case '*':
            case '-':
            case '+':
            case '/':
            case '~':
            case '?':
            case '<':
            case '>':
            case ',':
                return true;
        }

        return false;
    }

    private void consumeOperator() {
        while (peek() == '=' || isOperatorCharacter(peek())) {
            advance();
        }

        addToken(TokenType.OPERATOR);
    }

}
