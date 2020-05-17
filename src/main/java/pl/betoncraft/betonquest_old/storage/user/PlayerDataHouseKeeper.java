package pl.betoncraft.betonquest_old.storage.user;

import pl.betoncraft.betonquest_old.BetonQuest;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerDataHouseKeeper implements Runnable {
    private final BetonQuest plugin;
    private final PlayerDataManager playerDataManager;
    private final Map<UUID, Instant> usedByPlugin;

    public PlayerDataHouseKeeper(BetonQuest plugin, PlayerDataManager manager) {
        this.plugin = plugin;
        this.playerDataManager = manager;
        this.usedByPlugin = new HashMap<>();
    }

    public void registerUsage(UUID uuid) {
        this.usedByPlugin.put(uuid, Instant.now());
    }

    public void cleanup(UUID uuid) {
        if(this.usedByPlugin.remove(uuid) != null) {
            playerDataManager.unload(uuid);
        }
    }

    @Override
    public void run() {
        this.playerDataManager.getAll().keySet().iterator();
    }
}
