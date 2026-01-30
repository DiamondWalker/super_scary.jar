package diamondwalker.sscary.data.client;

public class StaticData {
    private float xScale;
    private float yScale;
    private float alpha;
    private int interval;
    private boolean noise;

    public int timeLeft;

    public float xScale() {
        return xScale;
    }

    public float yScale() {
        return yScale;
    }

    public float alpha() {
        return alpha;
    }

    public int interval() {
        return interval;
    }

    public boolean shouldPlaySound() {
        return noise;
    }

    public StaticData(float scale, float alpha, int time) {
        this(scale, scale, alpha, time);
    }

    public StaticData(float xScale, float yScale, float alpha, int time) {
        this(xScale, yScale, alpha, time, false);
    }

    public StaticData(float scale, float alpha, int fps, int time) {
        this(scale, scale, alpha, fps, time);
    }

    public StaticData(float xScale, float yScale, float alpha, int fps, int time) {
        this(xScale, yScale, alpha, fps, time, false);
    }

    public StaticData(float scale, float alpha, int time, boolean noise) {
        this(scale, scale, alpha, time, noise);
    }

    public StaticData(float xScale, float yScale, float alpha, int time, boolean noise) {
        this.xScale = xScale;
        this.yScale = yScale;
        this.alpha = alpha;
        this.interval = 1;
        this.timeLeft = time;
        this.noise = noise;
    }

    public StaticData(float scale, float alpha, int fps, int time, boolean noise) {
        this(scale, scale, alpha, fps, time, noise);
    }

    public StaticData(float xScale, float yScale, float alpha, int fps, int time, boolean noise) {
        this.xScale = xScale;
        this.yScale = yScale;
        this.alpha = alpha;
        interval = (int) Math.round(1e9 / fps);
        this.timeLeft = time;
        this.noise = noise;
    }
}
