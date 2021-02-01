package eu.rfox.tinySelfEE.parser.ast;

public class Block extends Obj {
    public <R> R accept(Visitor<R> visitor) {
        return visitor.visitBlock(this);
    }

    /**
     * Used to detect if the object is really object or just parens for priority.
     */
    public boolean isSingleExpression() {
        return false;
    }

    @Override
    protected String objTypeForToString() {
        return "Block";
    }
}
