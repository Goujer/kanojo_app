package jp.co.cybird.barcodekanojoForGAM.live2d.motion;

import java.util.ArrayList;
import java.util.Random;

import jp.co.cybird.barcodekanojoForGAM.live2d.KanojoLive2D;
import jp.co.cybird.barcodekanojoForGAM.live2d.KanojoResource;
import jp.co.cybird.barcodekanojoForGAM.live2d.KanojoSetting;
import jp.live2d.ALive2DModel;
import jp.live2d.motion.EyeBlinkMotion;
import jp.live2d.motion.Live2DMotion;
import jp.live2d.motion.MotionQueueManager;
import jp.live2d.util.UtFile;
import jp.live2d.util.UtSystem;

public class KanojoAnimation {
    private static final int EYE_INTERVAL_NORMAL = 4000;
    private static final int EYE_INTERVAL_TOUCHING = 1500;
    public static final int FLIP_LENGTH = 500;
    public static final float FLIP_START_BODY_H = 600.0f;
    public static final float FLIP_START_BODY_W = 600.0f;
    public static final float FLIP_START_BODY_X = 300.0f;
    public static final float FLIP_START_BODY_Y = 600.0f;

    public static final float FLIP_START_FACE_H = 400.0f;
    public static final float FLIP_START_FACE_W = 400.0f;
    public static final float FLIP_START_FACE_X = 400.0f;
    public static final float FLIP_START_FACE_Y = 200.0f;

    public static final int FLIP_START_TOUCH_H = 1100;
    public static final int FLIP_START_TOUCH_W = 600;
    public static final int FLIP_START_TOUCH_X = 300;
    public static final int FLIP_START_TOUCH_Y = 300;

    public static final int MAX_COMMON_MOTION = 1;
    public static final int MAX_DOUBLE_TAP_MOTION = 2;
    public static final int MAX_KURAKURA_MOTION = 3;

    public static final int MAX_LOVE_A_MOTION = 5;
    public static final int MAX_LOVE_B_MOTION = 4;
    public static final int MAX_LOVE_C_MOTION = 7;

    public static final int MAX_TOUCH_MOTION = 2;
    public static final float MOUSE_TO_FACE_TARGET_SCALE = 1.5f;
    private static long lastShakeEvent = 0;
    private static int no = 0;
    private static final Random rand = new Random();
    private boolean _flipAvailable;
    private float _flipStartX;
    private float _flipStartY;
    private float _lastX;
    private float _lastY;
    private float _totalD;
    private boolean _touchSingle;
    private EyeBlinkMotion eyeMotion;
    private float faceTargetX = 0.0f;
    private float faceTargetY = 0.0f;
    private float faceVX = 0.0f;
    private float faceVY = 0.0f;
    private float faceX = 0.0f;
    private float faceY = 0.0f;
    private MotionQueueManager hintMotionMgr = new MotionQueueManager();
    private KanojoLive2D kanojoLive2D;
    private KanojoTouchMotion kanojoTouch = null;
    private MotionQueueManager kigenMotionMgr = new MotionQueueManager();
    private MotionQueueManager mainMotionMgr = new MotionQueueManager();
    private ArrayList<Live2DMotion> motionDoubleTapList = new ArrayList<>();
    private ArrayList<Live2DMotion> motionKurakuraList = new ArrayList<>();
    private ArrayList<Live2DMotion> motionLoveAList = new ArrayList<>();
    private ArrayList<Live2DMotion> motionLoveBList = new ArrayList<>();
    private ArrayList<Live2DMotion> motionLoveCList = new ArrayList<>();
    private ArrayList<Live2DMotion> motionTouchList = new ArrayList<>();
    private float mouseX = 0.0f;
    private float mouseY = 0.0f;
    private float mouthOpen;
    private boolean setupMotionDataFinished = false;
    private long startTimeMSec;
    private double timeDoubleSec;
    private long timeMSec;
    private MotionQueueManager touchMotionMgr = new MotionQueueManager();
    private boolean touching;

