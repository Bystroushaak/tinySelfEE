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
}