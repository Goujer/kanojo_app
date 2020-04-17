package jp.co.cybird.barcodekanojoForGAM.live2d;

import java.util.ArrayList;
import jp.co.cybird.barcodekanojoForGAM.live2d.model.ColorConvert;
import jp.live2d.util.UtDebug;

public class KanojoSetting {
    public static final String COLOR_01_CLOTHES_A = "COLOR_01_CLOTHES_A";
    public static final String COLOR_01_CLOTHES_B = "COLOR_01_CLOTHES_B";
    public static final String COLOR_01_EYE = "COLOR_01_EYE";
    public static final String COLOR_01_HAIR = "COLOR_01_HAIR";
    public static final String COLOR_01_SKIN = "COLOR_01_SKIN";
    static final int DEFAULT_LOVE_GAGE = 75;
    public static final String FEATURE_01_BROW_POS = "FEATURE_01_BROW_POS";
    public static final String FEATURE_01_EYE_POS = "FEATURE_01_EYE_POS";
    public static final String FEATURE_01_MOUTH_POS = "FEATURE_01_MOUTH_POS";
    public static final int KANOJO_STATE_FRIEND = 3;
    public static final int KANOJO_STATE_KANOJO = 2;
    public static final int KANOJO_STATE_TANIN = 1;
    public static final int MAX_COLOR_CONVERT_CLOTHES_AB = 6;
    public static final int MAX_COLOR_CONVERT_EYE = 12;
    public static final int MAX_COLOR_CONVERT_HAIR = 24;
    public static final int MAX_COLOR_CONVERT_SKIN = 12;
    public static final int MAX_PARTS_01_BODY = 1;
    public static final int MAX_PARTS_01_CORE = 1;
    public static final int OPTION_FLAG_COLOR_CONVERT_CLOTHES_1 = 4;
    public static final int OPTION_FLAG_COLOR_CONVERT_CLOTHES_2 = 5;
    public static final int OPTION_FLAG_COLOR_CONVERT_CLOTHES_3 = 6;
    public static final int OPTION_FLAG_COLOR_CONVERT_CLOTHES_4 = 7;
    public static final int OPTION_FLAG_COLOR_CONVERT_CLOTHES_5A = 8;
    public static final int OPTION_FLAG_COLOR_CONVERT_CLOTHES_5B = 9;
    public static final int OPTION_FLAG_COLOR_CONVERT_EYE = 3;
    public static final int OPTION_FLAG_COLOR_CONVERT_HAIR = 1;
    public static final int OPTION_FLAG_COLOR_CONVERT_NONE = 0;
    public static final int OPTION_FLAG_COLOR_CONVERT_SKIN = 2;
    public static final String PARTS_01_ACCESSORY = "PARTS_01_ACCESSORY";
    public static final String PARTS_01_BODY = "PARTS_01_BODY";
    public static final String PARTS_01_BROW = "PARTS_01_BROW";
    public static final String PARTS_01_CLOTHES = "PARTS_01_CLOTHES";
    public static final String PARTS_01_CORE = "PARTS_01_CORE";
    public static final String PARTS_01_EAR = "PARTS_01_EAR";
    public static final String PARTS_01_EYE = "PARTS_01_EYE";
    public static final String PARTS_01_FACE = "PARTS_01_FACE";
    public static final String PARTS_01_FRINGE = "PARTS_01_FRINGE";
    public static final String PARTS_01_GLASSES = "PARTS_01_GLASSES";
    public static final String PARTS_01_HAIR = "PARTS_01_HAIR";
    public static final String PARTS_01_MOUTH = "PARTS_01_MOUTH";
    public static final String PARTS_01_NOSE = "PARTS_01_NOSE";
    public static final String PARTS_01_OPTION = "PARTS_01_OPTION";
    public static final String PARTS_01_SPOT = "PARTS_01_SPOT";
    ColorConvert[] CLOTHES_A_1_CONVERT = {new ColorConvert(0.0f, -0.0f, -0.0f), new ColorConvert(27.0f, -0.0f, 0.06f), new ColorConvert(98.0f, -0.15f, 0.07f), new ColorConvert(-63.0f, -0.21f, 0.04f), new ColorConvert(12.0f, -0.23f, 0.17f), new ColorConvert(0.0f, -0.24f, -0.23f)};
    ColorConvert[] CLOTHES_A_2_CONVERT = {new ColorConvert(0.0f, -0.0f, -0.0f), new ColorConvert(43.0f, -0.05f, -0.18f), new ColorConvert(-24.0f, -0.23f, -0.32f), new ColorConvert(-96.0f, -0.26f, -0.42f), new ColorConvert(5.0f, 0.12f, 0.49f), new ColorConvert(-113.0f, -0.06f, 0.38f)};
    ColorConvert[] CLOTHES_A_3_CONVERT = {new ColorConvert(0.0f, 0.01f, -0.0f), new ColorConvert(-100.0f, -0.03f, 0.01f), new ColorConvert(-44.0f, -0.0f, -0.0f), new ColorConvert(155.0f, 0.02f, -0.01f), new ColorConvert(79.0f, -0.0f, -0.0f), new ColorConvert(-1.0f, -0.06f, 0.03f)};
    ColorConvert[] CLOTHES_A_4_CONVERT = {new ColorConvert(0.0f, 0.01f, -0.0f), new ColorConvert(-50.0f, -0.24f, -0.05f), new ColorConvert(36.0f, -0.04f, -0.0f), new ColorConvert(-163.0f, -0.12f, 0.06f), new ColorConvert(28.0f, -0.0f, 0.19f), new ColorConvert(8.0f, -0.13f, -0.42f)};
    ColorConvert[] CLOTHES_A_5_CONVERT = {new ColorConvert(1.0f, -0.0f, -0.0f), new ColorConvert(76.0f, -0.09f, -0.51f), new ColorConvert(-24.0f, -0.0f, -0.45f), new ColorConvert(-5.0f, -0.11f, -0.92f), new ColorConvert(0.0f, -0.11f, -0.3f), new ColorConvert(18.0f, -0.06f, 0.2f)};
    ColorConvert[] EYE_CONVERT = {new ColorConvert(0.0f, 0.0f, 0.0f), new ColorConvert(33.0f, -0.06f, 0.2f), new ColorConvert(155.0f, -0.05f, 0.0f), new ColorConvert(5.0f, -0.17f, -0.02f), new ColorConvert(12.0f, 0.06f, 0.15f), new ColorConvert(-176.0f, 0.02f, 0.09f), new ColorConvert(-6.0f, -0.26f, 0.14f), new ColorConvert(10.0f, 0.22f, 0.01f), new ColorConvert(-143.0f, -0.12f, 0.05f), new ColorConvert(0.0f, -0.33f, -0.05f), new ColorConvert(-12.0f, 0.05f, 0.17f), new ColorConvert(-18.0f, -0.15f, 0.19f)};
    ColorConvert[] HAIR_CONVERT = {new ColorConvert(1.0f, 0.19f, 0.28f), new ColorConvert(27.0f, 0.3f, 0.64f), new ColorConvert(7.0f, -0.09f, 0.52f), new ColorConvert(7.0f, 0.01f, 0.51f), new ColorConvert(21.0f, 0.26f, 0.22f), new ColorConvert(-9.0f, 0.29f, 0.71f), new ColorConvert(-56.0f, 0.01f, 0.02f), new ColorConvert(21.0f, 1.0f, 0.91f), new ColorConvert(0.0f, 0.0f, 0.0f), new ColorConvert(12.0f, 0.26f, 0.23f), new ColorConvert(7.0f, 0.0f, 0.19f), new ColorConvert(8.0f, 0.09f, 0.24f), new ColorConvert(21.0f, 0.0f, 0.0f), new ColorConvert(180.0f, 0.17f, 0.64f), new ColorConvert(21.0f, 0.51f, 0.69f), new ColorConvert(10.0f, -0.14f, 0.96f), new ColorConvert(0.0f, -0.08f, -0.35f), new ColorConvert(4.0f, 0.17f, 0.0f), new ColorConvert(26.0f, -0.12f, -0.46f), new ColorConvert(3.0f, 0.0f, -0.31f), new ColorConvert(-25.0f, -0.1f, -0.42f), new ColorConvert(163.0f, 0.1f, 0.16f), new ColorConvert(97.0f, 0.1f, 0.55f), new ColorConvert(-9.0f, 0.11f, 0.9f)};
    ColorConvert[] SKIN_CONVERT = {new ColorConvert(0.0f, 0.0f, 0.0f), new ColorConvert(3.0f, 0.16f, 0.16f), new ColorConvert(0.0f, 0.04f, -0.22f), new ColorConvert(-5.0f, -0.05f, -0.55f), new ColorConvert(0.0f, -0.09f, -0.91f), new ColorConvert(-160.0f, -0.07f, -0.46f), new ColorConvert(-5.0f, -0.01f, 0.07f), new ColorConvert(-6.0f, -0.01f, 0.2f), new ColorConvert(-9.0f, 0.0f, -0.21f), new ColorConvert(12.0f, -0.01f, 0.11f), new ColorConvert(8.0f, 0.09f, 0.0f), new ColorConvert(0.0f, 0.0f, -0.62f)};
    ArrayList<ColorSet> colorSetList = new ArrayList<>();
    ArrayList<FeatureSet> featureSetList = new ArrayList<>();
    KanojoLive2D kanojoLive2D;
    int kanojoState;
    double loveGage = 75.0d;
    ArrayList<PartsSet> partsSetList = new ArrayList<>();
    boolean silhouetteMode = false;

