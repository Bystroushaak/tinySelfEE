package eu.rfox.tinySelfEE.vm.object_layout.symbolic;

import eu.rfox.tinySelfEE.parser.ast.*;

import java.util.ArrayList;


/**
 * This class takes AST and compiles all Sends / Resends / Cascades to the
 * Symbolic representations, which are more suitable for evaluation.
 */
public class SymbolicCompiler implements ASTVisitor<Void> {
    ArrayList<SymbolicEvalProtocol> symbolic_sends;

    public SymbolicCompiler() {
        symbolic_sends = new ArrayList<SymbolicEvalProtocol>();
    }

    public ArrayList<SymbolicEvalProtocol> getCode() {
        return this.symbolic_sends;
    }

    public void compile(ArrayList<ASTItem> ast) {
        for (ASTItem item : ast) {
            item.accept(this);
        }
    }

    public Void visitSend(Send send) {
        this.symbolic_sends.add(send.toSymbolic());
        return null;
    }

    public Void visitResend(Resend resend) {
        this.symbolic_sends.add(resend.toSymbolic());
        return null;
    }

    public Void visitCascade(Cascade cascade) {
        this.symbolic_sends.addAll(cascade.toSymbolicMessages());
        return null;
    }

    public Void visitObj(Obj object) {
        return null;
    }

    public Void visitBlock(Block block) {
        return null;
    }

    public Void visitAssignmentPrimitive(AssignmentPrimitive expr) {
        return null;
    }

    public Void visitStr(Str string) {
        return null;
    }

    public Void visitSelf(Self self) {
        return null;
    }

    public Void visitRoot(Root root) {
        return null;
    }

    public Void visitNumberFloat(NumberFloat numberFloat) {
        return null;
    }

    public Void visitNumberInt(NumberInt numberInt) {
        return null;
    }

    public Void visitReturn(Return aReturn) {
        return null;
    }

    public Void visitNil(Nil nil) {
        return null;
    }

    public Void visitMessageUnary(MessageUnary messageUnary) {
        return null;
    }

    public Void visitMessageBinary(MessageBinary messageKeyword) {
        return null;
    }

    public Void visitMessageKeyword(MessageKeyword messageKeyword) {
        return null;
    }

    public Void visitMessageBase(MessageBase messageBase) {
        return null;
    }
}
