package eu.rfox.tinySelfEE.tokenizer;

public enum TokenType {
    SELF,
    NUMBER,
    NUMBER_HEX,
    NUMBER_FLOAT,
    OBJ_START,
    OBJ_END,
    BLOCK_START,
    BLOCK_END,
    SINGLE_Q_STRING,
    DOUBLE_Q_STRING,
    FIRST_KW,
    KEYWORD,
    ARGUMENT,
    RW_ASSIGNMENT,
    OPERATOR,
    RETURN,
    END_OF_EXPR,
    SEPARATOR,
    CASCADE,
    IDENTIFIER,
    ASSIGNMENT,
    COMMENT,
    UNEXPECTED,
    EOF
}
