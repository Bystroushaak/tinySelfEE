package eu.rfox.tinySelfEE.vm.primitives;

import eu.rfox.tinySelfEE.vm.Code;
import eu.rfox.tinySelfEE.vm.Process;
import eu.rfox.tinySelfEE.vm.object_layout.SlotNotFoundException;
import org.junit.Test;

import static org.junit.Assert.*;

public class PrimitiveIntTest {
    @Test
    public void testConstructor() {
        PrimitiveInt i = new PrimitiveInt(1);
        assertEquals(i.getValue(), 1);
    }

    @Test
    public void testSetter() {
        PrimitiveInt i = new PrimitiveInt(1);
        assertEquals(i.getValue(), 1);

        i.setValue(2);
        assertEquals(i.getValue(), 2);
    }

    @Test
    public void testEq() {
        PrimitiveInt first = new PrimitiveInt(2);
        assertEquals(first.getValue(), 2);

        PrimitiveInt second = new PrimitiveInt(2);
        assertEquals(second.getValue(), 2);

        assertEquals(first, second);
    }

    @Test
    public void testTraitLookup() {
        PrimitiveInt first = new PrimitiveInt(2);
        assertEquals(first.getValue(), 2);

        PrimitiveIntAdd plus;
        try {
            plus = (PrimitiveIntAdd) first.parentLookup("+");
        } catch (SlotNotFoundException e) {
            assertTrue(false);
            return;
        }

    }

    @Test
    public void addTwoInts() {
        PrimitiveInt first = new PrimitiveInt(2);
        PrimitiveInt second = new PrimitiveInt(3);

        PrimitiveIntAdd plus;
        try {
            plus = (PrimitiveIntAdd) first.parentLookup("+");
        } catch (SlotNotFoundException e) {
            assertTrue(false);
            return;
        }

        Process p = new Process(new Code());
        PrimitiveInt result = (PrimitiveInt) plus.evaluate(p, first, second);
        assertEquals(result.getValue(), 5);
    }

}