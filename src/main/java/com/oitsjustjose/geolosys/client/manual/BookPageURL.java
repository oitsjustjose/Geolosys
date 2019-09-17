package com.oitsjustjose.geolosys.client.manual;

import net.minecraft.client.resources.I18n;

/**
 * @author Mangoose / https://github.com/the-realest-stu/ Code taken directly from:
 *         https://github.com/the-realest-stu/Adventurers-Toolbox/tree/master/src/main/java/api/guide
 */

public class BookPageURL extends BookPage
{

    private final String text;
    private final String url;
    private final String buttonText;

    public BookPageURL(String title, String text, String URL, String buttonText)
    {
        super(I18n.format(title));
        this.text = text;
        this.url = URL;
        this.buttonText = buttonText;
    }

    public String getText()
    {
        return this.text;
    }

    public String getURL()
    {
        return this.url;
    }

    public String getButtonText()
    {
        return this.buttonText;
    }
}
