package pl.betoncraft.betonquest.api;

import pl.betoncraft.betonquest.api.objective.Objective;
import pl.betoncraft.betonquest.id.ObjectiveID;

import java.util.Locale;
import java.util.UUID;

public interface PlayerProfile {
    UUID getUuid();
    String getName();

    void removeRawObjective(ObjectiveID objectiveID);
    void addRawObjective(ObjectiveID objectiveID);
    boolean hasActiveObjective(ObjectiveID objectiveID);

    void setObjectiveData(ObjectiveID id, Object context);
    <U> U getObjectiveData(ObjectiveID id, Objective<?, U> objective);

    Locale getLocale();
    void setLocale(Locale locale);

}
