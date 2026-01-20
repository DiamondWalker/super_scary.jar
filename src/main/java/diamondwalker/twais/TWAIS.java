package diamondwalker.twais;

import diamondwalker.twais.registry.*;
import net.neoforged.fml.loading.FMLLoader;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(TWAIS.MODID)
public class TWAIS {
    public static final String MODID = "twais";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final boolean DEV_MODE = !FMLLoader.isProduction();

    public TWAIS(IEventBus modEventBus, ModContainer modContainer) {
        TWAISBlocks.register(modEventBus);
        TWAISItems.register(modEventBus);
        TWAISEntities.register(modEventBus);
        TWAISSounds.register(modEventBus);
        TWAISDataAttachments.register(modEventBus);

        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        TWAISRandomEvents.registerRandomEvents();
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);

        if (DEV_MODE) LOGGER.info("TWAIS is running in a dev environment. Debug/WIP features will be active.");
    }
}
