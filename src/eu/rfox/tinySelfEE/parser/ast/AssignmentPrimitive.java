package eu.rfox.tinySelfEE.parser.ast;

import eu.rfox.tinySelfEE.vm.object_layout.symbolic.SymbolicEvalProtocol;

public class AssignmentPrimitive implements ASTItem {
    public <R> R accept(ASTVisitor<R> visitor) {
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

    public SymbolicEvalProtocol toSymbolic() {
        return null;
    }
}
