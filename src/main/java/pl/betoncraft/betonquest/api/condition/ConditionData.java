package pl.betoncraft.betonquest.api.condition;

import pl.betoncraft.betonquest.api.PlayerProfile;
import pl.betoncraft.betonquest.exceptions.QuestRuntimeException;
import pl.betoncraft.betonquest.id.ConditionID;

public class ConditionData<T> {
    private ConditionID id;
    private Condition<T> condition;
    private T context;

    ConditionData(ConditionID id, Condition<T> condition, T context) {
        this.id = id;
        this.condition = condition;
        this.context = context;
    }

    public ConditionID getId() {
        return id;
    }

    public Condition<T> getCondition() {
        return condition;
    }

    public T getContext() {
        return context;
    }

    public boolean check(PlayerProfile player) throws QuestRuntimeException {
        return condition.check(context, player);
    }
}
