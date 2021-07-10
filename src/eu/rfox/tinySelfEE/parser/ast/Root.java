package eu.rfox.tinySelfEE.parser.ast;

import eu.rfox.tinySelfEE.vm.CodeContext;

import java.util.ArrayList;

public class Root implements ASTItem {
    boolean was_in_parens = false;
    public ArrayList<ASTItem> ast;

    public Root() {
        ast = new ArrayList<>();
    }

    public Root(ASTItem item) {
        if (ast == null) {
            ast = new ArrayList<>();
        }
        ast.add(item);
    }

    public Root(ArrayList<ASTItem> ast) {
        this.ast = ast;
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
    public CodeContext compile(CodeContext context) {
        for (ASTItem item : ast) {
            item.compile(context);
        }

        return context;
    }
}
