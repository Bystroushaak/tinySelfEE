package eu.rfox.tinySelfEE.parser.ast;

import eu.rfox.tinySelfEE.vm.CodeContext;

import java.util.Objects;

public class MessageUnary extends MessageBase {
    public MessageUnary(String message_name) {
        super(message_name);
    }

    public <R> R accept(ASTVisitor<R> visitor) {
        return visitor.visitMessageUnary(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MessageUnary that = (MessageUnary) o;
        return Objects.equals(message_name, that.message_name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(message_name);
    }

    @Override
    public String toString() {
        return "MessageUnary{" +
                "message_name='" + message_name + '\'' +
                '}';
    }

    @Override
    public CodeContext compile(CodeContext context) {
        context.addUnaryMessageSendBytecode(message_name);
        return context;
    }
}
