package diamondwalker.sscary.data;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;

import java.io.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.function.Function;

public class PermanentSaveData {
    private final File dataFile;

    private static PermanentSaveData instance;

    private String username = System.getProperty("user.name");
    private boolean corrupedAngered = false;
    private boolean corrupedAngered2 = false;

    private PermanentSaveData(File directory) {
        dataFile = new File(directory, "sscary.dat");

        if (dataFile.exists()) readFile();
    }

    public static PermanentSaveData getOrCreateInstance() {
        if (instance == null) instance = new PermanentSaveData(Minecraft.getInstance().gameDirectory);

        return instance;
    }

    //region Accessors and Setters
    public void setUsername(String string) {
        ensureThreadSafety();
        this.username = string;
    }

    public String getUsername() {
        return username;
    }

    public void setCorruptedAngered(boolean angered) {
        this.corrupedAngered = angered;
    }

    public boolean getCorruptedAngered() {
        return corrupedAngered;
    }

    public void setCorruptedRemembered(boolean remembered) {
        this.corrupedAngered2 = remembered;
    }

    public boolean getCorruptedRemembered() {
        return corrupedAngered2;
    }

    public void saveChanges() {
        ensureThreadSafety();
        writeLock.set(true);
        new Thread(() -> {
            saveFile();
            writeLock.set(false);
        }).start();
    }
    //endregion

    //region Internal IO
    private void readFile() {
        try (DataInputStream stream = new DataInputStream(new FileInputStream(dataFile))) {
            username = stream.readUTF();
            corrupedAngered = stream.readBoolean();
            corrupedAngered2 = stream.readBoolean();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void saveFile() {
        try (DataOutputStream stream = new DataOutputStream(new FileOutputStream(dataFile))) {
            stream.writeUTF(username);
            stream.writeBoolean(corrupedAngered);
            stream.writeBoolean(corrupedAngered2);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    //endregion

    //region Threading
    // locks thread if file is being written
    private final AtomicBoolean writeLock = new AtomicBoolean(false);

    private void ensureThreadSafety() {
        while (writeLock.get()) Thread.yield();
    }
    //endregion
}
