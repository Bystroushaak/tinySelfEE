package eu.rfox.tinySelfEE.parser.ast;

import java.util.Objects;

public class Send extends SendBase implements ASTItem {
    public MessageBase message;
    private boolean default_self = false;

    public Send(MessageBase message) {
        this.obj = new Self();
        this.message = message;
        this.default_self = true;
    }

    public Send(ASTItem obj, MessageBase message) {
        this.obj = obj;
        this.message = message;
    }

    /**
    true if the Self in the .obj property was assigned automatically - used for
    joining multiple sends into one message chain.
     */
    public boolean hasDefaultSelf() {
        return default_self;
    }
    public void hasDefaultSelf(boolean new_state) {
        default_self = new_state;
    }

    public <R> R accept(ASTVisitor<R> visitor) {
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
