package eu.rfox.tinySelfEE.parser.ast;

public interface ASTItem {
    <R> R accept(Visitor<R> visitor);
}
