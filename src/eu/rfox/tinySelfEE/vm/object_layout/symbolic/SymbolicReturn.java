package eu.rfox.tinySelfEE.vm.object_layout.symbolic;

import eu.rfox.tinySelfEE.parser.ast.Return;

enum ReturnType {
    SEND,
    OBJECT
}

public class SymbolicReturn implements SymbolicEvalProtocol, SymbolicallyVisitable {
    ReturnType return_type;
    SymbolicSend send;
    SymbolicEvalProtocol obj;

    public SymbolicReturn(SymbolicSend send) {
        return_type = ReturnType.SEND;
        this.send = send;
    }

    public SymbolicReturn(SymbolicEvalProtocol obj) {
        return_type = ReturnType.OBJECT;
        this.obj = obj;
    }

    public void evaluate(SymbolicObject namespace, SymbolicFrame frame) {
        if (return_type == ReturnType.SEND) {
            send.evaluate(namespace, frame);
            // TODO: ..
        }
    }

    public <R> R accept(SymbolicVisitor<R> visitor) {
        return visitor.visitSymbolicReturn(this);
    }
}
