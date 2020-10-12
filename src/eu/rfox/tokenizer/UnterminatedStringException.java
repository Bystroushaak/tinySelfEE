package eu.rfox.tokenizer;

public class UnterminatedStringException extends TokenizerException {
    public UnterminatedStringException(Token token) {
        super(token);
    }
}
