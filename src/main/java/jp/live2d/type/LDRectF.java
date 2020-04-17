package jp.live2d.type;

public class LDRectF {
    public float height;
    public float width;
    public float x;
    public float y;

    public LDRectF() {
    }

    public LDRectF(float x2, float y2, float width2, float height2) {
        this.x = x2;
        this.y = y2;
        this.width = width2;
        this.height = height2;
    }

    public float getCenterX() {
        return 0.5f * (this.x + this.x + this.width);
    }

    public float getCenterY() {
        return 0.5f * (this.y + this.y + this.height);
    }

    public float getRight() {
        return this.x + this.width;
    }

    public float getBottom() {
        return this.y + this.height;
    }

    public void setRect(float x2, float y2, float width2, float height2) {
        this.x = x2;
        this.y = y2;
        this.width = width2;
        this.height = height2;
    }

    public void setRect(LDRectF r) {
        this.x = r.x;
        this.y = r.y;
        this.width = r.width;
        this.height = r.height;
    }
}
