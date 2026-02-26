package diamondwalker.sscary.data.client;

public class ColorOverlayData {
    private float red;
    private float green;
    private float blue;
    private float alpha;
    public int timeLeft;

    public ColorOverlayData(float r, float g, float b, float a, int ticks) {
        this.red = r;
        this.green = g;
        this.blue = b;
        this.alpha = a;
        this.timeLeft = ticks;
    }

    public float red() {
        return red;
    }

    public float green() {
        return green;
    }

    public float blue() {
        return blue;
    }

    public float alpha() {
        return alpha;
    }
}
