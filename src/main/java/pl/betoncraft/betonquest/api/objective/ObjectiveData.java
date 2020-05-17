package pl.betoncraft.betonquest.api.objective;

import pl.betoncraft.betonquest.id.ConditionID;
import pl.betoncraft.betonquest.id.EventID;
import pl.betoncraft.betonquest.id.ObjectiveID;

import java.util.Set;

public class ObjectiveData<T> {
    protected boolean persistent;
    protected boolean global;
    private ObjectiveID id;
    private Objective<T, ?> objective;
    private T context;
    private Set<ConditionID> conditions;
    private Set<EventID> events;

    ObjectiveData(ObjectiveID id, Objective<T, ?> objective, T context, Set<ConditionID> conditionSet, Set<EventID> eventSet) {
        this.id = id;
        this.objective = objective;
        this.context = context;
        this.conditions = conditionSet;
        this.events = eventSet;
    }

    public void setPersistent(boolean persistent) {
        this.persistent = persistent;
    }

    public void setGlobal(boolean global) {
        this.global = global;
    }

    public boolean isGlobal() {
        return global;
    }

    public boolean isPersistent() {
        return persistent;
    }

    public T getContext() {
        return context;
    }

    public Objective<T, ?> getObjective() {
        return objective;
    }

    public ObjectiveID getId() {
        return id;
    }

    public Set<ConditionID> getConditions() {
        return conditions;
    }

    public Set<EventID> getEvents() {
        return events;
    }
}
