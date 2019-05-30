package com.oitsjustjose.geolosys.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import javax.annotation.Nonnull;

import com.oitsjustjose.geolosys.Geolosys;
import com.oitsjustjose.geolosys.common.api.GeolosysAPI;
import com.oitsjustjose.geolosys.common.api.world.IOre;
import com.oitsjustjose.geolosys.common.config.ModConfig;
import com.oitsjustjose.geolosys.common.items.ItemCluster;
import com.oitsjustjose.geolosys.common.manual.BookChapter;
import com.oitsjustjose.geolosys.common.manual.BookPage;
import com.oitsjustjose.geolosys.common.manual.BookPageContents;
import com.oitsjustjose.geolosys.common.manual.BookPageItemDisplay;
import com.oitsjustjose.geolosys.common.manual.BookPageOre;
import com.oitsjustjose.geolosys.common.manual.BookPageText;
import com.oitsjustjose.geolosys.common.manual.ChapterLink;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

/**
 * @author Mangoose / https://github.com/the-realest-stu/ Code taken directly from:
 *         https://github.com/the-realest-stu/Adventurers-Toolbox/tree/master/src/main/java/api/guide
 */

@SideOnly(Side.CLIENT)
public class GuiManual extends GuiScreen
{
    private static final int WIDTH = 146;
    private static final int HEIGHT = 180;
    private static final ResourceLocation BACKGROUND = new ResourceLocation(Geolosys.MODID, "textures/gui/book.png");
    private static HashMap<String, BookChapter> chapters = new HashMap<>();
    private String currentChapter;
    private int currentPageNum;
    private BookPage currentPage;
    private String lastChapter;
    private int lastPageNum;
    private ItemStack display = ItemStack.EMPTY;
    private int left, top;

    public GuiManual()
    {
        currentChapter = "home";
        currentPageNum = 0;
    }