    public class PartsSet {
        public String partsID;
        public int partsItemNo;

        public PartsSet() {
        }
    }

    public class ColorSet {
        public String colorID;
        public int colorNo;

        public ColorSet() {
        }
    }

    public class FeatureSet {
        public String featureID;
        public float featureValue;

        public FeatureSet() {
        }
    }

    KanojoSetting() {
        this.partsSetList.add(pset(PARTS_01_BODY, 1));
        this.partsSetList.add(pset(PARTS_01_FACE, 1));
        this.partsSetList.add(pset(PARTS_01_EYE, 1));
        this.partsSetList.add(pset(PARTS_01_BROW, 1));
        this.partsSetList.add(pset(PARTS_01_MOUTH, 1));
        this.partsSetList.add(pset(PARTS_01_NOSE, 1));
        this.partsSetList.add(pset(PARTS_01_EAR, 1));
        this.partsSetList.add(pset(PARTS_01_FRINGE, 1));
        this.partsSetList.add(pset(PARTS_01_HAIR, 1));
        this.partsSetList.add(pset(PARTS_01_CLOTHES, 1));
        this.partsSetList.add(pset(PARTS_01_GLASSES, 1));
        this.partsSetList.add(pset(PARTS_01_ACCESSORY, 1));
        this.partsSetList.add(pset(PARTS_01_SPOT, 1));
        this.partsSetList.add(pset(PARTS_01_OPTION, 1));
        this.colorSetList.add(cset(COLOR_01_SKIN, 1));
        this.colorSetList.add(cset(COLOR_01_HAIR, 1));
        this.colorSetList.add(cset(COLOR_01_EYE, 1));
        this.colorSetList.add(cset("COLOR_01_CLOTHES_A", 1));
        this.colorSetList.add(cset(COLOR_01_CLOTHES_B, 1));
        this.featureSetList.add(fset(FEATURE_01_EYE_POS, 0));
        this.featureSetList.add(fset(FEATURE_01_BROW_POS, 0));
        this.featureSetList.add(fset(FEATURE_01_MOUTH_POS, 0));
    }

