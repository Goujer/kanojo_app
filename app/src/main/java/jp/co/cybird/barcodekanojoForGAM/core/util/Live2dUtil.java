package jp.co.cybird.barcodekanojoForGAM.core.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import java.io.File;
import java.util.HashMap;
import java.util.Observer;
import jp.co.cybird.barcodekanojoForGAM.Defs;
import jp.co.cybird.barcodekanojoForGAM.core.model.Kanojo;
import jp.co.cybird.barcodekanojoForGAM.live2d.KanojoLive2D;
import jp.co.cybird.barcodekanojoForGAM.live2d.KanojoSetting;

public class Live2dUtil {
    public static final String AVATAR_DATA_CACHE_DIR = "avatar_data_cache";
    public static final String BACKGROUND_DIR = "background";
    public static final int DEFAULT_LOVE_GAUGE = 85;
    public static final float ICON_OFFSET_Y = -0.3f;
    public static final float ICON_SCALE = 0.78f;
    public static final int ICON_SIZE = 420;	//Export size should be 1024
    public static final float PREICON_OFFSET_Y = 0.2f;
    public static final float PREICON_SCALE = 1.3f;
    private static final int REQUEST_MAX = 5;
    public static final String TAG = "Live2dUtil";
    private Context mContext;
    private KanojoLive2D mKanojoLive2D;
    private Live2dDiskCache mLive2dDisk;
    private Observer mRrObserver;
    //private RemoteResourceManager mRrm;
    private HashMap<String, Integer> map = new HashMap<>();

    public Live2dUtil(KanojoLive2D kanojoLive2d, Context context) {
        this.mContext = context;
        this.mKanojoLive2D = kanojoLive2d;
        this.mKanojoLive2D.setPartsCacheDirectory(AVATAR_DATA_CACHE_DIR);
    }

    public void setOnRrObserver(Observer rro) {
        this.mRrObserver = rro;
    }

    public void removeObserver() {
        //if (this.mRrm != null && this.mRrObserver != null) {
        //    this.mRrm.deleteObserver(this.mRrObserver);
        //}
    }

    public boolean setLive2DKanojoPartsAndRequest(KanojoSetting setting, Kanojo kanojo) {
        boolean isPartsAvailable = setParts(setting, KanojoSetting.PARTS_01_EYE, kanojo.getEye_type()) && setParts(setting, KanojoSetting.PARTS_01_NOSE, kanojo.getNose_type()) && setParts(setting, KanojoSetting.PARTS_01_MOUTH, kanojo.getMouth_type()) && setParts(setting, KanojoSetting.PARTS_01_FACE, kanojo.getFace_type()) && setParts(setting, KanojoSetting.PARTS_01_BROW, kanojo.getBrow_type()) && setParts(setting, KanojoSetting.PARTS_01_FRINGE, kanojo.getFringe_type()) && setParts(setting, KanojoSetting.PARTS_01_HAIR, kanojo.getHair_type()) && setParts(setting, KanojoSetting.PARTS_01_ACCESSORY, kanojo.getAccessory_type()) && setParts(setting, KanojoSetting.PARTS_01_SPOT, kanojo.getSpot_type()) && setParts(setting, KanojoSetting.PARTS_01_GLASSES, kanojo.getGlasses_type()) && setParts(setting, KanojoSetting.PARTS_01_BODY, kanojo.getBody_type()) && setParts(setting, KanojoSetting.PARTS_01_CLOTHES, kanojo.getClothes_type()) && setParts(setting, KanojoSetting.PARTS_01_EAR, kanojo.getEar_type());
        setting.setFeature(KanojoSetting.FEATURE_01_EYE_POS, kanojo.getEye_position());
        setting.setFeature(KanojoSetting.FEATURE_01_BROW_POS, kanojo.getBrow_position());
        setting.setFeature(KanojoSetting.FEATURE_01_MOUTH_POS, kanojo.getMouth_position());
        setting.setColor(KanojoSetting.COLOR_01_SKIN, kanojo.getSkin_color());
        setting.setColor(KanojoSetting.COLOR_01_HAIR, kanojo.getHair_color());
        setting.setColor(KanojoSetting.COLOR_01_EYE, kanojo.getEye_color());
        return isPartsAvailable;
    }

    private boolean setParts(KanojoSetting setting, String partsID, int partsItemNo) {
        if (this.mKanojoLive2D.isAvailableParts(partsID, partsItemNo)) {
            setting.setParts(partsID, partsItemNo);
            return true;
        }
        String partsUrl = Defs.URL_BASE_LIVE2D_EXTPARTS + File.separator + partsID + File.separator + partsID + "_" + String.format("%03d", partsItemNo) + ".zip";
        if (this.map.containsKey(partsUrl)) {
            int count = this.map.get(partsUrl);
            if (count > REQUEST_MAX) {
                setting.setParts(partsID, 0);
                return true;
            }
            this.map.put(partsUrl, count + 1);
        } else {
            this.map.put(partsUrl, 0);
        }
        this.mLive2dDisk = new Live2dDiskCache(this.mContext, AVATAR_DATA_CACHE_DIR, partsID);
//        this.mRrm = new RemoteResourceManager(this.mLive2dDisk);
//        if (this.mRrObserver != null) {
//            this.mRrm.addObserver(this.mRrObserver);
//        }
//        ImageCache.requestImage(partsUrl, this.mRrm);
        return false;
    }

