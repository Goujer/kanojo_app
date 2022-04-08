package jp.co.cybird.barcodekanojoForGAM.live2d.model;

public class ColorConvertUtil {
	static final int COLOR_CONV_PERCENT = 0;
	static final int COLOR_SATULATION_ADJUST = 1;

	public static void convertGray(int[] imageDataInt, int grayValue, int width, int height, int texSizeW, int texSizeH) {
		for (int y = 0; y < height; y++) {
			int offy = y * texSizeW;
			for (int x = 0; x < width; x++) {
				int off = x + offy;
				int a = (imageDataInt[off] >> 24) & 255;
				if (a > 0) {
					imageDataInt[off] = ((a << 24) & -16777216) | ((grayValue << 16) & 16711680) | ((grayValue << 8) & 65280) | (grayValue & 255);
				}
			}
		}
	}

	//Have no illusions this thing follows no standard for HSL that I can find. That said Hue and Lum seems correct but I have no idea what's happening with Sat.
	static void convertColor_exe1(int[] imageDataInt, ColorConvert colorConvert, int width, int height, int texSizeW, int texSizeH) {
		int lastA = -1;
		int lastR = -1;
		int lastG = -1;
		int lastB = -1;

		int lastDR = -1;
		int lastDG = -1;
		int lastDB = -1;

		for (int y = 0; y < height; y++) {
			int offy = y * texSizeW;
			for (int x = 0; x < width; x++) {
				int off = x + offy;
				int v = imageDataInt[off];
				int a = (v >> 24) & 0xff;

				if (a >= 26) {	//Process conversion if alpha [0,255] is greater than 26
					int r = (v >> 16) & 0xff;
					int g = (v >> 8) & 0xff;
					int b = v & 0xff;
					if (r != g || r != b) {
						if (r == lastR && g == lastG && b == lastB && a == lastA) {
							imageDataInt[off] = ((a << 24) & 0xff000000) | ((lastDR << 16) & 0xff0000) | ((lastDG << 8) & 0xff00) | (lastDB & 0xff);
						} else {
							lastA = a;
							lastR = r;
							lastG = g;
							lastB = b;

							float sr = r / 255.0f;
							float sg = g / 255.0f;
							float sb = b / 255.0f;

							float cmax2 = sr >= sg ? Math.max(sr, sb) : Math.max(sg, sb);
							float cmin2 = sr <= sg ? Math.min(sr, sb) : Math.min(sg, sb);
							float hsl_H;
							float hsl_S;
							float hsl_L = (cmax2 + cmin2) / 2.0f;	//RGB to Lum
							float chroma = cmax2 - cmin2;

							float shusendo = -6.6666666f*hsl_L - 1.6666666f;
							if (shusendo <= 1.0f) {
								if (sr == cmax2) {	//RGB to Hue
									hsl_H = 60.0f * ((sg - sb) / chroma);
								} else if (sg == cmax2) {
									hsl_H = 60.0f * (2.0f + ((sb - sr) / chroma));
								} else {
									hsl_H = 60.0f * (4.0f + ((sr - sg) / chroma));
								}

								if (hsl_L <= 0.5f) {	//RGB to Sat
									hsl_S = chroma / (cmax2 + cmin2);
								} else {
									hsl_S = chroma / (2.0f - (cmax2 + cmin2));
								}
								hsl_S *= 1.0f - Math.abs(2.0f*hsl_L-1);

								//Alter values.
								hsl_H += colorConvert.hue;
								hsl_S += colorConvert.sat;
								hsl_L += colorConvert.lum;
								float dr, dg, db;

								//Sat Correction
								float div = 1.0f - Math.abs(2.0f*hsl_L-1);
								if (div != 0.0f) {
									hsl_S /= div;
								}
								if (hsl_S < 0.0f) {
									hsl_S = 0.0f;
								} else if (hsl_S > 1.0f) {
									hsl_S = 1.0f;
								}

								//Lum Correction
								if (hsl_L < 0.0f) {
									hsl_L = 0.0f;
								} else if (hsl_L > 1.0f) {
									hsl_L = 1.0f;
								}

								//Hue Correction
								hsl_H %= 360.0f;
								if (hsl_H < 0.0f) {
									hsl_H += 360.0f;
								}

								float cmax, cmin;
								if (hsl_L <= 0.5f) {
									cmin = hsl_L * (1.0f - hsl_S);
									cmax = 2.0f*hsl_L - cmin;
								} else {
									cmax = hsl_L * (1.0f - hsl_S) + hsl_S;
									cmin = 2.0f*hsl_L - cmax;
								}

								//Red
								float tmph = (hsl_H + 120.0f) % 360.0f;
								if (tmph < 60.0f) {
									dr = cmin + (((cmax - cmin) * tmph) / 60.0f);
								} else if (tmph < 180.0f) {
									dr = cmax;
								} else if (tmph < 240.0f) {
									dr = cmin + (((cmax - cmin) * (240.0f - tmph)) / 60.0f);
								} else {
									dr = cmin;
								}

								//Green
								if (hsl_H < 60.0f) {
									dg = cmin + (((cmax - cmin) * hsl_H) / 60.0f);
								} else if (hsl_H < 180.0f) {
									dg = cmax;
								} else if (hsl_H < 240.0f) {
									dg = cmin + (((cmax - cmin) * (240.0f - hsl_H)) / 60.0f);
								} else {
									dg = cmin;
								}

								//Blue
								tmph = hsl_H - 120.0f;
								if (tmph < 0.0f) {
									tmph += 360.0f;
								}
								if (tmph < 60.0f) {
									db = cmin + (((cmax - cmin) * tmph) / 60.0f);
								} else if (tmph < 180.0f) {
									db = cmax;
								} else if (tmph < 240.0f) {
									db = cmin + (((cmax - cmin) * (240.0f - tmph)) / 60.0f);
								} else {
									db = cmin;
								}

								if (shusendo < 0.0f) {
									lastDR = (int) (255.0f * dr);
									lastDG = (int) (255.0f * dg);
									lastDB = (int) (255.0f * db);
								} else {
									lastDR = (int) ((sr * shusendo) + (255.0f * dr * (1.0f - shusendo)));
									lastDG = (int) ((sg * shusendo) + (255.0f * dg * (1.0f - shusendo)));
									lastDB = (int) ((sb * shusendo) + (255.0f * db * (1.0f - shusendo)));
								}
								imageDataInt[off] = ((a << 24) & 0xff000000) | ((lastDR << 16) & 0xff0000) | ((lastDG << 8) & 0xff00) | (lastDB & 0xff);
							}
						}
					}
				}
			}
		}
	}
}