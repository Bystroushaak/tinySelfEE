package eu.rfox.oplang.parser;

import eu.rfox.oplang.tokenizer.Token;
import eu.rfox.oplang.tokenizer.TokenType;
import eu.rfox.oplang.tokenizer.Tokenizer;
import eu.rfox.oplang.tokenizer.TokenizerException;

import eu.rfox.oplang.parser.ast.ASTItem;

import java.util.ArrayList;

public class Parser {
    private String source_code;
    private ArrayList<ASTItem> ast = new ArrayList<>();
    private ArrayList<ParserException> exceptions = new ArrayList<ParserException>();

    private ArrayList<Token> tokens;
    private int current_token_index = 0;

    public Parser(String source_code) {
        this.source_code = source_code;
    }

    public ArrayList<ASTItem> parse() throws TokenizerException {
        Tokenizer tokenizer = new Tokenizer(source_code);
        tokens = tokenizer.tokenize();
//        for (Token token : tokens) {
//            System.out.println(token.toString());
//        }


        return ast;
    }

    private boolean match(TokenType... types) {
        for (TokenType type : types) {
            if (check(type)) {
                advance();
                return true;
            }
        }

        return false;
    }

    private boolean check(TokenType type) {
        if (isAtEnd()) return false;
        return peek().type == type;
    }

    private Token advance() {
        if (!isAtEnd()) current_token_index++;
        return previous();
    }

    private boolean isAtEnd() {
        return peek().type == TokenType.EOF;
    }

    private Token peek() {
        return tokens.get(current_token_index);
    }

    private Token previous() {
        return tokens.get(current_token_index - 1);
    }
}
