package eu.rfox.oplang.parser;

import eu.rfox.oplang.parser.ast.*;
import eu.rfox.oplang.tokenizer.Token;
import eu.rfox.oplang.tokenizer.TokenType;
import eu.rfox.oplang.tokenizer.Tokenizer;
import eu.rfox.oplang.tokenizer.TokenizerException;

import eu.rfox.oplang.parser.ast.ASTItem;

import java.util.ArrayList;

class SlotnameAndArguments {
    public String slot_name = "";
    public ArrayList<String> arguments;

    SlotnameAndArguments() {
        arguments = new ArrayList<>();
    }

    public void addArgument(String argument) {
        arguments.add(argument);
    }
}


class ObjTokensInfo {
    boolean has_slots;
    boolean has_code;

    int obj_start = -1;
    int obj_end = -1;
    int first_separator_index = -1;
    int second_separator_index = -1;

    public boolean hasNoSeparator() {
        return (first_separator_index == -1 && second_separator_index == -1);
    }

    public boolean hasBothSeparators() {
        return (first_separator_index >= 0 && second_separator_index >= 0);
    }
}


public class Parser {
    private String source_code;
    ArrayList<ASTItem> ast = new ArrayList<>();
    private ArrayList<ParserException> errors = new ArrayList<ParserException>();
    public boolean hadErrors = false;

    ArrayList<Token> tokens;
    int current_token_index = 0;

    public Parser(String source_code) {
        this.source_code = source_code;
    }

    private void logError(String message, Token start, Token end) {
        hadErrors = true;
        errors.add(new ParserException(message, start, end));
    }

