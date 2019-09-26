package com.oitsjustjose.geolosys.client;

import com.oitsjustjose.geolosys.client.manual.GUIManual;
import com.oitsjustjose.geolosys.common.CommonProxy;

public class ClientProxy extends CommonProxy
{
    public void init()
    {
        GUIManual.initPages();
    }
}