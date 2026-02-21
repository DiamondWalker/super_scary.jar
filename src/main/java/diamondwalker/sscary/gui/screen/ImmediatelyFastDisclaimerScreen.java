package diamondwalker.sscary.gui.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FastColor;

import java.io.File;
import java.util.ArrayList;

public class ImmediatelyFastDisclaimerScreen extends Screen {
    private final String path;

    public ImmediatelyFastDisclaimerScreen(File file) {
        super(Component.literal("Compatibility Disclaimer"));

        path = file.getAbsolutePath().replace("\\.\\", "\\");
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        guiGraphics.fill(0, 0, width, height, FastColor.ARGB32.color(0, 0, 0));

        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate((float)width / 2, (float)height / 2, 0);

        String[] lines = new String[] {
                "It appears you are using the mod \"ImmediatelyFast\" and have HUD Batching enabled.",
                "",
                "This setting is incompatible with super_scary.jar and causes its visual effects to break.",
                "",
                "Please go to Immediately Fast's config file and change \"hud_batching\" to \"false\".",
                "",
                "This config file should be located at " + path
        };

        ArrayList<String> wrappedLines = new ArrayList<>();
        for (String line : lines) {
            StringBuilder lineBuilder = new StringBuilder();
            for (String character : line.split(" ")) {
                String currLine = lineBuilder.toString();
                if (font.width(currLine + character) >= 0.9 * width) {
                    wrappedLines.add(lineBuilder.toString());
                    lineBuilder = new StringBuilder();
                }
                if (!lineBuilder.isEmpty()) lineBuilder.append(' ');
                lineBuilder.append(character);
            }
            wrappedLines.add(lineBuilder.toString());
        }

        guiGraphics.setColor(1.0f, 1.0f, 1.0f, 1.0f);

        for (int i = 0; i < wrappedLines.size(); i++) {
            String line = wrappedLines.get(i);
            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate(-(float)font.width(line) / 2, 0, 0);
            guiGraphics.pose().translate(0, (double) (-(wrappedLines.size() + 1) / 2 * font.lineHeight), 0);
            guiGraphics.pose().translate(0, 12 * i, 0);
            guiGraphics.drawString(font, line, 0, 0, FastColor.ARGB32.color(255, 255, 255, 255));
            guiGraphics.pose().popPose();
        }

        guiGraphics.pose().popPose();
    }
}