    public KanojoAnimation(KanojoLive2D k) {
        this.kanojoLive2D = k;
        this.touching = false;
        this.eyeMotion = new EyeBlinkMotion();
        this.eyeMotion.setInterval(EYE_INTERVAL_NORMAL);
        this.startTimeMSec = UtSystem.getTimeMSec();
        this.timeDoubleSec = 0.0d;
        this.timeMSec = 0;
    }

    public void setupMotionData() {
        String dir = String.format("%s/%s", KanojoResource.AVATAR_DATA_DIR, KanojoResource.MOTION_DIR);
        String mdir = String.format("%s/%s", dir, "double_tap");
        this.motionDoubleTapList.add(loadMotion(mdir, "double_tap1"));
        this.motionDoubleTapList.add(loadMotion(mdir, "double_tap2"));
        this.motionDoubleTapList.add(loadMotion(mdir, "double_tap3"));
        this.motionDoubleTapList.add(loadMotion(mdir, "double_tap4"));

        String mdir2 = String.format("%s/%s", dir, "touch");
        this.motionTouchList.add(loadMotion(mdir2, "touch1"));
        this.motionTouchList.add(loadMotion(mdir2, "touch2"));
        this.motionTouchList.add(loadMotion(mdir2, "touch3"));
        this.motionTouchList.add(loadMotion(mdir2, "touch4"));

        String mdir3 = String.format("%s/%s", dir, "kurakura");
        this.motionKurakuraList.add(loadMotion(mdir3, "kurakura1"));
        this.motionKurakuraList.add(loadMotion(mdir3, "kurakura2"));
        this.motionKurakuraList.add(loadMotion(mdir3, "kurakura3"));

        String mdir4 = String.format("%s/%s", dir, "love_a");
        this.motionLoveAList.add(loadMotion(mdir4, "love_a01"));
        this.motionLoveAList.add(loadMotion(mdir4, "love_a02"));
        this.motionLoveAList.add(loadMotion(mdir4, "love_a03"));
        this.motionLoveAList.add(loadMotion(mdir4, "love_a04"));
        this.motionLoveAList.add(loadMotion(mdir4, "love_a05"));
        for (int i = 0; i < this.motionLoveAList.size(); i++) {
            this.motionLoveAList.get(i).setFadeIn(EYE_INTERVAL_NORMAL);
            this.motionLoveAList.get(i).setFadeOut(EYE_INTERVAL_NORMAL);
        }

        String mdir5 = String.format("%s/%s", dir, "love_b");
        this.motionLoveBList.add(loadMotion(mdir5, "love_b01"));
        this.motionLoveBList.add(loadMotion(mdir5, "love_b02"));
        this.motionLoveBList.add(loadMotion(mdir5, "love_b03"));
        this.motionLoveBList.add(loadMotion(mdir5, "love_b04"));
        this.motionLoveBList.add(loadMotion(mdir5, "sleepy"));
        for (int i2 = 0; i2 < this.motionLoveBList.size(); i2++) {
            this.motionLoveBList.get(i2).setFadeIn(EYE_INTERVAL_NORMAL);
            this.motionLoveBList.get(i2).setFadeOut(EYE_INTERVAL_NORMAL);
        }

        this.motionLoveCList.add(loadMotion(String.format("%s/%s", dir, "love_c"), "love_c01"));
        for (int i3 = 0; i3 < this.motionLoveCList.size(); i3++) {
            this.motionLoveCList.get(i3).setFadeIn(EYE_INTERVAL_NORMAL);
            this.motionLoveCList.get(i3).setFadeOut(EYE_INTERVAL_NORMAL);
        }
        this.setupMotionDataFinished = true;
    }

	private Live2DMotion loadMotion(String dir, String filename) {
        try {
            return Live2DMotion.loadMotion(UtFile.load(this.kanojoLive2D.getFileManager().open_resource(dir + "/" + filename + ".mtn")));
        } catch (Exception e) {
            System.err.printf("failed to load motion :: %s\n", filename);
            return new Live2DMotion();
        }
    }

