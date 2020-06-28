package pl.betoncraft.betonquest.utils;

import com.google.gson.*;
import pl.betoncraft.betonquest.BetonQuest;

import java.lang.reflect.Type;
import java.util.function.Function;

public class SimpleDataSerializer<T> implements DataSerializer<T> {
    private Function<String, T> deserialize;
    private Function<T, String> seriailze;

    public SimpleDataSerializer(Function<String, T> deserialize, Function<T, String> seriailze) {
        this.deserialize = deserialize;
        this.seriailze = seriailze;
    }

    @Override
    public T deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject asJsonObject = json.getAsJsonObject();
        String val = asJsonObject.get("value").getAsString();
        return deserialize.apply(val);
    }

    @Override
    public JsonElement serialize(T src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject values = new JsonObject();
        values.add("value", new JsonPrimitive(seriailze.apply(src)));
        return values;
    }

    static void registerAll() {
        BetonQuest.getInstance().registerSerializer(String.class, new SimpleDataSerializer<>(s -> s, s -> s);
        BetonQuest.getInstance().registerSerializer(Integer.class, new SimpleDataSerializer<>(Integer::parseInt, Object::toString));

    }
}
