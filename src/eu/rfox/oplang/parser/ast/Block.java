package eu.rfox.oplang.parser.ast;

public class Block extends Obj {
    public <R> R accept(Visitor<R> visitor) {
        return visitor.visitBlock(this);
    }

    private String objTypeForToString() {
        return "Block";
    }
}
