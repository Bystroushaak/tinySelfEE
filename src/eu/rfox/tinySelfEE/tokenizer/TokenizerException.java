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
}

