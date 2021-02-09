package eu.rfox.tinySelfEE.parser.ast;

public class AssignmentPrimitive implements ASTItem {
    public <R> R accept(Visitor<R> visitor) {
        return visitor.visitAssignmentPrimitive(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (getClass() == o.getClass()) {
            return true;
        }

        return false;
    }

    public boolean wasInParens() {
        return false;
    }

    public void wasInParens(boolean was_in_parens) {
    }
}
