package diamondwalker.sscary.data.client;

import diamondwalker.sscary.entity.entity.construct.EntityConstruct;
import diamondwalker.sscary.entity.entity.friedsteve.EntityFriedSteve;
import diamondwalker.sscary.entity.entity.watchtower.EntityWatchtower;
import net.minecraft.client.Minecraft;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.function.Supplier;

public class ClientData {
    public boolean slience = false;

    private boolean isVisageActive = false;
    private boolean pauseBlocked = false;

    public int visageFogAmount = 0;

    public boolean darkWorld = false;
    public boolean wackyColors = false;

    public StaticData staticData = null;

    public ColorOverlayData colorOverlay = null;

    public ScreenFlashData flash = null;

    public NewScriptsClientData scripts = new NewScriptsClientData();

    private boolean silence = false;

    public EntityFriedSteve friedSteve = null;
    public int friedSteveChaseTint = 0;

    public EntityWatchtower tower = null;

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
