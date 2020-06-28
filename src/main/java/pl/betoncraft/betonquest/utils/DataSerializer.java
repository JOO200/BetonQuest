package pl.betoncraft.betonquest.utils;

import com.google.gson.*;
import pl.betoncraft.betonquest.BetonQuest;

import java.lang.reflect.Type;

public interface DataSerializer<T> extends JsonDeserializer<T>, JsonSerializer<T> {

}
