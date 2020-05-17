package pl.betoncraft.betonquest.registry;


import pl.betoncraft.betonquest.exceptions.RegistryConflictException;

import java.util.Map;

public interface BetonQuestRegistry<T extends RegistryEntry> extends Iterable<T> {
    void register(T value) throws RegistryConflictException;

    T get(String id);

    Map<String, T> getAll();

    int size();

    void setInitialized(boolean initialized);
    boolean isInitialized();
}
