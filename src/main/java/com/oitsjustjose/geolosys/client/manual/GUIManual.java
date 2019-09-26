package com.oitsjustjose.geolosys.client.manual;

import com.mojang.blaze3d.platform.GlStateManager;
import com.oitsjustjose.geolosys.api.world.IDeposit;
import com.oitsjustjose.geolosys.client.ConfigClient;
import com.oitsjustjose.geolosys.common.blocks.BlockInit;
import com.oitsjustjose.geolosys.common.compat.ConfigCompat;
import com.oitsjustjose.geolosys.common.config.ModConfig;
import com.oitsjustjose.geolosys.common.items.ItemInit;
import com.oitsjustjose.geolosys.common.utils.Constants;
import com.oitsjustjose.geolosys.common.world.PlutonRegistry;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@OnlyIn(Dist.CLIENT)
public class GUIManual extends Screen
{
    private static final int WIDTH = 146;
    private static final int HEIGHT = 180;
    private static final ResourceLocation BACKGROUND = new ResourceLocation(Constants.MODID, "textures/gui/book.png");
    private static HashMap<String, Pages.BookChapter> chapters = new HashMap<>();
    private String currentChapter;
    private int currentPageNum;
    private Pages.BookPage currentPage;
    private String lastChapter;
    private int lastPageNum;
    private ItemStack display = ItemStack.EMPTY;
    private int left, top;
    private FontRenderer fontRenderer;

    public GUIManual()
    {
        super(new StringTextComponent("Field Manual"));
        currentChapter = "home";
        currentPageNum = 0;
        this.fontRenderer = Minecraft.getInstance().fontRenderer;
    }