    public static void initPages()
    {
        BookPageContents home = new BookPageContents("geolosys.guide.page.home.name");
        home.addLink(new ChapterLink("geolosys.guide.chapter.introduction.name", "introduction"));
        home.addLink(new ChapterLink("geolosys.guide.chapter.prospecting.name", "prospecting"));
        home.addLink(new ChapterLink("geolosys.guide.chapter.resources.name", "resources"));
        home.addLink(new ChapterLink("geolosys.guide.chapter.mod_compat.name", "mod_compat"));
        chapters.put("home", new BookChapter("home"));
        chapters.put("introduction", new BookChapter("introduction", "home"));
        chapters.put("prospecting", new BookChapter("prospecting", "home"));
        chapters.put("resources", new BookChapter("resources", "home"));
        chapters.put("mod_compat", new BookChapter("mod_compat", "home"));

        chapters.get("home").addPage(home);

        BookPageContents introduction = new BookPageContents("geolosys.guide.chapter.introduction.name");
        introduction.addLink(new ChapterLink("geolosys.guide.chapter.getting_started.name", "getting_started"));
        introduction.addLink(new ChapterLink("geolosys.guide.chapter.vanilla_ores.name", "vanilla_ores"));
        introduction.addLink(new ChapterLink("geolosys.guide.chapter.modded_ores.name", "modded_ores"));
        chapters.put("getting_started", new BookChapter("getting_started", "introduction"));
        chapters.put("vanilla_ores", new BookChapter("vanilla_ores", "introduction"));
        chapters.put("modded_ores", new BookChapter("modded_ores", "introduction"));

        chapters.get("introduction").addPage(introduction);
        chapters.get("getting_started").addPage(new BookPageText("geolosys.guide.chapter.getting_started.name",
                "geolosys.guide.chapter.getting_started.text"));
        chapters.get("vanilla_ores").addPage(new BookPageText("geolosys.guide.chapter.vanilla_ores.name",
                "geolosys.guide.chapter.vanilla_ores.text"));
        chapters.get("modded_ores").addPage(
                new BookPageText("geolosys.guide.chapter.modded_ores.name", "geolosys.guide.chapter.modded_ores.text"));

        BookPageContents prospecting = new BookPageContents("geolosys.guide.chapter.prospecting.name");
        prospecting.addLink(new ChapterLink("geolosys.guide.chapter.samples.name", "samples"));
        chapters.put("samples", new BookChapter("samples", "prospecting"));
        chapters.get("samples").addPage(new BookPageItemDisplay("geolosys.guide.chapter.samples.name",
                "geolosys.guide.chapter.samples_1.text", new ItemStack(Geolosys.getInstance().ORE_SAMPLE)));
        chapters.get("samples").addPage(new BookPageItemDisplay("geolosys.guide.chapter.samples.name",
                "geolosys.guide.chapter.samples_2.text", new ItemStack(Geolosys.getInstance().CLUSTER)));

        if (ModConfig.prospecting.enableProPick)
        {
            prospecting.addLink(new ChapterLink("geolosys.guide.chapter.propick.name", "propick"));
            chapters.put("propick", new BookChapter("propick", "prospecting"));
            chapters.get("propick").addPage(new BookPageItemDisplay("geolosys.guide.chapter.propick.name",
                    "geolosys.guide.chapter.propick_1.text", new ItemStack(Geolosys.getInstance().PRO_PICK)));
            chapters.get("propick").addPage(
                    new BookPageText("geolosys.guide.chapter.propick.name", "geolosys.guide.chapter.propick_2.text"));
            chapters.get("propick").addPage(
                    new BookPageText("geolosys.guide.chapter.propick.name", "geolosys.guide.chapter.propick_3.text"));
        }
        chapters.get("prospecting").addPage(prospecting);

        ArrayList<BookPageContents> resources = new ArrayList<>();
        resources.add(new BookPageContents("geolosys.guide.chapter.resources.name"));
        int count = 0;
        int page_num = 0;

        if (ModConfig.featureControl.modStones)
        {
            resources.get(page_num).addLink(new ChapterLink("geolosys.guide.chapter.stones.name", "stones"));
            chapters.put("stones", new BookChapter("stones", "resources"));
            chapters.get("stones").addPage(new BookPageItemDisplay("geolosys.guide.chapter.stones.name",
                    "geolosys.guide.chapter.stones.text", new ItemStack(Blocks.STONE, 1, new Random().nextInt(3) + 1)));
            count++;
            if (count == 12)
            {
                resources.add(new BookPageContents("geolosys.guide.chapter.resources.name"));
                page_num += 1;
                count = 0;
            }
        }
        for (IOre ore : GeolosysAPI.oreBlocks)
        {
            resources.get(page_num).addLink(new ChapterLink(ore.getFriendlyName(), ore.getFriendlyName()));
            chapters.put(ore.getFriendlyName(), new BookChapter(ore.getFriendlyName(), "resources"));
            chapters.get(ore.getFriendlyName()).addPage(new BookPageOre(ore));
            count++;
            if (count == 12)
            {
                resources.add(new BookPageContents("geolosys.guide.chapter.resources.name"));
                page_num += 1;
                count = 0;
            }
        }

        for (BookPageContents contents : resources)
        {
            chapters.get("resources").addPage(contents);
        }

        BookPageContents modCompat = new BookPageContents("geolosys.guide.chapter.mod_compat.name");

        if (ModConfig.featureControl.retroReplace)
        {
            modCompat.addLink(new ChapterLink("geolosys.guide.chapter.ore_converter.name", "ore_converter"));
            chapters.put("ore_converter", new BookChapter("ore_converter", "mod_compat"));
            chapters.get("ore_converter").addPage(new BookPageText("geolosys.guide.chapter.ore_converter.name",
                    "geolosys.guide.chapter.ore_converter.text"));
        }
        if (Loader.isModLoaded("journeymap"))
        {
            modCompat.addLink(new ChapterLink("geolosys.guide.chapter.journeymap.name", "journeymap"));
            chapters.put("journeymap", new BookChapter("journeymap", "mod_compat"));
            chapters.get("journeymap").addPage(new BookPageText("geolosys.guide.chapter.journeymap.name",
                    "geolosys.guide.chapter.journeymap.text"));
        }
        if (Loader.isModLoaded("immersiveengineering") && ModConfig.featureControl.enableIECompat)
        {
            modCompat.addLink(
                    new ChapterLink("geolosys.guide.chapter.immersive_engineering.name", "immersive_engineering"));
            chapters.put("immersive_engineering", new BookChapter("immersive_engineering", "mod_compat"));
            chapters.get("immersive_engineering")
                    .addPage(new BookPageItemDisplay("geolosys.guide.chapter.immersive_engineering.name",
                            "geolosys.guide.chapter.immersive_engineering.text",
                            OreDictionary.getOres("dustSulfur").get(0)));
        }
        if (Loader.isModLoaded("betterwithmods"))
        {
            modCompat.addLink(new ChapterLink("geolosys.guide.chapter.better_with_mods.name", "better_with_mods"));
            chapters.put("better_with_mods", new BookChapter("better_with_mods", "mod_compat"));
            chapters.get("better_with_mods")
                    .addPage(new BookPageItemDisplay("geolosys.guide.chapter.better_with_mods.name",
                            "geolosys.guide.chapter.better_with_mods.text", new ItemStack(Items.IRON_NUGGET)));
        }
        if (Loader.isModLoaded("twilightforest"))
        {
            modCompat.addLink(new ChapterLink("geolosys.guide.chapter.twilight_forest.name", "twilight_forest"));
            chapters.put("twilight_forest", new BookChapter("twilight_forest", "mod_compat"));
            chapters.get("twilight_forest").addPage(new BookPageText("geolosys.guide.chapter.twilight_forest.name",
                    "geolosys.guide.chapter.twilight_forest.text"));
        }
        if (ForgeRegistries.ITEMS.getValue(new ResourceLocation("appliedenergistics2", "material")) != null)
        {
            modCompat
                    .addLink(new ChapterLink("geolosys.guide.chapter.applied_energistics.name", "applied_energistics"));
            chapters.put("applied_energistics", new BookChapter("applied_energistics", "mod_compat"));
            chapters.get("applied_energistics").addPage(new BookPageItemDisplay(
                    "geolosys.guide.chapter.applied_energistics.name",
                    "geolosys.guide.chapter.applied_energistics.text",
                    new ItemStack(Objects.requireNonNull(
                            ForgeRegistries.ITEMS.getValue(new ResourceLocation("appliedenergistics2", "material"))), 1,
                            0)));
        }
        if (ForgeRegistries.ITEMS.getValue(new ResourceLocation("extrautils2", "ingredients")) != null)
        {
            modCompat.addLink(new ChapterLink("geolosys.guide.chapter.extra_utils.name", "extra_utils"));
            chapters.put("extra_utils", new BookChapter("extra_utils", "mod_compat"));
            chapters.get("extra_utils").addPage(new BookPageItemDisplay("geolosys.guide.chapter.extra_utils.name",
                    "geolosys.guide.chapter.extra_utils.text", new ItemStack(Objects.requireNonNull(
                            ForgeRegistries.ITEMS.getValue(new ResourceLocation("extrautils2", "ingredients"))))));
        }
        if (ForgeRegistries.ITEMS.getValue(new ResourceLocation("actuallyadditions", "item_misc")) != null)
        {
            modCompat.addLink(new ChapterLink("geolosys.guide.chapter.actually_additions.name", "actually_additions"));
            chapters.put("actually_additions", new BookChapter("actually_additions", "mod_compat"));
            chapters.get("actually_additions").addPage(new BookPageItemDisplay(
                    "geolosys.guide.chapter.actually_additions.name", "geolosys.guide.chapter.actually_additions.text",
                    new ItemStack(Objects.requireNonNull(
                            ForgeRegistries.ITEMS.getValue(new ResourceLocation("actuallyadditions", "item_misc"))), 1,
                            5)));
        }
        if (ForgeRegistries.ITEMS.getValue(new ResourceLocation("thermalfoundation", "material")) != null)
        {
            modCompat.addLink(new ChapterLink("geolosys.guide.chapter.cofh_mods.name", "cofh_mods"));
            chapters.put("cofh_mods", new BookChapter("cofh_mods", "mod_compat"));
            chapters.get("cofh_mods").addPage(new BookPageItemDisplay("geolosys.guide.chapter.cofh_mods.name",
                    "geolosys.guide.chapter.cofh_mods.text",
                    new ItemStack(Objects.requireNonNull(
                            ForgeRegistries.ITEMS.getValue(new ResourceLocation("thermalfoundation", "material"))), 1,
                            866)));
        }
        if (ModConfig.featureControl.enableOsmium)
        {
            modCompat.addLink(new ChapterLink("geolosys.guide.chapter.mekanism.name", "mekanism"));
            chapters.put("mekanism", new BookChapter("mekanism", "mod_compat"));
            chapters.get("mekanism")
                    .addPage(new BookPageItemDisplay("geolosys.guide.chapter.mekanism.name",
                            "geolosys.guide.chapter.mekanism.text",
                            new ItemStack(Geolosys.getInstance().CLUSTER, 1, ItemCluster.META_OSMIUM)));
        }
        if (ModConfig.featureControl.enableYellorium)
        {
            modCompat.addLink(new ChapterLink("geolosys.guide.chapter.extreme_reactors.name", "extreme_reactors"));
            chapters.put("extreme_reactors", new BookChapter("extreme_reactors", "mod_compat"));
            chapters.get("extreme_reactors")
                    .addPage(new BookPageItemDisplay("geolosys.guide.chapter.extreme_reactors.name",
                            "geolosys.guide.chapter.extreme_reactors.text",
                            new ItemStack(Geolosys.getInstance().CLUSTER, 1, ItemCluster.META_YELLORIUM)));
        }
        if (ForgeRegistries.ITEMS.getValue(new ResourceLocation("nuclearcraft", "gem")) != null)
        {
            modCompat.addLink(new ChapterLink("geolosys.guide.chapter.nuclearcraft.name", "nuclearcraft"));
            chapters.put("nuclearcraft", new BookChapter("nuclearcraft", "mod_compat"));
            chapters.get("nuclearcraft")
                    .addPage(new BookPageItemDisplay("geolosys.guide.chapter.nuclearcraft.name",
                            "geolosys.guide.chapter.nuclearcraft.text",
                            new ItemStack(Objects.requireNonNull(
                                    ForgeRegistries.ITEMS.getValue(new ResourceLocation("nuclearcraft", "gem"))), 1,
                                    0)));
        }
        chapters.get("mod_compat").addPage(modCompat);

        for (BookChapter chapter : chapters.values())
        {
            if (chapter.getPageCount() <= 0)
            {
                chapter.addPage(new BookPage(chapter.getName()));
            }
        }
    }

