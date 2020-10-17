package eu.rfox.oplang.parser.ast;

import java.util.Objects;

public class Send implements ASTItem {
    ASTItem obj;
    MessageBase msg;

    public Send(MessageBase msg) {
        this.obj = new Self();
        this.msg = msg;
    }

    public Send(ASTItem obj, MessageBase msg) {
        this.obj = obj;
        this.msg = msg;
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
                Objects.equals(msg, send.msg);
    }

    @Override
    public int hashCode() {
        return Objects.hash(obj, msg);
    }

    @Override
    public String toString() {
        return "Send{" +
                "obj=" + obj.toString() +
                ", msg=" + msg.toString() +
                '}';
    }
}
