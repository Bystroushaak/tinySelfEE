package eu.rfox.tinySelfEE.parser.ast;

public interface Visitor<R> {
    R visitAssignmentPrimitive(AssignmentPrimitive expr);
    R visitStr(Str string);
    R visitSend(Send send);
    R visitSelf(Self self);
    R visitRoot(Root root);
    R visitNumberFloat(NumberFloat numberFloat);
    R visitNumberInt(NumberInt numberInt);
    R visitObj(Obj object);
    R visitResend(Resend resend);
    R visitReturn(Return aReturn);
    R visitNil(Nil nil);
    R visitMessageUnary(MessageUnary messageUnary);
    R visitMessageBinary(MessageBinary messageKeyword);
    R visitMessageKeyword(MessageKeyword messageKeyword);
    R visitMessageBase(MessageBase messageBase);
    R visitComment(Comment comment);
    R visitCascade(Cascade cascade);
    R visitBlock(Block block);
}
