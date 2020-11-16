package eu.rfox.tinySelfEE.parser;

import eu.rfox.tinySelfEE.tokenizer.Token;

import java.io.PrintStream;

public class ParserException extends Exception {
    public Token token_start;
    public Token token_end;

    public ParserException(Token token) {
        super("");
        token_start = token;
    }

    public ParserException(String message, Token token_start) {
        this(message, token_start, null);
    }

    public ParserException(String message, Token token_start, Token token_end) {
        super(message);
        this.token_start = token_start;
        this.token_end = token_end;
    }

    @Override
    public String toString() {
        return "ParserException{" +
                "token_start=" + token_start +
                ", token_end=" + token_end +
                '}';
    }

    public void prettify(String[] source_lines, PrintStream writer) {
        if (this.token_end == null) {
            writer.println(this.getMessage() + " on line " + this.token_start.line + ";");
            writer.println(source_lines[this.token_start.line - 1]);

            for (int i = 0; i < this.token_start.start; i++) {
                writer.print("-");
            }
            writer.println("^");
            return;
        }

        writer.print(this.getMessage() + " on lines " + this.token_start.line + " .. ");
        writer.println(this.token_end.line + ";");

        writer.println(source_lines[this.token_start.line - 1]);
        for (int i = 0; i < this.token_start.start; i++) {
            writer.print("-");
        }
        writer.println("^");
    }
}