    public static void initPages()
    {
        Pages.BookPageContents home = new Pages.BookPageContents("geolosys.guide.page.home.name");
        home.addLink(new ChapterLink("geolosys.guide.chapter.introduction.name", "introduction"));
        home.addLink(new ChapterLink("geolosys.guide.chapter.prospecting.name", "prospecting"));
        home.addLink(new ChapterLink("geolosys.guide.chapter.resources.name", "resources"));
        home.addLink(new ChapterLink("geolosys.guide.chapter.mod_compat.name", "mod_compat"));
        home.addLink(new ChapterLink("geolosys.guide.chapter.patrons.name", "patrons"));

        chapters.put("home", new Pages.BookChapter("home"));
        chapters.put("introduction", new Pages.BookChapter("introduction", "home"));
        chapters.put("prospecting", new Pages.BookChapter("prospecting", "home"));
        chapters.put("resources", new Pages.BookChapter("resources", "home"));
        chapters.put("mod_compat", new Pages.BookChapter("mod_compat", "home"));
        chapters.put("patrons", new Pages.BookChapter("patrons", "home"));

        chapters.get("home").addPage(home);

        Pages.BookPageContents introduction = new Pages.BookPageContents("geolosys.guide.chapter.introduction.name");
        introduction.addLink(new ChapterLink("geolosys.guide.chapter.getting_started.name", "getting_started"));
        introduction.addLink(new ChapterLink("geolosys.guide.chapter.vanilla_ores.name", "vanilla_ores"));
        introduction.addLink(new ChapterLink("geolosys.guide.chapter.modded_ores.name", "modded_ores"));
        chapters.put("getting_started", new Pages.BookChapter("getting_started", "introduction"));
        chapters.put("vanilla_ores", new Pages.BookChapter("vanilla_ores", "introduction"));
        chapters.put("modded_ores", new Pages.BookChapter("modded_ores", "introduction"));

        chapters.get("introduction").addPage(introduction);
        chapters.get("getting_started").addPage(new Pages.BookPageText("geolosys.guide.chapter.getting_started.name",
                "geolosys.guide.chapter.getting_started.text"));
        chapters.get("vanilla_ores").addPage(new Pages.BookPageText("geolosys.guide.chapter.vanilla_ores.name",
                "geolosys.guide.chapter.vanilla_ores.text"));
        chapters.get("modded_ores").addPage(new Pages.BookPageText("geolosys.guide.chapter.modded_ores.name",
                "geolosys.guide.chapter.modded_ores.text"));

        Pages.BookPageContents prospecting = new Pages.BookPageContents("geolosys.guide.chapter.prospecting.name");
        prospecting.addLink(new ChapterLink("geolosys.guide.chapter.samples.name", "samples"));
        chapters.put("samples", new Pages.BookChapter("samples", "prospecting"));
        chapters.get("samples")
                .addPage(new Pages.BookPageItemDisplay("geolosys.guide.chapter.samples.name",
                        "geolosys.guide.chapter.samples_1.text",
                        new ItemStack(BlockInit.getInstance().getModBlocks().get("geolosys:hematite_sample"))));
        chapters.get("samples")
                .addPage(new Pages.BookPageItemDisplay("geolosys.guide.chapter.samples.name",
                        "geolosys.guide.chapter.samples_2.text",
                        new ItemStack(BlockInit.getInstance().getModBlocks().get("geolosys:hematite_sample"))));

        if (ModConfig.ENABLE_PRO_PICK.get())
        {
            prospecting.addLink(new ChapterLink("geolosys.guide.chapter.propick.name", "propick"));
            chapters.put("propick", new Pages.BookChapter("propick", "prospecting"));
            // chapters.get("propick").addPage(new BookPageItemDisplay("geolosys.guide.chapter.propick.name",
            // "geolosys.guide.chapter.propick_1.text", new ItemStack(Geolosys.getInstance().PRO_PICK)));
            chapters.get("propick").addPage(new Pages.BookPageText("geolosys.guide.chapter.propick.name",
                    "geolosys.guide.chapter.propick_2.text"));
            chapters.get("propick").addPage(new Pages.BookPageText("geolosys.guide.chapter.propick.name",
                    "geolosys.guide.chapter.propick_3.text"));
        }
        chapters.get("prospecting").addPage(prospecting);

        ArrayList<Pages.BookPageContents> resources = new ArrayList<>();
        resources.add(new Pages.BookPageContents("geolosys.guide.chapter.resources.name"));
        int count = 0;
        int page_num = 0;

        if (ModConfig.REPLACE_STONES.get())
        {
            resources.get(page_num).addLink(new ChapterLink("geolosys.guide.chapter.stones.name", "stones"));
            chapters.put("stones", new Pages.BookChapter("stones", "resources"));
            chapters.get("stones").addPage(new Pages.BookPageItemDisplay("geolosys.guide.chapter.stones.name",
                    "geolosys.guide.chapter.stones.text", new ItemStack(Blocks.GRANITE, 1)));
            count++;
            if (count == 12)
            {
                resources.add(new Pages.BookPageContents("geolosys.guide.chapter.resources.name"));
                page_num += 1;
                count = 0;
            }
        }
        for (IDeposit ore : PlutonRegistry.getInstance().getOres())
        {
            resources.get(page_num).addLink(new ChapterLink(ore.getFriendlyName(), ore.getFriendlyName()));
            chapters.put(ore.getFriendlyName(), new Pages.BookChapter(ore.getFriendlyName(), "resources"));
            chapters.get(ore.getFriendlyName()).addPage(new Pages.BookPageOre(ore));
            count++;
            if (count == 12)
            {
                resources.add(new Pages.BookPageContents("geolosys.guide.chapter.resources.name"));
                page_num += 1;
                count = 0;
            }
        }

        for (Pages.BookPageContents contents : resources)
        {
            chapters.get("resources").addPage(contents);
        }

        Pages.BookPageContents modCompat = new Pages.BookPageContents("geolosys.guide.chapter.mod_compat.name");

        if (ModConfig.RETRO_REPLACE.get())
        {
            modCompat.addLink(new ChapterLink("geolosys.guide.chapter.ore_converter.name", "ore_converter"));
            chapters.put("ore_converter", new Pages.BookChapter("ore_converter", "mod_compat"));
            chapters.get("ore_converter").addPage(new Pages.BookPageText("geolosys.guide.chapter.ore_converter.name",
                    "geolosys.guide.chapter.ore_converter.text"));
        }
        if (ModList.get().isLoaded("journeymap"))
        {
            modCompat.addLink(new ChapterLink("geolosys.guide.chapter.journeymap.name", "journeymap"));
            chapters.put("journeymap", new Pages.BookChapter("journeymap", "mod_compat"));
            chapters.get("journeymap").addPage(new Pages.BookPageText("geolosys.guide.chapter.journeymap.name",
                    "geolosys.guide.chapter.journeymap.text"));
        }
        // if (ModList.get().isLoaded("immersiveengineering") && ModConfig.compat.enableIECompat)
        // {
        // modCompat.addLink(
        // new ChapterLink("geolosys.guide.chapter.immersive_engineering.name", "immersive_engineering"));
        // chapters.put("immersive_engineering", new BookChapter("immersive_engineering", "mod_compat"));
        // chapters.get("immersive_engineering")
        // .addPage(new BookPageItemDisplay("geolosys.guide.chapter.immersive_engineering.name",
        // "geolosys.guide.chapter.immersive_engineering.text",
        // OreDictionary.getOres("dustSulfur").get(0)));
        // }
        if (ModList.get().isLoaded("betterwithmods"))
        {
            modCompat.addLink(new ChapterLink("geolosys.guide.chapter.better_with_mods.name", "better_with_mods"));
            chapters.put("better_with_mods", new Pages.BookChapter("better_with_mods", "mod_compat"));
            chapters.get("better_with_mods")
                    .addPage(new Pages.BookPageItemDisplay("geolosys.guide.chapter.better_with_mods.name",
                            "geolosys.guide.chapter.better_with_mods.text", new ItemStack(Items.IRON_NUGGET)));
        }
        if (ModList.get().isLoaded("twilightforest"))
        {
            modCompat.addLink(new ChapterLink("geolosys.guide.chapter.twilight_forest.name", "twilight_forest"));
            chapters.put("twilight_forest", new Pages.BookChapter("twilight_forest", "mod_compat"));
            chapters.get("twilight_forest").addPage(new Pages.BookPageText(
                    "geolosys.guide.chapter.twilight_forest.name", "geolosys.guide.chapter.twilight_forest.text"));
        }
        if (ForgeRegistries.ITEMS.getValue(new ResourceLocation("appliedenergistics2", "material")) != null)
        {
            modCompat
                    .addLink(new ChapterLink("geolosys.guide.chapter.applied_energistics.name", "applied_energistics"));
            chapters.put("applied_energistics", new Pages.BookChapter("applied_energistics", "mod_compat"));
            chapters.get("applied_energistics").addPage(new Pages.BookPageItemDisplay(
                    "geolosys.guide.chapter.applied_energistics.name",
                    "geolosys.guide.chapter.applied_energistics.text",
                    new ItemStack(Objects.requireNonNull(
                            ForgeRegistries.ITEMS.getValue(new ResourceLocation("appliedenergistics2", "material"))),
                            1)));
        }
        if (ForgeRegistries.ITEMS.getValue(new ResourceLocation("extrautils2", "ingredients")) != null)
        {
            modCompat.addLink(new ChapterLink("geolosys.guide.chapter.extra_utils.name", "extra_utils"));
            chapters.put("extra_utils", new Pages.BookChapter("extra_utils", "mod_compat"));
            chapters.get("extra_utils").addPage(new Pages.BookPageItemDisplay("geolosys.guide.chapter.extra_utils.name",
                    "geolosys.guide.chapter.extra_utils.text", new ItemStack(Objects.requireNonNull(
                            ForgeRegistries.ITEMS.getValue(new ResourceLocation("extrautils2", "ingredients"))))));
        }
        if (ForgeRegistries.ITEMS.getValue(new ResourceLocation("actuallyadditions", "item_misc")) != null)
        {
            modCompat.addLink(new ChapterLink("geolosys.guide.chapter.actually_additions.name", "actually_additions"));
            chapters.put("actually_additions", new Pages.BookChapter("actually_additions", "mod_compat"));
            chapters.get("actually_additions").addPage(new Pages.BookPageItemDisplay(
                    "geolosys.guide.chapter.actually_additions.name", "geolosys.guide.chapter.actually_additions.text",
                    new ItemStack(Objects.requireNonNull(
                            ForgeRegistries.ITEMS.getValue(new ResourceLocation("actuallyadditions", "item_misc"))),
                            1)));
        }
        if (ForgeRegistries.ITEMS.getValue(new ResourceLocation("thermalfoundation", "material")) != null)
        {
            modCompat.addLink(new ChapterLink("geolosys.guide.chapter.cofh_mods.name", "cofh_mods"));
            chapters.put("cofh_mods", new Pages.BookChapter("cofh_mods", "mod_compat"));
            chapters.get("cofh_mods").addPage(new Pages.BookPageItemDisplay("geolosys.guide.chapter.cofh_mods.name",
                    "geolosys.guide.chapter.cofh_mods.text",
                    new ItemStack(Objects.requireNonNull(
                            ForgeRegistries.ITEMS.getValue(new ResourceLocation("thermalfoundation", "material"))),
                            1)));
        }
        if (ConfigCompat.ENABLE_OSMIUM.get())
        {
            modCompat.addLink(new ChapterLink("geolosys.guide.chapter.mekanism.name", "mekanism"));
            chapters.put("mekanism", new Pages.BookChapter("mekanism", "mod_compat"));
            chapters.get("mekanism")
                    .addPage(new Pages.BookPageItemDisplay("geolosys.guide.chapter.mekanism.name",
                            "geolosys.guide.chapter.mekanism.text",
                            new ItemStack(ItemInit.getInstance().getModItems().get("geolosys:osmium_cluster"), 1)));
        }
        if (ConfigCompat.ENABLE_YELLORIUM.get())
        {
            modCompat.addLink(new ChapterLink("geolosys.guide.chapter.extreme_reactors.name", "extreme_reactors"));
            chapters.put("extreme_reactors", new Pages.BookChapter("extreme_reactors", "mod_compat"));
            chapters.get("extreme_reactors")
                    .addPage(new Pages.BookPageItemDisplay("geolosys.guide.chapter.extreme_reactors.name",
                            "geolosys.guide.chapter.extreme_reactors.text",
                            new ItemStack(ItemInit.getInstance().getModItems().get("geolosys:yellorium_cluster"), 1)));
        }
        if (ForgeRegistries.ITEMS.getValue(new ResourceLocation("nuclearcraft", "gem")) != null)
        {
            modCompat.addLink(new ChapterLink("geolosys.guide.chapter.nuclearcraft.name", "nuclearcraft"));
            chapters.put("nuclearcraft", new Pages.BookChapter("nuclearcraft", "mod_compat"));
            chapters.get("nuclearcraft")
                    .addPage(new Pages.BookPageItemDisplay("geolosys.guide.chapter.nuclearcraft.name",
                            "geolosys.guide.chapter.nuclearcraft.text", new ItemStack(Objects.requireNonNull(
                                    ForgeRegistries.ITEMS.getValue(new ResourceLocation("nuclearcraft", "gem"))), 1)));
        }
        chapters.get("mod_compat").addPage(modCompat);

        ArrayList<Pages.BookPage> patrons = new ArrayList<>();
        ArrayList<String> patronNames = PatronUtil.getInstance().getPatrons();
        if (patronNames.size() == 0)
        {
            patrons.add(new Pages.BookPageURL("geolosys.guide.chapter.patrons.name",
                    "geolosys.guide.chapter.patrons.none.text", "https://patreon.com/oitsjustjose",
                    "geolosys.guide.chapter.patrons.link"));
        }
        else
        {
            patrons.add(new Pages.BookPageURL("geolosys.guide.chapter.patrons.name",
                    "geolosys.guide.chapter.patrons.desc.text", "https://patreon.com/oitsjustjose",
                    "geolosys.guide.chapter.patrons.link"));
            count = 0;
            page_num = 0;
            int total = 0;
            StringBuilder pageText = new StringBuilder();
            for (String patronName : patronNames)
            {
                pageText.append("\u2022 " + patronName);
                total = total + 1;

                if (count == 12 || total == patronNames.size())
                {
                    patrons.add(new Pages.BookPageText("geolosys.guide.chapter.patrons.name", pageText.toString()));
                    pageText = new StringBuilder();
                    count = 0;
                    page_num += 1;
                    continue;
                }

                pageText.append("<br>");
                count++;
            }
        }
        for (Pages.BookPage page : patrons)
        {
            chapters.get("patrons").addPage(page);
        }

        for (Pages.BookChapter chapter : chapters.values())
        {
            if (chapter.getPageCount() <= 0)
            {
                chapter.addPage(new Pages.BookPage(chapter.getName()));
            }
        }
    }