    @Override
    public void initGui()
    {
        this.top = (this.height - HEIGHT) / 2;
        this.left = (this.width - WIDTH) / 2;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        Minecraft.getMinecraft().renderEngine.bindTexture(BACKGROUND);
        this.drawTexturedModalRect(left, top, 0, 0, WIDTH, HEIGHT);

        if (currentPage != null)
        {
            String header = TextFormatting.BOLD + "" + TextFormatting.UNDERLINE + I18n.format(currentPage.getTitle());
            float textScale = ModConfig.client.manualFontScale;
            List<String> parts = this.fontRenderer.listFormattedStringToWidth(header,
                    (int) ((WIDTH - (18 * 2)) / textScale));
            // Draw the text above the book if it's multi-line :|
            if (parts.size() > 1)
            {
                int topToDraw = top - (12 * parts.size());
                for (String str : parts)
                {
                    int width = this.fontRenderer.getStringWidth(str);
                    this.fontRenderer.drawString(str, left + (WIDTH - width) / 2, topToDraw, 7829367);
                    topToDraw += 12;
                }
            }
            else
            {
                int headerWidth = this.fontRenderer.getStringWidth(header);
                this.fontRenderer.drawString(header, left + (WIDTH - headerWidth) / 2, top + 12, 0);
            }

            if (currentPage instanceof BookPageItemDisplay)
            {
                renderItemDisplayPage((BookPageItemDisplay) currentPage, mouseX, mouseY);
            }
            else if (currentPage instanceof BookPageText)
            {
                renderTextPage((BookPageText) currentPage);
            }
            else if (currentPage instanceof BookPageOre)
            {
                renderOrePage((BookPageOre) currentPage, mouseX, mouseY);
            }

            if (chapters.get(currentChapter).getPageCount() > 1)
            {
                GlStateManager.pushMatrix();
                GlStateManager.scale(textScale, textScale, textScale);
                String pageNum = (currentPageNum + 1) + "/" + chapters.get(currentChapter).getPageCount();
                int pageNumWidth = (int) (this.fontRenderer.getStringWidth(pageNum) * textScale);
                int x = (int) ((left + (WIDTH - pageNumWidth) / 2) / textScale);
                this.fontRenderer.drawSplitString(pageNum, x, (int) ((top + 164) / textScale),
                        (int) ((WIDTH - (18 * 2)) / textScale), 0);
                GlStateManager.popMatrix();
            }
        }

        super.drawScreen(mouseX, mouseY, partialTicks);

    }

