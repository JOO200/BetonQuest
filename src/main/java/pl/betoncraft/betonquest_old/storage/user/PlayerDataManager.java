package pl.betoncraft.betonquest_old.storage.user;

import co.aikar.util.LoadingMap;
import pl.betoncraft.betonquest_old.database.PlayerData;

import java.util.UUID;

public class PlayerDataManager {
    private final LoadingMap<UUID, PlayerData> loadedUser = LoadingMap.of(this);
}
