package eu.rfox.tinySelfEE.vm.object_layout;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public interface ObjectInterface {
    long getId();
    long getVersion();

    void isVisited(boolean visited);
    boolean isVisited();

    void setScopeParent(ObjectInterface scope_parent);
    ObjectInterface getScopeParent();

    boolean hasParents();
    boolean hasCode();
    boolean hasPrimitiveCode();

    ObjectInterface clone();

    void setSlot(String slot_name, ObjectInterface value);
    void setSlots(HashMap<String, ObjectInterface> slots);

    void setParent(String parent_name, ObjectInterface value);
    void setParents(HashMap<String, ObjectInterface> parents);
    Collection<ObjectInterface> getParents();

    void addArgument(String argument_name);
    void addArguments(ArrayList<String> arguments);
    ArrayList<String> getArguments();

    ObjectInterface parentLookup(String slot_name) throws SlotNotFoundException;
    ObjectInterface getLocalSlot(String slot_name);
    ObjectInterface slotLookup(String slot_name) throws SlotNotFoundException;
}
