package jp.co.cybird.barcodekanojoForGAM.live2d.model;

import android.support.v4.view.MotionEventCompat;

public class ColorConvertUtil {
    static final int COLOR_CONV_PERCENT = 0;
    static final int COLOR_SATULATION_ADJUST = 1;

    public static void convertGray(int[] imageDataInt, int grayValue, int width, int height, int texSizeW, int texSizeH) {
        for (int y = 0; y < height; y++) {
            int offy = y * texSizeW;
            for (int x = 0; x < width; x++) {
                int off = x + offy;
                int a = (imageDataInt[off] >> 24) & MotionEventCompat.ACTION_MASK;
                if (a > 0) {
                    int gv = grayValue;
                    imageDataInt[off] = ((a << 24) & -16777216) | ((gv << 16) & 16711680) | ((gv << 8) & MotionEventCompat.ACTION_POINTER_INDEX_MASK) | (gv & MotionEventCompat.ACTION_MASK);
                }
            }
        }
    }

    static void convertColor_exe1(int[] imageDataInt, ColorConvert colorConvert, int width, int height, int texSizeW, int texSizeH) {
        float hsl_S;
        float h;
        float hsl_H;
        float f;
        float hsl_S2;
        float f2;
        float cmax;
        float cmin;
        float dr;
        float dg;
        float db;
        float conv_H = colorConvert.hue;
        float conv_S = colorConvert.sat;
        float conv_L = colorConvert.bri;
        float lastSR = -1.0f;
        float lastSG = -1.0f;
        float lastSB = -1.0f;
        float lastSA = -1.0f;
        int lastDR = -1;
        int lastDG = -1;
        int lastDB = -1;
        for (int y = 0; y < height; y++) {
            int offy = y * texSizeW;
            for (int x = 0; x < width; x++) {
                int off = x + offy;
                int v = imageDataInt[off];
                int a = (v >> 24) & MotionEventCompat.ACTION_MASK;
                float sa = 0.003921569f * ((float) a);
                if (((double) sa) >= 0.1d) {
                    float sr = (float) ((v >> 16) & MotionEventCompat.ACTION_MASK);
                    float sg = (float) ((v >> 8) & MotionEventCompat.ACTION_MASK);
                    float sb = (float) (v & MotionEventCompat.ACTION_MASK);
                    if (sr == lastSR && sg == lastSG && sb == lastSB && sa == lastSA) {
                        imageDataInt[off] = ((a << 24) & -16777216) | ((lastDR << 16) & 16711680) | ((lastDG << 8) & MotionEventCompat.ACTION_POINTER_INDEX_MASK) | (lastDB & MotionEventCompat.ACTION_MASK);
                    } else {
                        lastSR = sr;
                        lastSG = sg;
                        lastSB = sb;
                        lastSA = sa;
                        float sr2 = sr * 0.003921569f;
                        float sg2 = sg * 0.003921569f;
                        float sb2 = sb * 0.003921569f;
                        float cmax2 = sr2 >= sg2 ? sr2 > sb2 ? sr2 : sb2 : sg2 > sb2 ? sg2 : sb2;
                        float cmin2 = sr2 <= sg2 ? sr2 < sb2 ? sr2 : sb2 : sg2 < sb2 ? sg2 : sb2;
                        float hsl_L = (cmax2 + cmin2) - 1.0f;
                        float cd = cmax2 - cmin2;
                        if (cd == 0.0f) {
                            hsl_S2 = 0.0f;
                            hsl_H = 0;
                        } else {
                            if (hsl_L <= 0.0f) {
                                hsl_S = cd / (cmax2 + cmin2);
                            } else {
                                hsl_S = cd / (2.0f - (cmax2 + cmin2));
                            }
                            if (sr2 == cmax2) {
                                h = (sg2 - sb2) / cd;
                            } else if (sg2 == cmax2) {
                                h = 2.0f + ((sb2 - sr2) / cd);
                            } else {
                                h = 4.0f + ((sr2 - sg2) / cd);
                            }
                            float h2 = h * 60.0f;
                            if (h2 < 0.0f) {
                                h2 += 360.0f;
                            }
                            hsl_H = h2;
                            if (hsl_L < 0.0f) {
                                f = -hsl_L;
                            } else {
                                f = hsl_L;
                            }
                            hsl_S2 = hsl_S * (1.0f - f);
                        }
                        float shusendo = (-0.5f - hsl_L) * 3.3333333f;
                        if (shusendo <= 1.0f) {
                            float hsl_H2 = hsl_H + conv_H;
                            float hsl_S3 = hsl_S2 + conv_S;
                            float hsl_L2 = hsl_L + conv_L;
                            if (hsl_L2 < 0.0f) {
                                f2 = -hsl_L2;
                            } else {
                                f2 = hsl_L2;
                            }
                            float div = 1.0f - f2;
                            if (div != 0.0f) {
                                hsl_S3 /= div;
                            }
                            if (hsl_L2 < -1.0f) {
                                hsl_L2 = -1082130432;
                            }
                            if (hsl_L2 > 1.0f) {
                                hsl_L2 = 1.0f;
                            }
                            if (hsl_S3 < 0.0f) {
                                hsl_S3 = 0.0f;
                            }
                            if (hsl_S3 > 1.0f) {
                                hsl_S3 = 1.0f;
                            }
                            float hsl_H3 = hsl_H2 - ((float) (((int) (hsl_H2 / 360.0f)) * 360));
                            if (hsl_H3 < 0.0f) {
                                hsl_H3 += 360.0f;
                            }
                            if (hsl_L2 <= 0.0f) {
                                cmin = ((1.0f + hsl_L2) / 2.0f) * (1.0f - hsl_S3);
                                cmax = (1.0f + hsl_L2) - cmin;
                            } else {
                                cmax = (((1.0f + hsl_L2) / 2.0f) * (1.0f - hsl_S3)) + hsl_S3;
                                cmin = (1.0f + hsl_L2) - cmax;
                            }
                            float tmph = hsl_H3 + 120.0f;
                            float tmph2 = tmph - ((float) (((int) (tmph / 360.0f)) * 360));
                            if (tmph2 < 0.0f) {
                                tmph2 += 360.0f;
                            }
                            if (tmph2 < 60.0f) {
                                dr = cmin + (((cmax - cmin) * tmph2) / 60.0f);
                            } else if (tmph2 < 180.0f) {
                                dr = cmax;
                            } else if (tmph2 < 240.0f) {
                                dr = cmin + (((cmax - cmin) * (240.0f - tmph2)) / 60.0f);
                            } else {
                                dr = cmin;
                            }
                            float tmph3 = hsl_H3;
                            float tmph4 = tmph3 - ((float) (((int) (tmph3 / 360.0f)) * 360));
                            if (tmph4 < 0.0f) {
                                tmph4 += 360.0f;
                            }
                            if (tmph4 < 60.0f) {
                                dg = cmin + (((cmax - cmin) * tmph4) / 60.0f);
                            } else if (tmph4 < 180.0f) {
                                dg = cmax;
                            } else if (tmph4 < 240.0f) {
                                dg = cmin + (((cmax - cmin) * (240.0f - tmph4)) / 60.0f);
                            } else {
                                dg = cmin;
                            }
                            float tmph5 = hsl_H3 - 120.0f;
                            float tmph6 = tmph5 - ((float) (((int) (tmph5 / 360.0f)) * 360));
                            if (tmph6 < 0.0f) {
                                tmph6 += 360.0f;
                            }
                            if (tmph6 < 60.0f) {
                                db = cmin + (((cmax - cmin) * tmph6) / 60.0f);
                            } else if (tmph6 < 180.0f) {
                                db = cmax;
                            } else if (tmph6 < 240.0f) {
                                db = cmin + (((cmax - cmin) * (240.0f - tmph6)) / 60.0f);
                            } else {
                                db = cmin;
                            }
                            if (shusendo < 0.0f) {
                                lastDR = (int) (255.0f * dr);
                                lastDG = (int) (255.0f * dg);
                                lastDB = (int) (255.0f * db);
                                imageDataInt[off] = ((a << 24) & -16777216) | ((lastDR << 16) & 16711680) | ((lastDG << 8) & MotionEventCompat.ACTION_POINTER_INDEX_MASK) | (lastDB & MotionEventCompat.ACTION_MASK);
                            } else {
                                lastDR = (int) ((sr2 * shusendo) + (255.0f * dr * (1.0f - shusendo)));
                                lastDG = (int) ((sg2 * shusendo) + (255.0f * dg * (1.0f - shusendo)));
                                lastDB = (int) ((sb2 * shusendo) + (255.0f * db * (1.0f - shusendo)));
                                imageDataInt[off] = ((a << 24) & -16777216) | ((lastDR << 16) & 16711680) | ((lastDG << 8) & MotionEventCompat.ACTION_POINTER_INDEX_MASK) | (lastDB & MotionEventCompat.ACTION_MASK);
                            }
                        }
                    }
                }
            }
        }
    }
}
