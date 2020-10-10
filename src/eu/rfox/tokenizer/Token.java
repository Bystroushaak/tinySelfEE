package eu.rfox.tokenizer;

enum TokenType {
    SELF,
    NUMBER,
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
    COMMENT
}

public class Token {
    public String content;
    public TokenType type;
    public int line;

    public Token(String content, TokenType type, int line) {
        this.content = content;
        this.type = type;
        this.line = line;
    }

    @Override
    public String toString() {
        return "Token{" +
                "content='" + content + '\'' +
                ", type=" + type +
                ", line=" + line +
                '}';
    }
}
