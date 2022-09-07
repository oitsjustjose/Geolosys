package com.oitsjustjose.geolosys.common.data.serializer;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.oitsjustjose.geolosys.api.world.deposit.DikeDeposit;

public class DikeDepositSerializer {
    public DikeDeposit deserialize(JsonObject json) {
        return DikeDeposit.deserialize(json);
    }

    public JsonElement serialize(DikeDeposit dep) {
        return dep.serialize();
    }
}
