package com.oitsjustjose.geolosys.common.data.serializer;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.oitsjustjose.geolosys.api.world.deposit.TopLayerDeposit;

public class TopLayerDepositSerializer {
    public TopLayerDeposit deserialize(JsonObject json, JsonDeserializationContext ctx) {
        return TopLayerDeposit.deserialize(json, ctx);
    }

    public JsonElement serialize(TopLayerDeposit dep, JsonSerializationContext ctx) {
        return dep.serialize(dep, ctx);
    }
}
