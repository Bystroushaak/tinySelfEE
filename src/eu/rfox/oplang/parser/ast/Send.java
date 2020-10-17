package eu.rfox.oplang.parser.ast;

import java.util.Objects;

public class Send extends SendBase implements ASTItem {
    public ASTItem obj;
    public MessageBase message;

    public Send(MessageBase message) {
        this.obj = new Self();
        this.message = message;
    }

    public Send(ASTItem obj, MessageBase message) {
        this.obj = obj;
        this.message = message;
    }

    public <R> R accept(Visitor<R> visitor) {
        return visitor.visitSend(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Send send = (Send) o;
        return Objects.equals(obj, send.obj) &&
                Objects.equals(message, send.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(obj, message);
    }

    @Override
    public String toString() {
        return "Send{" +
                "obj=" + obj.toString() +
                ", message=" + message.toString() +
                '}';
    }
}
