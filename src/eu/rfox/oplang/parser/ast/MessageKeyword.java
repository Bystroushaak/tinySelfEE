package eu.rfox.oplang.parser.ast;

import java.util.Objects;

public class MessageKeyword extends MessageBase {
    String message_name;
    ASTItem parameter;

    public MessageKeyword(String message_name, ASTItem parameter) {
        this.message_name = message_name;
        this.parameter = parameter;
    }

    public <R> R accept(Visitor<R> visitor) {
        return visitor.visitMessageKeyword(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MessageKeyword that = (MessageKeyword) o;
        return Objects.equals(message_name, that.message_name) &&
                Objects.equals(parameter, that.parameter);
    }

    @Override
    public int hashCode() {
        return Objects.hash(message_name, parameter);
    }

    @Override
    public String toString() {
        return "MessageKeyword{" +
                "message_name='" + message_name + '\'' +
                ", parameter=" + parameter.toString() +
                '}';
    }
}
