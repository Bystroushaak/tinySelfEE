package eu.rfox.oplang.parser;

import java.util.ArrayList;

import eu.rfox.oplang.tokenizer.Token;
import eu.rfox.oplang.tokenizer.TokenType;

/*
This class exists because I wanted parser to support some funky syntax, like
(slots|), (|code) and (slots | code), which requires forward lookup into the tokens.

This class holds data about position of slots, and can detect whether there is
some code between them.
 */
class ObjTokensInfo {
    boolean has_slots;
    boolean has_code;

    int obj_start = -1;
    int obj_end = -1;
    int first_separator_index = -1;
    int second_separator_index = -1;

    private ArrayList<Token> tokens;
    private int current_token_index;

    ObjTokensInfo() {
    }

    ObjTokensInfo(ArrayList<Token> tokens, int current_token_index) {
        this.tokens = tokens;
        this.current_token_index = current_token_index;
    }

    public boolean hasNoSeparator() {
        return (first_separator_index == -1 && second_separator_index == -1);
    }

    public boolean hasBothSeparators() {
        return (first_separator_index >= 0 && second_separator_index >= 0);
    }

    public void scan(TokenType end_token) throws ParserException {
        mapTokens(end_token);

        scanForSlots();
        scanForCode();
    }

    private void mapTokens(TokenType end_token) throws ParserException {
        int current_index = current_token_index;

        obj_start = current_index;

        int stack_count = 0;
        for (int i = current_index; ; i++) {
            Token t = tokens.get(i);

            if (t.type == TokenType.EOF) {
                throw new ParserException("Object's end not found.");
            }

            if (stack_count == 0) {
                if (t.type == end_token) {
                    obj_end = i;
                    break;
                } else if (t.type == TokenType.SEPARATOR) {
                    if (first_separator_index == -1) {
                        first_separator_index = i;
                    } else if (second_separator_index == -1) {
                        second_separator_index = i;
                    } else {
                        throw new ParserException("Too many separators!"); // consume to the end
                    }
                }
            }

            // ignore nested objects
            if (t.type == TokenType.OBJ_START || t.type == TokenType.BLOCK_START) {
                stack_count++;
            } else if (t.type == TokenType.OBJ_END || t.type == TokenType.BLOCK_END) {
                stack_count--;
            }
        }
    }

    private void scanForSlots() {
        if (first_separator_index == -1) {  // no separator -> no slots
            return;
        }

        int start = obj_start;
        int end = first_separator_index;
        if (hasBothSeparators()) {
            start = first_separator_index;
            end = second_separator_index;
        }

        if (end == -1) {
            end = obj_end;
        }

        for (int i = start; i < end; i++) {
            Token t = tokens.get(i);

            if (t.type != TokenType.OBJ_START && t.type != TokenType.SEPARATOR) {
                has_slots = true;
                break;
            }
        }
    }

    private void scanForCode() {
        int start = first_separator_index;
        if (hasBothSeparators()) {
            start = second_separator_index;
        }

        if (start < obj_start) {
            start = obj_start;
        }

        for (int i = start; i < obj_end; i++) {
            Token t = tokens.get(i);

            if (t.type != TokenType.SEPARATOR && t.type != TokenType.OBJ_END) {
                has_code = true;
                break;
            }
        }
    }
}