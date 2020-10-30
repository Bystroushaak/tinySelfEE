package eu.rfox.tinySelfEE.parser.ast;

import java.util.Objects;

public class Return implements ASTItem {
    ASTItem value;

    public Return(ASTItem value) {
        this.value = value;
    }

    public <R> R accept(Visitor<R> visitor) {
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
}
