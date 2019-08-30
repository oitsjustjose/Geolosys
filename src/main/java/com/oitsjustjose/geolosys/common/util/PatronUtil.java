package com.oitsjustjose.geolosys.common.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

public class PatronUtil
{
    private static PatronUtil instance;
    private ArrayList<String> patrons;

    public PatronUtil()
    {

        this.init();
    }

    public static PatronUtil getInstance()
    {
        if (instance == null)
        {
            instance = new PatronUtil();
        }
        return instance;
    }

    public ArrayList<String> getPatrons()
    {
        return this.patrons;
    }

    public void init()
    {
        this.patrons = new ArrayList<>();
        try
        {
            URL url = new URL("http://patreon.forgeserv.net");

            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));

            String line;
            while ((line = in.readLine()) != null)
            {
                System.out.println(line);
            }

            in.close();

        }
        catch (IOException e)
        {
        }
    }
}