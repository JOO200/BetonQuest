package pl.betoncraft.betonquest.internal;

import co.aikar.commands.PaperCommandManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import pl.betoncraft.betonquest.BetonQuest;
import pl.betoncraft.betonquest.api.PlayerProfile;
import pl.betoncraft.betonquest.api.objective.Objective;
import pl.betoncraft.betonquest.id.ObjectiveID;

import java.io.Serializable;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class BetonQuestPlayerProfile implements PlayerProfile {
    private UUID uuid;
    private ConcurrentMap<ObjectiveID, Object> objectives = new ConcurrentHashMap<>();

    @Override
    public UUID getUuid() {
        return this.uuid;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public void removeRawObjective(ObjectiveID objectiveID) {

    }

    @Override
    public void addRawObjective(ObjectiveID objectiveID) {

    }

    @Override
    public boolean hasActiveObjective(ObjectiveID objectiveID) {
        return false;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <U> U getObjectiveData(ObjectiveID id, Objective<?, U> objective) {
        Object object = objectives.get(id);
        return (U)object;

    }

    @Override
    public void setObjectiveData(ObjectiveID id, Object context) {
        objectives.put(id, context);
    }

    @Override
    public void setLocale(Locale locale) {
        Player player = Bukkit.getPlayer(uuid);
        if(player != null)
            BetonQuest.getInstance().getCommandManager().setPlayerLocale(player, locale);

        // TODO storage db
    }

    @Override
    public Locale getLocale() {
        PaperCommandManager commandManager = BetonQuest.getInstance().getCommandManager();

        Player player = Bukkit.getPlayer(uuid);
        if(player != null) {
            return commandManager.getIssuerLocale(commandManager.getCommandIssuer(player));
        }
        return commandManager.getLocales().getDefaultLocale();
    }
}
