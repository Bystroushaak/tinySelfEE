package eu.rfox.tinySelfEE.parser.ast;

public class ASTPrinter implements Visitor<String> {
    String print(ASTItem expr) {
        return expr.accept(this);
    }

    @Override
    public String visitAssignmentPrimitive(AssignmentPrimitive expr) {
        return "AssignmentPrimitive";
    }

    @Override
    public String visitStr(Str string) {
        return "\"" + string.value + "\"";  // TODO: escaping
    }

    @Override
    public String visitSend(Send send) {
        return "Send";
    }

    @Override
    public String visitSelf(Self self) {
        return "Self";
    }

    @Override
    public String visitRoot(Root root) {
        return "Root";
    }

    @Override
    public String visitNumberFloat(NumberFloat numberFloat) {
        return "NumberFloat";
    }

    @Override
    public String visitNumberInt(NumberInt numberInt) {
        return "NumberInt";
    }

    @Override
    public String visitObj(Obj object) {
        return "Obj";
    }

    @Override
    public String visitResend(Resend resend) {
        return "Resend";
    }

    @Override
    public String visitReturn(Return aReturn) {
        return "Return";
    }

    @Override
    public String visitNil(Nil nil) {
        return "Nil";
    }

    @Override
    public String visitMessageUnary(MessageUnary messageUnary) {
        return "MessageUnary";
    }

    @Override
    public String visitMessageBinary(MessageBinary messageKeyword) {
        return "MessageBinary";
    }

    @Override
    public String visitMessageKeyword(MessageKeyword messageKeyword) {
        return "MessageKeyword";
    }

    @Override
    public String visitMessageBase(MessageBase messageBase) {
        return "MessageBase";
    }

    @Override
    public String visitComment(Comment comment) {
        return "Comment";
    }

    @Override
    public String visitCascade(Cascade cascade) {
        return "Cascade";
    }

    @Override
    public String visitBlock(Block block) {
        return "Block";
    }
}
