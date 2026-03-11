package diamondwalker.sscary.script;

import net.minecraft.server.MinecraftServer;

import java.util.function.Function;

public class ScriptType <T extends Script> {
    private final Function<MinecraftServer, T> provider;
    private final Function<Integer, T> clientProvider;

    private boolean noSave = false;

    public ScriptType(Function<MinecraftServer, T> provider) {
        this(provider, null);
    }

    public ScriptType(Function<MinecraftServer, T> serverProvider, Function<Integer, T> clientProvider) {
        this.provider = serverProvider;
        this.clientProvider = clientProvider;
    }

    public ScriptType<T> noSave() {
        noSave = true;
        return this;
    }

    public T buildForServer(MinecraftServer server) {
        return provider.apply(server);
    }

    public T buildForClient(int id) {
        if (clientProvider == null) throw new IllegalStateException("Client constructor was never defined for script");
        return clientProvider.apply(id);
    }

    public boolean shouldSendToClient() {
        return clientProvider != null;
    }

    public boolean shouldSave() {
        return !noSave;
    }
}
