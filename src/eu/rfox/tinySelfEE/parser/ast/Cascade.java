package eu.rfox.tinySelfEE.parser.ast;

import eu.rfox.tinySelfEE.vm.object_layout.symbolic.SymbolicEvalProtocol;
import eu.rfox.tinySelfEE.vm.object_layout.symbolic.SymbolicSend;

import java.util.ArrayList;
import java.util.Objects;

public class Cascade extends SendBase implements ASTItem {
    ArrayList<MessageBase> messages;

    public Cascade(ASTItem obj) {
        this.obj = obj;
        this.messages = new ArrayList<>();
    }

    public Cascade(ArrayList<MessageBase> messages) {
        this(new Self(), messages);
    }

    public Cascade(ASTItem obj, MessageBase message) {
        this.obj = obj;
        this.messages = new ArrayList<>();
        this.messages.add(message);
    }

    public Cascade(ASTItem obj, ArrayList<MessageBase> messages) {
        this.obj = obj;
        this.messages = messages;
    }

    public void addMessage(MessageBase message) {
        this.messages.add(message);
    }

    public <R> R accept(ASTVisitor<R> visitor) {
        return visitor.visitCascade(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cascade cascade = (Cascade) o;
        return Objects.equals(obj, cascade.obj) &&
                Objects.equals(messages, cascade.messages);
    }

    @Override
    public int hashCode() {
        return Objects.hash(obj, messages);
    }

    @Override
    public String toString() {
        return "Cascade{" +
                "obj=" + obj.toString() +
                ", messages=" + messages.toString() +
                '}';
    }

    @Override
    public SymbolicEvalProtocol toSymbolic() {
        return null;
    }

    public ArrayList<SymbolicEvalProtocol> toSymbolicMessages() {
        ArrayList<SymbolicEvalProtocol> messages = new ArrayList<>();
        for (MessageBase msg : this.messages) {
            if (this.obj.equals(new Self())) {
                messages.add(new SymbolicSend(msg.toSymbolicMessage()));
            } else {
                messages.add(new SymbolicSend(this.obj.toSymbolic(), msg.toSymbolicMessage()));
            }
        }

        return messages;
    }
}
