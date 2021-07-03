package eu.rfox.tinySelfEE.parser.ast;

import eu.rfox.tinySelfEE.vm.primitives.PrimitiveFloat;

import java.util.Objects;

// TODO: convert to double
public class NumberFloat implements ASTItem {
    boolean was_in_parens = false;
    float value;

    public NumberFloat(float value) {
        this.value = value;
    }

    public <R> R accept(ASTVisitor<R> visitor) {
        return visitor.visitNumberFloat(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NumberFloat that = (NumberFloat) o;
        return Float.compare(that.value, value) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    public boolean wasInParens() {
        return this.was_in_parens;
    }

    public void wasInParens(boolean was_in_parens) {
        this.was_in_parens = was_in_parens;
    }
}
