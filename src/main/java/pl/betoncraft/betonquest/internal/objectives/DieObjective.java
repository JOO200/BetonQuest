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
package pl.betoncraft.betonquest.internal.objectives;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import pl.betoncraft.betonquest.api.PlayerProfile;
import pl.betoncraft.betonquest.api.objective.IntegerObjective;
import pl.betoncraft.betonquest.api.objective.ObjectiveData;
import pl.betoncraft.betonquest.exceptions.QuestRuntimeException;
import pl.betoncraft.betonquest.utils.yaml.YAMLNode;
import pl.betoncraft.betonquest_old.BetonQuest;

import java.io.Serializable;
import java.util.Set;

/**
 * Player needs to die. Death can be canceled, also respawn location can be set
 *
 * @author Jakub Sapalski
 */
public class DieObjective extends IntegerObjective<DieObjective.DieObjectiveData> implements Listener {

    public DieObjective(String id) {
        super(id);
    }

    @Override
    public DieObjectiveData unmarshal(YAMLNode node) {
        DieObjectiveData dieObjectiveData = new DieObjectiveData();
        dieObjectiveData.amount = node.getInt("amount");
        String cause = node.getString("cause");
        if (cause != null) {
            dieObjectiveData.cause = EntityDamageEvent.DamageCause.valueOf(node.getString("cause"));
        }
        return dieObjectiveData;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onDeath(PlayerDeathEvent event) {
        PlayerProfile profile = getProfile(event.getEntity().getUniqueId());
        if (profile == null) throw new QuestRuntimeException("PlayerProfile not loaded.");
        Set<ObjectiveData<DieObjectiveData>> objectiveData = activeObjectives(profile);
        for (ObjectiveData<DieObjectiveData> objectiveDatum : objectiveData) {
            if(objectiveDatum.getContext().cause != null) {
                if(event.getEntity().getLastDamageCause().getCause() != objectiveDatum.getContext().cause) continue;
            }
            super.completeObjective(objectiveDatum, profile);
        }
    }

    @Override
    public void start() {
        Bukkit.getPluginManager().registerEvents(this, BetonQuest.getInstance());
    }

    @Override
    public void stop() {
        HandlerList.unregisterAll(this);
    }

    public static class DieObjectiveData implements Serializable, IntegerObjective.IntegerObjectiveData {
        EntityDamageEvent.DamageCause cause;
        int amount;

        @Override
        public int getDefaultAmount() {
            return amount;
        }
    }

}
