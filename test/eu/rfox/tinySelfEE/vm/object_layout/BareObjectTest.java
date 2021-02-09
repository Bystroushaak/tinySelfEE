package eu.rfox.tinySelfEE.vm.object_layout;

import static org.junit.Assert.*;

import eu.rfox.tinySelfEE.vm.primitives.PrimitiveNil;
import eu.rfox.tinySelfEE.vm.primitives.PrimitiveTrue;
import org.junit.Test;

public class BareObjectTest {
    public ObjectInterface getEmptyObject() {
        return new BareObject();
    }

    public ObjectInterface getObjectWithOneSlot() {
        BareObject o = new BareObject();

        o.setSlot("first", PrimitiveNil.getInstance());

        return o;
    }

    public ObjectInterface getObjectTwoSlots() {
        BareObject o = new BareObject();

        o.setSlot("first", PrimitiveNil.getInstance());
        o.setSlot("second", PrimitiveNil.getInstance());

        return o;
    }

    public ObjectInterface getObjectWithScopeParent() {
        BareObject o = new BareObject();

        o.setSlot("first", PrimitiveNil.getInstance());
        o.setSlot("second", PrimitiveNil.getInstance());

        BareObject sp = new BareObject();
        sp.setSlot("third", PrimitiveNil.getInstance());
        o.setScopeParent(sp);

        return o;
    }

    public ObjectInterface getObjectWithScopeParentAndParents() {
        BareObject o = new BareObject();

        o.setSlot("first", PrimitiveNil.getInstance());
        o.setSlot("second", PrimitiveNil.getInstance());

        BareObject sp = new BareObject();
        sp.setSlot("third", PrimitiveNil.getInstance());
        o.setScopeParent(sp);

        BareObject p = new BareObject();
        p.setSlot("fourth", PrimitiveNil.getInstance());
        o.setParent("parent", p);

        return o;
    }

    public ObjectInterface getObjectWithComplexParents() {
        /*
        (|
            first = nil.
            second = nil.
            scope_parent* = (|
                third = nil.
            |).
            parent* = (|
                fourth = nil.
                conflict = nil.
            |).
            anotherParent* = (|
                shadowed = nil.
                superParent* = (|
                    conflict = nil.
                    shadowed = true. # !!
                |)
                scope_parent* = parent*. # !!
            |).
         |)
         */
        BareObject o = new BareObject();

        o.setSlot("first", PrimitiveNil.getInstance());
        o.setSlot("second", PrimitiveNil.getInstance());

        BareObject scope_parent = new BareObject();
        scope_parent.setSlot("third", PrimitiveNil.getInstance());
        o.setScopeParent(scope_parent);

        BareObject parent = new BareObject();
        parent.setSlot("fourth", PrimitiveNil.getInstance());
        parent.setSlot("conflict", PrimitiveNil.getInstance());
        o.setParent("parent", parent);

        BareObject anotherParent = new BareObject();
        anotherParent.setSlot("shadowed", PrimitiveNil.getInstance());
        o.setParent("anotherParent", anotherParent);

        BareObject superparent = new BareObject();
        superparent.setSlot("conflict", PrimitiveNil.getInstance());  // add multiple slots with same name
        superparent.setSlot("shadowed", PrimitiveTrue.getInstance());
        anotherParent.setParent("superParent", superparent);

        // add a cycle for fun
        superparent.setScopeParent(parent);

        return o;
    }

    @Test
    public void testEmptyObject() throws SlotNotFoundException {
        ObjectInterface o = getEmptyObject();

        assertEquals(o.getVersion(), 0);

        assertEquals(o.getLocalSlot("first"), null);
        assertEquals(o.slotLookup("first"), null);
    }

    @Test
    public void testgetLocalSlot() {
        ObjectInterface o = getObjectWithOneSlot();

        assertEquals(o.getVersion(), 1);

        assertEquals(o.getLocalSlot("first"), PrimitiveNil.getInstance());
    }

    @Test
    public void testgetLocalSlotMissingSlot() {
        ObjectInterface o = getObjectWithOneSlot();
        assertEquals(o.getLocalSlot("second"), null);
    }

    @Test
    public void testSlotLookup() throws SlotNotFoundException {
        ObjectInterface o = getObjectWithOneSlot();
        assertEquals(o.slotLookup("first"), PrimitiveNil.getInstance());
        assertEquals(o.slotLookup("second"), null);
    }

    @Test
    public void testSlotLookupTwoSlots() throws SlotNotFoundException {
        ObjectInterface o = getObjectTwoSlots();
        assertEquals(o.getVersion(), 2);

        assertEquals(o.slotLookup("first"), PrimitiveNil.getInstance());
        assertEquals(o.slotLookup("second"), PrimitiveNil.getInstance());
        assertEquals(o.slotLookup("third"), null);
    }

    @Test
    public void testSlotLookupScopeParent() throws SlotNotFoundException {
        ObjectInterface o = getObjectWithScopeParent();
        assertEquals(o.slotLookup("first"), PrimitiveNil.getInstance());
        assertEquals(o.slotLookup("second"), PrimitiveNil.getInstance());
        assertEquals(o.slotLookup("third"), PrimitiveNil.getInstance());
        assertEquals(o.slotLookup("fourth"), null);
    }

    @Test
    public void testSlotLookupScopeParentAndParents() throws SlotNotFoundException {
        ObjectInterface o = getObjectWithScopeParentAndParents();
        assertEquals(o.slotLookup("first"), PrimitiveNil.getInstance());
        assertEquals(o.slotLookup("second"), PrimitiveNil.getInstance());
        assertEquals(o.slotLookup("third"), PrimitiveNil.getInstance());
        assertEquals(o.slotLookup("fourth"), PrimitiveNil.getInstance());
        assertEquals(o.slotLookup("fifth"), null);
    }

    @Test
    public void testSlotLookupWithComplexParentsCycleAndMultipleSlotsWithSameName() throws SlotNotFoundException {
        ObjectInterface o = getObjectWithComplexParents();
        assertEquals(o.slotLookup("first"), PrimitiveNil.getInstance());
        assertEquals(o.slotLookup("second"), PrimitiveNil.getInstance());
        assertEquals(o.slotLookup("third"), PrimitiveNil.getInstance());
        assertEquals(o.slotLookup("fourth"), PrimitiveNil.getInstance());
        assertThrows(SlotNotFoundException.class, () -> {
            o.slotLookup("conflict");
        });
        assertEquals(o.slotLookup("shadowed"), PrimitiveNil.getInstance());
        assertEquals(o.slotLookup("seventh"), null);
    }

    @Test
    public void testClone() throws SlotNotFoundException {
        ObjectInterface o = getObjectWithScopeParentAndParents();

        assertEquals(o.slotLookup("first"), PrimitiveNil.getInstance());
        assertEquals(o.slotLookup("second"), PrimitiveNil.getInstance());
        assertEquals(o.slotLookup("third"), PrimitiveNil.getInstance());
        assertEquals(o.slotLookup("fourth"), PrimitiveNil.getInstance());
        assertEquals(o.slotLookup("fifth"), null);

        ObjectInterface clone = o.clone();

        assertEquals(clone.slotLookup("first"), PrimitiveNil.getInstance());
        assertEquals(clone.slotLookup("second"), PrimitiveNil.getInstance());
        assertEquals(clone.slotLookup("third"), PrimitiveNil.getInstance());
        assertEquals(clone.slotLookup("fourth"), PrimitiveNil.getInstance());
        assertEquals(clone.slotLookup("fifth"), null);
    }
}