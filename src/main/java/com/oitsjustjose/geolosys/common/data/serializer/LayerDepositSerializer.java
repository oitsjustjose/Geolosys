package com.oitsjustjose.geolosys.common.data.serializer;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.oitsjustjose.geolosys.api.world.deposit.LayerDeposit;

public class LayerDepositSerializer {
    public LayerDeposit deserialize(JsonObject json, JsonDeserializationContext ctx) {
        return LayerDeposit.deserialize(json, ctx);
    }

    public JsonElement serialize(LayerDeposit dep, JsonSerializationContext ctx) {
        return dep.serialize(dep, ctx);
    }
}
