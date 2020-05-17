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
package pl.betoncraft.betonquest.api.condition;

import pl.betoncraft.betonquest.BetonQuest;
import pl.betoncraft.betonquest.api.PlayerProfile;
import pl.betoncraft.betonquest.exceptions.InstructionParseException;
import pl.betoncraft.betonquest.exceptions.QuestRuntimeException;
import pl.betoncraft.betonquest.id.ConditionID;
import pl.betoncraft.betonquest.registry.RegistryEntry;
import pl.betoncraft.betonquest.utils.LogUtils;
import pl.betoncraft.betonquest.utils.yaml.YAMLNode;

import java.util.Set;

/**
 * <p>
 * Superclass for all conditions. You need to extend it in order to create new
 * custom conditions.
 * </p>
 *
 * <p>
 * Registering your condition is done through
 * {@link pl.betoncraft.betonquest_old.BetonQuest#registerConditions(String, Class)
 * registerConditions()} method.
 * </p>
 *
 * @author Jakub Sapalski
 */
public abstract class Condition<T> implements RegistryEntry {
    /**
     * name of this condition
     */
    private String id;

    public String getId() {
        return id;
    }

    /**
     * Creates new instance of the condition. The condition should parse
     * instruction string at this point and extract all the data from it. If
     * anything goes wrong, throw {@link InstructionParseException} with an
     * error message describing the problem.
     *
     * @param id the identifier of the condition
     */
    public Condition(String id) {
        this.id = id;
    }

    /**
     * Convert a raw type that was loaded (from a YAML file)
     * into the type that this flag uses.
     *
     * @param context a map of the context
     * @return The unmarshalled type
     */
    public abstract T unmarshal(YAMLNode node) throws InstructionParseException;

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
    abstract public boolean check(T value, PlayerProfile playerProfile) throws QuestRuntimeException;


    public ConditionData<T> create(ConditionID id, YAMLNode node) throws InstructionParseException {
        YAMLNode context = node.getNode("context");
        T type = unmarshal(context);
        return new ConditionData<>(id, this, type);
    }


    public static boolean checkConditions(Set<ConditionID> conditions, PlayerProfile profile) {
        if (conditions == null) return true;
        for (ConditionID condition : conditions) {
            if (!checkCondition(condition, profile)) return false;
        }
        return true;
    }

    public static boolean checkCondition(ConditionID condition, PlayerProfile profile) {
        try {
            if (!BetonQuest.getInstance().getConditions().get(condition).check(profile)) return true;
        } catch (QuestRuntimeException e) {
            LogUtils.getLogger().warning("QuestRuntimeException while checkConditions."); //TODO Logging
            e.printStackTrace();
        }
        return false;
    }
}
