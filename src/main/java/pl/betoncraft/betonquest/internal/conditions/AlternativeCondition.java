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
package pl.betoncraft.betonquest.internal.conditions;

import com.google.common.collect.Sets;
import pl.betoncraft.betonquest.api.PlayerProfile;
import pl.betoncraft.betonquest.api.condition.Condition;
import pl.betoncraft.betonquest.exceptions.QuestRuntimeException;
import pl.betoncraft.betonquest.exceptions.InstructionParseException;
import pl.betoncraft.betonquest.id.ConditionID;
import pl.betoncraft.betonquest.utils.yaml.YAMLNode;

import java.util.*;

/**
 * One of specified conditions has to be true
 *
 * @author Jakub Sapalski
 */
public class AlternativeCondition extends Condition<Set<ConditionID>> {
    public AlternativeCondition(String id) {
        super(id);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Set<ConditionID> unmarshal(YAMLNode node) throws InstructionParseException {
        Set<ConditionID> conditions = Sets.newConcurrentHashSet();
        List<String> conditionList = node.getStringList("conditions", Collections.emptyList());
        if (conditionList.isEmpty()) throw new InstructionParseException("Condition list not availible");
        for (String conditionId : conditionList) {
            conditions.add(new ConditionID(null, conditionId));
            // TODO BetonQuest.getInstance().getIdWrapper().wrap oder sowas einfügen
            // Muss checken, ob die ID überhaupt bekannt ist und wenn nicht, eine Warnung werfen und null zurück liefern
        }
        return conditions;
    }

    @Override
    public boolean check(Set<ConditionID> value, PlayerProfile playerProfile) throws QuestRuntimeException {
        for (ConditionID conditionID : value) {
            if (Condition.checkCondition(conditionID, playerProfile)) return true;
        }
        return false;
    }
}
