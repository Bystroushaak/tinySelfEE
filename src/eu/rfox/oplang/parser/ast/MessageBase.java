package eu.rfox.oplang.parser.ast;

public abstract class MessageBase implements ASTItem {
    public <R> R accept(Visitor<R> visitor) {
        return visitor.visitMessageBase(this);
    }
}
