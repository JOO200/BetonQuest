package pl.betoncraft.betonquest.api.condition;

import pl.betoncraft.betonquest.api.PlayerProfile;
import pl.betoncraft.betonquest.exceptions.InstructionParseException;
import pl.betoncraft.betonquest.exceptions.QuestRuntimeException;
import pl.betoncraft.betonquest.id.ConditionID;
import pl.betoncraft.betonquest.utils.yaml.YAMLNode;

public class ConditionData<T> {
    private ConditionID id;
    private Condition<T> condition;
    private T context;

    ConditionData(ConditionID id, Condition<T> condition) {
        this.id = id;
        this.condition = condition;
    }

    public void unmarshall(YAMLNode node) throws InstructionParseException {
        context = condition.unmarshal(node);
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