    private PartsSet pset(String partsID, int partsItemNo) {
        PartsSet ret = new PartsSet();
        ret.partsID = partsID;
        ret.partsItemNo = partsItemNo;
        return ret;
    }

    private ColorSet cset(String _colorID, int _colorNo) {
        ColorSet ret = new ColorSet();
        ret.colorID = _colorID;
        ret.colorNo = _colorNo;
        return ret;
    }

    private FeatureSet fset(String featureID, int featureValue) {
        FeatureSet ret = new FeatureSet();
        ret.featureID = featureID;
        ret.featureValue = (float) featureValue;
        return ret;
    }

    public static KanojoSetting createSetting_notForClientCall(KanojoLive2D ka) {
        KanojoSetting ret = new KanojoSetting();
        ret.kanojoLive2D = ka;
        return ret;
    }

    public ColorConvert getColorConvert(String colorID, int colorType) {
        int cno = getColor(colorID);
        if (cno < 1) {
            cno = 1;
        }
        if (colorID == COLOR_01_SKIN) {
            if (cno > 12) {
                cno = 1;
            }
            return this.SKIN_CONVERT[cno - 1];
        } else if (colorID == COLOR_01_HAIR) {
            if (cno > 24) {
                cno = 1;
            }
            return this.HAIR_CONVERT[cno - 1];
        } else if (colorID == COLOR_01_EYE) {
            if (cno > 12) {
                cno = 1;
            }
            return this.EYE_CONVERT[cno - 1];
        } else if (colorID != "COLOR_01_CLOTHES_A") {
            return null;
        } else {
            if (cno > 6) {
                cno = 1;
            }
            switch (colorType) {
                case 4:
                    return this.CLOTHES_A_1_CONVERT[cno - 1];
                case 5:
                    return this.CLOTHES_A_2_CONVERT[cno - 1];
                case 6:
                    return this.CLOTHES_A_3_CONVERT[cno - 1];
                case 7:
                    return this.CLOTHES_A_4_CONVERT[cno - 1];
                case 9:
                    return this.CLOTHES_A_5_CONVERT[cno - 1];
                default:
                    return null;
            }
        }
    }

