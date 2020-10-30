package eu.rfox.tinySelfEE.tokenizer;

public class UnterminatedStringException extends TokenizerException {
    public UnterminatedStringException(Token token) {
        super(token);
    }
}
