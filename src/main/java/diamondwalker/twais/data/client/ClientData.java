package diamondwalker.twais.data.client;

public class ClientData {
    public boolean slience = false;

    public StaticData staticData = null;

    public ScreenFlashData flash = null;

    private static ClientData INSTANCE = new ClientData();

    public static ClientData get() {
        return INSTANCE;
    }

    public static void reset() {
        INSTANCE = new ClientData();
    }
}
