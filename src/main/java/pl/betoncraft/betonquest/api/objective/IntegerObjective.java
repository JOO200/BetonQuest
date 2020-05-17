package pl.betoncraft.betonquest.api.objective;

import pl.betoncraft.betonquest.api.PlayerProfile;
import pl.betoncraft.betonquest.id.ObjectiveID;
import pl.betoncraft.betonquest.utils.yaml.YAMLNode;

public abstract class IntegerObjective<T extends IntegerObjective.IntegerObjectiveData> extends Objective<T, Integer> {
    /**
     * <p>
     * Creates new instance of the objective. The objective should parse
     * instruction string at this point and extract all the data from it.
     * </p>
     * <b>Do not register listeners here!</b> There is a {@link #start()} method
     * for it.
     *
     * @param id Instruction object representing the objective; you need to
     *           extract all required information from it
     */
    public IntegerObjective(String id) {
        super(id);
    }

    @Override
    public Integer createDefaultPlayerContext(ObjectiveData<T> data, PlayerProfile profile) {
        return data.getContext().getDefaultAmount();
    }

    public void nextCompletionStage(final ObjectiveData<T> data, final PlayerProfile player) {
        int value = getContextualData(data, player);
        value--;
        setContextualData(data, player, value);

        if(value == 0) {
            completeObjective(data, player);
        }
    }

    public interface IntegerObjectiveData {
        int getDefaultAmount();
    }
}
