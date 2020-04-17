package jp.live2d.util;

import org.apache.james.mime4j.field.datetime.parser.DateTimeParserConstants;

public class UtString {
    public static boolean startsWith(byte[] s, int start, String word) {
        int len = start + word.length();
        if (len >= s.length) {
            return false;
        }
        for (int i = start; i < len; i++) {
            if (s[i] != word.charAt(i - start)) {
                return false;
            }
        }
        return true;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:27:0x0087, code lost:
        r3 = r3 * 0.1d;
        r1 = r1 + 1;
     */
    public static double strToDouble(byte[] str, int len, int pos, int[] ret_endpos) {
        int i = pos;
        boolean minus = false;
        boolean period = false;
        double v1 = 0.0d;
        if (((char) (str[i] & 255)) == '-') {
            minus = true;
            i++;
        }
        while (true) {
            if (i < len) {
                switch ((char) (str[i] & 255)) {
                    case DateTimeParserConstants.DIGITS /*46*/:
                        period = true;
                        i++;
                        break;
                    case DateTimeParserConstants.ANY /*48*/:
                        v1 *= 10.0d;
                        continue;
                    case '1':
                        v1 = (10.0d * v1) + 1.0d;
                        continue;
                    case '2':
                        v1 = (10.0d * v1) + 2.0d;
                        continue;
                    case '3':
                        v1 = (10.0d * v1) + 3.0d;
                        continue;
                    case '4':
                        v1 = (10.0d * v1) + 4.0d;
                        continue;
                    case '5':
                        v1 = (10.0d * v1) + 5.0d;
                        continue;
                    case '6':
                        v1 = (10.0d * v1) + 6.0d;
                        continue;
                    case '7':
                        v1 = (10.0d * v1) + 7.0d;
                        continue;
                    case '8':
                        v1 = (10.0d * v1) + 8.0d;
                        continue;
                    case '9':
                        v1 = (10.0d * v1) + 9.0d;
                        continue;
                }
            }
            i++;
        }
        if (period) {
            double mul = 0.1d;
            while (i < len) {
                switch ((char) (str[i] & 255)) {
                    case DateTimeParserConstants.ANY /*48*/:
                        break;
                    case '1':
                        v1 += 1.0d * mul;
                        continue;
                    case '2':
                        v1 += 2.0d * mul;
                        continue;
                    case '3':
                        v1 += 3.0d * mul;
                        continue;
                    case '4':
                        v1 += 4.0d * mul;
                        continue;
                    case '5':
                        v1 += 5.0d * mul;
                        continue;
                    case '6':
                        v1 += 6.0d * mul;
                        continue;
                    case '7':
                        v1 += 7.0d * mul;
                        continue;
                    case '8':
                        v1 += 8.0d * mul;
                        continue;
                    case '9':
                        v1 += 9.0d * mul;
                        continue;
                }
            }
        }
        if (minus) {
            v1 = -v1;
        }
        ret_endpos[0] = i;
        return v1;
    }
}
