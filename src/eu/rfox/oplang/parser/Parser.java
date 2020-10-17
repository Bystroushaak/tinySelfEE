package eu.rfox.oplang.parser;

import eu.rfox.oplang.parser.ast.*;
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

    public ArrayList<ASTItem> parse() throws TokenizerException, ParserException {
        Tokenizer tokenizer = new Tokenizer(source_code);
        tokens = tokenizer.tokenize();
//        for (Token token : tokens) {
//            System.out.println(token.toString());
//        }

        while (!isAtEnd()) {
            ast.add(parseCascade());
        }

        return ast;
    }

    private ASTItem parseExpression() {
        ASTItem literal = parseLiterals();
        if (literal != null) return literal;

        if (isMessage()) {
            return parseMessage();
        }

        if (isCascade()) {
            return parseCascade();
        }

        advance();
        return parseExpression();
    }

    private ASTItem parseLiterals() {
        switch (current().type) {
            case SELF:
                return parseSelf();
            case DOUBLE_Q_STRING:
            case SINGLE_Q_STRING:
                return parseStr();
            case NUMBER:
                return parseInt();
            case NUMBER_FLOAT:
                return parseFloat();
            case COMMENT:
                return parseComment();
            case RETURN:
                return parseReturn();
//            case NUMBER_HEX:
//                return parseHexNumber();
        }
        return null;
    }

    private ASTItem parseSelf() {
        advance();
        return new Self();
    }

    private ASTItem parseStr() {
        Token string_token = advance();

        if (string_token.type == TokenType.SINGLE_Q_STRING) {
            return new Str(Str.unescape(string_token.content, '\''));
        }

        return new Str(Str.unescape(string_token.content, '"'));
    }

    private ASTItem parseInt() {
        return new NumberInt(Integer.parseInt(advance().content));
    }

    private ASTItem parseFloat() {
        return new NumberFloat(Float.parseFloat(advance().content));
    }

    private ASTItem parseComment() {
        return new Comment(advance().content);
    }

    private ASTItem parseReturn() {
        advance();
        return new Return(parseExpression());
    }

    private ASTItem parseMessage() {
        if (isUnaryMessage()) {
            return parseUnaryMessage();
        } else if (isBinaryMessage()) {
            return parseBinaryMessage();
        } else if (isKeywordMessage()) {
            return parseKeywordMessage();
        }

        return parseExpression();
    }

    private boolean isMessage() {
        return isUnaryMessage() || isBinaryMessage() || isKeywordMessage();
    }

    private boolean isUnaryMessage() {
        return check_current(TokenType.IDENTIFIER) || check_next(TokenType.IDENTIFIER);
    }

    private boolean isBinaryMessage() {
        return check_next(TokenType.OPERATOR, TokenType.ASSIGNMENT);
    }

    private boolean isKeywordMessage() {
        return check_current(TokenType.FIRST_KW) || check_next(TokenType.FIRST_KW);
    }

    private ASTItem parseUnaryMessage() {
        if (check_current(TokenType.IDENTIFIER)) {
            return new Send(new MessageUnary(advance().content));
        }

        return new Send(parseExpression(), new MessageUnary(advance().content));
    }

    private ASTItem parseBinaryMessage() {
        return new Send(parseExpression(), new MessageBinary(advance().content,
                parseExpression()));
    }

    private ASTItem parseKeywordMessage() {
        if (check_current(TokenType.FIRST_KW)) {
            MessageKeyword kwd_msg = new MessageKeyword(advance().content, parseExpression());
            return new Send(tryConsumeKeywordPairs(kwd_msg));
        }

        ASTItem expr = parseExpression();
        MessageKeyword kwd_msg = new MessageKeyword(advance().content, parseExpression());
        return new Send(expr, tryConsumeKeywordPairs(kwd_msg));
    }

    private MessageKeyword tryConsumeKeywordPairs(MessageKeyword kwd_msg) {
        while (check_current(TokenType.KEYWORD)) {
            kwd_msg.addPair(parseSingleKeywordPair());
        }

        return kwd_msg;
    }

    private KeywordPair parseSingleKeywordPair() {
        return new KeywordPair(advance().content, parseExpression());
    }

    private boolean isCascade() {
        return check_current(TokenType.CASCADE);
    }

    private ASTItem parseCascade() {
        ASTItem expr = parseMessage();

        if (check_current(TokenType.CASCADE)) {
            Send first_send = (Send) expr;
            Cascade cascade = new Cascade(first_send.obj, first_send.message);

            advance(); // consume cascade token
            Send another_send = (Send) parseMessage();
            cascade.addMessage(another_send.message);

            while (isCascade()) {
                advance(); // consume cascade token
                another_send = (Send) parseMessage();
                cascade.addMessage(another_send.message);
            }

            return cascade;
        }

        return expr;
    }

    private boolean match_any(TokenType... types) {
        for (TokenType type : types) {
            if (check_next(type)) {
                advance();
                return true;
            }
        }

        return false;
    }

    private boolean check_current(TokenType... types) {
        for (TokenType type : types) {
            if (check_current(type)) {
                return true;
            }
        }

        return false;
    }

    private boolean check_current(TokenType type) {
        if (isAtEnd()) return false;
        return current().type == type;
    }

    private boolean check_next(TokenType... types) {
        for (TokenType type : types) {
            if (check_next(type)) {
                return true;
            }
        }

        return false;
    }

    private boolean check_next(TokenType type) {
        if (isAtEnd()) return false;
        return peek().type == type;
    }

    private Token advance() {
        if (!isAtEnd()) current_token_index++;
        return previous();
    }

    private boolean isAtEnd() {
        return current().type == TokenType.EOF;
    }

    private Token current() {
        return tokens.get(current_token_index);
    }

    private Token peek() {
        return tokens.get(current_token_index + 1);
    }

    private Token previous() {
        if ((current_token_index - 1) < 0) {
            return new Token("", TokenType.EOF, -1);
        }

        return tokens.get(current_token_index - 1);
    }
}
