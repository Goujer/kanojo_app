package jp.live2d.type;

public class LDPoint {
    public int x;
    public int y;

    public LDPoint() {
    }

    public LDPoint(int x2, int y2) {
        this.x = x2;
        this.y = y2;
    }

    public LDPoint(LDPoint pt) {
        this.x = pt.x;
        this.y = pt.y;
    }

    public void setPoint(LDPoint pt) {
        this.x = pt.x;
        this.y = pt.y;
    }

    public void setPoint(int x2, int y2) {
        this.x = x2;
        this.y = y2;
    }
}
