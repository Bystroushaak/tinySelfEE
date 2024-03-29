package eu.rfox.tinySelfEE.parser.ast;

import eu.rfox.tinySelfEE.vm.CodeContext;

import java.util.ArrayList;
import java.util.Objects;

public class MessageKeyword extends MessageBase {
    ArrayList<ASTItem> parameters;

    public MessageKeyword(String message_name, ASTItem parameter) {
        super(message_name);

        this.parameters = new ArrayList<>();
        this.parameters.add(parameter);
    }

    public MessageKeyword(String message_name, ArrayList<ASTItem> parameters) {
        super(message_name);

        this.parameters = new ArrayList<>();
        this.parameters.addAll(parameters);
    }

    public void addPair(KeywordPair pair) {
        this.message_name += pair.message_name;
        this.parameters.add(pair.parameter);
    }

    public <R> R accept(ASTVisitor<R> visitor) {
        return visitor.visitMessageKeyword(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MessageKeyword that = (MessageKeyword) o;
        return Objects.equals(message_name, that.message_name) &&
                Objects.equals(parameters, that.parameters);
    }

    @Override
    public int hashCode() {
        return Objects.hash(message_name);
    }

    @Override
    public String toString() {
        return "MessageKeyword{" +
                "message_name='" + message_name + '\'' +
                ", parameter=" + parameters.toString() +
                '}';
    }

    @Override
    public CodeContext compile(CodeContext context) {
        for (ASTItem parameter: parameters) {
            parameter.compile(context);
        }
        context.addKeywordMessageSendBytecode(message_name, parameters.size());
        return context;
    }
}
