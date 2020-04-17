package jp.co.cybird.barcodekanojoForGAM.live2d.model;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import javax.microedition.khronos.opengles.GL10;
import jp.co.cybird.barcodekanojoForGAM.live2d.KanojoFileManager;
import jp.co.cybird.barcodekanojoForGAM.live2d.KanojoLive2D;
import jp.co.cybird.barcodekanojoForGAM.live2d.KanojoResource;
import jp.co.cybird.barcodekanojoForGAM.live2d.KanojoSetting;
import jp.co.cybird.barcodekanojoForGAM.live2d.util.UIImage;
import jp.live2d.android.Live2DModelAndroid;
import jp.live2d.avatar.AvatarPartsItem;
import jp.live2d.draw.DDTexture;
import jp.live2d.draw.IDrawData;
import jp.live2d.io.BReader;
import jp.live2d.util.UtDebug;

public class KanojoPartsItem {
    AvatarPartsItem avatarParts = null;
    String partsDataID;
    int partsItemNo = 0;
    ArrayList<KanojoPartsItemTexture> textureList = null;

    KanojoPartsItem() {
    }

    public void releaseItem() {
        if (this.textureList != null) {
            for (int i = this.textureList.size() - 1; i >= 0; i--) {
                this.textureList.get(i).releaseTexture();
            }
            this.textureList.clear();
            this.textureList = null;
        }
    }

    public int getPartsItemNo() {
        return this.partsItemNo;
    }

    public void setPartsItemNo(int no) {
        this.partsItemNo = no;
    }

    public String getPartsDataID() {
        return this.partsDataID;
    }

    public void setPartsDataID(String id) {
        this.partsDataID = id;
    }

    public AvatarPartsItem getAvatarPartsItem() {
        return this.avatarParts;
    }

    public static String getPartsDir(String partsRootDir, String _partsID, int _partsItemNo) {
        if (partsRootDir == null) {
            return null;
        }
        return String.format("%s/%s/%s_%03d", new Object[]{partsRootDir, _partsID, _partsID, Integer.valueOf(_partsItemNo)});
    }

    public static String getBkPartsData_resource(String partsDir) {
        return String.valueOf(partsDir) + "/" + "data.bkparts";
    }

    public static String getBkPartsData_cache(String partsDir) {
        return String.valueOf(partsDir) + "/" + "data.bkparts";
    }

    public static KanojoPartsItem importPartsItem(KanojoLive2D kanojoLive2D, String _partsID, int _partsItemNo) {
        String texPath;
        KanojoFileManager fileMgr = kanojoLive2D.getFileManager();
        String partsDir = getPartsDir(KanojoResource.AVATAR_DATA_DIR, _partsID, _partsItemNo);
        String dataPath = getBkPartsData_resource(partsDir);
        boolean isCache = false;
        if ((dataPath == null || !fileMgr.exists_resource(dataPath)) && (partsDir = getPartsDir(kanojoLive2D.getPartsCacheDirectory(), _partsID, _partsItemNo)) != null) {
            dataPath = getBkPartsData_cache(partsDir);
            if (dataPath == null || !fileMgr.exists_cache(dataPath)) {
                UtDebug.error("can not load parts data [%s / %d] ", _partsID, Integer.valueOf(_partsItemNo));
                return null;
            }
            isCache = true;
        }
        try {
            InputStream in = fileMgr.open(dataPath, isCache);
            if (in == null) {
                UtDebug.error("can not open stream ", dataPath);
                return null;
            }
            BReader br = new BReader(in);
            int b = br.readByte();
            int k = br.readByte();
            int p = br.readByte();
            br.setFormatVersion(br.readByte());
            if (b == 98 && k == 107 && p == 112) {
                int readInt = br.readInt();
                AvatarPartsItem _avatarParts = (AvatarPartsItem) br.readObject();
                int _clippedImageCount = br.readInt();
                if (br.readInt() != -2004318072) {
                    UtDebug.error("EOF check error¥n", new Object[0]);
                    return null;
                }
                if (_clippedImageCount < 0 || _clippedImageCount > 1000) {
                    _clippedImageCount = 0;
                }
                ArrayList<KanojoPartsItemTexture> textureList2 = new ArrayList<>();
                int i = 0;
                while (i < _clippedImageCount) {
                    try {
                        String dir = String.format("%s/tex%d", new Object[]{partsDir, 512});
                        String name = String.format("tex_%d", new Object[]{Integer.valueOf(i)});
                        if (isCache) {
                            texPath = String.format("%s/%s.png", new Object[]{dir, name});
                        } else {
                            texPath = String.valueOf(dir) + "/" + name + ".png";
                        }
                        InputStream texIn = fileMgr.open(texPath, isCache);
                        UIImage image = UIImage.loadImage(texIn);
                        texIn.close();
                        if (image != null) {
                            textureList2.add(new KanojoPartsItemTexture(image));
                        }
                        i++;
                    } catch (IOException e) {
                        e.printStackTrace();
                        UtDebug.error("read textureList error [%s / %d] ", _partsID, Integer.valueOf(_partsItemNo));
                        return null;
                    }
                }
                KanojoPartsItem ret = new KanojoPartsItem();
                ret.avatarParts = _avatarParts;
                ret.partsItemNo = _partsItemNo;
                ret.setPartsDataID(_partsID);
                ret.setTextureList(textureList2);
                return ret;
            }
            UtDebug.error("loaded file is not BarcodeKanojo parts¥n", new Object[0]);
            return null;
        } catch (Exception e2) {
            UtDebug.error("read parts error [%s / %d] ", _partsID, Integer.valueOf(_partsItemNo));
            return null;
        }
    }

