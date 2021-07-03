package eu.rfox.tinySelfEE.vm.object_layout;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;


/**
 * Implementation of the most simple object, without any caching, or any other implementation.
 *
 * This will be slow, but good for debugging.
 */
public class BareObject implements ObjectInterface {
    static long id_counter = 0;

    long id = 0;
    int version = 0;
//    CodeContext code = null;
    boolean visited = false;
    ArrayList<String> arguments = null;

    ObjectInterface scope_parent = null;
    HashMap<String, ObjectInterface> slots = null;
    HashMap<String, ObjectInterface> parent_slots = null;

    public BareObject() {
        this.id = id_counter++;
    }

    public long getId() {
        return id;
    }

    public long getVersion() {
        return version;
    }

    public void isVisited(boolean visited) {
        this.visited = visited;
    }

    public boolean isVisited() {
        return this.visited;
    }

    public boolean hasArguments() {
        return this.arguments != null;
    }

    public boolean hasSlots() {
        return this.slots != null;
    }

    public boolean hasParents() {
        return this.parent_slots != null;
    }

    public HashMap<String, ObjectInterface> getParentSlots() {
        return parent_slots;
    }

    public Collection<ObjectInterface> getParentObjects() {
        if (this.parent_slots == null) {
            return null;
        }

        return this.parent_slots.values();
    }

    public boolean hasCode() {
//        return this.code != null;
        return false;
    }

    public boolean hasPrimitiveCode() {
        return false;
    }

    public ObjectInterface clone() {
        BareObject o = new BareObject();

//        o.code = this.code;  // TODO!

        if (this.arguments != null) {
            o.addArguments((ArrayList<String>) this.arguments.clone());
        }
        o.setScopeParent(this.scope_parent);

        if (this.slots != null) {
            o.setSlots((HashMap<String, ObjectInterface>) this.slots.clone());
        }

        if (this.parent_slots != null) {
            o.setParents((HashMap<String, ObjectInterface>) this.parent_slots.clone());
        }

        return o;
    }

    private void createSlotsIfNotExists() {
        if (this.slots == null) {
            this.slots = new LinkedHashMap<>();
        }
    }

    private void createParentSlotsIfNotExists() {
        if (this.parent_slots == null) {
            this.parent_slots = new LinkedHashMap<>();
        }
    }

    private void createArgumentsIfNotExists() {
        if (this.arguments == null) {
            this.arguments = new ArrayList<>();
        }
    }

    public void setSlot(String slot_name, ObjectInterface value) {
        createSlotsIfNotExists();
        version++;
        this.slots.put(slot_name, value);
    }

    public HashMap<String, ObjectInterface> getSlots() {
        return slots;
    }

    public void setSlots(HashMap<String, ObjectInterface> slots) {
        version++;
        this.slots = slots;
    }

    public void setParent(String parent_name, ObjectInterface value) {
        version++;
        createParentSlotsIfNotExists();
        this.parent_slots.put(parent_name, value);
    }

    public void setParents(HashMap<String, ObjectInterface> parents) {
        version++;
        this.parent_slots = parents;
    }

    public void setScopeParent(ObjectInterface scope_parent) {
        this.scope_parent = scope_parent;
    }

    public ObjectInterface getScopeParent() {
        return this.scope_parent;
    }

    public void addArgument(String argument_name) {
        createArgumentsIfNotExists();
        arguments.add(argument_name);
    }

    public void addArguments(ArrayList<String> arguments) {
        if (this.arguments == null) {
            this.arguments = arguments;
            return;
        }

        this.arguments.addAll(arguments);
    }

    public ArrayList<String> getArguments() {
        return this.arguments;
    }

    public ObjectInterface getLocalSlot(String slot_name) {
        if (this.slots == null) {
            return null;
        }
        return slots.getOrDefault(slot_name, null);
    }

    public ObjectInterface slotLookup(String slot_name) throws SlotNotFoundException {
        if (this.slots != null) {
            ObjectInterface local_slot = getLocalSlot(slot_name);

            if (local_slot != null) {
                return local_slot;
            }
        }

        if (this.scope_parent != null) {
            ObjectInterface obj = this.scope_parent.getLocalSlot(slot_name);

            if (obj != null) {
                return obj;
            }
        }

        if (this.parent_slots == null) {
            return null;
        }

        return parentLookup(slot_name);
    }

    public ObjectInterface parentLookup(String slot_name) throws SlotNotFoundException {
        ArrayList<ObjectInterface> parents = new ArrayList<>();
        parents.add(this);

        return lookForSlotInParentTree(parents, slot_name);
    }

    private ObjectInterface lookForSlotInParentTree(ArrayList<ObjectInterface> parents, String slot_name) throws SlotNotFoundException {
        ObjectInterface result = null;

        ArrayList<ObjectInterface> visited_objects = new ArrayList<>();
        while (!parents.isEmpty()) {
            ObjectInterface obj = parents.remove(0);

            if (obj.isVisited()) {
                continue;
            }

            obj.isVisited(true);
            visited_objects.add(obj);

            ObjectInterface slot = obj.getLocalSlot(slot_name);
            if (slot != null) {
                if (result != null) {
                    for (ObjectInterface visited : visited_objects) {
                        visited.isVisited(false);
                    }
                    throw new SlotNotFoundException("Too many parent slots `" + slot_name + "`, use resend!");
                }

                result = slot;
                continue;
            }

            if (obj.getScopeParent() != null) {
                parents.add(obj.getScopeParent());
            }

            if (obj.hasParents()) {
                for (ObjectInterface parent : obj.getParentObjects()) {
                    if (!parent.isVisited()) {
                        parents.add(parent);
                    }
                }
            }
        }

        for (ObjectInterface obj : visited_objects) {
            obj.isVisited(false);
        }

        return result;
    }
}
