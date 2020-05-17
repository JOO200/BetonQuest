package pl.betoncraft.betonquest.internal;

import pl.betoncraft.betonquest.api.PlayerProfile;
import pl.betoncraft.betonquest.api.objective.Objective;
import pl.betoncraft.betonquest.api.objective.ObjectiveProgress;
import pl.betoncraft.betonquest.id.ObjectiveID;

import java.io.Serializable;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class BetonQuestPlayerProfile implements PlayerProfile {
    private UUID uuid;
    private ConcurrentMap<ObjectiveID, Serializable> objectives = new ConcurrentHashMap<>();

    @Override
    public UUID getUuid() {
        return this.uuid;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public void removeRawObjective(ObjectiveID objectiveID) {

    }

    @Override
    public void addRawObjective(ObjectiveID objectiveID) {

    }

    @Override
    public boolean hasActiveObjective(ObjectiveID objectiveID) {
        return false;
    }

    @Override
    public <U extends Serializable> U getObjectiveData(ObjectiveID id, Objective<?, U> objective) {
        return null;
    }

    @Override
    public void setObjectiveData(ObjectiveID id, Serializable context) {
        objectives.put(id, context);
    }
}
