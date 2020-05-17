package pl.betoncraft.betonquest.internal.conditions;

import pl.betoncraft.betonquest.api.PlayerProfile;
import pl.betoncraft.betonquest.api.condition.Condition;
import pl.betoncraft.betonquest.exceptions.InstructionParseException;
import pl.betoncraft.betonquest.exceptions.QuestRuntimeException;
import pl.betoncraft.betonquest.id.ConditionID;
import pl.betoncraft.betonquest.utils.yaml.YAMLNode;

public class InvertedCondition extends Condition<ConditionID> {
    /**
     * Creates new instance of the condition. The condition should parse
     * instruction string at this point and extract all the data from it. If
     * anything goes wrong, throw {@link InstructionParseException} with an
     * error message describing the problem.
     *
     * @param id the identifier of the condition
     */
    public InvertedCondition(String id) {
        super(id);
    }

    @Override
    public ConditionID unmarshal(YAMLNode node) throws InstructionParseException {
        String conditionId = node.getString("id");
        if (conditionId == null) throw new InstructionParseException("No id given.");
        return new ConditionID(null, conditionId);
    }

    @Override
    public boolean check(ConditionID value, PlayerProfile playerProfile) throws QuestRuntimeException {
        return !Condition.checkCondition(value, playerProfile);
    }
}