    @Override
    protected void init()
    {
        this.top = (this.height - HEIGHT) / 2;
        this.left = (this.width - WIDTH) / 2;
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks)
    {
        this.renderBackground();
        this.setFocused(null);

        currentPage = chapters.get(currentChapter).getPage(currentPageNum);
        if (currentPageNum != lastPageNum || !currentChapter.equals(lastChapter))
        {
            resetPage();
        }

        this.lastChapter = this.currentChapter;
        this.lastPageNum = this.currentPageNum;

        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        Minecraft.getInstance().textureManager.bindTexture(BACKGROUND);
        this.blit(left, top, 0, 0, WIDTH, HEIGHT);

        if (currentPage != null)
        {
            String header = TextFormatting.BOLD + "" + TextFormatting.UNDERLINE + I18n.format(currentPage.getTitle());
            double textScale = ConfigClient.MANUAL_FONT_SCALE.get();
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

            if (currentPage instanceof Pages.BookPageItemDisplay)
            {
                renderItemDisplayPage((Pages.BookPageItemDisplay) currentPage, mouseX, mouseY);
            }
            else if (currentPage instanceof Pages.BookPageText)
            {
                renderTextPage((Pages.BookPageText) currentPage);
            }
            else if (currentPage instanceof Pages.BookPageOre)
            {
                renderOrePage((Pages.BookPageOre) currentPage, mouseX, mouseY);
            }
            else if (currentPage instanceof Pages.BookPageURL)
            {
                renderURLPage((Pages.BookPageURL) currentPage, mouseX, mouseY, partialTicks);
            }

            if (chapters.get(currentChapter).getPageCount() > 1)
            {
                GlStateManager.pushMatrix();
                GlStateManager.scaled(textScale, textScale, textScale);
                String pageNum = (currentPageNum + 1) + "/" + chapters.get(currentChapter).getPageCount();
                int pageNumWidth = (int) (this.fontRenderer.getStringWidth(pageNum) * textScale);
                int x = (int) ((left + (WIDTH - pageNumWidth) / 2) / textScale);
                this.fontRenderer.drawSplitString(pageNum, x, (int) ((top + 164) / textScale),
                        (int) ((WIDTH - (18 * 2)) / textScale), 0);
                GlStateManager.popMatrix();
            }
        }

        super.render(mouseX, mouseY, partialTicks);

    }

