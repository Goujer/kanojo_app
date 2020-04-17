package com.google.zxing;

import android.support.v4.view.MotionEventCompat;

public final class RGBLuminanceSource extends LuminanceSource {
    private final int dataHeight;
    private final int dataWidth;
    private final int left;
    private final byte[] luminances;
    private final int top;

    public RGBLuminanceSource(int width, int height, int[] pixels) {
        super(width, height);
        this.dataWidth = width;
        this.dataHeight = height;
        this.left = 0;
        this.top = 0;
        this.luminances = new byte[(width * height)];
        for (int y = 0; y < height; y++) {
            int offset = y * width;
            for (int x = 0; x < width; x++) {
                int pixel = pixels[offset + x];
                int r = (pixel >> 16) & MotionEventCompat.ACTION_MASK;
                int g = (pixel >> 8) & MotionEventCompat.ACTION_MASK;
                int b = pixel & MotionEventCompat.ACTION_MASK;
                if (r == g && g == b) {
                    this.luminances[offset + x] = (byte) r;
                } else {
                    this.luminances[offset + x] = (byte) ((((r + g) + g) + b) >> 2);
                }
            }
        }
    }

    private RGBLuminanceSource(byte[] pixels, int dataWidth2, int dataHeight2, int left2, int top2, int width, int height) {
        super(width, height);
        if (left2 + width > dataWidth2 || top2 + height > dataHeight2) {
            throw new IllegalArgumentException("Crop rectangle does not fit within image data.");
        }
        this.luminances = pixels;
        this.dataWidth = dataWidth2;
        this.dataHeight = dataHeight2;
        this.left = left2;
        this.top = top2;
    }

    public byte[] getRow(int y, byte[] row) {
        if (y < 0 || y >= getHeight()) {
            throw new IllegalArgumentException("Requested row is outside the image: " + y);
        }
        int width = getWidth();
        if (row == null || row.length < width) {
            row = new byte[width];
        }
        System.arraycopy(this.luminances, ((this.top + y) * this.dataWidth) + this.left, row, 0, width);
        return row;
    }

    public byte[] getMatrix() {
        int width = getWidth();
        int height = getHeight();
        if (width == this.dataWidth && height == this.dataHeight) {
            return this.luminances;
        }
        int area = width * height;
        byte[] matrix = new byte[area];
        int inputOffset = (this.top * this.dataWidth) + this.left;
        if (width == this.dataWidth) {
            System.arraycopy(this.luminances, inputOffset, matrix, 0, area);
            return matrix;
        }
        byte[] rgb = this.luminances;
        for (int y = 0; y < height; y++) {
            System.arraycopy(rgb, inputOffset, matrix, y * width, width);
            inputOffset += this.dataWidth;
        }
        return matrix;
    }

    public boolean isCropSupported() {
        return true;
    }

    public LuminanceSource crop(int left2, int top2, int width, int height) {
        return new RGBLuminanceSource(this.luminances, this.dataWidth, this.dataHeight, this.left + left2, this.top + top2, width, height);
    }
}