    private void renderItemDisplayPage(BookPageItemDisplay page, int mouseX, int mouseY)
    {
        ItemStack stack = page.getDisplayStack();

        if (stack.getMetadata() == 32767)
        {
            stack = new ItemStack(stack.getItem(), 1, 0, stack.getTagCompound());
        }

        if (stack.getItem() != display.getItem() || stack.getMetadata() != display.getMetadata())
        {
            display = stack;
        }

        GlStateManager.pushMatrix();
        GlStateManager.scale(2F, 2F, 2F);
        RenderHelper.enableGUIStandardItemLighting();
        int itemX = (left + (WIDTH - 32) / 2);
        int itemY = (top + 24);
        float itemScale = 2F;
        this.itemRender.renderItemAndEffectIntoGUI(display, (int) (itemX / itemScale), (int) (itemY / itemScale));
        RenderHelper.disableStandardItemLighting();
        GlStateManager.popMatrix();

        GlStateManager.pushMatrix();
        float textScale = ModConfig.client.manualFontScale;
        GlStateManager.scale(textScale, textScale, textScale);
        this.fontRenderer.drawSplitString(I18n.format(page.getDescription()),

                (int) ((left + 18) / textScale), (int) ((top + 58) / textScale), (int) ((WIDTH - (18 * 2)) / textScale),
                0);
        GlStateManager.popMatrix();

        renderTooltip(mouseX, mouseY, itemX, itemY, itemScale);

    }

