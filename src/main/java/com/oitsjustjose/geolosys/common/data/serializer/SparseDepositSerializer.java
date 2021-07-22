package com.oitsjustjose.geolosys.common.data.serializer;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.oitsjustjose.geolosys.api.world.deposit.SparseDeposit;

public class SparseDepositSerializer {
    public SparseDeposit deserialize(JsonObject json, JsonDeserializationContext ctx) {
        return SparseDeposit.deserialize(json, ctx);
    }

    public JsonElement serialize(SparseDeposit dep, JsonSerializationContext ctx) {
        return dep.serialize(dep, ctx);
    }
}