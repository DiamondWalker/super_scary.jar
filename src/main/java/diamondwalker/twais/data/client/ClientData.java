package diamondwalker.twais.data.client;

public class ClientData {
    public boolean slience = false;

    private boolean isVisageActive = false;
    private boolean pauseBlocked = false;

    public int visageFogAmount = 0;

    public boolean darkWorld = false;
    public boolean wackyColors = false;

    public StaticData staticData = null;

    public ScreenFlashData flash = null;

    public void setVisageActive(boolean active) {
        isVisageActive = active;
        if (!isVisageActive) pauseBlocked = false;
    }

    public boolean isVisageActive() {
        return isVisageActive;
    }

    public void blockPause() {
        pauseBlocked = true;
    }

    public boolean canPause() {
        return !pauseBlocked;
    }

    private static ClientData INSTANCE = new ClientData();

    public static ClientData get() {
        return INSTANCE;
    }

    public static void reset() {
        INSTANCE = new ClientData();
    }
}
