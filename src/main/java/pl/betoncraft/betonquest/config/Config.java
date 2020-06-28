package pl.betoncraft.betonquest.config;

import pl.betoncraft.betonquest_old.config.ConfigPackage;

import java.util.HashMap;
import java.util.Map;

public class Config {
    private static Config instance;

    private static Map<String, ConfigPackage> packages = new HashMap<>();

    public static Config getInstance() {
        if(instance == null) instance = new Config();
        return instance;
    }

    private Config() {

    }
}