    public ArrayList<ASTItem> parse() throws TokenizerException, ParserException {
        Tokenizer tokenizer = new Tokenizer(source_code);
        tokens = tokenizer.tokenize();

        while (!isAtEnd()) {
            ast.add(parseObjectOrBlock());
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

        if (isObjectOrBlock()) {
            return parseObjectOrBlock();
        }

        advance();
//        return parseObjectOrBlock();
        return null;
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

    private boolean isObjectOrBlock() {
        return check_current(TokenType.OBJ_START, TokenType.BLOCK_START);
    }

    private ASTItem parseObjectOrBlock() {
        if (!isObjectOrBlock()) {
            return parseCascade(); // go back to parseExpression() via the chain of other possible expr
        }

        Obj new_obj;
        TokenType obj_end = TokenType.OBJ_END;
        if (check_current(TokenType.OBJ_START)) {
            new_obj = new Obj();
        } else {
            new_obj = new Block();
            obj_end = TokenType.BLOCK_END;
        }

        advance();  // consume obj start

        if (check_current(obj_end)) {
            advance();
            return new_obj;
        } else if (check_current(TokenType.SEPARATOR) && check_next(obj_end)) {
            advance();
            advance();
            return new_obj;
        } else if (check_current(TokenType.SEPARATOR) && check_next(TokenType.SEPARATOR)) {
            advance();
            advance();

            if (check_current(obj_end)) {
                advance();
                return new_obj;
            }

            parseCode(new_obj, obj_end);
            advance();
            return new_obj;
        } else {
            ObjTokensInfo obj_info;
            try {
                obj_info = scanObjTokens(obj_end);
            } catch (ParserException e) {
                // handle parsing recovery
                advance();
                return null;
            }

            if (obj_info.has_slots) {
                // consume stuff until first separator and then also that
                if (obj_info.hasBothSeparators()) {
                    while (! check_current(TokenType.SEPARATOR)) {
                        advance();
                    }
                    advance();
                }

                parseSlotDefinition(new_obj, obj_end);

                if (check_current(obj_end)) {
                    advance();
                    return new_obj;
                } else if (check_current(TokenType.SEPARATOR)) {
                    advance();
                }
            }

            if (obj_info.has_code) {
                parseCode(new_obj, obj_end);
            }

            advance();
            return new_obj;


        }
    }

    ObjTokensInfo scanObjTokens(TokenType end_token) throws ParserException {
        ObjTokensInfo obj_info = scanTokens(end_token);

        if (obj_info.hasNoSeparator()) {
            return new ObjTokensInfo();
        }

        scanForSlots(obj_info);
        scanForCode(obj_info);

        return obj_info;
    }

    private ObjTokensInfo scanTokens(TokenType end_token) throws ParserException {
        int current_index = current_token_index;

        ObjTokensInfo obj_info = new ObjTokensInfo();
        obj_info.obj_start = current_index;

        int stack_count = 0;
        for (int i = current_index; ; i++) {
            Token t = tokens.get(i);

            if (t.type == TokenType.EOF) {
                throw new ParserException("Object's end not found.");
            }

            if (stack_count == 0) {
                if (t.type == end_token) {
                    obj_info.obj_end = i;
                    break;
                } else if (t.type == TokenType.SEPARATOR) {
                    if (obj_info.first_separator_index == -1) {
                        obj_info.first_separator_index = i;
                    } else if (obj_info.second_separator_index == -1) {
                        obj_info.second_separator_index = i;
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

        return obj_info;
    }

    private void scanForSlots(ObjTokensInfo obj_info) {
        int start = obj_info.obj_start;
        int end = obj_info.first_separator_index;
        if (obj_info.hasBothSeparators()) {
            start = obj_info.first_separator_index;
            end = obj_info.second_separator_index;
        }

        for (int i = start; i < end; i++) {
            Token t = tokens.get(i);

            if (t.type != TokenType.OBJ_START && t.type != TokenType.SEPARATOR) {
                obj_info.has_slots = true;
                break;
            }
        }
    }

    private void scanForCode(ObjTokensInfo obj_info) {
        int start = obj_info.first_separator_index;
        if (obj_info.hasBothSeparators()) {
            start = obj_info.second_separator_index;
        }

        for (int i = start; i < obj_info.obj_end; i++) {
            Token t = tokens.get(i);

            if (t.type != TokenType.SEPARATOR && t.type != TokenType.OBJ_END) {
                obj_info.has_code = true;
                break;
            }
        }
    }

    private void parseCode(Obj obj, TokenType obj_end) {
        obj.addCode(parseObjectOrBlock());

        while (check_current(TokenType.END_OF_EXPR)) {
            advance();
            if (check_current(obj_end)) {
                return;
            }

            obj.addCode(parseObjectOrBlock());
        }
    }

    private void parseSlotDefinition(Obj obj, TokenType obj_end) {
        while (check_current(TokenType.IDENTIFIER, TokenType.ARGUMENT, TokenType.FIRST_KW, TokenType.OPERATOR,
                             TokenType.ASSIGNMENT)) {
            consumeOneSlotArgument(obj);

            if (check_current(TokenType.END_OF_EXPR)) {
                advance();
            }

            if (check_current(TokenType.SEPARATOR, obj_end)) {
                return;
            }
        }
    }

    private void consumeOneSlotArgument(Obj obj) {
        String argument_name;

        if (check_current(TokenType.ARGUMENT)) {
            obj.addArgument(current().content);
            advance();
            return;
        } else if (check_current(TokenType.IDENTIFIER) && check_next(TokenType.ASSIGNMENT)) {
            argument_name = current().content;
            advance();
            advance();
            obj.addSlot(argument_name, parseObjectOrBlock());
            return;
        } else if (check_current(TokenType.IDENTIFIER) && check_next(TokenType.RW_ASSIGNMENT)) {
            argument_name = current().content;
            advance();
            advance();
            obj.addSlot(argument_name, parseObjectOrBlock());
            obj.addRWSlot(argument_name);
            return;
        } else if (check_current(TokenType.FIRST_KW)) {
            SlotnameAndArguments slot_args = consumeKeywordArguments(obj);
            advance();  // take assignment token

            ASTItem code_obj = parseObjectOrBlock();
            if (!(code_obj instanceof Obj)) {
                // TODO: can't assign arguments to non-code obj
                return;
            }

            ((Obj) code_obj).addArguments(slot_args.arguments);
            obj.addSlot(slot_args.slot_name, code_obj);
            return;
        } else if (check_current(TokenType.OPERATOR, TokenType.ASSIGNMENT)) {
            String slot_name = current().content;
            advance();

            if (!check_current(TokenType.IDENTIFIER)) {
                // TODO: error handling - but maybe not, because arguments can be in obj
            }

            argument_name = current().content;
            advance();

            advance();  // take assignment token

            ASTItem code_obj = parseObjectOrBlock();
            if (!(code_obj instanceof Obj)) {
                // TODO: can't assign arguments to non-code obj
                return;
            }

            ((Obj) code_obj).addArgument(argument_name);
            obj.addSlot(slot_name, code_obj);
            return;
        } else if (check_current(TokenType.IDENTIFIER)) {
            obj.addSlot(current().content);
            advance();
            return;
        }

        // TODO: error handling
    }

    private SlotnameAndArguments consumeKeywordArguments(Obj obj) {
        SlotnameAndArguments slot_args = new SlotnameAndArguments();

        while (check_current(TokenType.KEYWORD, TokenType.FIRST_KW)) {
            if (!check_next(TokenType.IDENTIFIER)) {
                // TODO: error handling
            }

            slot_args.slot_name += current().content;
            advance();
            slot_args.addArgument(current().content);
            advance();
        }

        if (slot_args.slot_name.equals("")) {
            // TODO: error handling
        }

        return slot_args;
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
