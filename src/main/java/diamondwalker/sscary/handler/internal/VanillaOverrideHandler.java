package diamondwalker.sscary.handler.internal;

import diamondwalker.sscary.OverworldSpecialEffects;
import diamondwalker.sscary.data.client.ClientData;
import diamondwalker.sscary.gui.screen.ConsoleScreen;
import diamondwalker.sscary.gui.screen.FakePauseScreen;
import diamondwalker.sscary.registry.SScaryMusic;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterDimensionSpecialEffectsEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.neoforged.neoforge.client.event.SelectMusicEvent;
import net.neoforged.neoforge.event.level.BlockEvent;

@EventBusSubscriber
public class VanillaOverrideHandler {
    private static boolean changed = false;

    @SubscribeEvent
    private static void handleMenuOpen(ScreenEvent.Opening event) {
        if (event.getNewScreen() instanceof TitleScreen title && !changed) {
            changed = true;
            event.setNewScreen(new ConsoleScreen(title));
        }
    }

    @SubscribeEvent
    private static void handleVisagePauseScreen(ScreenEvent.Opening event) {
        if (ClientData.get().isVisageActive()) {
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
        if (ClientData.get().wackyColors) {
            event.overrideMusic(SScaryMusic.PARTY);
            return;
        }
        event.overrideMusic(null);
    }

    @SubscribeEvent
    private static void disableNetherPortal(BlockEvent.PortalSpawnEvent event) {
        MinecraftServer server = event.getLevel().getServer();
        server.getPlayerList().broadcastSystemMessage(Component.literal("super_scary.jar disables the Nether since the mod currently has issues when the player is outside the Overworld.").withStyle(ChatFormatting.DARK_RED), false);
        server.getPlayerList().broadcastSystemMessage(Component.literal("I'll enable it again in a future update, once I've fixed the bugs.").withStyle(ChatFormatting.DARK_RED), false);
        server.getPlayerList().broadcastSystemMessage(Component.literal("I'm sorry for the inconvenience. Please keep in mind this is an alpha version!").withStyle(ChatFormatting.DARK_RED), false);
        event.setCanceled(true);
    }
}
