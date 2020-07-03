package com.oitsjustjose.geolosys.client.errors;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiErrorScreen;
import net.minecraftforge.fml.client.CustomModLoadingErrorDisplayException;

public class DownloadErrorDisplayException extends CustomModLoadingErrorDisplayException {

    private static final long serialVersionUID = -3330597739456228630L;
    final String title, message;

    public DownloadErrorDisplayException(String title, String message) {
        this.title = title;
        this.message = message;
    }

    @Override
    public void initGui(GuiErrorScreen errorScreen, FontRenderer fontRenderer) {

    }

    @Override
    public void drawScreen(GuiErrorScreen gui, FontRenderer fontRenderer, int mouseRelX, int mouseRelY,
            float tickTime) {
        gui.drawCenteredString(fontRenderer, this.title, gui.width / 2, 90, 16777215);
        int y = 110;
        for (String s : fontRenderer.listFormattedStringToWidth(message, (gui.width * 9) / 10)) {
            gui.drawCenteredString(fontRenderer, s, gui.width / 2, y, 16777215);
            y += fontRenderer.FONT_HEIGHT + 1;
        }
    }
}