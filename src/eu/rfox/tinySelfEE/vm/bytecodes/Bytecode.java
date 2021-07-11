package eu.rfox.tinySelfEE.vm.bytecodes;

/*
    Instruction set:

    SEND_UNARY, <str index>, NOP
        Take object from top of the stack.
        Resolve <str index> in the CodeContext object as `slot_name`.
        Resolve `slot_name` in object.
        If the result doesn't have code:
            Push result on top of the stack.
            End.
        If it does:
            .. hm, parameters & default values..
    SEND_BINARY, <str index>, <number of parameters>
    SEND_KEYWORD, <str index>, <number of parameters>
        Take <number of parameters> of objects from stack. Save them as `parameters`.
        Take `object` from top of the stack.
        Resolve `message` <str index> in the CodeContext object.
        Resolve code object in the slot called `message`.
        Create `intermediate parameters object` populated with slots from `parameters`
            mapped to slots by `arguments` from the `object`.
        Set IPO's `scope parent` to object's `scope parent`
        Set `scope parent` of object to this IPO.
        Create new stack frame. Push the object on top.
        Run new code.
    PUSH_SELF, NOP, NOP
        Push self on top of the stack.
    PUSH_PARENT, NOP, <str index>
        Take object from the top of the stack.
        Find parent named <str index> in the object.
        Push that object on top of the stack.
        -> Used in resends.
    PUSH_LITERAL<literal type>, <literal index>, NOP
        Push literal from <literal type> array at index <literal index> on top of the stack.
    ADD_SLOT, <str index>, NOP
        Take Self object.
        Take `slot` object from top of the stack.
        Resolve string at <str index> from CodeContext.
        Add `slot` object to Self at slot name suggested by the string.
    ADD_SLOT_PARENT, <str index>, NOP
        Take Self object.
        Take `parent` object from top of the stack.
        Resolve string at <str index> from CodeContext.
        Add `parent` object to Self at slot name suggested by the string.
    RETURN_TOP, NOP, NOP
        Take object from the top of the stack.
        Pop stack linked lists in frame.
        Push object on top of the stack.
    RETURN_BLOCK, NOP, NOP
        Take object for the top of the stack.
        Pop stack linked lists in frame, until you get to block's surrounding object.
        Push object on top of the stack.
 */
public enum Bytecode {
    NOP(0),

    SEND_UNARY(1),
    SEND_BINARY(2),
    SEND_KEYWORD(3),

    PUSH_SELF(4),
    PUSH_PARENT(5),

    PUSH_LITERAL_NIL(6),
    PUSH_LITERAL_ASSIGNMENT(7),
    PUSH_LITERAL_INT(8),
    PUSH_LITERAL_FLOAT(9),
    PUSH_LITERAL_STRING(10),
    PUSH_LITERAL_OBJ(11),
    PUSH_LITERAL_BLOCK(12),

    ADD_SLOT(13),
    ADD_PARENT(14),

    RETURN_TOP(15),
    RETURN_BLOCK(16);

    public final int value;

    Bytecode(int value) {
        this.value = value;
    }
}
