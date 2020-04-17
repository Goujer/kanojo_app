package twitter4j.internal.http;

public final class BASE64Encoder {
    private static final char[] encodeTable = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/'};
    private static final char last2byte = ((char) Integer.parseInt("00000011", 2));
    private static final char last4byte = ((char) Integer.parseInt("00001111", 2));
    private static final char last6byte = ((char) Integer.parseInt("00111111", 2));
    private static final char lead2byte = ((char) Integer.parseInt("11000000", 2));
    private static final char lead4byte = ((char) Integer.parseInt("11110000", 2));
    private static final char lead6byte = ((char) Integer.parseInt("11111100", 2));

    private BASE64Encoder() {
    }

    public static String encode(byte[] from) {
        StringBuilder to = new StringBuilder(((int) (((double) from.length) * 1.34d)) + 3);
        int num = 0;
        char currentByte = 0;
        for (int i = 0; i < from.length; i++) {
            num %= 8;
            while (num < 8) {
                switch (num) {
                    case 0:
                        currentByte = (char) (((char) (from[i] & lead6byte)) >>> 2);
                        break;
                    case 2:
                        currentByte = (char) (from[i] & last6byte);
                        break;
                    case 4:
                        currentByte = (char) (((char) (from[i] & last4byte)) << 2);
                        if (i + 1 >= from.length) {
                            break;
                        } else {
                            currentByte = (char) (((from[i + 1] & lead2byte) >>> 6) | currentByte);
                            break;
                        }
                    case 6:
                        currentByte = (char) (((char) (from[i] & last2byte)) << 4);
                        if (i + 1 >= from.length) {
                            break;
                        } else {
                            currentByte = (char) (((from[i + 1] & lead4byte) >>> 4) | currentByte);
                            break;
                        }
                }
                to.append(encodeTable[currentByte]);
                num += 6;
            }
        }
        if (to.length() % 4 != 0) {
            for (int i2 = 4 - (to.length() % 4); i2 > 0; i2--) {
                to.append("=");
            }
        }
        return to.toString();
    }
}
