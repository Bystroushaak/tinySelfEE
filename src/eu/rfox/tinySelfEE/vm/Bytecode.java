package eu.rfox.tinySelfEE.vm;

/*
    Instruction set:

    PUSH_SELF, NOP, NOP, NOP
        Push self on top of the stack.
    PUSH_PARENT, <str index>, NOP, NOP
        Take object from the top of the stack.
        Find parent named <str index> in the object.
        Push that object on top of the stack.
        -> Used in resends.
    PUSH_LITERAL, <literal type>, <literal index>, NOP
        Push literal from <literal type> array at index <literal index> on top of the stack.
    SEND, MessageType.UNARY, <str index>, NOP
        Take object from top of the stack.
        Resolve <str index> in the CodeContext object
        Send that message to the object.
        Push result on top of the stack.
    SEND, MessageType.BINARY or KEYWORD, <str index>, <number of parameters>
        Take <number of parameters> of objects from stack. Save them as parameters.
        Take object on top of the stack.
        Resolve <str index> in the CodeContext object.
        Send literal to object with parameters.
        Push result on top of the stack.
    ADD_SLOT, SlotType.SLOT, <str index>, NOP
        Take Self object.
        Take `slot` object from top of the stack.
        Resolve string at <str index> from CodeContext.
        Add `slot` object to Self at slot name suggested by the string.
    ADD_SLOT, SlotType.PARENT, <str index>, NOP
        Take Self object.
        Take `parent` object from top of the stack.
        Resolve string at <str index> from CodeContext.
        Add `parent` object to Self at slot name suggested by the string.
    RETURN_TOP, NOP, NOP, NOP
        Take object from the top of the stack.
        Pop stack linked lists in frame.
        Push object on top of the stack.
    RETURN_BLOCK, NOP, NOP, NOP
        Take object for the top of the stack.
        Pop stack linked lists in frame, until you get to block's surrounding object.
        Push object on top of the stack.
 */
public enum Bytecode {
    NOP(0),
    SEND(1),
    PUSH_SELF(2),
    PUSH_PARENT(3),
    PUSH_LITERAL(4),
    ADD_SLOT(5),

    RETURN_TOP(6),
    RETURN_BLOCK(7);

    public final int value;

    Bytecode(int value) {
        this.value = value;
    }
}