    private void renderOrePage(BookPageOre page, int mouseX, int mouseY)
    {
        ItemStack stack = page.getDisplayStack();

        if (stack.getMetadata() == 32767)
        {
            stack = new ItemStack(stack.getItem(), 1, 0, stack.getTagCompound());
        }

        if (stack.getItem() != display.getItem() || stack.getMetadata() != display.getMetadata())
        {
            display = stack;
        }

        GlStateManager.pushMatrix();
        GlStateManager.scale(2F, 2F, 2F);
        RenderHelper.enableGUIStandardItemLighting();
        int itemX = (left + (WIDTH - 32) / 2);
        int itemY = (top + 24);
        float itemScale = 2F;
        this.itemRender.renderItemAndEffectIntoGUI(display, (int) (itemX / itemScale), (int) (itemY / itemScale));
        RenderHelper.disableStandardItemLighting();
        GlStateManager.popMatrix();

        GlStateManager.pushMatrix();
        float textScale = ModConfig.client.manualFontScale;
        GlStateManager.scale(textScale, textScale, textScale);
        String minDepthFromSeaLevel = getFormattedSeaLevel(
                Minecraft.getMinecraft().world.getSeaLevel() - page.getMinY());
        String maxDepthFromSeaLevel = getFormattedSeaLevel(
                Minecraft.getMinecraft().world.getSeaLevel() - page.getMaxY());

        String description;
        if (page.isBiomeRestricted() && page.isMultiOre())
        {
            description = I18n.format("geolosys.guide.chapter.ore.mutli.biome.description", page.getFriendlyName(),
                    minDepthFromSeaLevel, maxDepthFromSeaLevel, page.getBiomes(), page.getChance(), page.getSize(),
                    page.getHarvestLevel());
        }
        else if (page.isMultiOre())
        {
            description = I18n.format("geolosys.guide.chapter.ore.multi.description", page.getFriendlyName(),
                    minDepthFromSeaLevel, maxDepthFromSeaLevel, page.getChance(), page.getSize(),
                    page.getHarvestLevel());
        }
        else if (page.isBiomeRestricted())
        {
            description = I18n.format("geolosys.guide.chapter.ore.biome.description", page.getFriendlyName(),
                    minDepthFromSeaLevel, maxDepthFromSeaLevel, page.getBiomes(), page.getChance(), page.getSize(),
                    page.getHarvestLevel());
        }
        else
        {
            description = I18n.format("geolosys.guide.chapter.ore.description", page.getFriendlyName(),
                    minDepthFromSeaLevel, maxDepthFromSeaLevel, page.getChance(), page.getSize(),
                    page.getHarvestLevel());
        }
        this.fontRenderer.drawSplitString(description, (int) ((left + 18) / textScale), (int) ((top + 58) / textScale),
                (int) ((WIDTH - (18 * 2)) / textScale), 0);
        GlStateManager.popMatrix();
        renderTooltip(mouseX, mouseY, itemX, itemY, itemScale);
    }

