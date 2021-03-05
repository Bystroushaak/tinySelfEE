package eu.rfox.tinySelfEE.vm.object_layout.symbolic;


import eu.rfox.tinySelfEE.vm.object_layout.ObjectRepr;

import java.util.ArrayList;

public class SymbolicObject extends ObjectRepr implements SymbolicEvalProtocol, SymbolicallyVisitable {
    ArrayList<SymbolicEvalProtocol> code;

    @Override
    public boolean hasCode() {
        return this.code != null;
    }

    public ArrayList<SymbolicEvalProtocol> getCode() {
        return code;
    }

    public boolean hasPrimitiveCode() {
        return false;
    }

    @Override
    public SymbolicObject clone() {
        SymbolicObject new_obj = (SymbolicObject) super.clone();
        new_obj.code = code;

        return new_obj;
    }

    public void addMessage(SymbolicSend message) {
        if (this.code == null) {
            this.code = new ArrayList<>();
        }

        this.code.add(message);
    }

    public void addMessages(ArrayList<SymbolicEvalProtocol> messages) {
        if (this.code == null) {
            this.code = messages;
            return;
        }

        this.code.addAll(messages);
    }

    protected String objTypeForToString() {
        return "Object";
    }

    @Override
    public String toString() {
        return objTypeForToString();
    }

    @Override
    public <R> R accept(SymbolicVisitor<R> visitor) {
        return visitor.visitSymbolicObject(this);
    }
}
