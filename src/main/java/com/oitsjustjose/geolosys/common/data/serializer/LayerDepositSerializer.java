package com.oitsjustjose.geolosys.common.data.serializer;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.oitsjustjose.geolosys.api.world.deposit.LayerDeposit;

public class LayerDepositSerializer {
    public LayerDeposit deserialize(JsonObject json) {
        return LayerDeposit.deserialize(json);
    }

    public JsonElement serialize(LayerDeposit dep) {
        return dep.serialize();
    }
}
