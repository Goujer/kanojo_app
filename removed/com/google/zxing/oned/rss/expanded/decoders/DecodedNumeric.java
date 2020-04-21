package com.google.zxing.oned.rss.expanded.decoders;

final class DecodedNumeric extends DecodedObject {
    static final int FNC1 = 10;
    private final int firstDigit;
    private final int secondDigit;

    DecodedNumeric(int newPosition, int firstDigit2, int secondDigit2) {
        super(newPosition);
        this.firstDigit = firstDigit2;
        this.secondDigit = secondDigit2;
        if (this.firstDigit < 0 || this.firstDigit > 10) {
            throw new IllegalArgumentException("Invalid firstDigit: " + firstDigit2);
        } else if (this.secondDigit < 0 || this.secondDigit > 10) {
            throw new IllegalArgumentException("Invalid secondDigit: " + secondDigit2);
        }
    }

    /* access modifiers changed from: package-private */
    public int getFirstDigit() {
        return this.firstDigit;
    }

    /* access modifiers changed from: package-private */
    public int getSecondDigit() {
        return this.secondDigit;
    }

    /* access modifiers changed from: package-private */
    public int getValue() {
        return (this.firstDigit * 10) + this.secondDigit;
    }

    /* access modifiers changed from: package-private */
    public boolean isFirstDigitFNC1() {
        return this.firstDigit == 10;
    }

    /* access modifiers changed from: package-private */
    public boolean isSecondDigitFNC1() {
        return this.secondDigit == 10;
    }

    /* access modifiers changed from: package-private */
    public boolean isAnyFNC1() {
        return this.firstDigit == 10 || this.secondDigit == 10;
    }
}
