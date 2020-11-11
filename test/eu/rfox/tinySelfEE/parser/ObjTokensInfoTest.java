package eu.rfox.tinySelfEE.parser;

import eu.rfox.tinySelfEE.tokenizer.TokenType;
import eu.rfox.tinySelfEE.tokenizer.Tokenizer;
import eu.rfox.tinySelfEE.tokenizer.TokenizerException;
import org.junit.Test;

import static org.junit.Assert.*;

public class ObjTokensInfoTest {
    private ObjTokensInfo scanSomething(String source, TokenType end) throws TokenizerException {
        ObjTokensInfo obj_info = new ObjTokensInfo(new Tokenizer(source).tokenize(), 0);
        obj_info.scan(end);

        return obj_info;
    }

    ObjTokensInfo scanBlock(String source) throws ParserException, TokenizerException {
        return scanSomething(source, TokenType.BLOCK_END);
    }

    ObjTokensInfo scanObj(String source) throws ParserException, TokenizerException {
        return scanSomething(source, TokenType.OBJ_END);
    }

    @Test
    public void scanObjTokensWithSlotsAndCode() throws TokenizerException, ParserException {
        ObjTokensInfo token_info = scanBlock("xxx. asd = 1. | self xx. asd. ^ 1.]");

        assertEquals(token_info.has_slots, true);
        assertEquals(token_info.has_code, true);
    }

    @Test
    public void scanObjTokensWithSlots() throws TokenizerException, ParserException {
        ObjTokensInfo token_info = scanBlock("xxx. asd = 1. |]");

        assertEquals(token_info.has_slots, true);
        assertEquals(token_info.has_code, false);
    }

    @Test
    public void scanObjTokensWithCode() throws TokenizerException, ParserException {
        ObjTokensInfo token_info = scanBlock("| self xx. asd. ^ 1.]");

        assertEquals(token_info.has_slots, false);
        assertEquals(token_info.has_code, true);
    }

    @Test
    public void scanObjTokensOneSlot() throws TokenizerException, ParserException {
        ObjTokensInfo token_info = scanBlock(" asd |]");

        assertEquals(token_info.has_slots, true);
        assertEquals(token_info.has_code, false);
    }

    @Test
    public void scanObjTokensEmptyObj() throws TokenizerException, ParserException {
        ObjTokensInfo token_info = scanBlock("|]");

        assertEquals(token_info.has_slots, false);
        assertEquals(token_info.has_code, false);
    }

    @Test
    public void scanObjWithOneNumber() throws TokenizerException, ParserException {
        ObjTokensInfo token_info = scanBlock("1]");

        assertEquals(token_info.has_slots, false);
        assertEquals(token_info.has_code, true);
    }

    @Test
    public void scanComplexObject() throws TokenizerException, ParserException {
        ObjTokensInfo token_info = scanObj("|a = (| f | f t: 'a' A: (|| (self == 0.0) ifTrue: [^false]. true. ).).|)");

        assertEquals(token_info.has_slots, true);
        assertEquals(token_info.has_code, false);
        assertEquals(token_info.exception, null);
    }
}