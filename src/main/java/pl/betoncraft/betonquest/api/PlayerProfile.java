package pl.betoncraft.betonquest.api;

import pl.betoncraft.betonquest.api.objective.Objective;
import pl.betoncraft.betonquest.id.ObjectiveID;

import java.io.Serializable;
import java.util.UUID;

public interface PlayerProfile {
    UUID getUuid();
    String getName();

    void removeRawObjective(ObjectiveID objectiveID);
    void addRawObjective(ObjectiveID objectiveID);
    boolean hasActiveObjective(ObjectiveID objectiveID);

    void setObjectiveData(ObjectiveID id, Serializable context);
    <U extends Serializable> U getObjectiveData(ObjectiveID id, Objective<?, U> objective);

}
