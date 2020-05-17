package pl.betoncraft.betonquest.registry;

import com.google.common.collect.Iterators;
import com.google.common.collect.Maps;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.betoncraft.betonquest.exceptions.RegistryConflictException;

import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

import static com.google.common.base.Preconditions.checkNotNull;

public class SimpleBetonQuestRegistry<T extends RegistryEntry> implements BetonQuestRegistry<T> {
    private final Object lock = new Object();
    private final ConcurrentMap<String, T> values = Maps.newConcurrentMap();
    private boolean initialized = false;

    @Override
    public boolean isInitialized() {
        return initialized;
    }

    @Override
    public void setInitialized(boolean initialized) {
        this.initialized = initialized;
    }

    @Override
    public void register(T value) throws RegistryConflictException {
        checkNotNull(value, "value");
        checkNotNull(value.getId(), "id");
        String id = value.getId();
        synchronized (lock) {
            if (initialized) {
                throw new IllegalStateException("New addons cannot be registered at this time. Please register it at onLoad()");
            }
            if (values.containsKey(id)) {
                throw new RegistryConflictException(id);
            }

            values.put(id, value);
        }
    }

    @Override
    @Nullable
    public T get(String id) {
        checkNotNull(id, "id");
        return values.get(id);
    }

    @Override
    public Map<String, T> getAll() {
        return Collections.unmodifiableMap(values);
    }

    @NotNull
    @Override
    public Iterator<T> iterator() {
        return Iterators.unmodifiableIterator(values.values().iterator());
    }

    @Override
    public int size() {
        return values.size();
    }
}
