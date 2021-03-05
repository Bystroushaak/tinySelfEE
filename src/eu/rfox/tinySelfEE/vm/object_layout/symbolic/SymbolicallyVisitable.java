package eu.rfox.tinySelfEE.vm.object_layout.symbolic;

public interface SymbolicallyVisitable {
    <R> R accept(SymbolicVisitor<R> visitor);
}
