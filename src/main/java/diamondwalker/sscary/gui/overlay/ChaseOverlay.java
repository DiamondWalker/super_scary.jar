package diamondwalker.sscary.gui.overlay;

import diamondwalker.sscary.SScary;
import diamondwalker.sscary.data.client.ClientData;
import diamondwalker.sscary.entity.entity.friedsteve.EntityFriedSteve;
import diamondwalker.sscary.entity.entity.friedsteve.EnumFriedSteveState;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;

public class ChaseOverlay implements LayeredDraw.Layer {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(SScary.MODID, "textures/gui/distance_meter.png");
    private static final ResourceLocation FRIED_STEVE_TEXTURE = ResourceLocation.fromNamespaceAndPath(SScary.MODID, "textures/entity/fried_steve.png");

    @Override
    public void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        EntityFriedSteve steve = ClientData.get().friedSteve;
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null && steve != null && !steve.isRemoved() && steve.getState() == EnumFriedSteveState.CHASING) {
            int xMin = guiGraphics.guiWidth() / 2 - 201 / 2;
            int startX = xMin + 5;
            int endX = xMin + 201 - 5;
            int y = 12;

            double dist = player.getPosition(deltaTracker.getGameTimeDeltaPartialTick(true)).distanceTo(steve.getPosition(deltaTracker.getGameTimeDeltaPartialTick(true)));
            dist = Math.clamp((dist - 2) / 32, 0, 1);
            int steveX = (int)Math.round(Mth.lerp(1 - dist, startX, endX));
            guiGraphics.blit(TEXTURE, xMin, y - 5, 0, 0, 201, 10, 201, 10);

            guiGraphics.blit(player.getSkin().texture(), endX - 6, y - 6, 12, 12, 8, 8, 8, 8, 64, 64);
            guiGraphics.blit(player.getSkin().texture(), endX - 6, y - 6, 12, 12, 40, 8, 8, 8, 64, 64);

            guiGraphics.blit(FRIED_STEVE_TEXTURE, steveX - 6, y - 6, 12, 12, 8, 8, 8, 8, 64, 64);
            guiGraphics.blit(FRIED_STEVE_TEXTURE, steveX - 6, y - 6, 12, 12, 40, 8, 8, 8, 64, 64);
        }
    }
}
