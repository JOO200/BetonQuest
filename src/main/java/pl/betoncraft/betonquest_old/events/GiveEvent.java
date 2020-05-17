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

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import pl.betoncraft.betonquest_old.BetonQuest;
import pl.betoncraft.betonquest_old.Instruction;
import pl.betoncraft.betonquest_old.Instruction.Item;
import pl.betoncraft.betonquest_old.VariableNumber;
import pl.betoncraft.betonquest.api.event.QuestEvent;
import pl.betoncraft.betonquest_old.config.Config;
import pl.betoncraft.betonquest.exceptions.InstructionParseException;
import pl.betoncraft.betonquest.exceptions.QuestRuntimeException;
import pl.betoncraft.betonquest_old.item.QuestItem;
import pl.betoncraft.betonquest_old.utils.PlayerConverter;
import pl.betoncraft.betonquest_old.utils.Utils;

import java.util.HashMap;

/**
 * Gives the player specified items
 *
 * @author Jakub Sapalski
 */
public class GiveEvent extends QuestEvent {

    private final Item[] questItems;
    private final boolean notify;

    public GiveEvent(Instruction instruction) throws InstructionParseException {
        super(instruction);
        questItems = instruction.getItemList();
        notify = instruction.hasArgument("notify");
    }

    @Override
    public void run(String playerID) throws QuestRuntimeException {
        Player player = PlayerConverter.getPlayer(playerID);
        for (Item theItem : questItems) {
            QuestItem questItem = theItem.getItem();
            VariableNumber amount = theItem.getAmount();
            int amountInt = amount.getInt(playerID);
            if (notify) {
                Config.sendNotify(playerID, "items_given",
                        new String[]{
                                (questItem.getName() != null) ? questItem.getName()
                                        : questItem.getMaterial().toString().toLowerCase().replace("_", " "),
                                String.valueOf(amountInt)},
                        "items_given,info");
            }
            while (amountInt > 0) {
                int stackSize;
                if (amountInt > 64) {
                    stackSize = 64;
                } else {
                    stackSize = amountInt;
                }
                ItemStack item = questItem.generate(stackSize);
                HashMap<Integer, ItemStack> left = player.getInventory().addItem(item);
                for (Integer leftNumber : left.keySet()) {
                    ItemStack itemStack = left.get(leftNumber);
                    if (Utils.isQuestItem(itemStack)) {
                        BetonQuest.getInstance().getPlayerData(playerID).addItem(itemStack, stackSize);
                    } else {
                        player.getWorld().dropItem(player.getLocation(), itemStack);
                    }
                }
                amountInt = amountInt - stackSize;
            }
        }
    }
}
