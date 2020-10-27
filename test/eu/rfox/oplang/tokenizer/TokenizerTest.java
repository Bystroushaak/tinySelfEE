package eu.rfox.oplang.tokenizer;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class TokenizerTest {
    Token getOneIdentifierWithEOFCheck(String source) throws TokenizerException {
        Tokenizer t = new Tokenizer(source);
        ArrayList<Token> tokens = t.tokenize();

        Token identifier = tokens.get(0);

        assertEquals(tokens.get(1).type, TokenType.EOF);

        return identifier;
    }

    void checkOneIdentifier(String source, TokenType expected_type, String content) throws TokenizerException {
        Token t = getOneIdentifierWithEOFCheck(source);
        assertEquals(t.type, expected_type);
        assertEquals(t.content, content);
    }

    @Test
    public void consumeDoubleQuoteString() throws TokenizerException {
        checkOneIdentifier("\"some string\"", TokenType.DOUBLE_Q_STRING, "some string");
    }

    @Test
    public void consumeSingleQuoteString() throws TokenizerException {
        checkOneIdentifier("'some string'", TokenType.SINGLE_Q_STRING, "some string");
    }

    @Test
    public void consumeHexNumber() throws TokenizerException {
        checkOneIdentifier("0x01", TokenType.NUMBER_HEX, "0x01");
    }

    @Test
    public void consumeNumber() throws TokenizerException {
        checkOneIdentifier("123", TokenType.NUMBER, "123");
    }

    @Test
    public void consumeFloatNumber() throws TokenizerException {
        checkOneIdentifier("123.3", TokenType.NUMBER_FLOAT, "123.3");
    }

    @Test
    public void consumeFloatNumberMultipleDots() throws TokenizerException {
        Tokenizer t = new Tokenizer("123.3.4");
        ArrayList<Token> tokens = t.tokenize();

        Token hex_number = tokens.get(0);
        assertEquals(hex_number.type, TokenType.NUMBER_FLOAT);
        assertEquals(hex_number.content, "123.3");

        assertEquals(tokens.get(1).type, TokenType.END_OF_EXPR);

        Token number = tokens.get(2);
        assertEquals(number.type, TokenType.NUMBER);
        assertEquals(number.content, "4");

        assertEquals(tokens.get(3).type, TokenType.EOF);
    }

    @Test
    public void consumeComment() throws TokenizerException {
        Tokenizer t = new Tokenizer("1 # comment\n");
        ArrayList<Token> tokens = t.tokenize();

        Token number = tokens.get(0);
        assertEquals(number.type, TokenType.NUMBER);
        assertEquals(number.content, "1");

        Token comment = tokens.get(1);
        assertEquals(comment.type, TokenType.COMMENT);
        assertEquals(comment.content, "# comment");

        assertEquals(tokens.get(2).type, TokenType.EOF);
    }

    @Test
    public void consumeCommentWithEOF() throws TokenizerException {
        Tokenizer t = new Tokenizer("1 # comment");
        ArrayList<Token> tokens = t.tokenize();

        Token number = tokens.get(0);
        assertEquals(number.type, TokenType.NUMBER);
        assertEquals(number.content, "1");

        Token comment = tokens.get(1);
        assertEquals(comment.type, TokenType.COMMENT);
        assertEquals(comment.content, "# comment");

        assertEquals(tokens.get(2).type, TokenType.EOF);
    }

    @Test
    public void consumeArgument() throws TokenizerException {
        checkOneIdentifier(" :something ", TokenType.ARGUMENT, "something");
    }

    @Test
    public void consumeOperator() throws TokenizerException {
        Tokenizer t = new Tokenizer("= == != +");
        ArrayList<Token> tokens = t.tokenize();

        Token assignment = tokens.get(0);
        assertEquals(assignment.type, TokenType.ASSIGNMENT);
        assertEquals(assignment.content, "=");

        Token operator = tokens.get(1);
        assertEquals(operator.type, TokenType.OPERATOR);
        assertEquals(operator.content, "==");

        operator = tokens.get(2);
        assertEquals(operator.type, TokenType.OPERATOR);
        assertEquals(operator.content, "!=");

        operator = tokens.get(3);
        assertEquals(operator.type, TokenType.OPERATOR);
        assertEquals(operator.content, "+");

        assertEquals(tokens.get(4).type, TokenType.EOF);
    }

    @Test
    public void consumeFirstKeyword() throws TokenizerException {
        checkOneIdentifier(" something: ", TokenType.FIRST_KW, "something:");
    }

    @Test
    public void consumeMultikeyword() throws TokenizerException {
        checkOneIdentifier("some:Something:", TokenType.FIRST_KW, "some:Something:");
    }

    @Test
    public void consumeKeywords() throws TokenizerException {
        checkOneIdentifier(" Something: ", TokenType.KEYWORD, "Something:");
    }

    @Test
    public void consumeIdentifier() throws TokenizerException {
        checkOneIdentifier(" Someth1n_g", TokenType.IDENTIFIER, "Someth1n_g");
    }

    @Test
    public void consumeIdentifierWithStar() throws TokenizerException {
        checkOneIdentifier("parent*", TokenType.IDENTIFIER, "parent*");
    }

    @Test
    public void consumeIdentifierWithDot() throws TokenizerException {
        checkOneIdentifier("parent.something", TokenType.IDENTIFIER, "parent.something");
    }

    @Test
    public void consumeSelf() throws TokenizerException {
        Tokenizer t = new Tokenizer("self Someth1n_g self");
        ArrayList<Token> tokens = t.tokenize();

        Token self = tokens.get(0);
        assertEquals(self.type, TokenType.SELF);
        assertEquals(self.content, "self");

        Token identifier = tokens.get(1);
        assertEquals(identifier.type, TokenType.IDENTIFIER);
        assertEquals(identifier.content, "Someth1n_g");

        self = tokens.get(2);
        assertEquals(self.type, TokenType.SELF);
        assertEquals(self.content, "self");

        assertEquals(tokens.get(3).type, TokenType.EOF);
    }
}