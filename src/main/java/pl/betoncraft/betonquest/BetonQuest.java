package pl.betoncraft.betonquest;

import co.aikar.commands.PaperCommandManager;
import org.bukkit.plugin.java.JavaPlugin;
import pl.betoncraft.betonquest.api.event.QuestEventData;
import pl.betoncraft.betonquest.api.objective.Objective;
import pl.betoncraft.betonquest.api.Variable;
import pl.betoncraft.betonquest.api.condition.Condition;
import pl.betoncraft.betonquest.api.event.QuestEvent;
import pl.betoncraft.betonquest.id.ConditionID;
import pl.betoncraft.betonquest.id.EventID;
import pl.betoncraft.betonquest.id.ObjectiveID;
import pl.betoncraft.betonquest.internal.conditions.Conditions;
import pl.betoncraft.betonquest.internal.objectives.Objectives;
import pl.betoncraft.betonquest.registry.BetonQuestRegistry;
import pl.betoncraft.betonquest.registry.SimpleBetonQuestRegistry;
import pl.betoncraft.betonquest.api.condition.ConditionData;
import pl.betoncraft.betonquest.api.objective.ObjectiveData;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class BetonQuest extends JavaPlugin {
    private static BetonQuest instance;

    private BetonQuestRegistry<Condition<?>> conditionRegistry = new SimpleBetonQuestRegistry<>();
    private BetonQuestRegistry<Objective<?, ?>> objectiveRegistry = new SimpleBetonQuestRegistry<>();
    private BetonQuestRegistry<QuestEvent<?>> eventRegisty = new SimpleBetonQuestRegistry<>();
    private BetonQuestRegistry<Variable<?>> variableRegistry = new SimpleBetonQuestRegistry<>();

    private ConcurrentMap<ConditionID, ConditionData<?>> conditions = new ConcurrentHashMap<>();
    private ConcurrentMap<ObjectiveID, ObjectiveData<?>> objectives = new ConcurrentHashMap<>();
    private ConcurrentMap<EventID, QuestEventData<?>> events = new ConcurrentHashMap<>();

    private ConcurrentMap<Class<? extends Objective<?, ?>>, Set<ObjectiveData<?>>> objectiveByType = new ConcurrentHashMap<>();

    private PaperCommandManager commandManager;

    @Override
    public void onLoad() {
        instance = this;
        Conditions.registerAll();
        Objectives.registerAll();
    }

    @Override
    public void onEnable() {
        conditionRegistry.setInitialized(true);
        objectiveRegistry.setInitialized(true);
        eventRegisty.setInitialized(true);
        variableRegistry.setInitialized(true);
    }

    private void registerCommands() {
        this.commandManager = new PaperCommandManager(this);

    }

    public static BetonQuest getInstance() {
        return instance;
    }

    public BetonQuestRegistry<Condition<?>> getConditionRegistry() {
        return conditionRegistry;
    }

    public BetonQuestRegistry<Objective<?, ?>> getObjectiveRegistry() {
        return objectiveRegistry;
    }

    public BetonQuestRegistry<QuestEvent<?>> getEventRegisty() {
        return eventRegisty;
    }

    public BetonQuestRegistry<Variable<?>> getVariableRegistry() {
        return variableRegistry;
    }

    public ConcurrentMap<ConditionID, ConditionData<?>> getConditions() {
        return conditions;
    }

    public ConcurrentMap<EventID, QuestEventData<?>> getEvents() { return events; }

    public ConcurrentMap<Class<? extends Objective<?, ?>>, Set<ObjectiveData<?>>> getObjectiveByType() {
        return objectiveByType;
    }
}
