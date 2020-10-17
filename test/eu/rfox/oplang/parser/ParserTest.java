package eu.rfox.oplang.parser;

import eu.rfox.oplang.parser.ast.*;
import eu.rfox.oplang.tokenizer.TokenizerException;
import org.junit.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void parseIntNumber() throws TokenizerException, ParserException {
        Parser p = new Parser("3");
        ArrayList<ASTItem> ast = p.parse();

        assertEquals(ast.get(0), new NumberInt(3));
    }

    @Test
    public void parseFloatNumber() throws TokenizerException, ParserException {
        Parser p = new Parser("3.5");
        ArrayList<ASTItem> ast = p.parse();

        assertEquals(ast.get(0), new NumberFloat((float) 3.5));
    }

    @Test
    public void parseSelf() throws TokenizerException, ParserException {
        Parser p = new Parser("self");
        ArrayList<ASTItem> ast = p.parse();

        assertEquals(ast.get(0), new Self());
    }

    @Test
    public void parseSingleQString() throws TokenizerException, ParserException {
        Parser p = new Parser("'something'");
        ArrayList<ASTItem> ast = p.parse();

        assertEquals(ast.get(0), new Str("something"));
    }

    @Test
    public void parseDoubleQString() throws TokenizerException, ParserException {
        Parser p = new Parser("\"something\"");
        ArrayList<ASTItem> ast = p.parse();

        assertEquals(ast.get(0), new Str("something"));
    }

    @Test
    public void parseUnaryMessageSingle() throws TokenizerException, ParserException {
        Parser p = new Parser("message");
        ArrayList<ASTItem> ast = p.parse();

        assertEquals(ast.get(0), new Send(new MessageUnary("message")));
    }

    @Test
    public void parseUnaryMessageToSomething() throws TokenizerException, ParserException {
        Parser p = new Parser("1 message");
        ArrayList<ASTItem> ast = p.parse();

        assertEquals(ast.get(0), new Send(new NumberInt(1),
                new MessageUnary("message")));
    }

    @Test
    public void parseBinaryMessage() throws TokenizerException, ParserException {
        Parser p = new Parser("1 + 2");
        ArrayList<ASTItem> ast = p.parse();

        assertEquals(ast.get(0), new Send(new NumberInt(1),
                new MessageBinary("+", new NumberInt(2))));
    }

    @Test
    public void parseSingleKeywordMessage() throws TokenizerException, ParserException {
        Parser p = new Parser("1 send: 2");
        ArrayList<ASTItem> ast = p.parse();

        assertEquals(ast.get(0), new Send(new NumberInt(1),
                new MessageKeyword("send:", new NumberInt(2))));
    }

    @Test
    public void parseKeywordMessage() throws TokenizerException, ParserException {
        Parser p = new Parser("1 send: 2 And: 3");
        ArrayList<ASTItem> ast = p.parse();

        ArrayList<ASTItem> parameters = new ArrayList<ASTItem>(Arrays.asList(new NumberInt(2), new NumberInt(3)));
        MessageKeyword msg = new MessageKeyword("send:And:", parameters);
        assertEquals(ast.get(0), new Send(new NumberInt(1), msg));
    }
}