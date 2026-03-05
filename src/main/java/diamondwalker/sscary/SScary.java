package diamondwalker.sscary;

import diamondwalker.sscary.registry.*;
import net.neoforged.fml.loading.FMLLoader;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(SScary.MODID)
public class SScary {
    public static final String MODID = "sscary";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final boolean DEV_MODE = !FMLLoader.isProduction();

    public SScary(IEventBus modEventBus, ModContainer modContainer) {
        SScaryBlocks.register(modEventBus);
        SScaryItems.register(modEventBus);
        SScaryEntities.register(modEventBus);
        SScaryScripts.register(modEventBus);
        SScaryScriptVariables.register(modEventBus);
        SScarySounds.register(modEventBus);
        SScaryDataAttachments.register(modEventBus);

        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        SScaryRandomEvents.registerRandomEvents();
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);

        if (DEV_MODE) LOGGER.info("super_scary.jar is running in a dev environment. Debug/WIP features will be active.");
    }
}
