package eu.rfox.tinySelfEE.vm.bytecodes;

public enum SendType {
    UNARY(0),
    BINARY(1),
    KEYWORD(2);
//    UNARY_RESEND(3),
//    KEYWORD_RESEND(4);

    public final int value;

    SendType(int value) {
        this.value = value;
    }
}
