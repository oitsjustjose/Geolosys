package com.oitsjustjose.geolosys.client.manual.pages;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mangoose / https://github.com/the-realest-stu/ Code taken directly
 * from:
 * https://github.com/the-realest-stu/Adventurers-Toolbox/tree/master/src/main/java/api/guide
 */

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
