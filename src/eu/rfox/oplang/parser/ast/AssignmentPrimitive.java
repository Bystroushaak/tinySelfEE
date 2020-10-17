package eu.rfox.oplang.parser.ast;

public class AssignmentPrimitive implements ASTItem {
    public <R> R accept(Visitor<R> visitor) {
        return visitor.visitAssignmentPrimitive(this);
    }
}
