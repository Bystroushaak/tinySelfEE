package eu.rfox.tinySelfEE.vm.bytecodes;

public enum LiteralType {
    NIL(0),
    INT(1),
    FLOAT(2),
    STR(3),
    OBJ(4),
    BLOCK(5),
    ASSIGNMENT(6);

    public final int value;

    LiteralType(int value) {
        this.value = value;
    }
}
