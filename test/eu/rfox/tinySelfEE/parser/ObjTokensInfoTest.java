package eu.rfox.tinySelfEE.parser;

import eu.rfox.tinySelfEE.tokenizer.TokenType;
import eu.rfox.tinySelfEE.tokenizer.Tokenizer;
import eu.rfox.tinySelfEE.tokenizer.TokenizerException;
import org.junit.Test;

import static org.junit.Assert.*;

public class ObjTokensInfoTest {
    ObjTokensInfo scanCodeBlock(String source) throws ParserException, TokenizerException {
        ObjTokensInfo obj_info = new ObjTokensInfo(new Tokenizer(source).tokenize(), 0);
        obj_info.scan(TokenType.BLOCK_END);

        return obj_info;
    }

    ObjTokensInfo scanCodeObj(String source) throws ParserException, TokenizerException {
        ObjTokensInfo obj_info = new ObjTokensInfo(new Tokenizer(source).tokenize(), 0);
        obj_info.scan(TokenType.OBJ_END);

        return obj_info;
    }

    @Test
    public void scanObjTokensWithSlotsAndCode() throws TokenizerException, ParserException {
        ObjTokensInfo token_info = scanCodeBlock("xxx. asd = 1. | self xx. asd. ^ 1.]");

        assertEquals(token_info.has_slots, true);
        assertEquals(token_info.has_code, true);
    }

    @Test
    public void scanObjTokensWithSlots() throws TokenizerException, ParserException {
        ObjTokensInfo token_info = scanCodeBlock("xxx. asd = 1. |]");

        assertEquals(token_info.has_slots, true);
        assertEquals(token_info.has_code, false);
    }

    @Test
    public void scanObjTokensWithCode() throws TokenizerException, ParserException {
        ObjTokensInfo token_info = scanCodeBlock("| self xx. asd. ^ 1.]");

        assertEquals(token_info.has_slots, false);
        assertEquals(token_info.has_code, true);
    }

    @Test
    public void scanObjTokensOneSlot() throws TokenizerException, ParserException {
        ObjTokensInfo token_info = scanCodeBlock(" asd |]");

        assertEquals(token_info.has_slots, true);
        assertEquals(token_info.has_code, false);
    }

    @Test
    public void scanObjTokensEmptyObj() throws TokenizerException, ParserException {
        ObjTokensInfo token_info = scanCodeBlock("|]");

        assertEquals(token_info.has_slots, false);
        assertEquals(token_info.has_code, false);
    }

    @Test
    public void scanObjWithOneNumber() throws TokenizerException, ParserException {
        ObjTokensInfo token_info = scanCodeBlock("1]");

        assertEquals(token_info.has_slots, false);
        assertEquals(token_info.has_code, true);
    }

    @Test
    public void scanComplexObject() throws TokenizerException, ParserException {
        ObjTokensInfo token_info = scanCodeObj("| i = (| a | add: (|| (a) a ). |)");

        assertEquals(token_info.has_slots, true);
        assertEquals(token_info.has_code, false);
        assertEquals(token_info.exception, null);
    }
}