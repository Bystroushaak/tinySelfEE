package eu.rfox.tinySelfEE.vm.object_layout;

public class BlockRepr extends ObjectRepr {
    public ObjectRepr surrounding_obj;

    public BlockRepr() {
        super();
    }

    public BlockRepr clone() {
        BlockRepr b = new BlockRepr();
        super.clone(b);

        b.surrounding_obj = surrounding_obj;

        return b;
    }
}
