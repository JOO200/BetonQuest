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
package pl.betoncraft.betonquest.api.event;

import pl.betoncraft.betonquest.BetonQuest;
import pl.betoncraft.betonquest.api.PlayerProfile;
import pl.betoncraft.betonquest.api.condition.Condition;
import pl.betoncraft.betonquest.exceptions.InstructionParseException;
import pl.betoncraft.betonquest.exceptions.QuestRuntimeException;
import pl.betoncraft.betonquest.id.ConditionID;
import pl.betoncraft.betonquest.id.EventID;
import pl.betoncraft.betonquest.registry.RegistryEntry;
import pl.betoncraft.betonquest.utils.LogUtils;
import pl.betoncraft.betonquest.utils.yaml.YAMLNode;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * Superclass for all events. You need to extend it in order to create new
 * custom events.
 * </p>
 * <p>
 * Registering your events is done through
 * {@link pl.betoncraft.betonquest_old.BetonQuest#registerEvents(String, Class) registerEvents()} method.
 * </p>
 *
 * @author Jakub Sapalski
 */
public abstract class QuestEvent<T> implements RegistryEntry {

    private String id;

    public QuestEvent(String id) {
        this.id = id;
    }

    /**
     * Convert a raw type that was loaded (from a YAML file)
     * into the type that this flag uses.
     *
     * @param node the section of the given condition
     * @return The unmarshalled type
     */
    public abstract T unmarshal(YAMLNode node);

    public QuestEventData<T> create(EventID id, YAMLNode node) throws InstructionParseException {
        T instruction = unmarshal(node.getNode("context"));

        List<String> conditions = node.getStringList("conditions", Collections.emptyList());
        Set<ConditionID> conditionSet = conditions.stream().map(c -> new ConditionID(null, c)).collect(Collectors.toSet());
        return new QuestEventData<>(id,this, instruction, conditionSet);
    }

    @Override
    public String getId() {
        return id;
    }


    /**
     * This method should contain all logic for the condition and use data
     * parsed by the constructor. Don't worry about inverting the condition,
     * it's done by the rest of BetonQuest's logic. When this method is called
     * all the required data must be present and parsed correctly.
     *
     * @param playerProfile Profile of the player for whom the condition will be checked
     * @return the result of the check
     * @throws QuestRuntimeException when an error happens at runtime (for example a numeric
     *                               variable resolves to a string)
     */
    abstract protected boolean fire(QuestEventData<T> entry, PlayerProfile playerProfile) throws QuestRuntimeException;

    public boolean checkAndFire(QuestEventData<T> entry, PlayerProfile playerProfile) throws QuestRuntimeException {
        if (!Condition.checkConditions(entry.getConditions(), playerProfile)) return false;
        return fire(entry, playerProfile);
    }

    /**
     * Fires an event for the player. It checks event conditions, so there's no need to
     * do that in {@link #fire(QuestEventData, PlayerProfile) run()} method.
     *
     * @param player profile of the player for whom the event will fire
     * @throws QuestRuntimeException passes the exception from the event up the stack
     */
    public static void fireEvent(EventID id, PlayerProfile player) {
        try {
            BetonQuest.getInstance().getEvents().get(id).fire(player);
        } catch (QuestRuntimeException e) {
            LogUtils.getLogger().warning("QuestRuntimeException while checkConditions."); //TODO Logging
            e.printStackTrace();
        }
    }

    public static void fireEvents(Set<EventID> id, PlayerProfile player) {
        for (EventID eventID : id) {
            fireEvent(eventID, player);
        }
    }
}
