package eu.rfox.oplang.parser;

import eu.rfox.oplang.tokenizer.Token;

public class ParserException extends Exception {
    Token token_start;
    Token token_end;

    ParserException(String message) {
        super(message);
    }

    ParserException(String message, Token token_start) {
        this(message, token_start, null);
    }

    ParserException(String message, Token token_start, Token token_end) {
        super(message);
        this.token_start = token_start;
        this.token_end = token_end;
    }
}
