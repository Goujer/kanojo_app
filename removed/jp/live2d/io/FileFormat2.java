package jp.live2d.io;

import jp.live2d.Def;
import jp.live2d.avatar.AvatarPartsItem;
import jp.live2d.base.AffineEnt;
import jp.live2d.base.BDAffine;
import jp.live2d.base.BDBoxGrid;
import jp.live2d.draw.DDTexture;
import jp.live2d.model.ModelImpl;
import jp.live2d.model.PartsData;
import jp.live2d.param.ParamDefFloat;
import jp.live2d.param.ParamDefSet;
import jp.live2d.param.ParamPivots;
import jp.live2d.param.PivotManager;

public class FileFormat2 {
    public static final int ARRAY_NO = 23;
    public static final int LIVE2D_FORMAT_EOF_VALUE = -2004318072;
    public static final int LIVE2D_FORMAT_VERSION_AVAILABLE = 9;
    public static final int LIVE2D_FORMAT_VERSION_V2_6_INTIAL = 6;
    public static final int LIVE2D_FORMAT_VERSION_V2_7_OPACITY = 7;
    public static final int LIVE2D_FORMAT_VERSION_V2_8_TEX_OPTION = 8;
    public static final int LIVE2D_FORMAT_VERSION_V2_9_AVATAR_PARTS = 9;
    public static final int NULL_NO = 0;
    public static final int OBJECT_REF = 33;

    static void skipClass(int classNo) {
        System.out.printf("FileFormat1 :: not implemented classNo : %d\n", new Object[]{Integer.valueOf(classNo)});
    }

    public static Object newInstance(int classNo) {
        if (classNo < 40) {
            skipClass(classNo);
            return null;
        } else if (classNo < 50) {
            skipClass(classNo);
            return null;
        } else if (classNo < 60) {
            skipClass(classNo);
            return null;
        } else if (classNo < 100) {
            switch (classNo) {
                case Def.PIVOT_TABLE_SIZE:
                    return new BDBoxGrid();
                case 66:
                    return new PivotManager();
                case 67:
                    return new ParamPivots();
                case 68:
                    return new BDAffine();
                case 69:
                    return new AffineEnt();
                case 70:
                    return new DDTexture();
                default:
                    skipClass(classNo);
                    return null;
            }
        } else {
            if (classNo < 150) {
                switch (classNo) {
                    case 131:
                        return new ParamDefFloat();
                    case 133:
                        return new PartsData();
                    case 136:
                        return new ModelImpl();
                    case 137:
                        return new ParamDefSet();
                    case 142:
                        return new AvatarPartsItem();
                }
            }
            skipClass(classNo);
            return null;
        }
    }
}
