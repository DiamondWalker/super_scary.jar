package diamondwalker.twais.data.client;

public class ClientData {
    public boolean slience = false;

    public boolean visageFog = false;
    public int visageFogAmount = 0;

    public boolean darkWorld = false;
    public boolean wackyColors = false;

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
