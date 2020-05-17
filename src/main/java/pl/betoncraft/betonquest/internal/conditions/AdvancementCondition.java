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

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.advancement.Advancement;
import org.bukkit.entity.Player;
import pl.betoncraft.betonquest.api.PlayerProfile;
import pl.betoncraft.betonquest.api.condition.Condition;
import pl.betoncraft.betonquest.exceptions.QuestRuntimeException;
import pl.betoncraft.betonquest.exceptions.InstructionParseException;
import pl.betoncraft.betonquest.utils.yaml.YAMLNode;

import java.util.Map;

/**
 * Checks if the player has specified condition.
 *
 * @author Jakub Sapalski
 */
public class AdvancementCondition extends Condition<Advancement> {
    public AdvancementCondition(String id) {
        super(id);
    }

    @Override
    public Advancement unmarshal(YAMLNode node) throws InstructionParseException {
        String key = node.getString("key");
        if (key == null) throw new InstructionParseException("key not defined.");
        String namespace = node.getString("namespace");
        if (namespace != null) {
            return Bukkit.getAdvancement(new NamespacedKey(namespace, key));
        } else {
            return Bukkit.getAdvancement(NamespacedKey.minecraft(key));
        }
    }

    @Override
    public boolean check(Advancement value, PlayerProfile playerProfile) throws QuestRuntimeException {
        Player player = Bukkit.getPlayer(playerProfile.getUuid());
        if(player == null) return false;
        return player.getAdvancementProgress(value).isDone();
    }
}
