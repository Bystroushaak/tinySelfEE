package eu.rfox.tinySelfEE.vm.object_layout.symbolic;


import eu.rfox.tinySelfEE.vm.object_layout.MappedObject;

import java.util.ArrayList;

public class SymbolicObject extends MappedObject {
    ArrayList<SymbolicMessageSend> code;

    public boolean hasCode() {
        return this.code != null;
    }

    public boolean hasPrimitiveCode() {
        return false;
    }

    @Override
    public MappedObject clone() {
        SymbolicObject new_obj = (SymbolicObject) super.clone();
        new_obj.code = code;

        return new_obj;
    }

    public void addMessage(SymbolicMessageSend message) {
        if (this.code == null) {
            this.code = new ArrayList<>();
        }

        this.code.add(message);
    }

    protected String objTypeForToString() {
        return "Object";
    }

    @Override
    public String toString() {
        return objTypeForToString();
    }
}
