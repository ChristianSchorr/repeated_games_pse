package loop.model.repository;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import loop.model.simulationengine.strategies.MixedStrategy;
import loop.model.simulationengine.strategies.PureStrategy;
import loop.model.simulationengine.strategies.Strategy;

public class StrategySerializer implements JsonSerializer<Strategy>, JsonDeserializer<Strategy> {

    @Override
    public JsonElement serialize(Strategy src, Type typeOfSrc, JsonSerializationContext context) {
        if (src instanceof PureStrategy) {
            return context.serialize(src, PureStrategy.class);
        } else if (src instanceof MixedStrategy) {
            return context.serialize(src, MixedStrategy.class);
        }
        return context.serialize(src, typeOfSrc);
    }

    @Override
    public Strategy deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        Strategy strategy;
        try {
            strategy = context.deserialize(json, PureStrategy.class);
        } catch(Exception e) {
            //wrong class
        }
        strategy = context.deserialize(json, MixedStrategy.class);
        return strategy;
    }

}
