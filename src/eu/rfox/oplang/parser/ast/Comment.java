package eu.rfox.oplang.parser.ast;

public class Comment implements ASTItem {
    public <R> R accept(Visitor<R> visitor) {
        return visitor.visitComment(this);
    }
}