    public void setParts(String _partsID, int partsItemNo) {
        if (partsItemNo < 1) {
            UtDebug.error("illegal argment @KanojoSetting#setParts()/ id : %s / partsItemNo : %d\n", _partsID, Integer.valueOf(partsItemNo));
            return;
        }
        for (int i = this.partsSetList.size() - 1; i >= 0; i--) {
            if (_partsID == this.partsSetList.get(i).partsID) {
                this.partsSetList.get(i).partsItemNo = partsItemNo;
                return;
            }
        }
        UtDebug.error("Live2D Error . no partsID @ KanojoSetting#setParts() ", new Object[0]);
    }

    public ArrayList<PartsSet> getPartsSetList() {
        return this.partsSetList;
    }

    public void setColor(String _colorID, int colorNo) {
        for (int i = this.colorSetList.size() - 1; i >= 0; i--) {
            if (_colorID == this.colorSetList.get(i).colorID) {
                this.colorSetList.get(i).colorNo = colorNo;
                return;
            }
        }
        UtDebug.error("Live2D Error . no partsID @ KanojoSetting#setColor() ", new Object[0]);
    }

    public int getColor(String _colorID) {
        for (int i = this.colorSetList.size() - 1; i >= 0; i--) {
            if (_colorID == this.colorSetList.get(i).colorID) {
                return this.colorSetList.get(i).colorNo;
            }
        }
        UtDebug.error("Live2D Error . no partsID @ KanojoSetting#getColor() ", new Object[0]);
        return 0;
    }

    public void setFeature(String _featureID, float featureValue) {
        for (int i = this.featureSetList.size() - 1; i >= 0; i--) {
            if (_featureID == this.featureSetList.get(i).featureID) {
                this.featureSetList.get(i).featureValue = featureValue;
                return;
            }
        }
        UtDebug.error("Live2D Error . no featureID @ KanojoSetting#setColor() ", new Object[0]);
    }

    public float getFeature(String _featureID) {
        for (int i = this.featureSetList.size() - 1; i >= 0; i--) {
            if (_featureID == this.featureSetList.get(i).featureID) {
                return this.featureSetList.get(i).featureValue;
            }
        }
        UtDebug.error("Live2D Error . no featureID @ KanojoSetting#getColor() ", new Object[0]);
        return 0.0f;
    }

    public void dump() {
        System.out.printf("--------- DUMP SETTING ---------\n", new Object[0]);
        System.out.printf("\n << parts setting >>\n  ", new Object[0]);
        for (int i = 0; i < this.partsSetList.size(); i++) {
            System.out.printf("%18s [%2d]  ", new Object[]{this.partsSetList.get(i).partsID, Integer.valueOf(this.partsSetList.get(i).partsItemNo)});
            if (i % 5 == 4) {
                System.out.printf("\n  ", new Object[0]);
            }
        }
        System.out.printf("\n << color setting >>\n", new Object[0]);
        for (int i2 = 0; i2 < this.colorSetList.size(); i2++) {
            System.out.printf("%18s [%2d]  ", new Object[]{this.colorSetList.get(i2).colorID, Integer.valueOf(this.colorSetList.get(i2).colorNo)});
            if (i2 % 5 == 4) {
                System.out.printf("\n  ", new Object[0]);
            }
        }
        System.out.printf("\n << feature setting >>\n  ", new Object[0]);
        for (int i3 = 0; i3 < this.featureSetList.size(); i3++) {
            System.out.printf("%18s [%6.2f]  ", new Object[]{this.featureSetList.get(i3).featureID, Float.valueOf(this.featureSetList.get(i3).featureValue)});
            if (i3 % 5 == 4) {
                System.out.printf("\n  ", new Object[0]);
            }
        }
        System.out.printf("\n\n", new Object[0]);
    }

    public void setLoveGage(double s) {
        this.loveGage = s;
    }

    public double getLoveGage() {
        return this.loveGage;
    }

    public void setKanojoState(int state) {
        this.kanojoState = state;
    }

    public int getKanojoState() {
        return this.kanojoState;
    }

    public boolean isSilhouetteMode() {
        return this.silhouetteMode;
    }

    public void setSilhouetteMode_notForClientCall(boolean f) {
        this.silhouetteMode = f;
    }
}
