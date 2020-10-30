package eu.rfox.tinySelfEE.parser.ast;

import java.util.Objects;

public class NumberInt implements ASTItem {
    int value;

    public NumberInt(int value) {
        this.value = value;
    }

    public <R> R accept(Visitor<R> visitor) {
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
}