package eu.rfox.tinySelfEE.parser.ast;

import eu.rfox.tinySelfEE.vm.object_layout.symbolic.SymbolicMessageSend;

public abstract class MessageBase implements ASTItem {
    boolean was_in_parens = false;
    String message_name;

    public MessageBase(String message_name) {
        this.message_name = message_name;
    }

    public boolean isResend() {
        return message_name.indexOf('.') > -1;
    }

    public String removeParentName() {
        int dot_index = message_name.indexOf('.');
        String parent_name = message_name.substring(0, dot_index);
        message_name = message_name.substring(dot_index + 1);

        return parent_name;
    }

    public <R> R accept(ASTVisitor<R> visitor) {
        return visitor.visitMessageBase(this);
    }

    public SymbolicMessageSend toSymbolicMessage() {
        return null;
    }

    public boolean wasInParens() {
        return this.was_in_parens;
    }

    public void wasInParens(boolean was_in_parens) {
        this.was_in_parens = was_in_parens;
    }
}
