package eu.rfox.oplang.parser;

import eu.rfox.oplang.parser.ast.*;
import eu.rfox.oplang.tokenizer.Token;
import eu.rfox.oplang.tokenizer.TokenType;
import eu.rfox.oplang.tokenizer.Tokenizer;
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
    public void parseHexNumber() throws TokenizerException, ParserException {
        Parser p = new Parser("0xFFAA01");
        ArrayList<ASTItem> ast = p.parse();

        assertEquals(ast.get(0), new NumberInt(16755201));
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

    @Test
    public void parseCascade() throws TokenizerException, ParserException {
        Parser p = new Parser("1 msg; another");
        ArrayList<ASTItem> ast = p.parse();

        ArrayList<MessageBase> messages = new ArrayList<>(Arrays.asList(new MessageUnary("msg"),
                                                                        new MessageUnary("another")));
        assertEquals(ast.get(0), new Cascade(new NumberInt(1), messages));
    }

    @Test
    public void parseMultiCascade() throws TokenizerException, ParserException {
        Parser p = new Parser("1 msg; another; keyword: 1");
        ArrayList<ASTItem> ast = p.parse();

        ArrayList<MessageBase> messages = new ArrayList<>(Arrays.asList(new MessageUnary("msg"),
                                                                        new MessageUnary("another"),
                                                                        new MessageKeyword("keyword:", new NumberInt(1))));
        assertEquals(ast.get(0), new Cascade(new NumberInt(1), messages));
    }

    @Test
    public void parseReturn() throws TokenizerException, ParserException {
        Parser p = new Parser("^ 1");
        ArrayList<ASTItem> ast = p.parse();

        assertEquals(ast.get(0), new Return(new NumberInt(1)));
    }

    @Test
    public void parseEmptyObject() throws TokenizerException, ParserException {
        Parser p = new Parser("()");
        ArrayList<ASTItem> ast = p.parse();

        assertEquals(ast.get(0), new Obj());
    }

    @Test
    public void parseEmptyObjectWithOneSeparator() throws TokenizerException, ParserException {
        Parser p = new Parser("(|)");
        ArrayList<ASTItem> ast = p.parse();

        assertEquals(ast.get(0), new Obj());
    }

    @Test
    public void parseEmptyObjectWithTwoSeparators() throws TokenizerException, ParserException {
        Parser p = new Parser("(||)");
        ArrayList<ASTItem> ast = p.parse();

        assertEquals(ast.get(0), new Obj());
    }

    @Test
    public void parseEmptyObjectWithCode() throws TokenizerException, ParserException {
        Parser p = new Parser("(|| 1 asd)");
        ArrayList<ASTItem> ast = p.parse();

        Obj o = new Obj();
        o.addCode(new Send(new NumberInt(1), new MessageUnary("asd")));

        assertEquals(ast.get(0), o);
    }

    @Test
    public void parseEmptyObjectWithMultipleCodeExpr() throws TokenizerException, ParserException {
        Parser p = new Parser("(|| 1 asd. 2 xxx)");
        ArrayList<ASTItem> ast = p.parse();

        Obj o = new Obj();
        o.addCode(new Send(new NumberInt(1), new MessageUnary("asd")));
        o.addCode(new Send(new NumberInt(2), new MessageUnary("xxx")));

        assertEquals(ast.get(0), o);
    }

    @Test
    public void parseEmptyObjectWithMultipleCodeAndDotAtTheEnd() throws TokenizerException, ParserException {
        Parser p = new Parser("(|| 1 asd. 2 xxx.)");
        ArrayList<ASTItem> ast = p.parse();

        Obj o = new Obj();
        o.addCode(new Send(new NumberInt(1), new MessageUnary("asd")));
        o.addCode(new Send(new NumberInt(2), new MessageUnary("xxx")));

        assertEquals(ast.get(0), o);
    }

    @Test
    public void parseObjectWithSlot() throws TokenizerException, ParserException {
        Parser p = new Parser("(| asd |)");
        ArrayList<ASTItem> ast = p.parse();

        Obj o = new Obj();
        o.addSlot("asd", new Nil());

        assertEquals(ast.get(0), o);
    }

    @Test
    public void parseObjectWithSlotsAndOneSeparator() throws TokenizerException, ParserException {
        Parser p = new Parser("( asd |)");
        ArrayList<ASTItem> ast = p.parse();

        Obj o = new Obj();
        o.addSlot("asd", new Nil());

        assertEquals(ast.get(0), o);
    }

    @Test
    public void parseObjectWithSlotAssign() throws TokenizerException, ParserException {
        Parser p = new Parser("(| asd = 1 |)");
        ArrayList<ASTItem> ast = p.parse();

        Obj o = new Obj();
        o.addSlot("asd", new NumberInt(1));

        assertEquals(ast.get(0), o);
    }

    @Test
    public void parseObjectWithMultipleSlots() throws TokenizerException, ParserException {
        Parser p = new Parser("(| xxx. asd = 1. |)");
        ArrayList<ASTItem> ast = p.parse();

        Obj o = new Obj();
        o.addSlot("xxx", new Nil());
        o.addSlot("asd", new NumberInt(1));

        assertEquals(ast.get(0), o);
    }

    @Test
    public void parseObjectWithMultipleSlotsAndKw() throws TokenizerException, ParserException {
        Parser p = new Parser("(| xxx. asd = 1. keyword: a = (). |)");
        ArrayList<ASTItem> ast = p.parse();

        Obj o = new Obj();
        o.addSlot("xxx", new Nil());
        o.addSlot("asd", new NumberInt(1));

        Obj obj_with_one_argument_a = new Obj();
        obj_with_one_argument_a.addArgument("a");
        o.addSlot("keyword:", obj_with_one_argument_a);

        assertEquals(ast.get(0), o);
    }

    @Test
    public void parseObjectWithMultipleSlotsAndOperator() throws TokenizerException, ParserException {
        Parser p = new Parser("(| xxx. asd = 1. keyword: a = (). == b = (). |)");
        ArrayList<ASTItem> ast = p.parse();

        Obj o = new Obj();
        o.addSlot("xxx", new Nil());
        o.addSlot("asd", new NumberInt(1));

        Obj obj_with_one_argument_a = new Obj();
        obj_with_one_argument_a.addArgument("a");
        o.addSlot("keyword:", obj_with_one_argument_a);

        Obj operator_obj = new Obj();
        operator_obj.addArgument("b");
        o.addSlot("==", operator_obj);

        assertEquals(ast.get(0), o);
    }

    @Test
    public void parseObjectWithSlotsAndCode() throws TokenizerException, ParserException {
        Parser p = new Parser("(| xxx. asd = 1. | self xx)");
        ArrayList<ASTItem> ast = p.parse();

        Obj o = new Obj();
        o.addSlot("xxx", new Nil());
        o.addSlot("asd", new NumberInt(1));
        o.addCode(new Send(new MessageUnary("xx")));

        assertEquals(ast.get(0), o);
    }

    @Test
    public void parseObjectWithSlotsAndMultipleCodeStatements() throws TokenizerException, ParserException {
        Parser p = new Parser("(| xxx. asd = 1. | self xx. asd. ^ 1.)");
        ArrayList<ASTItem> ast = p.parse();

        Obj o = new Obj();
        o.addSlot("xxx", new Nil());
        o.addSlot("asd", new NumberInt(1));
        o.addCode(new Send(new MessageUnary("xx")));
        o.addCode(new Send(new MessageUnary("asd")));
        o.addCode(new Return(new NumberInt(1)));

        assertEquals(ast.get(0), o);
    }

    @Test
    public void parseBlockWithSlotsAndMultipleCodeStatements() throws TokenizerException, ParserException {
        Parser p = new Parser("[| xxx. asd = 1. | self xx. asd. ^ 1.]");
        ArrayList<ASTItem> ast = p.parse();

        Block b = new Block();
        b.addSlot("xxx", new Nil());
        b.addSlot("asd", new NumberInt(1));
        b.addCode(new Send(new MessageUnary("xx")));
        b.addCode(new Send(new MessageUnary("asd")));
        b.addCode(new Return(new NumberInt(1)));

        assertEquals(ast.get(0), b);
    }

    ObjTokensInfo scanCode(String source) throws ParserException, TokenizerException {
        ObjTokensInfo obj_info = new ObjTokensInfo(new Tokenizer(source).tokenize(), 0);
        obj_info.scan(TokenType.BLOCK_END);

        return obj_info;
    }

    @Test
    public void scanObjTokensWithSlotsAndCode() throws TokenizerException, ParserException {
        ObjTokensInfo token_info = scanCode("xxx. asd = 1. | self xx. asd. ^ 1.]");

        assertEquals(token_info.has_slots, true);
        assertEquals(token_info.has_code, true);
    }

    @Test
    public void scanObjTokensWithSlots() throws TokenizerException, ParserException {
        ObjTokensInfo token_info = scanCode("xxx. asd = 1. |]");

        assertEquals(token_info.has_slots, true);
        assertEquals(token_info.has_code, false);
    }

    @Test
    public void scanObjTokensWithCode() throws TokenizerException, ParserException {
        ObjTokensInfo token_info = scanCode("| self xx. asd. ^ 1.]");

        assertEquals(token_info.has_slots, false);
        assertEquals(token_info.has_code, true);
    }

    @Test
    public void scanObjTokensOneSlot() throws TokenizerException, ParserException {
        ObjTokensInfo token_info = scanCode(" asd |]");

        assertEquals(token_info.has_slots, true);
        assertEquals(token_info.has_code, false);
    }

    @Test
    public void scanObjTokensEmptyObj() throws TokenizerException, ParserException {
        ObjTokensInfo token_info = scanCode("|]");

        assertEquals(token_info.has_slots, false);
        assertEquals(token_info.has_code, false);
    }

    @Test
    public void scanObjWithOneNumber() throws TokenizerException, ParserException {
        ObjTokensInfo token_info = scanCode("1]");

        assertEquals(token_info.has_slots, false);
        assertEquals(token_info.has_code, true);
    }

    @Test
    public void parseObjectWithParent() throws TokenizerException, ParserException {
        Parser p = new Parser("(| parent* = 1. slot = nil. |)");
        ArrayList<ASTItem> ast = p.parse();

        Obj o = new Obj();
        o.addSlot("slot", new Nil());
        o.addParent("parent*", new NumberInt(1));

        assertEquals(ast.get(0), o);
    }

    @Test
    public void parseResend() throws TokenizerException, ParserException {
        Parser p = new Parser("(|| parent.msg)");
        ArrayList<ASTItem> ast = p.parse();

        Resend resend = new Resend(new MessageUnary("msg"));
        resend.parent_name = "parent";

        Obj o = new Obj();
        o.addCode(resend);

        assertEquals(ast.get(0), o);
    }
}


// TODO: keyword: = 1 // keyword: 1 ve slotu chyba