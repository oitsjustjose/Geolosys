package com.oitsjustjose.geolosys.client.manual;

import com.oitsjustjose.geolosys.api.world.DepositBiomeRestricted;
import com.oitsjustjose.geolosys.api.world.DepositMultiOre;
import com.oitsjustjose.geolosys.api.world.DepositMultiOreBiomeRestricted;
import com.oitsjustjose.geolosys.api.world.IDeposit;
import net.minecraft.block.BlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;

import java.util.ArrayList;
import java.util.List;

public class Pages
{
    /**
     * @author Mangoose / https://github.com/the-realest-stu/ Code taken directly from:
     *         https://github.com/the-realest-stu/Adventurers-Toolbox/tree/master/src/main/java/api/guide
     */

    public static class BookChapter
    {
        private String name;
        private String parent;
        private List<BookPage> pages;

        public BookChapter(String name)
        {
            this.name = name;
            this.parent = name;
            this.pages = new ArrayList<>();
        }

        public BookChapter(String name, String parent)
        {
            this.name = name;
            this.parent = parent;
            this.pages = new ArrayList<>();
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

    /**
     * @author Mangoose / https://github.com/the-realest-stu/ Code taken directly from:
     *         https://github.com/the-realest-stu/Adventurers-Toolbox/tree/master/src/main/java/api/guide
     */

    public static class BookPage
    {
        private final String title;

        public BookPage(String title)
        {
            this.title = title;
        }

        public String getTitle()
        {
            return this.title;
        }
    }

    /**
     * @author Mangoose / https://github.com/the-realest-stu/ Code taken directly from:
     *         https://github.com/the-realest-stu/Adventurers-Toolbox/tree/master/src/main/java/api/guide
     */

    public static class BookPageContents extends BookPage
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

    /**
     * @author Mangoose / https://github.com/the-realest-stu/ Code taken directly from:
     *         https://github.com/the-realest-stu/Adventurers-Toolbox/tree/master/src/main/java/api/guide
     */

    public static class BookPageItemDisplay extends BookPage
    {
        private final String description;
        private ItemStack displayStack;

        public BookPageItemDisplay(String title, String desc, ItemStack stack)
        {
            super(title);
            this.description = desc;
            this.displayStack = stack;
        }

        public ItemStack getDisplayStack()
        {
            return displayStack.copy();
        }

        public String getDescription()
        {
            return description;
        }
    }

    /**
     * @author oitsjustjose,
     * @author Mangoose / https://github.com/the-realest-stu/ Code inspired directly from:
     *         https://github.com/the-realest-stu/Adventurers-Toolbox/tree/master/src/main/java/api/guide
     */

    public static class BookPageOre extends BookPage
    {
        private IDeposit ore;
        private long lastUpdate;
        private ItemStack displayStack;

        public BookPageOre(IDeposit ore)
        {
            super(ore.getFriendlyName());
            this.ore = ore;
            this.lastUpdate = System.currentTimeMillis();
            BlockState tmp = ore.getOre();
            this.displayStack = new ItemStack(tmp.getBlock().asItem());
        }

        public ItemStack getDisplayStack()
        {
            if (this.isMultiOre())
            {
                if (System.currentTimeMillis() - this.lastUpdate >= 1000)
                {
                    BlockState tmp = ore.getOre();
                    this.displayStack = new ItemStack(tmp.getBlock().asItem());
                    this.lastUpdate = System.currentTimeMillis();
                }
            }
            return this.displayStack;
        }

        public int getMinY()
        {
            return this.ore.getYMin();
        }

        public int getMaxY()
        {
            return this.ore.getYMax();
        }

        public int getSize()
        {
            return this.ore.getSize();
        }

        public int getChance()
        {
            return this.ore.getChance();
        }

        public boolean isMultiOre()
        {
            return this.ore instanceof DepositMultiOre;
        }

        public boolean isBiomeRestricted()
        {
            return this.ore instanceof DepositBiomeRestricted || this.ore instanceof DepositMultiOreBiomeRestricted;
        }

        public String getFriendlyName()
        {
            return this.ore.getFriendlyName();
        }

        public String getBiomes()
        {
            if (this.ore instanceof DepositBiomeRestricted)
            {
                DepositBiomeRestricted biomeRestricted = (DepositBiomeRestricted) this.ore;
                if (biomeRestricted.getBiomeList().size() > 1)
                {
                    StringBuilder sb = new StringBuilder();
                    for (Biome b : biomeRestricted.getBiomeList())
                    {
                        sb.append(", ");
                        sb.append(b.getDisplayName().getFormattedText());
                    }
                    for (BiomeDictionary.Type b : biomeRestricted.getBiomeTypes())
                    {
                        sb.append(", ");
                        sb.append(b.getName());
                    }
                    String retVal = sb.toString();
                    return retVal.substring(2, retVal.lastIndexOf(",")) + " &"
                            + retVal.substring(retVal.lastIndexOf(",") + 1);
                }
                return biomeRestricted.getBiomeList().get(0).getDisplayName().getFormattedText();
            }
            else if (this.ore instanceof DepositMultiOreBiomeRestricted)
            {
                DepositMultiOreBiomeRestricted biomeRestricted = (DepositMultiOreBiomeRestricted) this.ore;
                if (biomeRestricted.getBiomeList().size() > 1)
                {
                    StringBuilder sb = new StringBuilder();
                    for (Biome b : biomeRestricted.getBiomeList())
                    {
                        sb.append(", ");
                        sb.append(b.getDisplayName().getFormattedText());
                    }
                    for (BiomeDictionary.Type b : biomeRestricted.getBiomeTypes())
                    {
                        sb.append(", ");
                        sb.append(b.getName());
                    }
                    String retVal = sb.toString();
                    return retVal.substring(2, retVal.lastIndexOf(",")) + " &"
                            + retVal.substring(retVal.lastIndexOf(",") + 1);
                }
                return biomeRestricted.getBiomeList().get(0).getDisplayName().getFormattedText();
            }
            return null;
        }

        public int getHarvestLevel()
        {
            if (this.isMultiOre())
            {
                DepositMultiOre multiOre = (DepositMultiOre) this.ore;
                int highest = 0;
                for (BlockState state : multiOre.getOres())
                {
                    if (state.getHarvestLevel() > highest)
                    {
                        highest = state.getBlock().getHarvestLevel(state);
                    }
                }
                return highest;
            }
            return this.ore.getOre().getBlock().getHarvestLevel(this.ore.getOre());
        }
    }

    /**
     * @author Mangoose / https://github.com/the-realest-stu/ Code taken directly from:
     *         https://github.com/the-realest-stu/Adventurers-Toolbox/tree/master/src/main/java/api/guide
     */

    public static class BookPageText extends BookPage
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

    /**
     * @author Mangoose / https://github.com/the-realest-stu/ Code taken directly from:
     *         https://github.com/the-realest-stu/Adventurers-Toolbox/tree/master/src/main/java/api/guide
     */

    public static class BookPageURL extends BookPage
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
}
