package org.abstruck.miraibangumi.data;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

public class Infobox implements JsonDeserializer<Infobox> {

    @Override
    public Infobox deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        
        boolean isExtraMap = json.getAsJsonObject().get("value").isJsonArray();
        if(!isExtraMap){
            return new StringOrderedMap(json.getAsJsonObject().get("key").getAsString(),json.getAsJsonObject().get("value").getAsString());
        }

        AtomicInteger isKvMap = new AtomicInteger(0);
        json.getAsJsonObject().get("value").getAsJsonArray().asList().stream().findAny().ifPresent(j -> {
            isKvMap.set(j.getAsJsonObject().keySet().contains("k")?1:2);   
        });
        if(isKvMap.get()==1){
            List<KVMap.KV> value = json.getAsJsonObject().get("value").getAsJsonArray().asList().stream()
                    .map(j -> new KVMap.KV(j.getAsJsonObject().get("k").getAsString(), j.getAsJsonObject().get("v").getAsString()))
                    .toList();

            return new KVMap(json.getAsJsonObject().get("key").getAsString(), value);        
        }
        if(isKvMap.get()==2){
            List<VMap.V> value =  json.getAsJsonObject().get("value").getAsJsonArray().asList().stream()
                .map(j -> new VMap.V(j.getAsJsonObject().get("v").getAsString()))
                .toList();
            return new VMap(json.getAsJsonObject().get("key").getAsString(), value);
        }
        throw new JsonParseException("");
    }
    
}
