package eu.rfox.oplang.parser.ast;

public class Block extends Obj {
    public <R> R accept(Visitor<R> visitor) {
        return visitor.visitBlock(this);
    }

    public boolean isSingleExpression() {
        return false;
    }

    @Override
    protected String objTypeForToString() {
        return "Block";
    }
}
