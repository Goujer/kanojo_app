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
//import jp.live2d.avatar.AvatarPartsItem;
//import jp.live2d.draw.DDTexture;
import jp.live2d.draw.IDrawData;
import jp.live2d.io.BReader;
import jp.live2d.util.UtDebug;

public class KanojoPartsItem {
	/*AvatarPartsItem*/private jp.live2d.a.a avatarParts = null;
    private String partsDataID;
    private int partsItemNo = 0;
    private ArrayList<KanojoPartsItemTexture> textureList = null;

    private KanojoPartsItem() {
    }

    void releaseItem() {
        if (this.textureList != null) {
            for (int i = this.textureList.size() - 1; i >= 0; i--) {
                this.textureList.get(i).releaseTexture();
            }
            this.textureList.clear();
            this.textureList = null;
        }
    }

    int getPartsItemNo() {
        return this.partsItemNo;
    }

    public void setPartsItemNo(int no) {
        this.partsItemNo = no;
    }

    String getPartsDataID() {
        return this.partsDataID;
    }

    private void setPartsDataID(String id) {
        this.partsDataID = id;
    }

    /*AvatarPartsItem*/jp.live2d.a.a getAvatarPartsItem() {
        return this.avatarParts;
    }

    public static String getPartsDir(String partsRootDir, String _partsID, int _partsItemNo) {
        if (partsRootDir == null) {
            return null;
        }
        return String.format("%s/%s/%s_%03d", partsRootDir, _partsID, _partsID, _partsItemNo);
    }

    public static String getBkPartsData_resource(String partsDir) {
        return String.valueOf(partsDir) + "/" + "data.bkparts";
    }

    public static String getBkPartsData_cache(String partsDir) {
        return String.valueOf(partsDir) + "/" + "data.bkparts";
    }

    static KanojoPartsItem importPartsItem(KanojoLive2D kanojoLive2D, String _partsID, int _partsItemNo) {
        String texPath;
        KanojoFileManager fileMgr = kanojoLive2D.getFileManager();
        String partsDir = getPartsDir(KanojoResource.AVATAR_DATA_DIR, _partsID, _partsItemNo);
        String dataPath = getBkPartsData_resource(partsDir);
        boolean isCache = false;
        if ((dataPath == null || !fileMgr.exists_resource(dataPath)) && (partsDir = getPartsDir(kanojoLive2D.getPartsCacheDirectory(), _partsID, _partsItemNo)) != null) {
            dataPath = getBkPartsData_cache(partsDir);
            if (dataPath == null || !fileMgr.exists_cache(dataPath)) {
                UtDebug.error("can not load parts data [%s / %d] ", _partsID, _partsItemNo);
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
			int b = br.f();//readByte();
			int k = br.f();//readByte();
			int p = br.f();//readByte();
			br./*setFormatVersion*/a(br.f()/*readByte()*/);
			if (b == 0x62 && k == 0x6b && p == 0x70) {
				int partsVersion = br.e();//readInt();
				//TODO: Cast might not be needed
				/*AvatarPartsItem*/ jp.live2d.a.a _avatarParts = (/*AvatarPartsItem*/ jp.live2d.a.a) br.n();//readObject();
				int _clippedImageCount = br.e();//readInt();//is giving 0x01 81 0e
				if (br.e()/*readInt()*/ != 0x88888888) {
                    UtDebug.error("EOF check error¥n");
                    return null;
                }
                if (_clippedImageCount < 0 || _clippedImageCount > 1000) {
                    _clippedImageCount = 0;
                }
                ArrayList<KanojoPartsItemTexture> textureList2 = new ArrayList<>();
                int i = 0;
                while (i < _clippedImageCount) {
                    try {
                        String dir = String.format("%s/tex%d", partsDir, 512);
                        String name = String.format("tex_%d", i);
                        if (isCache) {
                            texPath = String.format("%s/%s.png", dir, name);
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
                        UtDebug.error("read textureList error [%s / %d] ", _partsID, _partsItemNo);
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
            UtDebug.error("loaded file is not BarcodeKanojo parts¥n");
            return null;
        } catch (Exception e2) {
            UtDebug.error("read parts error [%s / %d] ", _partsID, _partsItemNo);
            return null;
        }
    }

    private void setTextureList(ArrayList<KanojoPartsItemTexture> tx) {
        this.textureList = tx;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:14:0x004a, code lost:
        r7 = (jp.live2d.draw.DDTexture) r5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x00ec, code lost:
        r7 = (jp.live2d.draw.DDTexture) r5;
     */
	void bindTextures_process1(Live2DModelAndroid model, KanojoSetting setting) {
		int i;
		int partsTextureNo;
		KanojoPartsItemTexture tex;
		ArrayList<IDrawData> drawList = avatarParts.b();//getDrawDataList();
		int len = drawList.size();
		for (i = 0; i < len; i++) {
			jp.live2d.draw.a dt;
			IDrawData dd = drawList.get(i);
			if (dd./*getType*/e() == 2) {
				dt = (jp.live2d.draw.a) dd;
				partsTextureNo = dt./*getTextureNo*/a();
				if (partsTextureNo >= 0 && this.textureList.size() > partsTextureNo) {
					tex = this.textureList.get(partsTextureNo);
					Integer colorTypeI = (Integer) dt./*getOptionData*/a(KanojoResource.DDTEXTURE_OPTION_BK_OPTION_COLOR);
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
		}
		if (!(this.textureList == null || this.textureList.isEmpty())) {
			for (i = 0; i < this.textureList.size(); i++) {
				tex = this.textureList.get(i);
				tex.setModelTextureNo(model.generateModelTextureNo());
				tex.bindTexture_process1(setting);
			}
		}
		for (i = 0; i < len; i++) {
			IDrawData dd = drawList.get(i);
			if (dd./*getType*/e() == 2) {
				jp.live2d.draw.a/*DDTexture*/ dt = (jp.live2d.draw.a) dd;
				partsTextureNo = dt./*getTextureNo*/a();
				if (partsTextureNo >= 0 && partsTextureNo < this.textureList.size()) {
					tex = this.textureList.get(partsTextureNo);
					dt./*setTextureNo*/a(tex.getModelTextureNo());
					float[] map = dt./*getUvMap*/b();
					int pointCount = dt./*getNumPoints*/d();
					double scalex = 512.0d / ((double) tex.getTextureWidth());
					double scaley = 512.0d / ((double) tex.getTextureHeight());
					for (int pti = 0; pti < pointCount; pti++) {
						int oi = pti << 1;
						map[oi] = (float) (((double) map[oi]) * scalex);
						int i2 = oi + 1;
						map[i2] = (float) (((double) map[i2]) * scaley);
					}
				}
			}
		}
	}

    void bindTextures_process2(GL10 gl, Live2DModelAndroid model) {
        for (int i = 0; i < this.textureList.size(); i++) {
            KanojoPartsItemTexture tex = this.textureList.get(i);
            int modelTexNo = tex.getModelTextureNo();
            int glTexNo = tex.bindTexture_process2(gl);
            if (glTexNo == 0) {
                UtDebug.error("failed to %s [%d] #bindTexture %d", this.partsDataID, this.partsItemNo, i);
            }
            model.setTexture(modelTexNo, glTexNo);
        }
    }
}
