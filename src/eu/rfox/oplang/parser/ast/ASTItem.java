package eu.rfox.oplang.parser.ast;

public interface ASTItem {
    <R> R accept(Visitor<R> visitor);
}
