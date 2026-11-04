package com.servermenu.gui;

import net.minecraft.client.gui.GuiGraphics;

public final class RoundedRect {

    private RoundedRect() {
    }

    public static void fill(GuiGraphics g, int x, int y, int w, int h, int radius, int colorTop, int colorBottom) {
        if (w <= 0 || h <= 0) {
            return;
        }
        int r = Math.max(0, Math.min(radius, Math.min(w, h) / 2));
        float step = h <= 1 ? 0f : 1f / (h - 1);
        for (int ly = 0; ly < h; ly++) {
            int inset = cornerInset(ly, h, r);
            if (inset >= w) {
                continue;
            }
            int color = lerpColor(colorTop, colorBottom, ly * step);
            g.fill(x + inset, y + ly, x + w - inset, y + ly + 1, color);
        }
    }

    private static int cornerInset(int ly, int h, int r) {
        if (r <= 0) {
            return 0;
        }
        int nearest = Math.min(ly, h - 1 - ly);
        if (nearest >= r) {
            return 0;
        }
        double d = (double) (r - 1 - nearest);
        double raw = r - Math.sqrt(Math.max(0, (double) r * r - d * d));
        return (int) Math.ceil(raw);
    }

    private static int lerpColor(int a, int b, float t) {
        if (t <= 0f) {
            return a;
        }
        if (t >= 1f) {
            return b;
        }
        int ar = (a >> 16) & 0xFF;
        int ag = (a >> 8) & 0xFF;
        int ab = a & 0xFF;
        int aa = (a >> 24) & 0xFF;
        int br = (b >> 16) & 0xFF;
        int bg = (b >> 8) & 0xFF;
        int bb = b & 0xFF;
        int ba = (b >> 24) & 0xFF;
        int r = (int) (ar + (br - ar) * t);
        int gg = (int) (ag + (bg - ag) * t);
        int bl = (int) (ab + (bb - ab) * t);
        int al = (int) (aa + (ba - aa) * t);
        return (al << 24) | (r << 16) | (gg << 8) | bl;
    }
}