package eu.rfox.tinySelfEE.vm;

import eu.rfox.tinySelfEE.vm.object_layout.ObjectRepr;

import java.util.ArrayList;

public class CodeContext {
    /*
    TODO: rework so that primitive objects are directly created - it will save time later and
          they can be cloned efficiently
    */
    public boolean will_have_slots = false;

    private ArrayList<String> strings;

    private ArrayList<Integer> literals_int;
    private ArrayList<Float> literals_float;
    private ArrayList<String> literals_str;
    private ArrayList<ObjectRepr> literals_obj;
    private ArrayList<ObjectRepr> literals_block;

    public void addInstruction(int bytecode, int literal_type, int index, int parameter) {

    }

    public void addInstruction(Bytecode bytecode, int literal_type, int index, int parameter) {
        addInstruction(bytecode.value, literal_type, index, parameter);
    }

    public void addInstruction(Bytecode bytecode, LiteralType literal_type, int index, int parameter) {
        addInstruction(bytecode.value, literal_type.value, index, parameter);
    }

    // Strings /////////////////////////////////////////////////////////////////////////////////////////////////////////
    public int addString(String s) {
        if (strings == null) {
            strings = new ArrayList<>();
        }

        strings.add(s);

        return (strings.size() - 1);
    }

    // Add literal & return index section //////////////////////////////////////////////////////////////////////////////
    public int addIntLiteral(int i) {
        if (literals_int == null) {
            literals_int = new ArrayList<>();
        }

        literals_int.add(i);

        return (literals_int.size() - 1);
    }

    public int addFloatLiteral(float f) {
        if (literals_float == null) {
            literals_float = new ArrayList<>();
        }

        literals_float.add(f);

        return literals_float.size() - 1;
    }

    public int addStringLiteral(String s) {
        if (literals_str == null) {
            literals_str = new ArrayList<>();
        }

        literals_str.add(s);

        return literals_str.size() - 1;
    }

    public int addObjectLiteral(ObjectRepr i) {
        if (literals_obj == null) {
            literals_obj = new ArrayList<>();
        }

        literals_obj.add(i);

        return literals_obj.size() - 1;
    }

    public int addBlockLiteral(ObjectRepr i) {
        if (literals_block == null) {
            literals_block = new ArrayList<>();
        }

        literals_block.add(i);

        return literals_block.size() - 1;
    }

    // Add literal & bytecode section //////////////////////////////////////////////////////////////////////////////////
    public void addNilLiteralBytecode() {
        addInstruction(Bytecode.PUSH_LITERAL, LiteralType.NIL, Bytecode.NOP.value, Bytecode.NOP.value);
    }

    public void addAssingmentLiteralBytecode() {
        addInstruction(Bytecode.PUSH_LITERAL, LiteralType.ASSIGNMENT, Bytecode.NOP.value, Bytecode.NOP.value);
    }

    public void addIntLiteralAndBytecode(int i) {
        int index = addIntLiteral(i);
        addInstruction(Bytecode.PUSH_LITERAL, LiteralType.INT, index, Bytecode.NOP.value);
    }

    public void addFloatLiteralAndBytecode(float f) {
        int index = addFloatLiteral(f);
        addInstruction(Bytecode.PUSH_LITERAL, LiteralType.FLOAT, index, Bytecode.NOP.value);
    }

    public void addStringLiteralAndBytecode(String s) {
        int index = addStringLiteral(s);
        addInstruction(Bytecode.PUSH_LITERAL, LiteralType.STR, index, Bytecode.NOP.value);
    }

    public void addObjectLiteralAndBytecode(ObjectRepr o) {
        int index = addObjectLiteral(o);
        addInstruction(Bytecode.PUSH_LITERAL, LiteralType.OBJ, index, Bytecode.NOP.value);
    }

    public void addBlockLiteralAndBytecode(ObjectRepr o) {
        int index = addBlockLiteral(o);
        addInstruction(Bytecode.PUSH_LITERAL, LiteralType.BLOCK, index, Bytecode.NOP.value);
    }

    // Other bytecodes /////////////////////////////////////////////////////////////////////////////////////////////////
    public void addPushSelfBytecode() {
        addInstruction(Bytecode.PUSH_SELF, Bytecode.NOP.value, Bytecode.NOP.value, Bytecode.NOP.value);
    }

    public void addPushParentBytecode(String parent_name) {
        int index;
        if (literals_str == null) {
            index = addString(parent_name);
        } else {
            int item_index = literals_str.indexOf(parent_name);

            if (item_index == -1) {
                index = addString(parent_name);
            } else {
                index = item_index;
            }
        }

        addInstruction(Bytecode.PUSH_PARENT, index, Bytecode.NOP.value, Bytecode.NOP.value);
    }

    public void addUnaryMessageSendBytecode(String message_name) {
        int index = addString(message_name);
        addInstruction(Bytecode.SEND, SendType.UNARY.value, index, Bytecode.NOP.value);
    }

    public void addBinaryMessageSendBytecode(String message_name) {
        int index = addString(message_name);
        addInstruction(Bytecode.SEND, SendType.BINARY.value, index, (byte) 1);
    }

    public void addKeywordMessageSendBytecode(String message_name, int number_of_arguments) {
        int index = addString(message_name);
        addInstruction(Bytecode.SEND, SendType.KEYWORD.value, index, number_of_arguments);
    }

    public void addReturnTopBytecode() {
        addInstruction(Bytecode.RETURN_TOP, Bytecode.NOP.value, Bytecode.NOP.value, Bytecode.NOP.value);
    }

    public void addReturnBlockBytecode() {
        addInstruction(Bytecode.RETURN_BLOCK, Bytecode.NOP.value, Bytecode.NOP.value, Bytecode.NOP.value);
    }

    public void addSlotBytecode(String slot_name) {
        int index = addString(slot_name);
        addInstruction(Bytecode.ADD_SLOT, SlotType.SLOT.value, index, Bytecode.NOP.value);
    }

    public void addParentBytecode(String parent_name) {
        int index = addString(parent_name);
        addInstruction(Bytecode.ADD_SLOT, SlotType.PARENT.value, index, Bytecode.NOP.value);
    }
}
