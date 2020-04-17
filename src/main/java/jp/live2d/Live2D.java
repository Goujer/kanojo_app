package jp.live2d;

import jp.live2d.id.ID;

public class Live2D {
    public static boolean L2D_DEBUG = true;
    public static boolean L2D_DEBUG_IMPORT = false;
    public static boolean L2D_FORCE_UPDATE = false;
    public static boolean L2D_INVERT_TEXTURE = false;
    public static boolean L2D_OUTSIDE_PARAM_AVAILABLE = false;
    public static boolean L2D_RANGE_CHECK = true;
    public static boolean L2D_RANGE_CHECK_POINT = true;
    public static boolean L2D_SAMPLE = true;
    public static boolean L2D_TEMPORARY_DEBUG = true;
    public static boolean L2D_VERBOSE = true;
    public static final int __L2D_VERSION_NO__ = 817;
    public static final String __L2D_VERSION_STR__ = "0.8.17";
    static boolean first_init_flag = true;

    public static void init() {
        if (first_init_flag) {
            System.out.printf("Live2D version %s ", new Object[]{__L2D_VERSION_STR__});
            first_init_flag = false;
            System.out.printf("for Java\n", new Object[0]);
            if (1 == 0) {
                System.out.printf("\n===========================================================\n", new Object[0]);
                System.out.printf("COMPILE TARGET NOT DEFINED!! @ Live2D.h/cpp\n\n", new Object[0]);
                System.out.printf("\t#define L2D_TARGET_PSP\n", new Object[0]);
                System.out.printf("\t#define L2D_TARGET_IPHONE\n", new Object[0]);
                System.out.printf("\t#define L2D_TARGET_QT\n", new Object[0]);
                System.out.printf("===========================================================\n", new Object[0]);
            }
        }
    }

    public void dispose() {
        ID.releaseStored_notForClientCall();
    }

    public static String getVersionStr() {
        return __L2D_VERSION_STR__;
    }

    public static int getVersionNo() {
        return __L2D_VERSION_NO__;
    }
}