    public boolean setLive2DKanojoBackground(Kanojo kanojo) {
        if (kanojo == null) {
            return true;
        }
        String StringUrl = kanojo.getAvatar_background_image_url();
        if (StringUrl == null || StringUrl.equals("") || !HttpUtil.isUrl(StringUrl)) {
            return true;
        }
        if (this.map.containsKey(StringUrl)) {
            int count = this.map.get(StringUrl);
            if (count > REQUEST_MAX) {
                this.mKanojoLive2D.setBackgroundImage(null, false);
                return true;
            }
            this.map.put(StringUrl, count + 1);
        } else {
            this.map.put(StringUrl, 0);
        }
        Uri backUri = Uri.parse(StringUrl);
        //this.mRrm = new RemoteResourceManager(new Live2dDiskCache(this.mContext, AVATAR_DATA_CACHE_DIR, BACKGROUND_DIR));
        //if (this.mRrm.exists(backUri)) {
        //    this.mKanojoLive2D.setBackgroundImage(this.mRrm.getFile(backUri).getAbsolutePath(), true);
        //    return true;
        //}
        //if (this.mRrObserver != null) {
        //    this.mRrm.addObserver(this.mRrObserver);
        //}
        //ImageCache.requestImage(StringUrl, this.mRrm);
        return false;
    }

    public boolean exists(Uri uri) {
        //return this.mRrm.exists(uri);
		return false;
    }

    public static Bitmap createNormalIcon(Context context, Kanojo kanojo, int emotion_status) {
        return createIcon(context, kanojo, emotion_status, ICON_SCALE, 0.0f, ICON_OFFSET_Y, 0);
    }

    public static Bitmap createNormalIcon(Context context, Kanojo kanojo) {
        return createIcon(context, kanojo, kanojo.getEmotion_status(), ICON_SCALE, 0.0f, ICON_OFFSET_Y, 0);
    }

    public static Bitmap createSilhouette(Context context, Kanojo kanojo, int emotion_status) {
        return createIcon(context, kanojo, emotion_status, PREICON_SCALE, 0.0f, PREICON_OFFSET_Y, 1);
    }

    private static Bitmap createIcon(Context context, Kanojo kanojo, int emotion_status, float scale, float offset_x, float offset_y, int iconFlg) {
        KanojoLive2D kanojoLive2D = new KanojoLive2D(context);
        if (kanojoLive2D == null) {
            return null;
        }
        KanojoSetting setting = kanojoLive2D.getKanojoSetting();
        setLive2DKanojoParts(setting, kanojo);
        setting.setLoveGage(DEFAULT_LOVE_GAUGE);
        return kanojoLive2D.createIcon(ICON_SIZE, ICON_SIZE, scale, offset_x, offset_y, iconFlg);
    }

    private static void setLive2DKanojoParts(KanojoSetting setting, Kanojo kanojo) {
        setting.setParts(KanojoSetting.PARTS_01_EYE, kanojo.getEye_type());
        setting.setParts(KanojoSetting.PARTS_01_NOSE, kanojo.getNose_type());
        setting.setParts(KanojoSetting.PARTS_01_MOUTH, kanojo.getMouth_type());
        setting.setParts(KanojoSetting.PARTS_01_FACE, kanojo.getFace_type());
        setting.setParts(KanojoSetting.PARTS_01_BROW, kanojo.getBrow_type());
        setting.setParts(KanojoSetting.PARTS_01_FRINGE, kanojo.getFringe_type());
        setting.setParts(KanojoSetting.PARTS_01_HAIR, kanojo.getHair_type());
        setting.setParts(KanojoSetting.PARTS_01_ACCESSORY, kanojo.getAccessory_type());
        setting.setParts(KanojoSetting.PARTS_01_SPOT, kanojo.getSpot_type());
        setting.setParts(KanojoSetting.PARTS_01_GLASSES, kanojo.getGlasses_type());
        setting.setParts(KanojoSetting.PARTS_01_BODY, kanojo.getBody_type());
        setting.setParts(KanojoSetting.PARTS_01_CLOTHES, kanojo.getClothes_type());
        setting.setParts(KanojoSetting.PARTS_01_EAR, kanojo.getEar_type());
        setting.setFeature(KanojoSetting.FEATURE_01_EYE_POS, kanojo.getEye_position());
        setting.setFeature(KanojoSetting.FEATURE_01_BROW_POS, kanojo.getBrow_position());
        setting.setFeature(KanojoSetting.FEATURE_01_MOUTH_POS, kanojo.getMouth_position());
        setting.setColor(KanojoSetting.COLOR_01_SKIN, kanojo.getSkin_color());
        setting.setColor(KanojoSetting.COLOR_01_HAIR, kanojo.getHair_color());
        setting.setColor(KanojoSetting.COLOR_01_EYE, kanojo.getEye_color());
    }
}
