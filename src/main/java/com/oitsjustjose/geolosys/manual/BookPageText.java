package com.oitsjustjose.geolosys.manual;

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
