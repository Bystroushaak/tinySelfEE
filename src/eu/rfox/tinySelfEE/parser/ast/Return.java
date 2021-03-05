package eu.rfox.tinySelfEE.parser.ast;

import eu.rfox.tinySelfEE.vm.object_layout.symbolic.SymbolicEvalProtocol;
import eu.rfox.tinySelfEE.vm.object_layout.symbolic.SymbolicReturn;

import java.util.Objects;

public class Return implements ASTItem {
    boolean was_in_parens = false;
    ASTItem value;

    public Return(ASTItem value) {
        this.value = value;
    }

    public <R> R accept(ASTVisitor<R> visitor) {
        return visitor.visitReturn(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Return aReturn = (Return) o;
        return Objects.equals(value, aReturn.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "Return{" +
                "value=" + value +
                '}';
    }

    public boolean wasInParens() {
        return this.was_in_parens;
    }

    public void wasInParens(boolean was_in_parens) {
        this.was_in_parens = was_in_parens;
    }

    @Override
    public SymbolicEvalProtocol toSymbolic() {
        return new SymbolicReturn(this.value.toSymbolic());
    }
}
