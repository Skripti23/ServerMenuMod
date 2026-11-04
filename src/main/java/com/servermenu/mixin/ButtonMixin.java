package com.servermenu.mixin;

import com.servermenu.gui.RoundedRect;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractButton.class)
public abstract class ButtonMixin {

    @Inject(method = "renderWidget", at = @At("HEAD"), cancellable = true)
    private void servermenu$renderCustomButton(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
        AbstractButton self = (AbstractButton) (Object) this;

        int x = self.getX();
        int y = self.getY();
        int w = self.getWidth();
        int h = self.getHeight();
        boolean hovered = self.isHoveredOrFocused();
        boolean active = self.active;

        int radius = Math.min(6, Math.min(h, w) / 2);

        int top, bottom;
        if (!active) {
            top = 0x99333333;
            bottom = 0x99202020;
        } else if (hovered) {
            top = 0xFF5CCFFF;
            bottom = 0xFFFF9CD4;
        } else {
            top = 0xFF2E8BFF;
            bottom = 0xFF7B3BFF;
        }

        RoundedRect.fill(guiGraphics, x, y, w, h, radius, top, bottom);

        if (active) {
            int border = hovered ? 0xFFFFE9A8 : 0xFFFFD700;
            RoundedRect.fill(guiGraphics, x, y, w, 1, 1, border, border);
            RoundedRect.fill(guiGraphics, x, y + h - 1, w, 1, 1, border, border);
            RoundedRect.fill(guiGraphics, x, y, 1, h, 1, border, border);
            RoundedRect.fill(guiGraphics, x + w - 1, y, 1, h, 1, border, border);
        }

        Component msg = self.getMessage();
        Minecraft mc = Minecraft.getInstance();
        int textColor = !active ? 0xFF9E9E9E : (hovered ? 0xFFFFFFFF : 0xFFFFF6D6);
        int textWidth = mc.font.width(msg);
        int textX = x + (w - textWidth) / 2;
        int textY = y + (h - 8) / 2;
        guiGraphics.drawString(mc.font, msg, textX, textY, textColor, false);

        ci.cancel();
    }
}