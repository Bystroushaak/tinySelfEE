package eu.rfox.tinySelfEE.parser.ast;

import eu.rfox.tinySelfEE.vm.object_layout.symbolic.SymbolicMessage;
import eu.rfox.tinySelfEE.vm.object_layout.symbolic.SymbolicEvalProtocol;

import java.util.ArrayList;
import java.util.Objects;

public class MessageBinary extends MessageBase {
    ASTItem parameter;

    public MessageBinary(String message_name, ASTItem parameter) {
        super(message_name);
        this.parameter = parameter;
    }

    public <R> R accept(ASTVisitor<R> visitor) {
        return visitor.visitMessageBinary(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MessageBinary that = (MessageBinary) o;
        return Objects.equals(message_name, that.message_name) &&
                Objects.equals(parameter, that.parameter);
    }

    @Override
    public int hashCode() {
        return Objects.hash(message_name, parameter);
    }

    @Override
    public String toString() {
        return "MessageBinary{" +
                "message_name='" + message_name + '\'' +
                ", parameter=" + parameter.toString() +
                '}';
    }

    @Override
    public SymbolicMessage toSymbolicMessage() {
        ArrayList<SymbolicEvalProtocol> arguments = new ArrayList<>();
        arguments.add(this.parameter.toSymbolic());
        return new SymbolicMessage(message_name, arguments);
    }

    @Override
    public SymbolicEvalProtocol toSymbolic() {
        return null;
    }
}
