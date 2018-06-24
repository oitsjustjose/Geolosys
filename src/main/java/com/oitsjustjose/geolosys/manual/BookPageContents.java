package com.oitsjustjose.geolosys.manual;

import java.util.ArrayList;
import java.util.List;

public class BookPageContents extends BookPage
{
    private ArrayList<ChapterLink> links;

    public BookPageContents(String title)
    {
        super(title);
        this.links = new ArrayList<>();
    }

    public List<ChapterLink> getLinks()
    {
        return links;
    }

    public void addLink(ChapterLink link)
    {
        links.add(link);
    }
}
