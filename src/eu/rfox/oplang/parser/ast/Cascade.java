package eu.rfox.oplang.parser.ast;

public class Cascade implements ASTItem {
    public <R> R accept(Visitor<R> visitor) {
        return visitor.visitCascade(this);
    }
}
