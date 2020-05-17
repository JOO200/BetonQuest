package pl.betoncraft.betonquest.api.event;

import pl.betoncraft.betonquest.api.PlayerProfile;
import pl.betoncraft.betonquest.exceptions.QuestRuntimeException;
import pl.betoncraft.betonquest.id.ConditionID;
import pl.betoncraft.betonquest.id.EventID;

import java.util.Collections;
import java.util.Set;

public class QuestEventData<T> {
    private EventID id;
    private QuestEvent<T> event;
    private T context;
    private Set<ConditionID> conditions;

    QuestEventData(EventID id, QuestEvent<T> event, T context, Set<ConditionID> conditions) {
        this.id = id;
        this.event = event;
        this.context = context;
        this.conditions = conditions;
    }

    public T getContext() {
        return context;
    }

    public EventID getId() {
        return id;
    }

    public QuestEvent<T> getEvent() {
        return event;
    }

    public Set<ConditionID> getConditions() {
        return conditions;
    }

    public boolean fire(PlayerProfile profile) throws QuestRuntimeException {
        return event.checkAndFire(this, profile);
    }
}
