package eu.rfox.tinySelfEE.parser.ast;

import eu.rfox.tinySelfEE.vm.object_layout.symbolic.SymbolicEvalProtocol;

import java.util.Objects;

public class Resend extends Send implements ASTItem {
    public String parent_name;

    public Resend(MessageBase message) {
        super(message);
        takeParentNameFromMessage();
    }

    public Resend(ASTItem obj, MessageBase message) {
        super(obj, message);
        takeParentNameFromMessage();
    }

    private void takeParentNameFromMessage() {
        if (message.isResend()) {
            parent_name = message.removeParentName();
        }
    }

    public <R> R accept(ASTVisitor<R> visitor) {
        return visitor.visitResend(this);
    }

    @Override
    public String toString() {
        return "Resend{" +
                "parent_name='" + parent_name + '\'' +
                ", obj=" + obj +
                ", message=" + message +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Resend resend = (Resend) o;
        return Objects.equals(parent_name, resend.parent_name) && message.equals(((Resend) o).message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), parent_name);
    }

    @Override
    public SymbolicEvalProtocol toSymbolic() {
        return null;
    }
}
