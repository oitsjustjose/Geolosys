package com.oitsjustjose.geolosys.common.world.feature;

import javax.annotation.Nullable;

public enum PlutonType
{
    SPARSE, DENSE, DIKE;

    @Nullable
    PlutonType fromString(String s)
    {
        if (s.equalsIgnoreCase("sparse"))
        {
            return SPARSE;
        }
        if (s.equalsIgnoreCase("dense"))
        {
            return DENSE;
        }
        if (s.equalsIgnoreCase("DIKE"))
        {
            return DIKE;
        }
        return null;
    }
}