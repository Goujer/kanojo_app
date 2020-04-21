package jp.live2d.util;

public class UtSystem {
    public static final long USER_TIME_AUTO = 0;
    static long userTimeMSec = 0;

    public static boolean isBigEndian() {
        return true;
    }

    public static void sleepMSec(long msec) {
        try {
            Thread.sleep(msec);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static long getUserTimeMSec() {
        return userTimeMSec == 0 ? getSystemTimeMSec() : userTimeMSec;
    }

    public static void setUserTimeMSec(long userTime) {
        userTimeMSec = userTime;
    }

    public static long updateUserTimeMSec() {
        long systemTimeMSec = getSystemTimeMSec();
        userTimeMSec = systemTimeMSec;
        return systemTimeMSec;
    }

    public static long getTimeMSec() {
        return System.nanoTime() / 1000000;
    }

    public static long getSystemTimeMSec() {
        return System.nanoTime() / 1000000;
    }

    public static void exit(int code) {
        System.exit(code);
    }
}
