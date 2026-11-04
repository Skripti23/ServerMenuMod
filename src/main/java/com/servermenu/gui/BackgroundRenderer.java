package com.servermenu.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public final class BackgroundRenderer {

    public static final ResourceLocation BACKGROUND =
            ResourceLocation.fromNamespaceAndPath("servermenu", "textures/gui/title/background/menu.png");

    public static final int IMG_W = 1536;
    public static final int IMG_H = 864;

    private BackgroundRenderer() {
    }

    public static void renderFull(GuiGraphics g, int screenW, int screenH) {
        g.fill(0, 0, screenW, screenH, 0xFF000000);
        g.blit(BACKGROUND, 0, 0, screenW, screenH, 0, 0, IMG_W, IMG_H, IMG_W, IMG_H);
    }
}