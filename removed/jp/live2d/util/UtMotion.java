package jp.live2d.util;

public class UtMotion {
    public static float getEasingSine(float value01) {
        if (value01 < 0.0f) {
            return 0.0f;
        }
        if (value01 > 1.0f) {
            return 1.0f;
        }
        return (float) (0.5d - (Math.cos(((double) value01) * 3.141592653589793d) * 0.5d));
    }
}
