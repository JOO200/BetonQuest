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
package pl.betoncraft.betonquest_old.events;

import pl.betoncraft.betonquest_old.BetonQuest;
import pl.betoncraft.betonquest_old.Instruction;
import pl.betoncraft.betonquest_old.Journal;
import pl.betoncraft.betonquest_old.Pointer;
import pl.betoncraft.betonquest.api.event.QuestEvent;
import pl.betoncraft.betonquest_old.config.Config;
import pl.betoncraft.betonquest.exceptions.InstructionParseException;
import pl.betoncraft.betonquest_old.utils.Utils;

import java.util.Date;

/**
 * Adds the entry to player's journal
 *
 * @author Jakub Sapalski
 */
public class JournalEvent extends QuestEvent {

    private final String name;
    private final boolean add;

    public JournalEvent(Instruction instruction) throws InstructionParseException {
        super(instruction);
        String first = instruction.next();
        if (first.equalsIgnoreCase("update")) {
            name = null;
            add = false;
        } else {
            add = first.equalsIgnoreCase("add");
            name = Utils.addPackage(instruction.getPackage(), instruction.next());
        }
    }

    @Override
    public void run(String playerID) {
        Journal journal = BetonQuest.getInstance().getPlayerData(playerID).getJournal();
        if (add) {
            journal.addPointer(new Pointer(name, new Date().getTime()));
            Config.sendNotify(playerID, "new_journal_entry", null, "new_journal_entry,info");
        } else if (name != null) {
            journal.removePointer(name);
        }
        journal.update();
    }

}
