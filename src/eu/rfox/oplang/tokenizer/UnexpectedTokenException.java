package eu.rfox.oplang.tokenizer;

public class UnexpectedTokenException extends TokenizerException {
    UnexpectedTokenException(Token token) {
        super(token);
    }
}
