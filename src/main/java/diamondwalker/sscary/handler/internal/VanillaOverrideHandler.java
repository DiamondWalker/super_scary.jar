package diamondwalker.sscary.handler.internal;

import diamondwalker.sscary.Config;
import diamondwalker.sscary.SScary;
import diamondwalker.sscary.entity.entity.friedsteve.EnumFriedSteveState;
import diamondwalker.sscary.gui.screen.ImmediatelyFastDisclaimerScreen;
import diamondwalker.sscary.script.Script;
import diamondwalker.sscary.sky.OverworldSpecialEffects;
import diamondwalker.sscary.data.client.ClientData;
import diamondwalker.sscary.gui.screen.ConsoleScreen;
import diamondwalker.sscary.gui.screen.FakePauseScreen;
import diamondwalker.sscary.registry.SScaryMusic;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.sounds.Music;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterDimensionSpecialEffectsEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.neoforged.neoforge.client.event.SelectMusicEvent;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

@EventBusSubscriber
public class VanillaOverrideHandler {
    private static boolean changed = false;

    @SubscribeEvent
    private static void handleMenuOpen(ScreenEvent.Opening event) {
        if (event.getNewScreen() instanceof TitleScreen title) {
            if (ModList.get().isLoaded("immediatelyfast")) {
                File file = new File(Minecraft.getInstance().gameDirectory, "config/immediatelyfast.json");

                if (file.exists()) {
                    try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                        String line;
                        while ((line = br.readLine()) != null) {
                            if (line.matches("(\\s+)?\"hud_batching\"(\\s+)?:(\\s+)?true(\\s+)?,(\\s+)?")) {
                                event.setNewScreen(new ImmediatelyFastDisclaimerScreen(file));
                                return;
                            }
                        }
                    } catch (IOException e) {
                        SScary.LOGGER.warn("Error while reading Immediately Fast's config file", e);
                    }
                }
            }

            if (!changed /*&& !SScary.DEV_MODE*/) {
                changed = true;
                event.setNewScreen(new ConsoleScreen(title));
                return;
            }
        }
    }

    @SubscribeEvent
    private static void handleVisagePauseScreen(ScreenEvent.Opening event) {
        if (ClientData.get().isVisageActive()/* && !SScary.DEV_MODE*/) {
            if (event.getNewScreen() instanceof PauseScreen originalPauseScreen) {
                if (ClientData.get().canPause()) {
                    event.setNewScreen(new FakePauseScreen(originalPauseScreen.showsPauseMenu()));
                } else {
                    event.setCanceled(true);
                }
            }
        }
    }

    @SubscribeEvent
    private static void overworldRendering(RegisterDimensionSpecialEffectsEvent event) {
        event.register(BuiltinDimensionTypes.OVERWORLD_EFFECTS, new OverworldSpecialEffects());
    }

    @SubscribeEvent
    private static void stopMusic(SelectMusicEvent event) {
        ClientData data = ClientData.get();

        if (data.friedSteve != null && data.friedSteve.getState() == EnumFriedSteveState.CHASING) {
            event.overrideMusic(SScaryMusic.FRIED_STEVE);
            return;
        }

        for (Script script : ClientData.get().scripts.getScripts()) {
            Music music = script.getMusic();
            if (music != null) {
                event.overrideMusic(music);
                return;
            }
        }

        if (data.wackyColors) {
            event.overrideMusic(SScaryMusic.PARTY);
            return;
        }

        if (
                !Config.ALLOW_VANILLA_MUSIC.get() ||
                Minecraft.getInstance().screen instanceof ConsoleScreen
        ) {
            event.overrideMusic(null);
        }
    }
}
