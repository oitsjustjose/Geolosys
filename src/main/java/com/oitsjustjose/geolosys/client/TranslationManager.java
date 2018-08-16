package com.oitsjustjose.geolosys.common.util;

import com.oitsjustjose.geolosys.Geolosys;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.Language;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

@SideOnly(Side.CLIENT)
public class TranslationManager
{
    private static TranslationManager instance;
    private HashMap<String, HashMap<String, String>> translations = new HashMap<>();

    private TranslationManager()
    {
        this.loadLanguages();
    }

    private void loadLanguages()
    {
        Geolosys.getInstance().LOGGER.info("[Geolosys] Caching languages");
        for (Language lang : Minecraft.getMinecraft().getLanguageManager().getLanguages())
        {
            translations.put(lang.getLanguageCode(), new HashMap<>());
            InputStream in;
            try
            {
                in = Geolosys.class.getResourceAsStream("/assets/geolosys/lang/" + lang.getLanguageCode().substring(0, lang.getLanguageCode().indexOf("_")) + lang.getLanguageCode().substring(lang.getLanguageCode().indexOf("_")).toUpperCase() + ".lang");
            }
            catch (StringIndexOutOfBoundsException e)
            {
                in = Geolosys.class.getResourceAsStream("/assets/geolosys/lang/en_US.lang");
                Geolosys.getInstance().LOGGER.info("Couldn't find langfile " + (lang.getLanguageCode().substring(0, lang.getLanguageCode().indexOf("_")) + lang.getLanguageCode().substring(lang.getLanguageCode().indexOf("_")).toUpperCase()) + ", defaulting to en_US");
            }
            if (in == null)
            {
                continue;
            }
            try
            {
                for (String s : IOUtils.readLines(in, "utf-8"))
                {
                    if (!s.contains("="))
                    {
                        continue;
                    }
                    translations.get(lang.getLanguageCode()).put(s.substring(0, s.indexOf("=")), s.substring(s.indexOf("=") + 1));
                }
            }
            catch (IOException ignored)
            {
            }
        }
        Geolosys.getInstance().LOGGER.info("[Geolosys] Done caching languages!");
    }

    public String translate(String untranslated)
    {
        if (translations.containsKey(Minecraft.getMinecraft().getLanguageManager().getCurrentLanguage().getLanguageCode()))
        {
            if (translations.get(Minecraft.getMinecraft().getLanguageManager().getCurrentLanguage().getLanguageCode()).containsKey(untranslated))
            {
                return translations.get(Minecraft.getMinecraft().getLanguageManager().getCurrentLanguage().getLanguageCode()).get(untranslated);
            }
        }
        return translations.get("en_us").get(untranslated);
    }

    public static void init()
    {
        instance = new TranslationManager();
    }

    public static TranslationManager getInstance()
    {
        return instance;
    }
}
