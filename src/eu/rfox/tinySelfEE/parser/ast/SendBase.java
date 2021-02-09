package eu.rfox.tinySelfEE.parser.ast;

abstract class SendBase implements ASTItem {
    boolean was_in_parens = false;
    
    public boolean wasInParens() {
        return this.was_in_parens;
    }

    public void wasInParens(boolean was_in_parens) {
        this.was_in_parens = was_in_parens;
    }
}
