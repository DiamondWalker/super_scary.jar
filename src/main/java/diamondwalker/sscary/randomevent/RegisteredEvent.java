package diamondwalker.sscary.randomevent;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;

import java.util.function.Function;

public class RegisteredEvent {
    public final EnumEventRarity rarity;
    public final ResourceLocation id;
    public final Function<MinecraftServer, Boolean> function;

    public RegisteredEvent(EnumEventRarity rarity, ResourceLocation id, Function<MinecraftServer, Boolean> func) {
        this.rarity = rarity;
        this.id = id;
        this.function = func;

        RandomEventRegistry.register(this);
    }
}
