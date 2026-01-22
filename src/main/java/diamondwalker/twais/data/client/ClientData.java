package diamondwalker.twais.data.client;

import net.minecraft.client.Minecraft;

public class ClientData {
    public boolean slience = false;

    private boolean isVisageActive = false;
    private boolean pauseBlocked = false;

    public int visageFogAmount = 0;

    public boolean darkWorld = false;
    public boolean wackyColors = false;

    public StaticData staticData = null;

    public ScreenFlashData flash = null;

    private boolean silence = false;

    public void setVisageActive(boolean active) {
        isVisageActive = active;
        if (!isVisageActive) pauseBlocked = false;
    }

    public void setSilenced(boolean active) {
        this.silence = active;
        if (silence) Minecraft.getInstance().getSoundManager().stop();
    }

    public boolean isSilenced() {
        return silence;
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
