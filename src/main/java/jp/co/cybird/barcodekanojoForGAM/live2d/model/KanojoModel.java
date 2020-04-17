package jp.co.cybird.barcodekanojoForGAM.live2d.model;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import javax.microedition.khronos.opengles.GL10;
import jp.co.cybird.barcodekanojoForGAM.live2d.KanojoLive2D;
import jp.co.cybird.barcodekanojoForGAM.live2d.KanojoSetting;
import jp.co.cybird.barcodekanojoForGAM.live2d.motion.KanojoAnimation;
import jp.live2d.ALive2DModel;
import jp.live2d.android.Live2DModelAndroid;
import jp.live2d.model.PartsData;
import jp.live2d.util.UtDebug;

public class KanojoModel {
    public static final String COLOR_01_CLOTHES_A = "COLOR_01_CLOTHES_A";
    int INSTANCE_COUNT = 0;
    float[] accel = null;
    KanojoLive2D kanojoLive2D;
    Live2DModelAndroid live2DModel;
    KanojoAnimation live2dAnimation;
    boolean modelInitialized;
    ArrayList<KanojoPartsItem> partsItemList = new ArrayList<>();
    GL10 setupGl = null;
    boolean textureBinded;

    public KanojoModel(KanojoLive2D ka) {
        this.INSTANCE_COUNT++;
        this.live2DModel = null;
        this.modelInitialized = false;
        this.textureBinded = false;
        this.accel = null;
        this.live2dAnimation = null;
        this.kanojoLive2D = ka;
    }

    /* access modifiers changed from: package-private */
    public boolean initBaseModel() throws Exception {
        InputStream in = this.kanojoLive2D.getFileManager().open_resource("avatar_data/kanojoBaseModel.moc");
        this.live2DModel = Live2DModelAndroid.loadModel(in);
        in.close();
        return true;
    }

    public void setupModel_process1(KanojoLive2D ka) throws Exception {
        if (ka != null) {
            KanojoSetting kanojoSetting = ka.getKanojoSetting();
            if (this.live2DModel != null || initBaseModel()) {
                ArrayList<KanojoSetting.PartsSet> partsSetList = kanojoSetting.getPartsSetList();
                for (int i = 0; i < partsSetList.size(); i++) {
                    String id = partsSetList.get(i).partsID;
                    int no = partsSetList.get(i).partsItemNo;
                    int partsIndex = -1;
                    if (this.partsItemList != null && !this.partsItemList.isEmpty()) {
                        int j = 0;
                        while (true) {
                            if (j >= this.partsItemList.size()) {
                                break;
                            } else if (this.partsItemList.get(j).getPartsDataID() == id) {
                                partsIndex = j;
                                break;
                            } else {
                                j++;
                            }
                        }
                    }
                    KanojoPartsItem partsItem = KanojoPartsItem.importPartsItem(this.kanojoLive2D, id, no);
                    if (partsItem == null) {
                        partsItem = KanojoPartsItem.importPartsItem(this.kanojoLive2D, id, 1);
                    }
                    if (partsItem == null) {
                        UtDebug.error("Failed to load parts item [ %s / %d]¥n", id, Integer.valueOf(no));
                    } else if (partsIndex >= 0) {
                        this.partsItemList.set(partsIndex, partsItem);
                    } else {
                        this.partsItemList.add(partsItem);
                    }
                }
                for (int h = 0; h < this.partsItemList.size(); h++) {
                    this.partsItemList.get(h).bindTextures_process1(this.live2DModel, kanojoSetting);
                }
                ArrayList<PartsData> modelPartsDataList = this.live2DModel.getModelImpl().getPartsDataList();
                for (int h2 = 0; h2 < this.partsItemList.size(); h2++) {
                    KanojoPartsItem item = this.partsItemList.get(h2);
                    String _partsID = item.getPartsDataID();
                    int k = 0;
                    while (true) {
                        if (k < modelPartsDataList.size()) {
                            PartsData partsData = modelPartsDataList.get(k);
                            if (_partsID.equals(partsData.getPartsID().toString())) {
                                item.getAvatarPartsItem().replacePartsData(partsData);
                                break;
                            }
                            k++;
                        } else {
                            break;
                        }
                    }
                }
                this.live2DModel.init();
                this.live2DModel.setParamFloat("PARAM_EYE_POS", kanojoSetting.getFeature(KanojoSetting.FEATURE_01_EYE_POS));
                this.live2DModel.setParamFloat("PARAM_MOUTH_POS", kanojoSetting.getFeature(KanojoSetting.FEATURE_01_MOUTH_POS));
                if (this.live2dAnimation != null) {
                    this.live2dAnimation.initParam(this.live2DModel);
                }
                this.modelInitialized = true;
            }
        }
    }

