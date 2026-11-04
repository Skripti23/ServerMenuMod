package com.servermenu.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.OptionsScreen;
import net.minecraft.client.gui.screens.worldselection.SelectWorldScreen;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.gui.screens.ConnectScreen;
import net.minecraft.client.multiplayer.resolver.ServerAddress;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ServerMenuScreen extends Screen {

    private static final String SERVER_NAME = "Survivalndustry";
    private static final String SERVER_IP = "d24.aurorix.net:25019";

    private final Minecraft mc = Minecraft.getInstance();
    private final Random random = new Random();
    private final List<Snow> snowflakes = new ArrayList<>();
    private long lastUpdate = 0;

    private static class Snow {
        float x, y, size, speed, drift;
        int alpha;
    }

    public ServerMenuScreen() {
        super(Component.literal("Server Menu"));
    }

    @Override
    protected void init() {
        super.init();

        int bx = 12;
        int by = 56;
        int bw = 170;
        int bh = 20;
        int step = 26;

        int titleY = 8;
        net.minecraft.client.gui.Font font = this.font;
        net.minecraft.client.gui.components.StringWidget title = new net.minecraft.client.gui.components.StringWidget(
                Component.literal("Запуск на SurvivaIndustry"), font) {
            @Override
            public void renderWidget(GuiGraphics g, int mX, int mY, float pT) {
                g.drawString(font, this.getMessage(), this.getX(), this.getY(), 0xFFFFFFFF, true);
            }
        };
        title.setPosition(bx, titleY);
        title.setSize(this.width - bx, 14);
        addRenderableWidget(title);

        net.minecraft.client.gui.components.StringWidget welcome = new net.minecraft.client.gui.components.StringWidget(
                Component.literal("Добро пожаловать!"), font) {
            @Override
            public void renderWidget(GuiGraphics g, int mX, int mY, float pT) {
                g.drawString(font, this.getMessage(), this.getX(), this.getY(), 0xFFFFF6D6, true);
            }
        };
        welcome.setPosition(bx, titleY + 14);
        welcome.setSize(this.width - bx, 12);
        addRenderableWidget(welcome);

        addRenderableWidget(new MenuButton(
                bx, by, bw, bh,
                Component.literal("Зайти на сервер"),
                b -> connectToServer()));

        addRenderableWidget(new MenuButton(
                bx, by + step, bw, bh,
                Component.literal("Одиночная игра"),
                b -> mc.setScreen(new SelectWorldScreen(this))));

        addRenderableWidget(new MenuButton(
                bx, by + step * 2, bw, bh,
                Component.literal("Настройки"),
                b -> mc.setScreen(new OptionsScreen(this, mc.options))));

        addRenderableWidget(new MenuButton(
                bx, by + step * 3, bw, bh,
                Component.literal("Выйти"),
                b -> mc.stop()));

        initSnow();
    }

    private void initSnow() {
        snowflakes.clear();
        int count = Math.max(1, (this.width * this.height) / 9000);
        for (int i = 0; i < count; i++) {
            Snow s = new Snow();
            s.x = random.nextInt(this.width);
            s.y = random.nextInt(this.height);
            s.size = 0.8f + random.nextFloat() * 1.6f;
            s.speed = 0.25f + random.nextFloat() * 0.5f;
            s.drift = 0.3f + random.nextFloat() * 0.9f;
            s.alpha = 120 + random.nextInt(110);
            snowflakes.add(s);
        }
    }

    private void tickSnow(float delta) {
        for (Snow s : snowflakes) {
            s.y += s.speed * 60 * delta;
            s.x += (float) Math.sin((s.y * 0.03f) + s.drift) * 0.6f;
            if (s.y > this.height + 2) {
                s.y = -2;
                s.x = random.nextInt(this.width);
            }
            if (s.x < -2) s.x = this.width + 2;
            if (s.x > this.width + 2) s.x = -2;
        }
    }

    private void drawSnow(GuiGraphics g) {
        for (Snow s : snowflakes) {
            int size = Math.max(1, Math.round(s.size));
            g.fill(Math.round(s.x), Math.round(s.y),
                    Math.round(s.x) + size, Math.round(s.y) + size, 0xFFFFFF | (s.alpha << 24));
        }
    }

    private void connectToServer() {
        try {
            ServerData data = new ServerData(SERVER_NAME, SERVER_IP, ServerData.Type.OTHER);
            ConnectScreen.startConnecting(
                    this,
                    this.mc,
                    ServerAddress.parseString(SERVER_IP),
                    data,
                    false,
                    null
            );
        } catch (Throwable t) {
            mc.setScreen(this);
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        long now = System.currentTimeMillis();
        float delta = (now - lastUpdate) / 1000.0f;
        lastUpdate = now;
        if (delta > 0.1f || delta <= 0) delta = 0.016f;
        tickSnow(delta);

        BackgroundRenderer.renderFull(guiGraphics, this.width, this.height);
        drawSnow(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    @Override
    public void resize(Minecraft mc, int w, int h) {
        super.resize(mc, w, h);
        initSnow();
    }
}