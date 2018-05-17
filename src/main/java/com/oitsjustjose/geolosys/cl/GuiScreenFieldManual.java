package com.oitsjustjose.geolosys.cl;

import com.google.common.collect.Lists;
import com.google.gson.JsonParseException;
import com.oitsjustjose.geolosys.Geolosys;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiUtilRenderComponents;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemWrittenBook;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiScreenFieldManual extends GuiScreen
{
    private static final ResourceLocation TEXTURES = new ResourceLocation(Geolosys.MODID, "textures/gui/field_manual.png");
    private final EntityPlayer editingPlayer;
    private final ItemStack book;
    private boolean bookGettingSigned;
    private int bookTotalPages = 1;
    private int currPage;
    private NBTTagList bookPages;
    private List<ITextComponent> cachedComponents;
    private int cachedPage = -1;
    private NextPageButton buttonNextPage;
    private NextPageButton buttonPreviousPage;
    private GuiButton buttonDone;
    private FontRenderer font;

    public GuiScreenFieldManual(EntityPlayer player, ItemStack book)
    {
        this.editingPlayer = player;
        this.book = book;
        this.font = new FontRenderer(Minecraft.getMinecraft().gameSettings, new ResourceLocation("textures/font/ascii.png"), Minecraft.getMinecraft().renderEngine, true);
        this.font.setUnicodeFlag(true);
        if (book.hasTagCompound())
        {
            NBTTagCompound nbttagcompound = book.getTagCompound();
            assert nbttagcompound != null;
            this.bookPages = nbttagcompound.getTagList("pages", 8).copy();
            this.bookTotalPages = this.bookPages.tagCount();

            if (this.bookTotalPages < 1)
            {
                this.bookPages.appendTag(new NBTTagString("")); // Forge: fix MC-1685
                this.bookTotalPages = 1;
            }
        }
    }

    @Override
    public void updateScreen()
    {
        super.updateScreen();
    }

    @Override
    public void initGui()
    {
        this.buttonList.clear();
        Keyboard.enableRepeatEvents(true);

        this.buttonDone = this.addButton(new GuiButton(0, this.width / 2 - 100, 196, 200, 20, I18n.format("gui.done")));

        int i = (this.width - 192) / 2;
        this.buttonNextPage = this.addButton(new NextPageButton(1, i + 120, 156, true));
        this.buttonPreviousPage = this.addButton(new NextPageButton(2, i + 38, 156, false));
        this.updateButtons();
    }


    private void updateButtons()
    {
        this.buttonNextPage.visible = !this.bookGettingSigned && (this.currPage < this.bookTotalPages - 1);
        this.buttonPreviousPage.visible = !this.bookGettingSigned && this.currPage > 0;
        this.buttonDone.visible = !this.bookGettingSigned;
    }


    /**
     * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
     */
    protected void actionPerformed(GuiButton button)
    {
        if (button.enabled)
        {
            if (button.id == 0)
            {
                this.mc.displayGuiScreen(null);
            }
            else
            {
                if (button.id == 1)
                {
                    if (this.currPage < this.bookTotalPages - 1)
                    {
                        ++this.currPage;
                    }
                }
                else if (button.id == 2)
                {
                    if (this.currPage > 0)
                    {
                        --this.currPage;
                    }
                }
                else if (button.id == 5 && this.bookGettingSigned)
                {
                    this.mc.displayGuiScreen(null);
                }
                else if (button.id == 4 && this.bookGettingSigned)
                {
                    this.bookGettingSigned = false;
                }
            }

            this.updateButtons();
        }
    }

    /**
     * Fired when a key is typed (except F11 which toggles full screen). This is the equivalent of
     * KeyListener.keyTyped(KeyEvent e). Args : character (character on the key), keyCode (lwjgl Keyboard key code)
     */
    protected void keyTyped(char typedChar, int keyCode) throws IOException
    {
        super.keyTyped(typedChar, keyCode);
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(TEXTURES);
        int i = (this.width - 192) / 2;
        this.drawTexturedModalRect(i, 2, 0, 0, 192, 192);
        if (this.bookGettingSigned)
        {
            String s = "";
            String s1 = I18n.format("book.editTitle");
            int k = font.getStringWidth(s1);
            font.drawString(s1, i + 36 + (116 - k) / 2, 34, 0);
            int l = font.getStringWidth(s);
            font.drawString(s, i + 36 + (116 - l) / 2, 50, 0);
            String s2 = I18n.format("book.byAuthor", this.editingPlayer.getName());
            int i1 = font.getStringWidth(s2);
            font.drawString(TextFormatting.DARK_GRAY + s2, i + 36 + (116 - i1) / 2, 60, 0);
            String s3 = I18n.format("book.finalizeWarning");
            font.drawSplitString(s3, i + 36, 82, 116, 0);
        }
        else
        {
            String s4 = I18n.format("book.pageIndicator", this.currPage + 1, this.bookTotalPages);
            String s5 = "";

            if (this.bookPages != null && this.currPage >= 0 && this.currPage < this.bookPages.tagCount())
            {
                s5 = this.bookPages.getStringTagAt(this.currPage);
            }

            if (this.cachedPage != this.currPage)
            {
                assert this.book.getTagCompound() != null;
                if (ItemWrittenBook.validBookTagContents(this.book.getTagCompound()))
                {
                    try
                    {
                        ITextComponent itextcomponent = ITextComponent.Serializer.jsonToComponent(s5);
                        this.cachedComponents = itextcomponent != null ? GuiUtilRenderComponents.splitText(itextcomponent, 116, font, true, true) : null;
                    }
                    catch (JsonParseException var13)
                    {
                        this.cachedComponents = null;
                    }
                }
                else
                {
                    TextComponentString textcomponentstring = new TextComponentString(TextFormatting.DARK_RED + "* Invalid book tag *");
                    this.cachedComponents = Lists.newArrayList(textcomponentstring);
                }

                this.cachedPage = this.currPage;
            }

            int j1 = font.getStringWidth(s4);
            font.drawString(s4, i - j1 + 192 - 44, 18, 0);

            if (this.cachedComponents == null)
            {
                font.drawSplitString(s5, i + 36, 34, 116, 0);
            }
            else
            {
                int k1 = Math.min(128 / font.FONT_HEIGHT, this.cachedComponents.size());

                for (int l1 = 0; l1 < k1; ++l1)
                {
                    ITextComponent itextcomponent2 = this.cachedComponents.get(l1);
                    font.drawString(itextcomponent2.getUnformattedText(), i + 36, 34 + l1 * font.FONT_HEIGHT, 0);
                }

                ITextComponent itextcomponent1 = this.getClickedComponentAt(mouseX, mouseY);

                if (itextcomponent1 != null)
                {
                    this.handleComponentHover(itextcomponent1, mouseX, mouseY);
                }
            }
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    /**
     * Called when the mouse is clicked. Args : mouseX, mouseY, clickedButton
     */
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
    {
        if (mouseButton == 0)
        {
            ITextComponent itextcomponent = this.getClickedComponentAt(mouseX, mouseY);

            if (itextcomponent != null && this.handleComponentClick(itextcomponent))
            {
                return;
            }
        }

        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    /**
     * Executes the click event specified by the given chat component
     */
    public boolean handleComponentClick(ITextComponent component)
    {
        ClickEvent clickevent = component.getStyle().getClickEvent();

        if (clickevent == null)
        {
            return false;
        }
        else if (clickevent.getAction() == ClickEvent.Action.CHANGE_PAGE)
        {
            String s = clickevent.getValue();

            try
            {
                int i = Integer.parseInt(s) - 1;

                if (i >= 0 && i < this.bookTotalPages && i != this.currPage)
                {
                    this.currPage = i;
                    this.updateButtons();
                    return true;
                }
            }
            catch (Throwable ignored)
            {
            }

            return false;
        }
        else
        {
            boolean flag = super.handleComponentClick(component);

            if (flag && clickevent.getAction() == ClickEvent.Action.RUN_COMMAND)
            {
                this.mc.displayGuiScreen(null);
            }

            return flag;
        }
    }

    @Nullable
    public ITextComponent getClickedComponentAt(int p_175385_1_, int p_175385_2_)
    {
        if (this.cachedComponents == null)
        {
            return null;
        }
        else
        {
            int i = p_175385_1_ - (this.width - 192) / 2 - 36;
            int j = p_175385_2_ - 2 - 16 - 16;

            if (i >= 0 && j >= 0)
            {
                int k = Math.min(128 / font.FONT_HEIGHT, this.cachedComponents.size());

                if (i <= 116 && j < this.mc.fontRenderer.FONT_HEIGHT * k + k)
                {
                    int l = j / this.mc.fontRenderer.FONT_HEIGHT;

                    if (l >= 0 && l < this.cachedComponents.size())
                    {
                        ITextComponent itextcomponent = this.cachedComponents.get(l);
                        int i1 = 0;

                        for (ITextComponent itextcomponent1 : itextcomponent)
                        {
                            if (itextcomponent1 instanceof TextComponentString)
                            {
                                i1 += this.mc.fontRenderer.getStringWidth(((TextComponentString) itextcomponent1).getText());

                                if (i1 > i)
                                {
                                    return itextcomponent1;
                                }
                            }
                        }
                    }

                    return null;
                }
                else
                {
                    return null;
                }
            }
            else
            {
                return null;
            }
        }
    }

    @SideOnly(Side.CLIENT)
    static class NextPageButton extends GuiButton
    {
        private final boolean isForward;

        NextPageButton(int buttonId, int x, int y, boolean isForwardIn)
        {
            super(buttonId, x, y, 23, 13, "");
            this.isForward = isForwardIn;
        }

        /**
         * Draws this button to the screen.
         */
        public void drawButton(@Nonnull Minecraft mc, int mouseX, int mouseY, float partialTicks)
        {
            if (this.visible)
            {
                boolean flag = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                mc.getTextureManager().bindTexture(TEXTURES);
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
    }


}
