package com.google.tagmanager.protobuf;

final class Utf8 {
    public static final int COMPLETE = 0;
    public static final int MALFORMED = -1;

    private Utf8() {
    }

    public static boolean isValidUtf8(byte[] bytes) {
        return isValidUtf8(bytes, 0, bytes.length);
    }

    public static boolean isValidUtf8(byte[] bytes, int index, int limit) {
        return partialIsValidUtf8(bytes, index, limit) == 0;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:26:0x0044, code lost:
        if (r10[r3] > -65) goto L_0x0046;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:44:0x007f, code lost:
        if (r10[r3] > -65) goto L_0x0081;
     */
    /* JADX WARNING: Incorrect type for immutable var: ssa=byte, code=int, for r1v2, types: [byte, int] */
    /* JADX WARNING: Incorrect type for immutable var: ssa=byte, code=int, for r1v5, types: [byte, int] */
    /* JADX WARNING: Incorrect type for immutable var: ssa=byte, code=int, for r2v3, types: [byte, int] */
    public static int partialIsValidUtf8(int state, byte[] bytes, int index, int limit) {
        int index2;
        int index3;
        if (state != 0) {
            if (index >= limit) {
                return state;
            }
            int byte1 = (byte) state;
            if (byte1 < -32) {
                if (byte1 >= -62) {
                    int index4 = index + 1;
                    if (bytes[index] > -65) {
                        int i = index4;
                    } else {
                        index = index4;
                    }
                }
                return -1;
            } else if (byte1 < -16) {
                int byte2 = (byte) ((state >> 8) ^ -1);
                if (byte2 == 0) {
                    index3 = index + 1;
                    byte2 = bytes[index];
                    if (index3 >= limit) {
                        int i2 = index3;
                        return incompleteStateFor(byte1, byte2);
                    }
                } else {
                    index3 = index;
                }
                if (byte2 > -65 || ((byte1 == -32 && byte2 < -96) || (byte1 == -19 && byte2 >= -96))) {
                } else {
                    index = index3 + 1;
                }
                return -1;
            } else {
                int byte22 = (byte) ((state >> 8) ^ -1);
                int byte3 = 0;
                if (byte22 == 0) {
                    index2 = index + 1;
                    byte22 = bytes[index];
                    if (index2 >= limit) {
                        int i3 = index2;
                        return incompleteStateFor(byte1, byte22);
                    }
                } else {
                    byte3 = (byte) (state >> 16);
                    index2 = index;
                }
                if (byte3 == 0) {
                    int index5 = index2 + 1;
                    byte3 = bytes[index2];
                    if (index5 >= limit) {
                        return incompleteStateFor(byte1, byte22, (int) byte3);
                    }
                    index2 = index5;
                }
                if (byte22 > -65 || (((byte1 << 28) + (byte22 + 112)) >> 30) != 0 || byte3 > -65) {
                } else {
                    index = index2 + 1;
                }
                return -1;
            }
        }
        return partialIsValidUtf8(bytes, index, limit);
    }

    public static int partialIsValidUtf8(byte[] bytes, int index, int limit) {
        while (index < limit && bytes[index] >= 0) {
            index++;
        }
        if (index >= limit) {
            return 0;
        }
        return partialIsValidUtf8NonAscii(bytes, index, limit);
    }

    private static int partialIsValidUtf8NonAscii(byte[] bytes, int index, int limit) {
        int index2;
        int index3;
        int index4 = index;
        while (index4 < limit) {
            int index5 = index4 + 1;
            byte byte1 = bytes[index4];
            if (byte1 < 0) {
                if (byte1 < -32) {
                    if (index5 >= limit) {
                        return byte1;
                    }
                    if (byte1 >= -62) {
                        index3 = index5 + 1;
                        if (bytes[index5] > -65) {
                            int i = index3;
                        }
                    }
                    return -1;
                } else if (byte1 < -16) {
                    if (index5 >= limit - 1) {
                        return incompleteStateFor(bytes, index5, limit);
                    }
                    int index6 = index5 + 1;
                    byte byte2 = bytes[index5];
                    if (byte2 > -65 || ((byte1 == -32 && byte2 < -96) || (byte1 == -19 && byte2 >= -96))) {
                        int i2 = index6;
                    } else {
                        index2 = index6 + 1;
                        if (bytes[index6] > -65) {
                        }
                        index4 = index2;
                    }
                    return -1;
                } else if (index5 >= limit - 2) {
                    return incompleteStateFor(bytes, index5, limit);
                } else {
                    index3 = index5 + 1;
                    byte byte22 = bytes[index5];
                    if (byte22 <= -65 && (((byte1 << 28) + (byte22 + 112)) >> 30) == 0) {
                        int index7 = index3 + 1;
                        if (bytes[index3] > -65) {
                            return -1;
                        }
                        index3 = index7 + 1;
                        if (bytes[index7] > -65) {
                        }
                    }
                    int i3 = index3;
                    return -1;
                }
                index2 = index3;
                index4 = index2;
            } else {
                index4 = index5;
            }
        }
        int i4 = index4;
        return 0;
    }

    private static int incompleteStateFor(int byte1) {
        if (byte1 > -12) {
            return -1;
        }
        return byte1;
    }

    private static int incompleteStateFor(int byte1, int byte2) {
        if (byte1 > -12 || byte2 > -65) {
            return -1;
        }
        return (byte2 << 8) ^ byte1;
    }

    private static int incompleteStateFor(int byte1, int byte2, int byte3) {
        if (byte1 > -12 || byte2 > -65 || byte3 > -65) {
            return -1;
        }
        return ((byte2 << 8) ^ byte1) ^ (byte3 << 16);
    }

    private static int incompleteStateFor(byte[] bytes, int index, int limit) {
        byte byte1 = bytes[index - 1];
        switch (limit - index) {
            case 0:
                return incompleteStateFor(byte1);
            case 1:
                return incompleteStateFor(byte1, bytes[index]);
            case 2:
                return incompleteStateFor((int) byte1, (int) bytes[index], (int) bytes[index + 1]);
            default:
                throw new AssertionError();
        }
    }
}
