package eu.rfox.oplang.tokenizer;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class TokenizerTest {

    @Test
    public void consumeHexNumber() throws TokenizerException {
        Tokenizer t = new Tokenizer("0x01");
        ArrayList<Token> tokens = t.tokenize();

        Token hex_number = tokens.get(0);
        assertEquals(hex_number.type, TokenType.NUMBER_HEX);
        assertEquals(hex_number.content, "0x01");

        assertEquals(tokens.get(1).type, TokenType.EOF);
    }

    @Test
    public void consumeNumber() throws TokenizerException {
        Tokenizer t = new Tokenizer("123");
        ArrayList<Token> tokens = t.tokenize();

        Token hex_number = tokens.get(0);
        assertEquals(hex_number.type, TokenType.NUMBER);
        assertEquals(hex_number.content, "123");

        assertEquals(tokens.get(1).type, TokenType.EOF);
    }

    @Test
    public void consumeFloatNumber() throws TokenizerException {
        Tokenizer t = new Tokenizer("123.3");
        ArrayList<Token> tokens = t.tokenize();

        Token hex_number = tokens.get(0);
        assertEquals(hex_number.type, TokenType.NUMBER_FLOAT);
        assertEquals(hex_number.content, "123.3");

        assertEquals(tokens.get(1).type, TokenType.EOF);
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
        Tokenizer t = new Tokenizer(" :something ");
        ArrayList<Token> tokens = t.tokenize();

        Token argument = tokens.get(0);
        assertEquals(argument.type, TokenType.ARGUMENT);
        assertEquals(argument.content, "something");

        assertEquals(tokens.get(1).type, TokenType.EOF);
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
}