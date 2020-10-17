package eu.rfox.oplang.parser.ast;

import java.util.Objects;

public class Comment implements ASTItem {
    String value;

    public Comment(String value) {
        this.value = value;
    }

    public <R> R accept(Visitor<R> visitor) {
        return visitor.visitComment(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;
        return Objects.equals(value, comment.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "Comment{" +
                "value='" + value + '\'' +
                '}';
    }
}
