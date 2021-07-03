package eu.rfox.tinySelfEE.parser.ast;

public interface ASTItem {
    <R> R accept(ASTVisitor<R> visitor);
    boolean wasInParens();
    void wasInParens(boolean was_in_parens);
}
