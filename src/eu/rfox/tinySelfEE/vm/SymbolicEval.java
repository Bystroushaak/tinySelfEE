package eu.rfox.tinySelfEE.vm;

import eu.rfox.tinySelfEE.parser.ast.*;


public class SymbolicEval implements ASTVisitor<Obj> {
    public Obj eval(ASTItem expr) {
        return expr.accept(this);
    }

    @Override
    public Obj visitObj(Obj object) {
        return null;
    }

    @Override
    public Obj visitBlock(Block block) {
        return null;
    }

    @Override
    public Obj visitAssignmentPrimitive(AssignmentPrimitive expr) {
        return null;
    }

    @Override
    public Obj visitStr(Str string) {
        return null;
    }

    @Override
    public Obj visitNumberInt(NumberInt numberInt) {
        return null;
    }

    @Override
    public Obj visitNumberFloat(NumberFloat numberFloat) {
        return null;
    }

    @Override
    public Obj visitSend(Send send) {
        return null;
    }

    @Override
    public Obj visitSelf(Self self) {
        return null;
    }

    @Override
    public Obj visitRoot(Root root) {
        return null;
    }


    @Override
    public Obj visitResend(Resend resend) {
        return null;
    }

    @Override
    public Obj visitReturn(Return aReturn) {
        return null;
    }

    @Override
    public Obj visitNil(Nil nil) {
        return null;
    }

    @Override
    public Obj visitMessageUnary(MessageUnary messageUnary) {
        return null;
    }

    @Override
    public Obj visitMessageBinary(MessageBinary messageBinary) {
        return null;
    }

    @Override
    public Obj visitMessageKeyword(MessageKeyword messageKeyword) {
        return null;
    }

    @Override
    public Obj visitMessageBase(MessageBase messageBase) {
        return null;
    }

    @Override
    public Obj visitCascade(Cascade cascade) {
        return null;
    }
}
