package com.oitsjustjose.geolosys.client.manual;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import com.oitsjustjose.geolosys.Geolosys;

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
        Geolosys.getInstance().LOGGER.info("Fetching Patrons from the web...");
        try
        {

            URL url = new URL("http://patreon.forgeserv.net");

            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));

            String line;
            StringBuilder allData = new StringBuilder();
            while ((line = in.readLine()) != null)
            {
                allData.append(line);
            }
            in.close();
            for (String patronName : allData.toString().split("<br>"))
            {
                if (!patronName.isEmpty())
                {
                    this.patrons.add(patronName);
                }
            }
        }
        catch (IOException e)
        {
            Geolosys.getInstance().LOGGER.error("Patrons could not be fetched - are you connected to the internet?");
        }
        finally
        {
            Geolosys.getInstance().LOGGER.info("Patrons successfully fetched!");
        }
    }
}