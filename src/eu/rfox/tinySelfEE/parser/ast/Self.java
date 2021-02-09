package eu.rfox.tinySelfEE.parser.ast;

public class Self implements ASTItem {
    boolean was_in_parens = false;

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

    public boolean wasInParens() {
        return this.was_in_parens;
    }

    public void wasInParens(boolean was_in_parens) {
        this.was_in_parens = was_in_parens;
    }
}
