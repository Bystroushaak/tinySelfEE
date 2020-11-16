package eu.rfox.tinySelfEE.tokenizer;

public class TokenizerException extends Exception {
    public Token token;

    TokenizerException(Token token) {
        super("");
        this.token = token;
    }

    @Override
    public String toString() {
        return token.toString();
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

