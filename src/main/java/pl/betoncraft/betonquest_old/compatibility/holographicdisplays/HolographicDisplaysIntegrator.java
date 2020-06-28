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
package pl.betoncraft.betonquest_old.compatibility.holographicdisplays;

import pl.betoncraft.betonquest_old.compatibility.Compatibility;
import pl.betoncraft.betonquest_old.compatibility.Integrator;
import pl.betoncraft.betonquest_old.compatibility.citizens.CitizensHologram;


public class HolographicDisplaysIntegrator implements Integrator {

    private static HolographicDisplaysIntegrator instance;
    private HologramLoop hologramLoop;

    public HolographicDisplaysIntegrator() {
        instance = this;
    }

    @Override
    public void hook() {
        hologramLoop = new HologramLoop();

        // if Citizens is hooked, start CitizensHologram
        if (Compatibility.getHooked().contains("Citizens")) {
            new CitizensHologram();
        }
    }

    @Override
    public void reload() {
        if (instance.hologramLoop != null) {
            instance.hologramLoop.cancel();
            instance.hologramLoop = new HologramLoop();
        }
    }

    @Override
    public void close() {
        if (instance.hologramLoop != null) {
            hologramLoop.cancel();
        }
    }

}