    private void renderItemDisplayPage(Pages.BookPageItemDisplay page, int mouseX, int mouseY)
    {
        ItemStack stack = page.getDisplayStack();

        if (stack.getItem() != display.getItem())
        {
            display = stack;
        }

        GlStateManager.pushMatrix();
        GlStateManager.scalef(2F, 2F, 2F);
        RenderHelper.enableGUIStandardItemLighting();
        int itemX = (left + (WIDTH - 32) / 2);
        int itemY = (top + 24);
        float itemScale = 2F;
        this.itemRenderer.renderItemAndEffectIntoGUI(display, (int) (itemX / itemScale), (int) (itemY / itemScale));
        RenderHelper.disableStandardItemLighting();
        GlStateManager.popMatrix();

        GlStateManager.pushMatrix();
        float textScale = ConfigClient.MANUAL_FONT_SCALE.get().floatValue();
        GlStateManager.scalef(textScale, textScale, textScale);
        this.fontRenderer.drawSplitString(I18n.format(page.getDescription()),

                (int) ((left + 18) / textScale), (int) ((top + 58) / textScale), (int) ((WIDTH - (18 * 2)) / textScale),
                0);
        GlStateManager.popMatrix();

        renderTooltip(mouseX, mouseY, itemX, itemY, itemScale);

    }

