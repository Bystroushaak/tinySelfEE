package eu.rfox.oplang.tokenizer;

enum TokenType {
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

public class Token {
    public String content;
    public TokenType type;
    public int line;
    public int start;
    public int end;

    public Token(String content, TokenType type, int line) {
        this.content = content;
        this.type = type;
        this.line = line;
    }

    public Token(String content, TokenType type, int line, int start, int end) {
        this.content = content;
        this.type = type;
        this.line = line;
        this.start = start;
        this.end = end;
    }

    @Override
    public String toString() {
        return "Token{" +
                "content='" + content + '\'' +
                ", type=" + type +
                ", line=" + line +
                ", start=" + start +
                ", end=" + end +
                '}';
    }
}
