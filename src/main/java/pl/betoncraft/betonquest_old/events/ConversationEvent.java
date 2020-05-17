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

import pl.betoncraft.betonquest_old.Instruction;
import pl.betoncraft.betonquest.api.event.QuestEvent;
import pl.betoncraft.betonquest_old.conversation.Conversation;
import pl.betoncraft.betonquest.exceptions.InstructionParseException;
import pl.betoncraft.betonquest_old.utils.PlayerConverter;
import pl.betoncraft.betonquest_old.utils.Utils;

/**
 * Fires the conversation for the player
 *
 * @author Jakub Sapalski
 */
public class ConversationEvent extends QuestEvent {

    private final String conv;

    public ConversationEvent(Instruction instruction) throws InstructionParseException {
        super(instruction);
        conv = Utils.addPackage(instruction.getPackage(), instruction.next());
    }

    @Override
    public void run(String playerID) {
        new Conversation(playerID, conv, PlayerConverter.getPlayer(playerID).getLocation());
    }
}