    private static class GuiButtonURL extends Button
    {

        GuiButtonURL(int x, int y, int widthIn, int heightIn, String buttonText, String url)
        {
            super(x, y, widthIn, heightIn, buttonText, new IPressable()
            {
                @Override
                public void onPress(Button button)
                {
                    Util.getOSType().openURI(url);
                }
            });
        }
    }

    private void renderURLPage(Pages.BookPageURL page, int mouseX, int mouseY, float partialTicks)
    {
        GlStateManager.pushMatrix();
        float textScale = ConfigClient.MANUAL_FONT_SCALE.get().floatValue();
        GlStateManager.scalef(textScale, textScale, textScale);
        String text = I18n.format(page.getText());
        List<String> paragraphs = new ArrayList<>();

        int numOccurences = 0;
        while (text.contains("|"))
        {
            int i = text.indexOf("|");

            paragraphs.add("    " + text.substring(0, i));
            if (i < text.length() - 1)
            {
                text = text.substring(i + 1);
            }
            numOccurences++;
        }

        paragraphs.add((numOccurences > 0 ? "    " : "") + text);

        int i = 24;
        for (String par : paragraphs)
        {
            this.fontRenderer.drawSplitString(par, (int) ((left + 18) / textScale), (int) ((top + i) / textScale),
                    (int) ((WIDTH - (18 * 2)) / textScale), 0);
            i += (int) (2 + fontRenderer.getWordWrappedHeight(par, (int) ((WIDTH - (18 * 2)) / textScale)) * textScale);
        }

        GlStateManager.popMatrix();
    }

