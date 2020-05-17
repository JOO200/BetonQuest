package pl.betoncraft.betonquest.internal.objectives;

import pl.betoncraft.betonquest.BetonQuest;
import pl.betoncraft.betonquest.api.objective.Objective;
import pl.betoncraft.betonquest.exceptions.RegistryConflictException;

import java.util.ArrayList;
import java.util.List;

public class Objectives {
    private static List<String> INBUILT_OBJECTIVES = new ArrayList<>();

    public static DieObjective DIE = registerAll(new DieObjective("die"));

    private static <T extends Objective<?, ?>> T registerAll(final T objective) throws RegistryConflictException {
        BetonQuest.getInstance().getObjectiveRegistry().register(objective);
        INBUILT_OBJECTIVES.add(objective.getId());
        return objective;
    }

    public static void registerAll() {

    }
}