    private String getFormattedSeaLevel(int depth)
    {
        if (depth > 0)
        {
            return I18n.format("geolosys.guide.generic.belowsealevel", Math.abs(depth));
        }
        else if (depth < 0)
        {
            return I18n.format("geolosys.guide.generic.abovesealevel", Math.abs(depth));
        }
        else
        {
            return I18n.format("geolosys.guide.generic.atsealevel");
        }
    }

    private void renderTooltip(int mouseX, int mouseY, int itemX, int itemY, float itemScale)
    {
        if (mouseX >= itemX && mouseY >= itemY && mouseX <= itemX + (16 * itemScale)
                && mouseY <= itemY + (16 * itemScale))
        {
            GlStateManager.pushMatrix();
            float toolTipScale = .85F;
            GlStateManager.scale(toolTipScale, toolTipScale, toolTipScale);
            this.renderToolTip(display, (int) (mouseX / toolTipScale), (int) (mouseY / toolTipScale));
            RenderHelper.disableStandardItemLighting();
            GlStateManager.popMatrix();
        }
    }

    private void renderTextPage(BookPageText page)
    {
        GlStateManager.pushMatrix();
        float textScale = ModConfig.client.manualFontScale;
        GlStateManager.scale(textScale, textScale, textScale);
        String text = I18n.format(page.getText());
        List<String> paragraphs = new ArrayList<>();

        while (text.contains("|"))
        {
            int i = text.indexOf("|");
            paragraphs.add("    " + text.substring(0, i));
            if (i < text.length() - 1)
            {
                text = text.substring(i + 1);
            }
        }
        paragraphs.add("    " + text);

        int i = 24;
        for (String par : paragraphs)
        {
            this.fontRenderer.drawSplitString(par, (int) ((left + 18) / textScale), (int) ((top + i) / textScale),
                    (int) ((WIDTH - (18 * 2)) / textScale), 0);
            i += (int) (2 + fontRenderer.getWordWrappedHeight(par, (int) ((WIDTH - (18 * 2)) / textScale)) * textScale);
        }
        GlStateManager.popMatrix();
    }

    @Override
    public void updateScreen()
    {
        super.updateScreen();
        currentPage = chapters.get(currentChapter).getPage(currentPageNum);
        if (currentPageNum != lastPageNum || !currentChapter.equals(lastChapter))
        {
            resetPage();
        }

        this.lastChapter = this.currentChapter;
        this.lastPageNum = this.currentPageNum;
    }

