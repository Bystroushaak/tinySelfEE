package eu.rfox.oplang.parser.ast;

public class Block extends Object {
    public <R> R accept(Visitor<R> visitor) {
        return visitor.visitBlock(this);
    }
}