    public void tapEvent(int tapCount, float x, float y) {
        if (this.setupMotionDataFinished) {
            try {
                tapEvent_exe(tapCount, x, y);
            } catch (Throwable e) {
                System.err.printf("error @KanojoAnimation#tapEvent :: %s\n", e);
            }
        }
    }

    public void tapEvent_exe(int tapCount, float x, float y) {
        if (tapCount == 2 && 400.0f < x && x < 880.0f && 200.0f < y && y < 700.0f) {
			this.kanojoLive2D.addPositionTouch(x, y, KanojoLive2D.USER_ACTION_HEADPAT);
			if (this.kanojoLive2D.addUserAction_notForClientCall(KanojoLive2D.USER_ACTION_HEADPAT)) {
                no = (no + 1) % this.motionDoubleTapList.size();
                if (this.motionDoubleTapList.get(no) != null) {
                    setMainMotion(this.motionDoubleTapList.get(no));
                }
            }
        } else if (tapCount == 1 && 500.0f < x && x < 780.0f && 550.0f < y && y < 700.0f) {
			this.kanojoLive2D.addPositionTouch(x, y, KanojoLive2D.USER_ACTION_KISS);
			if (this.kanojoLive2D.addUserAction_notForClientCall(KanojoLive2D.USER_ACTION_KISS)) {
                no = (no + 1) % this.motionTouchList.size();
                if (this.motionTouchList.get(no) != null) {
                    setMainMotion(this.motionTouchList.get(no));
                }
            }
        } else if (tapCount == 1 && 400.0f < x && x < 880.0f && 700.0f < y && y < 1280.0f) {
			this.kanojoLive2D.addPositionTouch(x, y, KanojoLive2D.USER_ACTION_BREAST);
			if (this.kanojoLive2D.addUserAction_notForClientCall(KanojoLive2D.USER_ACTION_BREAST)) {
                no = (no + 1) % this.motionTouchList.size();
                if (this.motionTouchList.get(no) != null) {
                    setMainMotion(this.motionTouchList.get(no));
                }
            }
        }
    }

	private void setMainMotion(Live2DMotion motion) {
        motion.reinit();
        this.mainMotionMgr.startMotion(motion, false);
    }

    public void setMouthOpen(float v01) {
        this.mouthOpen = v01;
    }

    public void touchesBegan(float logicalX, float logicalY, int touchNum) {
        this.touching = true;
        this.mouseX = ((logicalX - 640.0f) * 2.0f) / 1280.0f;
        this.mouseY = ((logicalY - 500.0f) * 2.0f) / 1280.0f;
        this.faceTargetX = range(this.mouseX * 1.5f, -1.0f, 1.0f);
        this.faceTargetY = range(this.mouseY * 1.5f, -1.0f, 1.0f);
        if (this.kanojoLive2D.getKanojoSetting().getKanojoState() == 2) {
            this.kanojoTouch = KanojoTouchMotion.getTouchEntry(this.mouseX, this.mouseY);
        } else {
            this.kanojoTouch = null;
        }
        this.touchMotionMgr.startMotion(this.kanojoTouch, true);
        this.eyeMotion.setInterval(EYE_INTERVAL_TOUCHING);
        this._flipStartX = logicalX;
        this._flipStartY = logicalY;
        this._lastX = logicalX;
        this._lastY = logicalY;
        this._totalD = 0.0f;
        this._touchSingle = touchNum == 1;
        this._flipAvailable = true;
    }

    static boolean contains(float tx, float ty, float x, float y, float w, float h) {
        return x <= tx && tx <= x + w && y <= ty && ty <= y + h;
    }

