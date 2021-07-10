package eu.rfox.tinySelfEE.parser.ast;

import eu.rfox.tinySelfEE.vm.CodeContext;
import eu.rfox.tinySelfEE.vm.object_layout.BlockRepr;
import eu.rfox.tinySelfEE.vm.object_layout.ObjectRepr;

import java.util.ArrayList;

public class Block extends Obj {
    public <R> R accept(ASTVisitor<R> visitor) {
        return visitor.visitBlock(this);
    }

    @Override
    protected String objTypeForToString() {
        return "Block";
    }

    @Override
    ObjectRepr getOjbForCompilation() {
        return new BlockRepr();
    }

    @Override
    void addLiteral(CodeContext context, ObjectRepr obj) {
        context.addBlockLiteralAndBytecode(obj);
    }
}
