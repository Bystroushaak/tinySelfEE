package eu.rfox.oplang.parser.ast;

import java.util.Objects;

// TODO: convert to double
public class NumberFloat implements ASTItem {
    float value;

    public NumberFloat(float value) {
        this.value = value;
    }

    public <R> R accept(Visitor<R> visitor) {
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
}
