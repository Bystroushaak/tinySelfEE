package eu.rfox.tinySelfEE.tokenizer;

public class UnexpectedTokenException extends TokenizerException {
    UnexpectedTokenException(Token token) {
        super(token);
    }
}
