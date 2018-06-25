package com.oitsjustjose.geolosys.manual;
/**
 * @credits: Mangoose /  https://github.com/the-realest-stu/
 * Code taken directly from:
 * https://github.com/the-realest-stu/Adventurers-Toolbox/tree/master/src/main/java/api/guide
 */

import java.util.ArrayList;
import java.util.List;

public class BookChapter
{
    private String name;
    private String parent;
    private List<BookPage> pages;

    public BookChapter(String name)
    {
        this.name = name;
        this.parent = name;
        this.pages = new ArrayList<BookPage>();
    }

    public BookChapter(String name, String parent)
    {
        this.name = name;
        this.parent = parent;
        this.pages = new ArrayList<BookPage>();
    }

    public void addPage(BookPage page)
    {
        this.pages.add(page);
    }

    public BookPage getPage(int pageNum)
    {
        if (pageNum < 0 || pageNum >= this.pages.size())
        {
            return null;
        }
        return pages.get(pageNum);
    }

    public int getPageCount()
    {
        return this.pages.size();
    }

    public String getName()
    {
        return this.name;
    }

    public String getParent()
    {
        return this.parent;
    }
}
