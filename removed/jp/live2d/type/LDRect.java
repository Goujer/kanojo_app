package jp.live2d.type;

public class LDRect {
    public int height;
    public int width;
    public int x;
    public int y;

    public LDRect() {
    }

    public LDRect(int x2, int y2, int width2, int height2) {
        this.x = x2;
        this.y = y2;
        this.width = width2;
        this.height = height2;
    }

    public LDRect(LDRect r) {
        this.x = r.x;
        this.y = r.y;
        this.width = r.width;
        this.height = r.height;
    }

    public float getCenterX() {
        return 0.5f * ((float) (this.x + this.x + this.width));
    }

    public float getCenterY() {
        return 0.5f * ((float) (this.y + this.y + this.height));
    }

    public int getRight() {
        return this.x + this.width;
    }

    public int getBottom() {
        return this.y + this.height;
    }

    public void setRect(int x2, int y2, int width2, int height2) {
        this.x = x2;
        this.y = y2;
        this.width = width2;
        this.height = height2;
    }

    public void setRect(LDRect r) {
        this.x = r.x;
        this.y = r.y;
        this.width = r.width;
        this.height = r.height;
    }
}
