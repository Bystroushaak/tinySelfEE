package eu.rfox.tinySelfEE.vm.object_layout.symbolic;

import java.util.ArrayList;

public class SymbolicMessage implements SymbolicallyVisitable {
    String message;
    ArrayList<SymbolicEvalProtocol> arguments;

    public SymbolicMessage(String message) {
        this.message = message;
    }

    public SymbolicMessage(String message, ArrayList<SymbolicEvalProtocol> arguments) {
        this.message = message;
        this.arguments = arguments;
    }

    public String getMessage() {
        return message;
    }

    public ArrayList<SymbolicEvalProtocol> getArguments() {
        return arguments;
    }

    @Override
    public <R> R accept(SymbolicVisitor<R> visitor) {
        return visitor.visitSymbolicMessage(this);
    }
}
