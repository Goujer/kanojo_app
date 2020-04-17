package jp.live2d.type;

public class LDColor {
    public int color;

    public LDColor() {
    }

    public LDColor(int color2, boolean useAlpha) {
        this.color = !useAlpha ? color2 | -16777216 : color2;
    }
}
