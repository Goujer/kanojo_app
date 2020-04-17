package jp.live2d.type;

public class LDPointF {
    public float x;
    public float y;

    public LDPointF() {
    }

    public LDPointF(float x2, float y2) {
        this.x = x2;
        this.y = y2;
    }

    public LDPointF(LDPointF pt) {
        this.x = pt.x;
        this.y = pt.y;
    }

    public void setPoint(float x2, float y2) {
        this.x = x2;
        this.y = y2;
    }

    public void setPoint(LDPointF pt) {
        this.x = pt.x;
        this.y = pt.y;
    }
}
