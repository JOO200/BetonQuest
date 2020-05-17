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
package pl.betoncraft.betonquest_old.compatibility.effectlib;

import de.slikey.effectlib.EffectManager;
import pl.betoncraft.betonquest_old.BetonQuest;
import pl.betoncraft.betonquest_old.compatibility.Compatibility;
import pl.betoncraft.betonquest_old.compatibility.Integrator;
import pl.betoncraft.betonquest_old.compatibility.citizens.CitizensParticle;


public class EffectLibIntegrator implements Integrator {

    private static EffectLibIntegrator instance;
    private BetonQuest plugin;
    private EffectManager manager;

    public EffectLibIntegrator() {
        instance = this;
        plugin = BetonQuest.getInstance();
    }

    /**
     * @return the EffectLib effect manager
     */
    public static EffectManager getEffectManager() {
        return instance.manager;
    }

    @Override
    public void hook() {
        manager = new EffectManager(BetonQuest.getInstance());
        if (Compatibility.getHooked().contains("Citizens"))
            new CitizensParticle();
        plugin.registerEvents("particle", ParticleEvent.class);
    }

    @Override
    public void reload() {
    }

    @Override
    public void close() {
        if (manager != null) {
            manager.dispose();
        }
    }

}
