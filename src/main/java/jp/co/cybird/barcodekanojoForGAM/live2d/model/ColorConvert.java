package jp.co.cybird.barcodekanojoForGAM.live2d.model;

import android.support.v4.view.MotionEventCompat;

public class ColorConvert {
    static final int H = 0;
    static final int L = 0;
    static final int S = 0;
    float bri;
    float hue;
    float sat;

    public ColorConvert(float h, float l, float s) {
        this.hue = h;
        this.sat = l;
        this.bri = s;
    }

    public float h2v(float h, float cmin, float cmax) {
        float h2 = fmod(h, 360.0f);
        if (h2 < 0.0f) {
            h2 += 360.0f;
        }
        if (h2 < 60.0f) {
            return cmin + (((cmax - cmin) * h2) / 60.0f);
        }
        if (h2 < 60.0f || h2 >= 180.0f) {
            return (h2 < 180.0f || h2 >= 240.0f) ? cmin : cmin + (((cmax - cmin) * (240.0f - h2)) / 60.0f);
        }
        return cmax;
    }

    public float fmod(float v, float a) {
        return v - (((float) ((int) (v / a))) * a);
    }

    public void HSLtoRGB(float h, float s, float l, float[] rgb) {
        float f;
        float cmax;
        float cmin;
        if (l < 0.0f) {
            f = -l;
        } else {
            f = l;
        }
        float div = (float) (1.0d - ((double) f));
        if (div != 0.0f) {
            s /= div;
        }
        if (l < -1.0f) {
            l = -1.0f;
        }
        if (l > 1.0f) {
            l = 1.0f;
        }
        if (s < 0.0f) {
            s = 0.0f;
        }
        if (s > 1.0f) {
            s = 1.0f;
        }
        float h2 = fmod(h, 360.0f);
        if (h2 < 0.0f) {
            h2 += 360.0f;
        }
        if (l <= 0.0f) {
            cmin = ((1.0f + l) / 2.0f) * (1.0f - s);
            cmax = (1.0f + l) - cmin;
        } else {
            cmax = (((1.0f + l) / 2.0f) * (1.0f - s)) + s;
            cmin = (1.0f + l) - cmax;
        }
        rgb[0] = h2v(120.0f + h2, cmin, cmax);
        rgb[1] = h2v(h2, cmin, cmax);
        rgb[2] = h2v(h2 - 120.0f, cmin, cmax);
    }

    public int HSLtoRGB(float h, float s, float l) {
        float[] rgb = new float[3];
        HSLtoRGB(h, s, l, rgb);
        int r = (int) (((double) (rgb[0] * 255.0f)) + 0.5d);
        int g = (int) (((double) (rgb[1] * 255.0f)) + 0.5d);
        int b = (int) (((double) (rgb[2] * 255.0f)) + 0.5d);
        if (r < 0) {
            r = 0;
        } else if (r > 255) {
            r = MotionEventCompat.ACTION_MASK;
        }
        if (g < 0) {
            g = 0;
        } else if (g > 255) {
            g = MotionEventCompat.ACTION_MASK;
        }
        if (b < 0) {
            b = 0;
        } else if (b > 255) {
            b = MotionEventCompat.ACTION_MASK;
        }
        return (r << 16) | (g << 8) | b;
    }

    public void HSLtoRGB(float h, float s, float l, int[] retrgb) {
        float[] rgb = new float[3];
        HSLtoRGB(h, s, l, rgb);
        int r = (int) (0.5d + ((double) (rgb[0] * 255.0f)));
        int g = (int) (0.5d + ((double) (rgb[1] * 255.0f)));
        int b = (int) (0.5d + ((double) (rgb[2] * 255.0f)));
        if (r < 0) {
            r = 0;
        } else if (r > 255) {
            r = MotionEventCompat.ACTION_MASK;
        }
        if (g < 0) {
            g = 0;
        } else if (g > 255) {
            g = MotionEventCompat.ACTION_MASK;
        }
        if (b < 0) {
            b = 0;
        } else if (b > 255) {
            b = MotionEventCompat.ACTION_MASK;
        }
        retrgb[0] = r;
        retrgb[1] = g;
        retrgb[2] = b;
    }

    public void RGBtoHSL(float r, float g, float b, float[] hsl) {
        float cmax;
        float cmin;
        float h;
        float f;
        if (r >= g) {
            cmax = r > b ? r : b;
        } else {
            cmax = g > b ? g : b;
        }
        if (r <= g) {
            cmin = r < b ? r : b;
        } else {
            cmin = g < b ? g : b;
        }
        hsl[0] = (cmax + cmin) - 1.0f;
        float cd = cmax - cmin;
        if (cd == 0.0f) {
            hsl[0] = 0.0f;
            hsl[0] = 0.0f;
            return;
        }
        if (hsl[0] <= 0.0f) {
            hsl[0] = cd / (cmax + cmin);
        } else {
            hsl[0] = cd / (2.0f - (cmax + cmin));
        }
        if (r == cmax) {
            h = (g - b) / cd;
        } else if (g == cmax) {
            h = 2.0f + ((b - r) / cd);
        } else {
            h = 4.0f + ((r - g) / cd);
        }
        float h2 = h * 60.0f;
        if (h2 < 0.0f) {
            h2 += 360.0f;
        }
        hsl[0] = h2;
        double d = (double) hsl[0];
        if (hsl[0] < 0.0f) {
            f = -hsl[0];
        } else {
            f = hsl[0];
        }
        hsl[0] = (float) (d * (1.0d - ((double) f)));
    }

    public void RGBtoHSL(int rgb, float[] hsl) {
        RGBtoHSL(((float) ((rgb >> 16) & MotionEventCompat.ACTION_MASK)) / 255.0f, ((float) ((rgb >> 8) & MotionEventCompat.ACTION_MASK)) / 255.0f, ((float) (rgb & MotionEventCompat.ACTION_MASK)) / 255.0f, hsl);
    }

    public void RGBtoHSL(int r, int g, int b, float[] hsl) {
        RGBtoHSL(((float) r) / 255.0f, ((float) g) / 255.0f, ((float) b) / 255.0f, hsl);
    }
}
