package eu.rfox.tinySelfEE.parser.ast;

public class Self implements ASTItem {
    public Self() {
    }

    public <R> R accept(Visitor<R> visitor) {
        return visitor.visitSelf(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (getClass() == o.getClass()) {
            return true;
        }

        return false;
    }

    @Override
    public String toString() {
        return "Self{}";
    }
}
