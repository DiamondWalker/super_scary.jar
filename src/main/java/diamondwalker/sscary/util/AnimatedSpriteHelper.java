package diamondwalker.sscary.util;

public class AnimatedSpriteHelper {
    private final int frameCountX;
    private final int frameCountY;
    private int currentFrameX = 0;
    private int currentFrameY = 0;

    public AnimatedSpriteHelper(int frameCountX, int frameCountY) {
        this.frameCountX = frameCountX;
        this.frameCountY = frameCountY;
    }

    public void setFrameX(int frame) {
        currentFrameX = frame % frameCountX;
    }

    public void setFrameY(int frame) {
        currentFrameY = frame % frameCountY;
    }

    public void setFrame(int x, int y) {
        setFrameX(x);
        setFrameY(y);
    }

    public float u1() {
        float frameWidth = 1.0f / frameCountX;
        return frameWidth * currentFrameX;
    }

    public float u2() {
        float frameWidth = 1.0f / frameCountX;
        return frameWidth * (currentFrameX + 1);
    }

    public float v1() {
        float frameHeight = 1.0f / frameCountY;
        return frameHeight * currentFrameY;
    }

    public float v2() {
        float frameHeight = 1.0f / frameCountY;
        return frameHeight * (currentFrameY + 1);
    }
}
