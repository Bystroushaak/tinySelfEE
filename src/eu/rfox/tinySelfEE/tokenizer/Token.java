package eu.rfox.tinySelfEE.tokenizer;

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
