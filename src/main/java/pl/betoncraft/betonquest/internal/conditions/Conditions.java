package pl.betoncraft.betonquest.internal.conditions;


import pl.betoncraft.betonquest.BetonQuest;
import pl.betoncraft.betonquest.api.condition.Condition;
import pl.betoncraft.betonquest.exceptions.RegistryConflictException;

import java.util.ArrayList;
import java.util.List;

public class Conditions {
    private static final List<String> INBUILT_CONDITIONS = new ArrayList<>();

    public static final AdvancementCondition ADVANCEMENT = register(new AdvancementCondition("advancement"));
    public static final AlternativeCondition ALTERNATIVE = register(new AlternativeCondition("or"));
    public static final InvertedCondition INVERTED = register(new InvertedCondition("not"));

    private Conditions() {

    }

    private static <T extends Condition<?>> T register(final T condition) throws RegistryConflictException {
        BetonQuest.getInstance().getConditionRegistry().register(condition);
        INBUILT_CONDITIONS.add(condition.getId());
        return condition;
    }

    /**
     * Dummy method to call that initialises the class
     */
    public static void registerAll() {

    }
}
