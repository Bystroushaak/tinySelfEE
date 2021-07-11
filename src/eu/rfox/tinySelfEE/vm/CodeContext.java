package eu.rfox.tinySelfEE.vm;

import eu.rfox.tinySelfEE.vm.bytecodes.Bytecode;
import eu.rfox.tinySelfEE.vm.bytecodes.LiteralType;
import eu.rfox.tinySelfEE.vm.bytecodes.SendType;
import eu.rfox.tinySelfEE.vm.bytecodes.SlotType;
import eu.rfox.tinySelfEE.vm.object_layout.ObjectRepr;

import java.util.ArrayList;

class Instruction {
    public int bytecode = 0;
    public int type = 0;
    public int index = 0;
    public int number_of_arguments = 0;

    Instruction(Bytecode bytecode) {
        this.bytecode = bytecode.value;
    }

    Instruction(Bytecode bytecode, LiteralType type) {
        this.bytecode = bytecode.value;
        this.type = type.value;
    }

    Instruction(Bytecode bytecode, LiteralType type, int index) {
        this.bytecode = bytecode.value;
        this.type = type.value;
        this.index = index;
    }

    Instruction(Bytecode bytecode, SendType type, int index) {
        this.bytecode = bytecode.value;
        this.type = type.value;
        this.index = index;
    }

    Instruction(Bytecode bytecode, SlotType type, int index) {
        this.bytecode = bytecode.value;
        this.type = type.value;
        this.index = index;
    }

    Instruction(Bytecode bytecode, Bytecode type, int index) {
        this.bytecode = bytecode.value;
        this.type = type.value;
        this.index = index;
    }

    Instruction(Bytecode bytecode, int type, int index, int number_of_arguments) {
        this.bytecode = bytecode.value;
        this.type = type;
        this.index = index;
        this.number_of_arguments = number_of_arguments;
    }

    Instruction(Bytecode bytecode, SendType type, int index, int number_of_arguments) {
        this.bytecode = bytecode.value;
        this.type = type.value;
        this.index = index;
        this.number_of_arguments = number_of_arguments;
    }

}

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

    public void addInstruction(Instruction instruction) {

    }

    // Strings /////////////////////////////////////////////////////////////////////////////////////////////////////////
    int addString(String s) {
        if (strings == null) {
            strings = new ArrayList<>();
        }

        strings.add(s);

        return (strings.size() - 1);
    }

    // Add literal & return index section //////////////////////////////////////////////////////////////////////////////
    int addIntLiteral(int i) {
        if (literals_int == null) {
            literals_int = new ArrayList<>();
        }

        literals_int.add(i);

        return (literals_int.size() - 1);
    }

    int addFloatLiteral(float f) {
        if (literals_float == null) {
            literals_float = new ArrayList<>();
        }

        literals_float.add(f);

        return literals_float.size() - 1;
    }

    int addStringLiteral(String s) {
        if (literals_str == null) {
            literals_str = new ArrayList<>();
        }

        literals_str.add(s);

        return literals_str.size() - 1;
    }

    int addObjectLiteral(ObjectRepr i) {
        if (literals_obj == null) {
            literals_obj = new ArrayList<>();
        }

        literals_obj.add(i);

        return literals_obj.size() - 1;
    }

    int addBlockLiteral(ObjectRepr i) {
        if (literals_block == null) {
            literals_block = new ArrayList<>();
        }

        literals_block.add(i);

        return literals_block.size() - 1;
    }

    // Add literal & bytecode section //////////////////////////////////////////////////////////////////////////////////
    public void addNilLiteralBytecode() {
        addInstruction(new Instruction(Bytecode.PUSH_LITERAL, LiteralType.NIL));
    }

    public void addAssingmentLiteralBytecode() {
        addInstruction(new Instruction(Bytecode.PUSH_LITERAL, LiteralType.ASSIGNMENT));
    }

    public void addIntLiteralAndBytecode(int i) {
        int index = addIntLiteral(i);
        addInstruction(new Instruction(Bytecode.PUSH_LITERAL, LiteralType.INT, index));
    }

    public void addFloatLiteralAndBytecode(float f) {
        int index = addFloatLiteral(f);
        addInstruction(new Instruction(Bytecode.PUSH_LITERAL, LiteralType.FLOAT, index));
    }

    public void addStringLiteralAndBytecode(String s) {
        int index = addStringLiteral(s);
        addInstruction(new Instruction(Bytecode.PUSH_LITERAL, LiteralType.STR, index));
    }

    public void addObjectLiteralAndBytecode(ObjectRepr o) {
        int index = addObjectLiteral(o);
        addInstruction(new Instruction(Bytecode.PUSH_LITERAL, LiteralType.OBJ, index));
    }

    public void addBlockLiteralAndBytecode(ObjectRepr o) {
        int index = addBlockLiteral(o);
        addInstruction(new Instruction(Bytecode.PUSH_LITERAL, LiteralType.BLOCK, index));
    }

    // Other bytecodes /////////////////////////////////////////////////////////////////////////////////////////////////
    public void addPushSelfBytecode() {
        addInstruction(new Instruction(Bytecode.PUSH_SELF));
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

        addInstruction(new Instruction(Bytecode.PUSH_PARENT, Bytecode.NOP, index));
    }

    public void addUnaryMessageSendBytecode(String message_name) {
        int index = addString(message_name);
        addInstruction(new Instruction(Bytecode.SEND, SendType.UNARY, index));
    }

    public void addBinaryMessageSendBytecode(String message_name) {
        int index = addString(message_name);
        addInstruction(new Instruction(Bytecode.SEND, SendType.BINARY, index, 1));
    }

    public void addKeywordMessageSendBytecode(String message_name, int number_of_arguments) {
        int index = addString(message_name);
        addInstruction(new Instruction(Bytecode.SEND, SendType.KEYWORD, index, number_of_arguments));
    }

    public void addReturnTopBytecode() {
        addInstruction(new Instruction(Bytecode.RETURN_TOP));
    }

    public void addReturnBlockBytecode() {
        addInstruction(new Instruction(Bytecode.RETURN_BLOCK));
    }

    public void addSlotBytecode(String slot_name) {
        int index = addString(slot_name);
        addInstruction(new Instruction(Bytecode.ADD_SLOT, SlotType.SLOT, index));
    }

    public void addParentBytecode(String parent_name) {
        int index = addString(parent_name);
        addInstruction(new Instruction(Bytecode.ADD_SLOT, SlotType.PARENT, index));
    }
}
