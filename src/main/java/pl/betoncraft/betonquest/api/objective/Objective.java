/*
 * BetonQuest - advanced quests for Bukkit
 * Copyright (C) 2016  Jakub "Co0sh" Sapalski
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package pl.betoncraft.betonquest.api.objective;

import pl.betoncraft.betonquest.BetonQuest;
import pl.betoncraft.betonquest.api.PlayerProfile;
import pl.betoncraft.betonquest.api.condition.Condition;
import pl.betoncraft.betonquest.api.condition.ConditionData;
import pl.betoncraft.betonquest.api.event.QuestEvent;
import pl.betoncraft.betonquest.exceptions.InstructionParseException;
import pl.betoncraft.betonquest.exceptions.QuestRuntimeException;
import pl.betoncraft.betonquest.id.ConditionID;
import pl.betoncraft.betonquest.id.EventID;
import pl.betoncraft.betonquest.id.ObjectiveID;
import pl.betoncraft.betonquest.registry.RegistryEntry;
import pl.betoncraft.betonquest.utils.LogUtils;
import pl.betoncraft.betonquest.utils.yaml.YAMLNode;

import java.io.Serializable;
import java.util.*;
import java.util.function.BiFunction;
import java.util.logging.Level;
import java.util.stream.Collectors;

/**
 * <p>
 * Superclass for all objectives. You need to extend it in order to create new
 * custom objectives.
 * </p>
 * <p>
 * Registering your objectives is done through
 * {@link pl.betoncraft.betonquest_old.BetonQuest#registerObjectives(String, Class)
 * registerObjectives()} method.
 * </p>
 *
 * @author Jakub Sapalski
 */
public abstract class Objective<T, U extends Serializable> implements RegistryEntry {

    private String id;

    /**
     * <p>
     * Creates new instance of the objective. The objective should parse
     * instruction string at this point and extract all the data from it.
     * </p>
     * <b>Do not register listeners here!</b> There is a {@link #start()} method
     * for it.
     *
     * @param id
     *            Instruction object representing the objective; you need to
     *            extract all required information from it
     * @throws InstructionParseException
     *             if the syntax is wrong or any error happens while parsing
     */
    public Objective(String id) {
        this.id = id;
    }

    /**
     * Convert a raw type that was loaded (from a YAML file)
     * into the type that this flag uses.
     *
     * @param node the section of the given objective
     * @return The unmarshalled type
     */
    public abstract T unmarshal(YAMLNode node);

    public ObjectiveData<T> create(ObjectiveID id, YAMLNode node) {
        List<String> conditions = node.getStringList("conditions", Collections.emptyList());
        Set<ConditionID> conditionSet = conditions.stream().map(c -> new ConditionID(null, c)).collect(Collectors.toSet());

        List<String> events = node.getStringList("events", Collections.emptyList());
        Set<EventID> eventSet = events.stream().map(c -> new EventID(null, c)).collect(Collectors.toSet());

        T context = unmarshal(node.getNode("context"));

        return new ObjectiveData<>(id, this, context, conditionSet, eventSet);
    }

    public abstract U createDefaultPlayerContext(ObjectiveData<T> data, PlayerProfile profile);

    /**
     * This method is called by the plugin when the objective needs to start
     * listening for events. Register your Listeners here!
     */
    public abstract void start();

    /**
     * This method is called by the plugin when the objective needs to be
     * stopped. You have to unregister all Listeners here.
     */
    public abstract void stop();

    /**
     * This method fires events for the objective and removes it from player's
     * list of active objectives. Use it when you detect that the objective has
     * been completed. It deletes the objective using delete() method.
     *
     * @param playerID
     *            the ID of the player for whom the objective is to be completed
     */
    public final boolean completeObjective(ObjectiveData<T> data, final PlayerProfile player) {
        Set<ConditionID> conditions = data.getConditions();
        if (!Condition.checkConditions(conditions, player)) {
            return false;
        }

        player.removeRawObjective(data.getId());

        Set<EventID> events = data.getEvents();
        QuestEvent.fireEvents(events, player);
    }


    /**
     * Adds this new objective to the player. Also updates the database with the
     * objective.
     *
     * @param playerID
     *            ID of the player
     */
    public final void newPlayer(ObjectiveData<T> data, PlayerProfile profile) {
        U contextualData = createDefaultPlayerContext(data, profile);
        profile.setObjectiveData(data.getId(), contextualData);
    }

    public final U getContextualData(ObjectiveData<T> data, PlayerProfile profile) {
        return profile.getObjectiveData(data.getId(), this);
    }

    public final void setContextualData(ObjectiveData<T> data, PlayerProfile profile, U context) {
        profile.setObjectiveData(data.getId(), context);
    }

    public Set<ObjectiveData<T>> activeObjectives(PlayerProfile profile) {
        Set<ObjectiveData<T>> collection = new HashSet<>();
        for (ObjectiveData<?> objectiveData : BetonQuest.getInstance().getObjectiveByType().get(this.getClass())) {
            if (profile.hasActiveObjective(objectiveData.getId())) collection.add((ObjectiveData<T>)objectiveData);
        }
        return Collections.unmodifiableSet(collection);
    }

    public PlayerProfile getProfile(UUID uuid) {
        return null;
    }


    /**
     * A task that may throw a {@link QuestRuntimeException}
     */
    protected interface QREThrowing {

        void run() throws QuestRuntimeException;
    }

    /**
     * Can handle thrown{@link QuestRuntimeException} and rate limits them so
     * they don't spam console that hard
     *
     * @author Jonas Blocher
     */
    protected class QREHandler {

        /**
         * Interval in which errors are logged
         */
        public static final int ERROR_RATE_LIMIT_MILLIS = 5000;

        public long last = 0;

        /**
         * Runs a task and logs occurring quest runtime exceptions with a rate
         * limit
         *
         * @param qreThrowing
         *            a task that may throw a quest runtime exception
         */
        public void handle(QREThrowing qreThrowing) {
            try {
                qreThrowing.run();
            } catch (QuestRuntimeException e) {
                if (System.currentTimeMillis() - last < ERROR_RATE_LIMIT_MILLIS)
                    return;
                last = System.currentTimeMillis();
                LogUtils.getLogger().log(Level.WARNING,
                        "Error while handling '" + id + "' objective: " + e.getMessage());
                LogUtils.logThrowable(e);
            }
        }
    }

    @Override
    public String getId() {
        return id;
    }
}