    public void setupModel_process2(GL10 gl) {
        if (!this.modelInitialized) {
            System.err.printf("Model is not initialized\t\t\t\t\t@@KanojoModel#bindTexture\n", new Object[0]);
            return;
        }
        this.setupGl = gl;
        for (int h = 0; h < this.partsItemList.size(); h++) {
            this.partsItemList.get(h).bindTextures_process2(gl, this.live2DModel);
        }
    }

    public void setupAnimation(KanojoLive2D ka) {
        this.live2dAnimation = new KanojoAnimation(ka);
        if (this.live2DModel != null) {
            this.live2dAnimation.initParam(this.live2DModel);
        }
    }

    public void setupMotionData(KanojoLive2D ka) {
        if (this.live2dAnimation != null) {
            this.live2dAnimation.setupMotionData();
        }
    }

    public void releaseModel() {
        if (this.partsItemList != null) {
            Iterator<KanojoPartsItem> ite = this.partsItemList.listIterator();
            while (ite.hasNext()) {
                KanojoPartsItem item = ite.next();
                this.live2DModel.releaseModelTextureNo(item.getPartsItemNo());
                item.releaseItem();
            }
            this.partsItemList.clear();
            this.partsItemList = null;
        }
        this.kanojoLive2D = null;
        this.live2DModel = null;
    }

    public void drawModel(GL10 gl) throws Exception {
        if (this.setupGl != gl) {
            setupModel_process2(gl);
        }
        gl.glPushMatrix();
        drawModel_core(gl);
        gl.glPopMatrix();
    }

    public void drawModel_core(GL10 gl) throws Exception {
        KanojoSetting ks;
        if (this.live2DModel != null) {
            if (this.live2dAnimation != null) {
                this.live2dAnimation.updateParam(this.live2DModel, this.kanojoLive2D.getKanojoSetting());
            }
            if (this.accel != null) {
                this.live2DModel.addToParamFloat("PARAM_ANGLE_X", 60.0f * 1.5f * this.accel[0], 0.5f);
                this.live2DModel.addToParamFloat("PARAM_ANGLE_Y", 60.0f * 1.5f * this.accel[1], 0.5f);
                this.live2DModel.addToParamFloat("PARAM_BODY_ANGLE_X", 20.0f * 1.5f * this.accel[0], 0.5f);
                this.live2DModel.addToParamFloat("PARAM_BASE_X", -200.0f * this.accel[0], 0.5f);
                this.live2DModel.addToParamFloat("PARAM_BASE_Y", -100.0f * this.accel[1], 0.5f);
            }
            try {
                if (!(this.kanojoLive2D == null || (ks = this.kanojoLive2D.getKanojoSetting()) == null)) {
                    float srcBrowLY = this.live2DModel.getParamFloat("PARAM_BROW_L_Y");
                    float srcBrowRY = this.live2DModel.getParamFloat("PARAM_BROW_R_Y");
                    float browCenter = 0.5f + (ks.getFeature(KanojoSetting.FEATURE_01_BROW_POS) * 0.4f * 0.5f);
                    float dstBrowLY = range(browCenter + ((srcBrowLY - 0.5f) * 0.8f), 0.0f, 1.0f);
                    float dstBrowRY = range(browCenter + ((srcBrowRY - 0.5f) * 0.8f), 0.0f, 1.0f);
                    this.live2DModel.setParamFloat("PARAM_BROW_L_Y", dstBrowLY);
                    this.live2DModel.setParamFloat("PARAM_BROW_R_Y", dstBrowRY);
                }
            } catch (Throwable e) {
                System.err.printf("%s\t@@KanojoModel#setup brow\n", new Object[]{e.getMessage()});
            }
            this.live2DModel.setGL(gl);
            this.live2DModel.update();
            this.live2DModel.draw();
        }
    }

    private float range(float v, float min, float max) {
        if (v < min) {
            return min;
        }
        return v > max ? max : v;
    }

    public boolean isModelInitialized() {
        return this.modelInitialized;
    }

    public void setAccelarationValue(float[] accel2) {
        this.accel = accel2;
    }

    public KanojoAnimation getAnimation() {
        return this.live2dAnimation;
    }

    public ALive2DModel getModel() {
        return this.live2DModel;
    }
}
