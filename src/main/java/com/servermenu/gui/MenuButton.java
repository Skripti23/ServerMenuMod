package com.servermenu.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.Random;

public class MenuButton extends Button {

    private static final ResourceLocation SNOWFLAKE_ICON =
            ResourceLocation.fromNamespaceAndPath("servermenu", "textures/gui/snowflake.png");

    private static final int[] SPARKLE_COLORS = {
            0xFFE8F6FF, 0xFFD6ECFF, 0xFFDAF4FF, 0xFFE9FBFF, 0xFFCFE9FF
    };

    private final Random random = new Random();
    private long lastSpawn = 0;

    public MenuButton(int x, int y, int width, int height, Component message, OnPress onPress) {
        super(x, y, width, height, message, onPress, b -> b.get());
    }

    public static MenuButton create(int x, int y, int width, int height, Component message, OnPress onPress) {
        return new MenuButton(x, y, width, height, message, onPress);
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        boolean hovered = this.isHovered();
        int x = this.getX();
        int y = this.getY();
        int w = this.getWidth();
        int h = this.getHeight();

        int top = hovered ? 0xFF5CCFFF : 0xFF2E8BFF;
        int bot = hovered ? 0xFFFF9CD4 : 0xFF7B3BFF;
        guiGraphics.fillGradient(x, y, x + w, y + h, top, bot);

        int border = hovered ? 0xFFFFE9A8 : 0xFFFFD700;
        guiGraphics.hLine(x, x + w - 1, y, border);
        guiGraphics.hLine(x, x + w - 1, y + h - 1, border);
        guiGraphics.vLine(x, y, y + h - 1, border);
        guiGraphics.vLine(x + w - 1, y, y + h - 1, border);

        int iconX = x + 6;
        int iconY = y + (h - 10) / 2;
        guiGraphics.blit(SNOWFLAKE_ICON, iconX, iconY, 0, 0, 10, 10, 10, 10);

        if (hovered) {
            animateSparkles(guiGraphics, x, y, w, h);
        }

        int textX = iconX + 14;
        int textCenterX = x + (w - Minecraft.getInstance().font.width(this.getMessage())) / 2;
        if (this.getMessage().getString().contains("Зайти")) {
            textCenterX = textX;
        }
        int textY = y + (h - 8) / 2;
        guiGraphics.drawString(Minecraft.getInstance().font, this.getMessage(), textCenterX, textY,
                hovered ? 0xFFFFFFFF : 0xFFFFF6D6, false);
    }

    private void animateSparkles(GuiGraphics guiGraphics, int x, int y, int w, int h) {
        long now = System.currentTimeMillis();
        if (now - lastSpawn > 120) {
            lastSpawn = now;
            int size = 2 + random.nextInt(3);
            int sx = x + random.nextInt(w);
            int sy = y + random.nextInt(h);
            for (int ox = 0; ox < size; ox++) {
                for (int oy = 0; oy < size; oy++) {
                    int color = SPARKLE_COLORS[random.nextInt(SPARKLE_COLORS.length)];
                    guiGraphics.fill(sx + ox, sy + oy, sx + ox + 1, sy + oy + 1, color);
                }
            }
        }
    }
}