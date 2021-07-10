package eu.rfox.tinySelfEE.parser.ast;

import eu.rfox.tinySelfEE.vm.CodeContext;

public interface ASTItem {
    <R> R accept(ASTVisitor<R> visitor);
    boolean wasInParens();
    void wasInParens(boolean was_in_parens);
    CodeContext compile(CodeContext context);
}
