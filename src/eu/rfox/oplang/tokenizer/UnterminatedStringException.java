package eu.rfox.oplang.tokenizer;

public class UnterminatedStringException extends TokenizerException {
    public UnterminatedStringException(Token token) {
        super(token);
    }
}