    public void touchesMoved(float logicalX, float logicalY, int touchNum) {
        this.mouseX = (2.0f * (logicalX - 640.0f)) / 1280.0f;
        this.mouseY = (2.0f * (logicalY - 500.0f)) / 1280.0f;
        if (this.kanojoTouch != null) {
            this.kanojoTouch.touchesMoved(this.mouseX, this.mouseY);
        }
        this.faceTargetX = range(this.mouseX * 1.5f, -1.0f, 1.0f);
        this.faceTargetY = range(this.mouseY * 1.5f, -1.0f, 1.0f);
        this._totalD = (float) (((double) this._totalD) + Math.sqrt((double) (((logicalX - this._lastX) * (logicalX - this._lastX)) + ((logicalY - this._lastY) * (logicalY - this._lastY)))));
        this._lastX = logicalX;
        this._lastY = logicalY;
        this._touchSingle = this._touchSingle && touchNum == 1;
        if (this._touchSingle && this._totalD > 500.0f && this._flipAvailable) {
            if (contains(this._flipStartX, this._flipStartY, FLIP_START_FACE_X, FLIP_START_FACE_Y, FLIP_START_FACE_W, FLIP_START_FACE_H)) {
                this.kanojoLive2D.addUserAction_notForClientCall(10);
            } else {
                contains(this._flipStartX, this._flipStartY, FLIP_START_BODY_X, FLIP_START_BODY_Y, FLIP_START_BODY_W, FLIP_START_BODY_H);
            }
            this._flipAvailable = false;
        }
    }

    public void touchesEnded() {
        this.touching = false;
        this.faceTargetX = 0.0f;
        this.faceTargetY = 0.0f;
        if (this.kanojoTouch != null) {
            this.kanojoTouch.touchesEnded();
        }
        this.eyeMotion.setInterval(EYE_INTERVAL_NORMAL);
    }

    public void shakeEvent() {
        if (UtSystem.getTimeMSec() - lastShakeEvent >= 3000 && this.kanojoLive2D.addUserAction_notForClientCall(KanojoLive2D.USER_ACTION_SHAKE)) {
            this.mainMotionMgr.startMotion(this.motionKurakuraList.get(rand() % this.motionKurakuraList.size()), false);
        }
    }

    public void initParam(ALive2DModel model) {
        model.setParamFloat("PARAM_BODY_DIR", 15.0f);
        model.setParamFloat("PARAM_ANGLE_X", -15.0f);
        model.setParamFloat("PARAM_ANGLE_Y", -3.0f);
        model.setParamFloat("PARAM_ANGLE_Z", -15.0f);
        model.setParamFloat("PARAM_EYE_BALL_X", 0.0f);
        model.setParamFloat("PARAM_EYE_BALL_Y", -0.0f);
        model.setParamFloat("PARAM_EYE_L_OPEN", 1.0f);
        model.setParamFloat("PARAM_EYE_R_OPEN", 1.0f);
        model.setParamFloat("PARAM_SMILE", 0.5f);
        model.setParamFloat("PARAM_TERE", 0.2f);
        model.setParamFloat("PARAM_MOUTH_FORM", 0.5f);
        model.setParamFloat("PARAM_MOUTH_OPEN_Y", 0.0f);
        model.setParamFloat("PARAM_BROW_L_Y", 0.5f);
        model.setParamFloat("PARAM_BROW_R_Y", 0.5f);
    }

