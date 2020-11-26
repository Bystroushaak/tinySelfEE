package eu.rfox.tinySelfEE.parser;

import eu.rfox.tinySelfEE.parser.ast.*;
import eu.rfox.tinySelfEE.tokenizer.Token;
import eu.rfox.tinySelfEE.tokenizer.TokenType;
import eu.rfox.tinySelfEE.tokenizer.Tokenizer;

import eu.rfox.tinySelfEE.parser.ast.ASTItem;

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


public class Parser {
    private String source_code;
    ArrayList<ASTItem> ast = new ArrayList<>();
    public ArrayList<ParserException> exceptions = new ArrayList<>();
    public boolean hadErrors = false;

    ArrayList<Token> tokens;
    int current_token_index = 0;

    public Parser(String source_code) {
        this.source_code = source_code;
    }

    private void logError(ParserException exc) {
        hadErrors = true;
        exceptions.add(exc);
    }

    private void logError(String message, Token start, Token end) {
        logError(new ParserException(message, start, end));
    }

    private void logError(String message, Token start) {
        logError(message, start, null);
    }

    public ArrayList<ASTItem> parse() {
        Tokenizer tokenizer = new Tokenizer(source_code);
        tokens = tokenizer.tokenize();

        if (tokenizer.hadErrors) {
            hadErrors = true;
            exceptions.addAll(tokenizer.exceptions);
        }

        while (!isAtEnd()) {
            ASTItem item = parseObjectOrBlock();
            if (item != null) {
                ast.add(item);
            }
        }

        return ast;
    }

    private ASTItem parseExpression() {
        ASTItem literal = parseLiterals();
        if (literal != null) return literal;

        if (isMessage()) {
            return parseMessage();
        }

        if (isObjectOrBlock()) {
            return parseObjectOrBlock();
        }

        if (lastTokenAndDot()) {
            advance();
            return null;
        }

        logError("Unexpected token `" + current().content + "`", current());
        advance();
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
            case RETURN:
                return parseReturn();
            case NUMBER_HEX:
                return parseHexNumber();
        }

