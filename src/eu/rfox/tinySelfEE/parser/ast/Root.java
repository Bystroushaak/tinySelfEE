package eu.rfox.tinySelfEE.parser.ast;

import eu.rfox.tinySelfEE.vm.object_layout.symbolic.SymbolicEvalProtocol;

import java.util.ArrayList;

public class Root implements ASTItem {
    boolean was_in_parens = false;
    ArrayList<ASTItem> ast;

    Root() {
        ast = new ArrayList<ASTItem>();
    }

    Root(ASTItem item) {
        ast.add(item);
    }

    public <R> R accept(ASTVisitor<R> visitor) {
        return visitor.visitRoot(this);
    }

    public boolean wasInParens() {
        return this.was_in_parens;
    }

    public void wasInParens(boolean was_in_parens) {
        this.was_in_parens = was_in_parens;
    }

    @Override
    public SymbolicEvalProtocol toSymbolic() {
        return null;
    }
}
