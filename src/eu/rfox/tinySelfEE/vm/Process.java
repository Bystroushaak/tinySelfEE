package eu.rfox.tinySelfEE.vm;

import eu.rfox.tinySelfEE.vm.object_layout.ObjectRepr;

public class Process {
    Stack callstack;

    Code code;

    public Process(Code code) {
        callstack = new Stack();
        this.code = code;
    }

    public void push(ObjectRepr o) {
        callstack.push(o);
    }

    public ObjectRepr pop() {
        return callstack.pop();
    }

    public void pushFrame() {
        Stack new_stack = new Stack();
        new_stack.prev_stack = callstack;
        callstack = new_stack;
    }

    public void popFrame() {
        callstack = callstack.prev_stack;
    }

    public void popFrameDown() {
        callstack.prev_stack.push(callstack.pop());
        popFrame();
    }

    public void setSelf(ObjectRepr o) {
        callstack.self = o;
    }

    public ObjectRepr getSelf() {
        return callstack.self;
    }

    public void pushSelf() {
        push(callstack.self);
    }
}