    public void updateParam(ALive2DModel model, KanojoSetting setting) {
        if (model != null) {
            this.timeMSec = UtSystem.getTimeMSec() - this.startTimeMSec;
            this.timeDoubleSec = ((double) this.timeMSec) / 1000.0d;
            double t = this.timeDoubleSec * 2.0d * 3.141592653589793d;
            model.loadParam();
            if (!this.setupMotionDataFinished || !this.mainMotionMgr.isFinished()) {
                if (!this.mainMotionMgr.updateParam(model)) {
                    this.eyeMotion.setParam(model);
                }
            } else if (setting.getLoveGage() < 35.0d) {
                this.mainMotionMgr.startMotion(this.motionLoveCList.get(rand() % this.motionLoveCList.size()), false);
            } else if (setting.getLoveGage() < 80.0d) {
                this.mainMotionMgr.startMotion(this.motionLoveBList.get(rand() % this.motionLoveBList.size()), false);
            } else {
                this.mainMotionMgr.startMotion(this.motionLoveAList.get(rand() % this.motionLoveAList.size()), false);
            }
            model.saveParam();
            updateDragMotion(model);
            updateEmotion(model, setting);
            this.hintMotionMgr.updateParam(model);
            this.kigenMotionMgr.updateParam(model);
            model.setParamFloat("PARAM_BREATH", (float) ((Math.sin(t / 3.2345d) * 0.5d) + 0.5d));
            model.addToParamFloat("PARAM_ALL_X", (float) (0.10000000149011612d * Math.sin(t / 9.5345d)), 0.8f);
            model.addToParamFloat("PARAM_BODY_DIR", (float) (4.0d * Math.sin(t / 15.5345d)), 0.8f);
            model.addToParamFloat("PARAM_ANGLE_X", (float) (15.0d * Math.sin(t / 6.5345d)), 0.8f);
            model.addToParamFloat("PARAM_ANGLE_Y", (float) (8.0d * Math.sin(t / 3.5345d)), 0.8f);
            model.addToParamFloat("PARAM_ANGLE_Z", (float) (10.0d * Math.sin(t / 5.5345d)), 0.8f);
            model.addToParamFloat("PARAM_SMILE", (float) (0.1d * Math.sin(t / 12.5345d)), 0.8f);
            updateAuto(model);
            this.touchMotionMgr.updateParam(model);
        }
    }

    private int rand() {
        return rand.nextInt(Integer.MAX_VALUE);
    }

	private void updateDragMotion(ALive2DModel model) {
        float dx = this.faceTargetX - this.faceX;
        float dy = this.faceTargetY - this.faceY;
        if (dx != 0.0f || dy != 0.0f) {
            float d = (float) Math.sqrt((double) ((dx * dx) + (dy * dy)));
            float ax = ((0.08888889f * dx) / d) - this.faceVX;
            float ay = ((0.08888889f * dy) / d) - this.faceVY;
            float a = (float) Math.sqrt((double) ((ax * ax) + (ay * ay)));
            if (a < -0.009876544f || a > 0.009876544f) {
                ax *= 0.009876544f / a;
                ay *= 0.009876544f / a;
            }
            this.faceVX += ax;
            this.faceVY += ay;
            float max_v = 0.5f * (((float) Math.sqrt((double) ((9.7546115E-5f + (0.1580247f * d)) - (0.07901235f * d)))) - 0.009876544f);
            float cur_v = (float) Math.sqrt((double) ((this.faceVX * this.faceVX) + (this.faceVY * this.faceVY)));
            if (cur_v > max_v) {
                this.faceVX *= max_v / cur_v;
                this.faceVY *= max_v / cur_v;
            }
            this.faceX += this.faceVX;
            this.faceY += this.faceVY;
            float zzz = this.faceX * this.faceY;
            model.addToParamFloat("PARAM_ANGLE_X", range(this.faceX * 30.0f, -30.0f, 30.0f), 1.0f);
            model.addToParamFloat("PARAM_ANGLE_Y", range((-this.faceY) * 30.0f, -30.0f, 30.0f), 1.0f);
            model.addToParamFloat("PARAM_ANGLE_Z", range(30.0f * zzz, -30.0f, 30.0f), 1.0f);
            model.addToParamFloat("PARAM_BODY_ANGLE_X", range(this.faceX * 5.0f, -10.0f, 10.0f), 1.0f);
            model.addToParamFloat("PARAM_EYE_BALL_X", range(this.faceX, -1.0f, 1.0f), 1.0f);
            model.addToParamFloat("PARAM_EYE_BALL_Y", range(-this.faceY, -1.0f, 1.0f), 1.0f);
            if (this.faceY < -0.5f) {
                model.addToParamFloat("PARAM_BASE_Y", ((-this.faceY) - 0.5f) * 20.0f, 1.0f);
            }
        }
    }

