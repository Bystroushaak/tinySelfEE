package eu.rfox.tinySelfEE.parser.ast;

import eu.rfox.tinySelfEE.vm.CodeContext;
import eu.rfox.tinySelfEE.vm.primitives.PrimitiveInt;

import java.util.Objects;

public class NumberInt implements ASTItem {
    boolean was_in_parens = false;
    int value;

    public NumberInt(int value) {
        this.value = value;
    }

    public <R> R accept(ASTVisitor<R> visitor) {
        return visitor.visitNumberInt(this);
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NumberInt numberInt = (NumberInt) o;
        return value == numberInt.value;
    }

    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "NumberInt{" +
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
    public CodeContext compile(CodeContext context) {
        context.addIntLiteralAndBytecode(value);
        return context;
    }
}
