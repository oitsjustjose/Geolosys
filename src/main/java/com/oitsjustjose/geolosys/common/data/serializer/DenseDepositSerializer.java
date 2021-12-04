package com.oitsjustjose.geolosys.common.data.serializer;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.oitsjustjose.geolosys.api.world.deposit.DenseDeposit;

public class DenseDepositSerializer {
    public DenseDeposit deserialize(JsonObject json, JsonDeserializationContext ctx) {
        return DenseDeposit.deserialize(json, ctx);
    }

    public JsonElement serialize(DenseDeposit dep, JsonSerializationContext ctx) {
        return dep.serialize(dep, ctx);
    }
}
