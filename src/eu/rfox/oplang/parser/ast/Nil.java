package eu.rfox.oplang.parser.ast;

import eu.rfox.oplang.tokenizer.Token;

public class Nil implements ASTItem {
    public Nil() {
    }

    public <R> R accept(Visitor<R> visitor) {
        return visitor.visitNil(this);
    }
}