    private void renderOrePage(Pages.BookPageOre page, int mouseX, int mouseY)
    {
        ItemStack stack = page.getDisplayStack();

        if (stack.getItem() != display.getItem())
        {
            display = stack;
        }

        GlStateManager.pushMatrix();
        GlStateManager.scalef(2F, 2F, 2F);
        RenderHelper.enableGUIStandardItemLighting();
        int itemX = (left + (WIDTH - 32) / 2);
        int itemY = (top + 24);
        float itemScale = 2F;
        this.itemRenderer.renderItemAndEffectIntoGUI(display, (int) (itemX / itemScale), (int) (itemY / itemScale));
        RenderHelper.disableStandardItemLighting();
        GlStateManager.popMatrix();

        GlStateManager.pushMatrix();
        float textScale = ConfigClient.MANUAL_FONT_SCALE.get().floatValue();
        GlStateManager.scalef(textScale, textScale, textScale);
        String minDepthFromSeaLevel = getFormattedSeaLevel(
                Minecraft.getInstance().world.getSeaLevel() - page.getMinY());
        String maxDepthFromSeaLevel = getFormattedSeaLevel(
                Minecraft.getInstance().world.getSeaLevel() - page.getMaxY());

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
            GlStateManager.scalef(toolTipScale, toolTipScale, toolTipScale);
            this.renderTooltip(display, (int) (mouseX / toolTipScale), (int) (mouseY / toolTipScale));
            RenderHelper.disableStandardItemLighting();
            GlStateManager.popMatrix();
        }
    }

    private void renderTextPage(Pages.BookPageText page)
    {
        GlStateManager.pushMatrix();
        float textScale = ConfigClient.MANUAL_FONT_SCALE.get().floatValue();
        GlStateManager.scalef(textScale, textScale, textScale);
        String text = I18n.format(page.getText());
        List<String> paragraphs = new ArrayList<>();

        while (text.contains("|") || text.contains("<br>"))
        {
            int i = text.indexOf("|");
            int j = text.indexOf("<br>");

            if (i == -1)
            {
                paragraphs.add(text.substring(0, j));
                if (j < text.length() - 1)
                {
                    text = text.substring(j + 4);
                }

            }
            else if (j == -1)
            {
                paragraphs.add("    " + text.substring(0, i));
                if (i < text.length() - 1)
                {
                    text = text.substring(i + 1);
                }
            }
            else
            {
                if (i < j)
                {
                    paragraphs.add("    " + text.substring(0, i));
                    if (i < text.length() - 1)
                    {
                        text = text.substring(i + 1);
                    }
                }
                else
                {
                    paragraphs.add(text.substring(0, j));
                    if (j < text.length() - 1)
                    {
                        text = text.substring(j + 4);
                    }
                }
            }
        }

        paragraphs.add(text);
        int i = 24;
        for (String par : paragraphs)
        {
            this.fontRenderer.drawSplitString(par, (int) ((left + 18) / textScale), (int) ((top + i) / textScale),
                    (int) ((WIDTH - (18 * 2)) / textScale), 0);
            i += (int) (2 + fontRenderer.getWordWrappedHeight(par, (int) ((WIDTH - (18 * 2)) / textScale)) * textScale);
        }
        GlStateManager.popMatrix();
    }

    private void resetPage()
    {
        this.buttons.clear();
        int i = 0;
        if (currentPage instanceof Pages.BookPageContents)
        {
            List<ChapterLink> links = ((Pages.BookPageContents) currentPage).getLinks();
            for (ChapterLink link : links)
            {
                this.addButton(new ChapterLinkButton(i, left + 16, top + 24 + (i * 12), link.text, link.chapter));
                i++;
            }
        }
        else if (currentPage instanceof Pages.BookPageURL)
        {
            GuiButtonURL urlButton = new GuiButtonURL(left, (top + HEIGHT), WIDTH, 20,
                    I18n.format(((Pages.BookPageURL) (currentPage)).getButtonText()),
                    ((Pages.BookPageURL) (currentPage)).getURL());
            this.addButton(urlButton);
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
    public boolean keyPressed(int p_keyPressed_1_, int p_keyPressed_2_, int p_keyPressed_3_)
    {
        if (Minecraft.getInstance().gameSettings.keyBindInventory.isPressed())
        {
            if (currentChapter.equals(chapters.get(currentChapter).getParent()))
            {
                Minecraft.getInstance().displayGuiScreen(null);
                Minecraft.getInstance().setGameFocused(true);
            }
            else
            {
                currentChapter = chapters.get(currentChapter).getParent();
                currentPageNum = 0;
            }
        }
        else if (1 == p_keyPressed_2_)
        {
            Minecraft.getInstance().displayGuiScreen(null);
            Minecraft.getInstance().setGameFocused(true);
        }
        return super.keyPressed(p_keyPressed_1_, p_keyPressed_2_, p_keyPressed_3_);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton)
    {
        if (mouseButton == 1)
        {
            currentChapter = chapters.get(currentChapter).getParent();
            currentPageNum = 0;
        }
        return super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public boolean isPauseScreen()
    {
        return false;
    }

    public void resize(Minecraft mc, int w, int h)
    {
        super.resize(mc, w, h);
        this.resetPage();
    }

    @OnlyIn(Dist.CLIENT)
    public class ChapterLinkButton extends Button
    {

        private String chapter;

        public ChapterLinkButton(int buttonId, int x, int y, String buttonText, String chapter)
        {
            super(x, y, Minecraft.getInstance().fontRenderer.getStringWidth(I18n.format(buttonText)),
                    Minecraft.getInstance().fontRenderer.FONT_HEIGHT, buttonText, new IPressable()
                    {
                        @Override
                        public void onPress(Button button)
                        {
                            currentChapter = chapter;
                            currentPageNum = 0;
                        }
                    });
            this.chapter = chapter;
        }

        @Override
        public void render(int mouseX, int mouseY, float partialTicks)
        {
            if (this.visible)
            {
                FontRenderer fontrenderer = Minecraft.getInstance().fontRenderer;
                GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
                this.isHovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width
                        && mouseY < this.y + this.height;

                // this.mouseDragged(mouseX, mouseY);
                int j = 0;
                String p = "";

                if (!this.active)
                {
                    j = 10526880;
                }
                else if (this.isHovered)
                {
                    j = 8308926;
                    p += TextFormatting.UNDERLINE;
                }
                String toDraw = I18n.format(this.toString());
                if (fontrenderer.getStringWidth(
                        p + "\u2022 " + toDraw) > (int) ((WIDTH - (18 * 2)) / ConfigClient.MANUAL_FONT_SCALE.get()))
                {
                    toDraw = fontRenderer.trimStringToWidth(toDraw,
                            (int) ((WIDTH - (18 * 2)) / ConfigClient.MANUAL_FONT_SCALE.get()));
                    toDraw = toDraw.substring(0, toDraw.length() - 7);
                    toDraw += "...";
                }

                fontrenderer.drawSplitString(p + "\u2022 " + toDraw, this.x, this.y,
                        (int) ((WIDTH - (18 * 2)) / ConfigClient.MANUAL_FONT_SCALE.get()), j);
            }
        }

        String getChapter()
        {
            return this.chapter;
        }

    }

    @OnlyIn(Dist.CLIENT)
    public class PageTurnButton extends Button
    {
        private final boolean isForward;

        PageTurnButton(int buttonId, int x, int y, boolean isForward)
        {
            super(x, y, 23, 13, "", new IPressable()
            {
                @Override
                public void onPress(Button p_onPress_1_)
                {
                    if (isForward)
                    {
                        currentPageNum++;
                    }
                    else
                    {
                        currentPageNum--;
                    }
                }
            });
            this.isForward = isForward;
        }

        @Override
        public void render(int mouseX, int mouseY, float partialTicks)
        {
            if (this.visible)
            {
                boolean flag = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width
                        && mouseY < this.y + this.height;
                GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
                Minecraft.getInstance().getTextureManager().bindTexture(GUIManual.BACKGROUND);
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

                this.blit(this.x, this.y, i, j, 23, 13);
            }
        }

        boolean isForward()
        {
            return this.isForward;
        }
    }
}
