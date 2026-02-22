package diamondwalker.sscary.util.rendering;

import java.util.ArrayList;
import java.util.List;

public class AnimatedSpriteHelper {
    private final int frameCountX;
    private final int frameCountY;
    private int currentFrameX = 0;
    private int currentFrameY = 0;

    private SpriteAnimation currentAnimation = null;
    private int animationIndex = 0;
    private int frameTimer = 0;

    public AnimatedSpriteHelper(int frameCountX, int frameCountY) {
        this.frameCountX = frameCountX;
        this.frameCountY = frameCountY;
    }

    public void setFrameX(int frame) {
        currentFrameX = frame;
    }

    public void setFrameY(int frame) {
        currentFrameY = frame;
    }

    public void setFrame(int x, int y) {
        setFrameX(x);
        setFrameY(y);
    }

    public void tick() {
        if (currentAnimation != null && frameTimer-- <= 0) {
            setAnimationFrame((animationIndex + 1) % currentAnimation.getFrameCount());
        }
    }

    public void setAnimation(SpriteAnimation animation) {
        if (animation == currentAnimation) return;

        if (animation == null) {
            setFrame(0, 0);
            animationIndex = 0;
            frameTimer = 0;
            return;
        }

        if (animation.builtFor != this) throw new IllegalStateException("This animation was not built for this AnimatedSpriteHelper!");
        currentAnimation = animation;
        setAnimationFrame(0);
    }

    public AnimationBuilder defineAnimation() {
        return new AnimationBuilder(this);
    }

    private void setAnimationFrame(int i) {
        animationIndex = i;
        AnimationFrame frame = currentAnimation.getFrame(i);
        frameTimer = frame.time;
        setFrame(frame.frameX, frame.frameY);
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

    public class AnimationBuilder {
        private final AnimatedSpriteHelper buildingFor;
        private List<AnimationFrame> frames = new ArrayList<>();

        private AnimationBuilder(AnimatedSpriteHelper buildingFor) {
            this.buildingFor = buildingFor;
        }

        public AnimationBuilder addFrame(int frameX, int frameY, int tickCount) {
            frames.add(new AnimationFrame(frameX, frameY, tickCount));
            return this;
        }

        public SpriteAnimation build() {
            return new SpriteAnimation(buildingFor, frames.toArray(new AnimationFrame[]{}));
        }
    }

    public class SpriteAnimation {
        private final AnimatedSpriteHelper builtFor;
        private final AnimationFrame[] frames;

        protected SpriteAnimation(AnimatedSpriteHelper builtFor, AnimationFrame[] frames) {
            this.builtFor = builtFor;
            this.frames = frames;
        }

        private int getFrameCount() {
            return frames.length;
        }

        private AnimationFrame getFrame(int index) {
            return frames[index];
        }
    }

    private record AnimationFrame(int frameX, int frameY, int time) { }
}