    public void setTextureList(ArrayList<KanojoPartsItemTexture> tx) {
        this.textureList = tx;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:14:0x004a, code lost:
        r7 = (jp.live2d.draw.DDTexture) r5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x00ec, code lost:
        r7 = (jp.live2d.draw.DDTexture) r5;
     */
    public void bindTextures_process1(Live2DModelAndroid model, KanojoSetting setting) {
        DDTexture dt;
        int partsTextureNo;
        DDTexture dt2;
        int partsTextureNo2;
        ArrayList<IDrawData> drawList = this.avatarParts.getDrawDataList();
        int len = drawList.size();
        for (int i = 0; i < len; i++) {
            IDrawData dd = drawList.get(i);
            if (dd.getType() == 2 && (partsTextureNo2 = dt2.getTextureNo()) >= 0 && this.textureList.size() > partsTextureNo2) {
                KanojoPartsItemTexture tex = this.textureList.get(partsTextureNo2);
                Integer colorTypeI = (Integer) dt2.getOptionData(KanojoResource.DDTEXTURE_OPTION_BK_OPTION_COLOR);
                if (colorTypeI != null) {
                    int colorType = colorTypeI.intValue();
                    ColorConvert conv = null;
                    switch (colorType) {
                        case 1:
                            conv = setting.getColorConvert(KanojoSetting.COLOR_01_HAIR, colorType);
                            break;
                        case 2:
                            conv = setting.getColorConvert(KanojoSetting.COLOR_01_SKIN, colorType);
                            break;
                        case 3:
                            conv = setting.getColorConvert(KanojoSetting.COLOR_01_EYE, colorType);
                            break;
                        case 4:
                        case 5:
                        case 6:
                        case 7:
                        case 9:
                            conv = setting.getColorConvert("COLOR_01_CLOTHES_A", colorType);
                            break;
                    }
                    tex.setColorConvert(conv);
                }
            }
        }
        if (this.textureList != null && !this.textureList.isEmpty()) {
            for (int i2 = 0; i2 < this.textureList.size(); i2++) {
                KanojoPartsItemTexture tex2 = this.textureList.get(i2);
                tex2.setModelTextureNo(model.generateModelTextureNo());
                tex2.bindTexture_process1(setting);
            }
        }
        for (int i3 = 0; i3 < len; i3++) {
            IDrawData dd2 = drawList.get(i3);
            if (dd2.getType() == 2 && (partsTextureNo = dt.getTextureNo()) >= 0 && partsTextureNo < this.textureList.size()) {
                KanojoPartsItemTexture tex3 = this.textureList.get(partsTextureNo);
                dt.setTextureNo(tex3.getModelTextureNo());
                float[] map = dt.getUvMap();
                int pointCount = dt.getNumPoints();
                double scalex = 512.0d / ((double) tex3.getTextureWidth());
                double scaley = 512.0d / ((double) tex3.getTextureHeight());
                for (int pti = 0; pti < pointCount; pti++) {
                    int oi = pti << 1;
                    map[oi] = (float) (((double) map[oi]) * scalex);
                    int i4 = oi + 1;
                    map[i4] = (float) (((double) map[i4]) * scaley);
                }
            }
        }
    }

    public void bindTextures_process2(GL10 gl, Live2DModelAndroid model) {
        for (int i = 0; i < this.textureList.size(); i++) {
            KanojoPartsItemTexture tex = this.textureList.get(i);
            int modelTexNo = tex.getModelTextureNo();
            int glTexNo = tex.bindTexture_process2(gl);
            if (glTexNo == 0) {
                UtDebug.error("failed to %s [%d] #bindTexture %d", this.partsDataID, Integer.valueOf(this.partsItemNo), Integer.valueOf(i));
            }
            model.setTexture(modelTexNo, glTexNo);
        }
    }
}
