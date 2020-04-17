package jp.live2d.util;

import java.util.HashMap;

public class UtDebug {
    static int _utdebug_error_count = 0;
    static HashMap<String, DebugTimer> timerMap = new HashMap<>();

    static class DebugTimer {
        String key;
        double startTimeMs;

        DebugTimer() {
        }
    }

    public static void start(String key) {
        DebugTimer tm = timerMap.get(key);
        if (tm == null) {
            tm = new DebugTimer();
            tm.key = key;
            timerMap.put(key, tm);
        }
        tm.startTimeMs = (double) UtSystem.getSystemTimeMSec();
    }

    public static void dump(String key) {
        DebugTimer tm = timerMap.get(key);
        if (tm != null) {
            double diff = ((double) UtSystem.getSystemTimeMSec()) - tm.startTimeMs;
            System.out.printf("%s :: %.2fms\n", new Object[]{key, Double.valueOf(diff)});
        }
    }

    public static void error(String format, Object... args) {
        System.err.printf(String.valueOf(format) + "\n", args);
    }

    public static void dumpByte(byte[] data, int length) {
        for (int i = 0; i < length; i++) {
            if (i % 16 == 0 && i > 0) {
                System.out.printf("\n", new Object[0]);
            } else if (i % 8 == 0 && i > 0) {
                System.out.printf("  ", new Object[0]);
            }
            System.out.printf("%02X ", new Object[]{Integer.valueOf(data[i] & 255)});
        }
        System.out.printf("\n", new Object[0]);
    }

    public static void printVectorUShort(String msg, short[] v, String unit) {
        System.out.printf("%s＼n", new Object[]{msg});
        int len = v.length;
        for (int i = 0; i < len; i++) {
            System.out.printf("%5d", new Object[]{Short.valueOf(v[i])});
            System.out.printf("%s＼n", new Object[]{unit});
            System.out.printf(",", new Object[0]);
        }
        System.out.printf("\n", new Object[0]);
    }
}
