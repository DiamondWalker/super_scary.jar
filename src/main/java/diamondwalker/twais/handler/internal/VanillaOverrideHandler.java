package diamondwalker.twais.handler.internal;

import diamondwalker.twais.OverworldSpecialEffects;
import diamondwalker.twais.data.client.ClientData;
import diamondwalker.twais.gui.screen.NoticeScreen;
import diamondwalker.twais.registry.TWAISMusic;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterDimensionSpecialEffectsEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.neoforged.neoforge.client.event.SelectMusicEvent;

@EventBusSubscriber
public class VanillaOverrideHandler {
    private static boolean changed = false;

    @SubscribeEvent
    private static void handleMenuOpen(ScreenEvent.Opening event) {
        if (event.getNewScreen() instanceof TitleScreen title && !changed) {
            changed = true;
            //event.setNewScreen(new NoticeScreen(title));
        }
    }

    @SubscribeEvent
    private static void overworldRendering(RegisterDimensionSpecialEffectsEvent event) {
        event.register(BuiltinDimensionTypes.OVERWORLD_EFFECTS, new OverworldSpecialEffects());
    }

    @SubscribeEvent
    private static void stopMusic(SelectMusicEvent event) {
        if (ClientData.get().wackyColors) {
            event.overrideMusic(TWAISMusic.PARTY);
            return;
        }
        event.overrideMusic(null);
    }
}