        if (current().type == TokenType.IDENTIFIER && current().content.equals("nil")) {
            advance();
            return new Nil();
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

    private ASTItem parseHexNumber() {
        return new NumberInt(Integer.decode(advance().content));
    }

    private ASTItem parseReturn() {
        advance();
        return new Return(parseExpression());
    }

    private ASTItem parseMessage() {
        if (isBinaryMessage()) {
            return parseBinaryMessage();
        } else if (isKeywordMessage()) {
            return parseKeywordMessage();
        } else if (isUnaryMessage()) {
            return parseUnaryMessage();
        }

        return parseExpression();
    }

    private boolean isMessage() {
        return isUnaryMessage() || isBinaryMessage() || isKeywordMessage();
    }

    private boolean isUnaryMessage() {
        return check_current(TokenType.IDENTIFIER);
    }

    private boolean isBinaryMessage() {
        return check_current(TokenType.OPERATOR, TokenType.ASSIGNMENT);
    }

    private boolean isKeywordMessage() {
        return check_current(TokenType.FIRST_KW);
    }

    private ASTItem parseUnaryMessage() {
        if (check_current(TokenType.IDENTIFIER)) {
            return sendOrResend(new MessageUnary(advance().content));
        }

        // TODO: remove this, this should never happen (messages are joined later in parseCode)
        return sendOrResend(parseExpression(), new MessageUnary(advance().content));
    }

    private ASTItem parseBinaryMessage() {
        return sendOrResend(new MessageBinary(advance().content, parseExpression()));
    }

    private ASTItem parseKeywordMessage() {
        if (check_current(TokenType.FIRST_KW)) {
            MessageKeyword kwd_msg = new MessageKeyword(advance().content, parseExpression());
            return sendOrResend(tryConsumeKeywordPairs(kwd_msg));
        }

        ASTItem receiver;
        if (isUnaryMessage()) {
            receiver = parseUnaryMessage();
        } else if (isBinaryMessage()) {
            receiver = parseBinaryMessage();
        } else {
            receiver = parseExpression();
        }

        MessageKeyword kwd_msg = new MessageKeyword(advance().content, parseExpression());
        return sendOrResend(receiver, tryConsumeKeywordPairs(kwd_msg));
    }

    private Send sendOrResend(MessageBase msg) {
        if (msg.isResend()) {
            return new Resend(msg);
        }

        return new Send(msg);
    }

    private Send sendOrResend(ASTItem obj, MessageBase msg) {
        if (msg.isResend()) {
            return new Resend(obj, msg);
        }

        return new Send(obj, msg);
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

    private boolean isObjectOrBlock() {
        return check_current(TokenType.OBJ_START, TokenType.BLOCK_START);
    }

    private ASTItem parseObjectOrBlock() {
        if (!isObjectOrBlock()) {
            return parseExpression();
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
        } else {
            ObjTokensInfo obj_info = new ObjTokensInfo(tokens, current_token_index);
            obj_info.scan(obj_end);

            // recovery, just log the error and skip the object
            if (obj_info.had_exception) {
                logError(obj_info.exception);
                current_token_index = obj_info.obj_end;
                advance();
                return null;
            }

            if (obj_info.has_slots) {
                // consume stuff until first separator and then also that
                if (obj_info.hasBothSeparators()) {
                    while (!check_current(TokenType.SEPARATOR)) {
                        advance();
                    }
                    advance();
                }

                parseSlotDefinition(new_obj, obj_end);

                if (check_current(obj_end)) {
                    advance();
                    return new_obj;
                }
            }

            while (check_current(TokenType.SEPARATOR)) {
                advance();
            }

            if (obj_info.has_code) {
                if (!obj_info.hasNoSeparator() && check_current(TokenType.SEPARATOR)) {
                    advance();
                }
                parseCode(new_obj, obj_end);
            }
            advance();  // consume end of the object

            if (obj_info.hasNoSeparator()) {
                // blocks are always objects
                if (obj_end == TokenType.BLOCK_END) {
                    return new_obj;
                }

                if (new_obj.isSingleExpression()) {
                    return new_obj.getFirstExpression();
                } else {
                    logError("Invalid syntax; multiple expressions in parens. Use (| code) syntax instead!",
                             tokens.get(obj_info.obj_start), tokens.get(obj_info.obj_end));
                    return new_obj;
                }
            }

            return new_obj;
        }
    }

    private void parseCode(Obj obj, TokenType obj_end) {
        ArrayList<ASTItem> message_sends = new ArrayList<>();

        while (! check_current(obj_end)) {
            message_sends.add(parseObjectOrBlock());

            if (check_current(TokenType.END_OF_EXPR)) {
                advance();
                obj.addCode(joinMessageSends(message_sends));
                message_sends.clear();
            } else if (check_current(TokenType.CASCADE)) {
                advance();
                obj.addCode(parseCascade(message_sends, obj_end));
                message_sends.clear();
            }
        }

        if (! message_sends.isEmpty()) {
            obj.addCode(joinMessageSends(message_sends));
        }
    }

    private ASTItem parseCascade(ArrayList<ASTItem> message_sends, TokenType obj_end) {
        ASTItem receiver = message_sends.remove(0);

        Cascade cascade = new Cascade(receiver);

        if (message_sends.size() > 0) {
            cascade.addMessage(((Send) joinMessageSends(message_sends)).message);
        }

        while (! check_current(obj_end, TokenType.END_OF_EXPR, TokenType.SEPARATOR)) {
            message_sends.add(parseObjectOrBlock());

            if (check_current(TokenType.CASCADE)) {
                advance();
                cascade.addMessage(((Send) joinMessageSends(message_sends)).message);
            } else if (check_current(obj_end, TokenType.END_OF_EXPR)) {
                break;
            }
        }

        if (message_sends.size() > 0) {
            cascade.addMessage(((Send) joinMessageSends(message_sends)).message);
        }

        return cascade;
    }

    private ASTItem joinMessageSends(ArrayList<ASTItem> message_sends) {
        if (message_sends.size() == 1) {
            return message_sends.get(0);
        }

        ASTItem receiver = message_sends.remove(0);
        while (!message_sends.isEmpty()) {
            ASTItem message = message_sends.remove(0);

            if (message instanceof Send) {
                Send message_in_send = (Send) message;

                if (message_in_send.hasDefaultSelf()) {
                    message_in_send.obj = receiver;
                    message_in_send.hasDefaultSelf(false);
                    receiver = message_in_send;
                    continue;
                }
            }

            try {
                receiver = new Send(receiver, (MessageBase) message);
            } catch (ClassCastException e) {
                logError(message.toString() + " can't be used as message.", current());
            }
        }

        return receiver;
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
            obj.addSlot(argument_name, parseExpression());
            return;
        } else if (check_current(TokenType.IDENTIFIER) && check_next(TokenType.RW_ASSIGNMENT)) {
            argument_name = current().content;
            advance();
            advance();
            obj.addSlot(argument_name, parseExpression());
            obj.addRWSlot(argument_name);
            return;
        } else if (check_current(TokenType.FIRST_KW)) {
            SlotnameAndArguments slot_args = consumeKeywordArguments(obj);
            advance();  // take assignment token

            ASTItem code_obj = parseExpression();
            if ((code_obj instanceof Obj)) {
                ((Obj) code_obj).addArguments(slot_args.arguments);
            }

            obj.addSlot(slot_args.slot_name, code_obj);
            return;
        } else if (check_current(TokenType.OPERATOR, TokenType.ASSIGNMENT)) {
            String slot_name = current().content;
            advance();

            if (!check_current(TokenType.IDENTIFIER)) {
                logError("Invalid argument name: " + current().content, current());
            }

            argument_name = current().content;
            advance();

            advance();  // take assignment token

            ASTItem code_obj = parseExpression();
            if ((code_obj instanceof Obj)) {
                ((Obj) code_obj).addArgument(argument_name);
            }

            obj.addSlot(slot_name, code_obj);
            return;
        } else if (check_current(TokenType.IDENTIFIER)) {
            obj.addSlot(current().content);
            advance();
            return;
        }

        logError("Unknown type of slot definition: " + current().content, current());
        advance();
    }

    private SlotnameAndArguments consumeKeywordArguments(Obj obj) {
        SlotnameAndArguments slot_args = new SlotnameAndArguments();

        int index_of_first_slot_name = current_token_index;
        while (check_current(TokenType.KEYWORD, TokenType.FIRST_KW)) {
            if (!check_next(TokenType.IDENTIFIER)) {
                logError("Invalid name of the keyword argument: " + peek().content, peek());
            }

            slot_args.slot_name += current().content;
            advance();
            slot_args.addArgument(current().content);
            advance();
        }

        if (slot_args.slot_name.equals("")) {
            logError("Slot name can't be empty!", tokens.get(index_of_first_slot_name));
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

    private boolean lastTokenAndDot() {
        if (check_next(TokenType.EOF) && check_current(TokenType.END_OF_EXPR)) {
            return true;
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
