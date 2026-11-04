package com.servermenu;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import com.servermenu.gui.ServerMenuScreen;
import com.servermenu.gui.BackgroundRenderer;

@Mod(ServerMenuMod.MODID)
public class ServerMenuMod {
    public static final String MODID = "servermenu";

    private static final String OPTIONS_PACKAGE = "net.minecraft.client.gui.screens.options";

    public ServerMenuMod(IEventBus modEventBus) {
        NeoForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onScreenOpened(ScreenEvent.Opening event) {
        if (event.getNewScreen() instanceof TitleScreen && !(event.getNewScreen() instanceof ServerMenuScreen)) {
            event.setNewScreen(new ServerMenuScreen());
        }
    }

    @SubscribeEvent
    public void onBackgroundRendered(ScreenEvent.BackgroundRendered event) {
        Screen screen = event.getScreen();
        if (screen instanceof ServerMenuScreen) {
            return;
        }
        if (screen instanceof TitleScreen || isOptionsScreen(screen)) {
            BackgroundRenderer.renderFull(event.getGuiGraphics(), screen.width, screen.height);
        }
    }

    private static boolean isOptionsScreen(Screen screen) {
        String name = screen.getClass().getName();
        return name.startsWith(OPTIONS_PACKAGE);
    }
}