	private void updateAuto(ALive2DModel model) {
        float loop4x;
        float loop4y;
        int t4 = (int) ((this.timeMSec / 2) % 4000);
        if (t4 < 1000) {
            loop4x = ((float) t4) / 1000.0f;
            loop4y = 0.0f;
        } else if (t4 < 2000) {
            loop4x = 1.0f;
            loop4y = ((float) (t4 - 1000)) / 1000.0f;
        } else if (t4 < 3000) {
            loop4x = 1.0f - (((float) (t4 - 2000)) / 1000.0f);
            loop4y = 1.0f;
        } else {
            loop4x = 0.0f;
            loop4y = 1.0f - (((float) (t4 - 3000)) / 1000.0f);
        }
        model.setParamFloat("PARAM_LOOP_4_SEC_X", loop4x);
        model.setParamFloat("PARAM_LOOP_4_SEC_Y", loop4y);
    }

	private float g(float love, float badV, float normalV, float goodV) {
        if (((double) love) < 0.5d) {
            return ((normalV - badV) * love * 2.0f) + badV;
        }
        return ((goodV - normalV) * ((love * 2.0f) - 1.0f)) + normalV;
    }

	private void updateEmotion(ALive2DModel model, KanojoSetting setting) {
        if (setting != null) {
            float love = ((float) setting.getLoveGage()) / 100.0f;
            model.addToParamFloat("PARAM_BROW_L_Y", g(love, -0.5f, 0.0f, 0.1f), 1.0f);
            model.addToParamFloat("PARAM_BROW_L_FORM", g(love, -0.8f, 0.0f, 0.1f), 1.0f);
            model.addToParamFloat("PARAM_BROW_L_SIDE", g(love, -0.8f, 0.0f, 0.1f), 1.0f);
            model.addToParamFloat("PARAM_BROW_L_ANGLE", g(love, -0.8f, 0.0f, 0.1f), 1.0f);
            model.addToParamFloat("PARAM_BROW_R_Y", g(love, -0.5f, 0.0f, 0.1f), 1.0f);
            model.addToParamFloat("PARAM_BROW_R_FORM", g(love, -0.8f, 0.0f, 0.1f), 1.0f);
            model.addToParamFloat("PARAM_BROW_R_SIDE", g(love, -0.8f, 0.0f, 0.1f), 1.0f);
            model.addToParamFloat("PARAM_BROW_R_ANGLE", g(love, -0.8f, 0.0f, 0.1f), 1.0f);
            model.addToParamFloat("PARAM_TERE", g(love, -0.5f, 0.0f, 0.2f), 1.0f);
            model.addToParamFloat("PARAM_MOUTH_FORM", g(love, -0.8f, 0.2f, 0.9f), 1.0f);
            model.addToParamFloat("PARAM_EYE_L_OPEN", g(love, -0.2f, 0.0f, 0.0f), 1.0f);
            model.addToParamFloat("PARAM_EYE_R_OPEN", g(love, -0.2f, 0.0f, 0.0f), 1.0f);
            model.addToParamFloat("PARAM_ANGLE_X", g(love, -10.0f, 0.0f, 0.0f), 1.0f);
            model.addToParamFloat("PARAM_ANGLE_Z", g(love, -10.0f, 0.0f, 0.0f), 1.0f);
            model.addToParamFloat("PARAM_BODY_ANGLE_X", g(love, -5.0f, 0.0f, 0.0f), 1.0f);
            model.addToParamFloat("PARAM_EYE_BALL_X", g(love, -0.3f, 0.0f, 0.0f), 1.0f);
            model.addToParamFloat("PARAM_EYE_BALL_Y", g(love, -0.2f, 0.0f, 0.0f), 1.0f);
        }
    }

    private float range(float v, float min, float max) {
        if (v < min) {
            v = min;
        }
	    return Math.min(v, max);
    }
}
