package jp.live2d.util;

public class UtArray {
    public void dumpPoints(float[] array, int w, int h) {
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int index = ((y * w) + x) * 2;
                System.out.printf("(% 7.3f , % 7.3f) , ", new Object[]{Float.valueOf(array[index]), Float.valueOf(array[index + 1])});
            }
            System.out.printf("\n", new Object[0]);
        }
        System.out.printf("\n", new Object[0]);
    }
}
