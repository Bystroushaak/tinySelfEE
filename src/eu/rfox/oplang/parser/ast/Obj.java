package eu.rfox.oplang.parser.ast;

public class Obj implements ASTItem {
    public <R> R accept(Visitor<R> visitor) {
        return visitor.visitObj(this);
    }
}