    private void resetPage()
    {
        this.buttonList.clear();
        int i = 0;
        if (currentPage instanceof BookPageContents)
        {
            List<ChapterLink> links = ((BookPageContents) currentPage).getLinks();
            for (ChapterLink link : links)
            {
                this.addButton(new ChapterLinkButton(i, left + 16, top + 24 + (i * 12), link.text, link.chapter));
                i++;
            }
        }
        if (currentPageNum < chapters.get(currentChapter).getPageCount() - 1)
        {
            this.addButton(new PageTurnButton(i, left + 100, top + 154, true));
            i++;
        }
        if (currentPageNum > 0)
        {
            this.addButton(new PageTurnButton(i, left + 18, top + 154, false));
        }
    }

    @Override
    protected void actionPerformed(GuiButton button)
    {
        if (button instanceof ChapterLinkButton)
        {
            currentChapter = ((ChapterLinkButton) button).getChapter();
            currentPageNum = 0;
        }
        else if (button instanceof PageTurnButton)
        {
            if (((PageTurnButton) button).isForward())
            {
                currentPageNum++;
            }
            else
            {
                currentPageNum--;
            }
        }
    }

    @Override
    protected void keyTyped(char par1, int par2)
    {
        if (mc.gameSettings.keyBindInventory.getKeyCode() == par2)
        {
            if (currentChapter.equals(chapters.get(currentChapter).getParent()))
            {
                mc.displayGuiScreen(null);
                mc.setIngameFocus();
            }
            else
            {
                currentChapter = chapters.get(currentChapter).getParent();
                currentPageNum = 0;
            }
        }
        else if (1 == par2)
        {
            mc.displayGuiScreen(null);
            mc.setIngameFocus();
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
    {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (mouseButton == 1)
        {
            currentChapter = chapters.get(currentChapter).getParent();
            currentPageNum = 0;
        }
    }

    @Override
    public boolean doesGuiPauseGame()
    {
        return false;
    }

    @Override
    public void onResize(@Nonnull Minecraft mc, int w, int h)
    {
        this.setWorldAndResolution(mc, w, h);
        this.resetPage();
    }

    @SideOnly(Side.CLIENT)
    public class ChapterLinkButton extends GuiButton
    {

        private String chapter;

        public ChapterLinkButton(int buttonId, int x, int y, String buttonText, String chapter)
        {
            super(buttonId, x, y, Minecraft.getMinecraft().fontRenderer.getStringWidth(I18n.format(buttonText)),
                    Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT, buttonText);
            this.chapter = chapter;
        }

        @Override
        public void drawButton(@Nonnull Minecraft mc, int mouseX, int mouseY, float partialTicks)
        {
            if (this.visible)
            {
                FontRenderer fontrenderer = mc.fontRenderer;
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width
                        && mouseY < this.y + this.height;
                this.mouseDragged(mc, mouseX, mouseY);
                int j = 0;
                String p = "";

                if (!this.enabled)
                {
                    j = 10526880;
                }
                else if (this.hovered)
                {
                    j = 8308926;
                    p += TextFormatting.BOLD;
                }

                fontrenderer.drawSplitString(p + "• " + I18n.format(this.displayString), this.x, this.y,
                        (int) ((WIDTH - (18 * 2)) / ModConfig.client.manualFontScale), j);
            }
        }

        String getChapter()
        {
            return this.chapter;
        }

    }

    @SideOnly(Side.CLIENT)
    public class PageTurnButton extends GuiButton
    {

        private final boolean isForward;

        PageTurnButton(int buttonId, int x, int y, boolean isForward)
        {
            super(buttonId, x, y, 23, 13, "");
            this.isForward = isForward;
        }

        @Override
        public void drawButton(@Nonnull Minecraft mc, int mouseX, int mouseY, float partialTicks)
        {
            if (this.visible)
            {
                boolean flag = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width
                        && mouseY < this.y + this.height;
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                mc.getTextureManager().bindTexture(GuiManual.BACKGROUND);
                int i = 0;
                int j = 192;

                if (flag)
                {
                    i += 23;
                }

                if (!this.isForward)
                {
                    j += 13;
                }

                this.drawTexturedModalRect(this.x, this.y, i, j, 23, 13);
            }
        }

        boolean isForward()
        {
            return this.isForward;
        }
    }
}
