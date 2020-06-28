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
package pl.betoncraft.betonquest.id;

import pl.betoncraft.betonquest_old.config.ConfigPackage;
import pl.betoncraft.betonquest.exceptions.ObjectNotFoundException;

public class ConditionID extends ID {

    private boolean inverted;

    public ConditionID(ConfigPackage pack, String id) throws ObjectNotFoundException {
        super(pack, removeExclamationMark(id));
        this.inverted = id.startsWith("!");
    }

    private static String removeExclamationMark(String id) {
        if (id.startsWith("!")) {
            id = id.substring(1);
        }
        return id;
    }

    public boolean isInverted() {
        return inverted;
    }
}
