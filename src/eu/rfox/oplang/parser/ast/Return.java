package eu.rfox.oplang.parser.ast;

public class Return implements ASTItem {
    public <R> R accept(Visitor<R> visitor) {
        return visitor.visitReturn(this);
    }
}
