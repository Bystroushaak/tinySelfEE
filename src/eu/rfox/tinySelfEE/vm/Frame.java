package eu.rfox.tinySelfEE.vm;

import eu.rfox.tinySelfEE.vm.object_layout.ObjectRepr;

import java.util.ArrayList;

public class Frame {
    int pointer = -1;
    ArrayList<ObjectRepr> obj_stack;
    ObjectRepr self;
    boolean has_self = false;

    public Frame() {
        obj_stack = new ArrayList<>();
    }

    public ObjectRepr topObject() {
        return obj_stack.get(pointer);
    }

    public void push(ObjectRepr obj) {
        obj_stack.add(obj);
        pointer++;

        if (! has_self) {
            self = obj;
            has_self = true;
        }
    }

    public void pushSelf() {
        push(self);
    }

    public ObjectRepr pop() {
        return obj_stack.remove(pointer--);
    }

    public String toString() {
        String out = "Frame(\n";
        out += "    depth: " + String.valueOf(pointer) + ",\n";

        out += "    self: ";
        if (has_self) {
            out += self.toString() + ",\n";
        } else {
            out += "null, \n";
        }

        for (ObjectRepr item : obj_stack)
            out += "    " + item.toString() + ",\n";

        return out + ")\n";
    }
}
