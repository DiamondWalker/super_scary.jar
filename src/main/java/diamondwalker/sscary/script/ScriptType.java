package diamondwalker.sscary.script;

import net.minecraft.server.MinecraftServer;

import java.util.function.Function;

public class ScriptType <T extends Script> {
    private final Function<MinecraftServer, T> provider;

    public ScriptType(Function<MinecraftServer, T> provider) {
        this.provider = provider;
    }

    public T build(MinecraftServer server) {
        return provider.apply(server);
    }
}
