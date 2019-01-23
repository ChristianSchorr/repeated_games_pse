package loop.model.repository;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import loop.model.simulationengine.SimulationHistory;
import loop.model.simulationengine.SimulationHistoryTable;

public class HistorySerializer implements JsonSerializer<SimulationHistory>, JsonDeserializer<SimulationHistory> {

    @Override
    public JsonElement serialize(SimulationHistory src, Type typeOfSrc, JsonSerializationContext context) {
        if (src instanceof SimulationHistoryTable) {
            return context.serialize(src, SimulationHistoryTable.class);
        }
        return context.serialize(src, typeOfSrc);
    }

    @Override
    public SimulationHistory deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        return context.deserialize(json, SimulationHistoryTable.class);
    }

}
