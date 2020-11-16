package eu.rfox.tinySelfEE.tokenizer;

import eu.rfox.tinySelfEE.parser.ParserException;

import java.io.PrintStream;

public class TokenizerException extends ParserException {
    public TokenizerException(Token token) {
        super(token);
    }

    @Override
    public String toString() {
        return token_start.toString();
    }

    public void prettify(String[] source_lines, PrintStream writer) {
        writer.println("Unexpected token `" + this.token_start.content + "` on line " + this.token_start.line + ";");
        writer.println(source_lines[this.token_start.line - 1]);

        for (int i = 0; i < this.token_start.start; i++) {
            writer.print("-");
        }
        writer.println("^");
    }
}

