package eu.rfox.tinySelfEE.parser.ast;

import java.util.Objects;

public class Str implements ASTItem {
    String value;
    public Str(String value) {
        this.value = value;
    }

    public static String unescape(String input, char boundary_char) {
        return input;
    }

    public <R> R accept(Visitor<R> visitor) {
        return visitor.visitStr(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Str str = (Str) o;
        return Objects.equals(value, str.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "Str{" +
                "value='" + value + '\'' +
                '}';
    }
}