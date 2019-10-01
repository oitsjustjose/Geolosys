package com.oitsjustjose.geolosys.client.manual.pages;

/**
 * @author Mangoose / https://github.com/the-realest-stu/ Code taken directly
 * from:
 * https://github.com/the-realest-stu/Adventurers-Toolbox/tree/master/src/main/java/api/guide
 */

public class BookPageText extends BookPage
{

    private final String text;

    public BookPageText(String title, String text)
    {
        super(title);
        this.text = text;
    }

    public String getText()
    {
        return this.text;
    }
}
