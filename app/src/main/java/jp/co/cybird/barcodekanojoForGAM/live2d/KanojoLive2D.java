package jp.co.cybird.barcodekanojoForGAM.live2d;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import javax.microedition.khronos.opengles.GL10;
import jp.co.cybird.barcodekanojoForGAM.activity.kanojo.KanojoRoomActivity;
import jp.co.cybird.barcodekanojoForGAM.live2d.model.IconRenderer;
import jp.co.cybird.barcodekanojoForGAM.live2d.model.KanojoModel;
import jp.co.cybird.barcodekanojoForGAM.live2d.model.KanojoPartsItem;
import jp.co.cybird.barcodekanojoForGAM.live2d.motion.KanojoAnimation;
import jp.co.cybird.barcodekanojoForGAM.live2d.util.AccelHelper;
import jp.co.cybird.barcodekanojoForGAM.live2d.view.AndroidEAGLView;
import jp.co.cybird.barcodekanojoForGAM.live2d.view.AndroidES1Renderer;
import jp.live2d.Live2D;
import jp.live2d.util.UtDebug;

public class KanojoLive2D implements KanojoResource {
    public static final int ICON_FLAG_FIXED_STYLE = 2;
    public static final int ICON_FLAG_SILHOUETTE = 1;
    public static final int USER_ACTION_COUNT = 20;

	public static final int USER_ACTION_NADERU = 10;
    public static final int USER_ACTION_FURU = 11;
	public static final int USER_ACTION_TSUTSUKU = 12;
    public static final int USER_ACTION_KISS = 20;
    public static final int USER_ACTION_MUNE = 21;

    private AccelHelper accelHelper;
    private int curUserActionNo = -1;
    private boolean dirtyFlag = true;
    private KanojoFileManager fileManager;
    private AndroidEAGLView glView = null;
    private boolean inRoom = true;
    private KanojoModel kanojoModel = null;
    private KanojoRoomActivity kanojoRoomActivity;
    private KanojoSetting kanojoSetting;
    private boolean modelAvailable = false;
    private boolean modelUpdating = false;
    private String partsCacheDir = null;
    private boolean process1Finished = false;
    private int textureSize = 512;
    private int[] userActions = new int[20];

    public void setKanojoRoomActivity(KanojoRoomActivity kanojoRoomActivity2) {
        this.kanojoRoomActivity = kanojoRoomActivity2;
    }

    public KanojoLive2D(Context androidContext) {
        Live2D.init();
        this.fileManager = new KanojoFileManager(androidContext);
        this.kanojoSetting = KanojoSetting.createSetting_notForClientCall(this);
        for (int i = 0; i < 20; i++) {
            this.userActions[i] = -1;
        }
        if (!Live2D.L2D_RANGE_CHECK_POINT) {
            UtDebug.error("RANGE_CHECK_POINTをオンにしない場合 BarcodeKanojoのモデルは崩れる場合があります");
        }
    }

    public AndroidEAGLView createView(Context a, Rect rect) {
        if (this.glView == null) {
            this.glView = new AndroidEAGLView(this, a);
        }
        if (this.accelHelper == null) {
            this.accelHelper = new AccelHelper(a);
            this.accelHelper.setAccelListener((a1, a2, a3) -> {
				AndroidES1Renderer ren;
				try {
					if (KanojoLive2D.this.glView != null && (ren = KanojoLive2D.this.glView.getMyRenderer()) != null) {
						ren.setCurAccel(a1, a2, a3);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
        }
        return this.glView;
    }

    public void releaseView() {
        if (this.glView != null) {
            this.glView = null;
        }
        if (this.accelHelper != null) {
            this.accelHelper.stop();
            this.accelHelper = null;
        }
    }

    public void startAnimation() {
        if (this.glView != null) {
            this.glView.startAnimation();
            if (this.accelHelper != null) {
                this.accelHelper.start();
            }
        }
    }

    public void stopAnimation() {
        if (this.glView != null) {
            this.glView.stopAnimation();
            if (this.accelHelper != null) {
                this.accelHelper.stop();
            }
        }
    }

    public KanojoSetting getKanojoSetting() {
        return this.kanojoSetting;
    }

    public KanojoModel getKanojoModel(GL10 gl) throws Exception {
        if (this.dirtyFlag) {
            setupModel_withGL(gl);
        }
        return this.kanojoModel;
    }

    public KanojoAnimation getAnimation() {
        if (this.kanojoModel == null) {
            return null;
        }
        return this.kanojoModel.getAnimation();
    }

    public boolean isModelUpdating() {
        return this.modelUpdating;
    }

    public boolean isModelAvailable() {
        return this.modelAvailable;
    }

    public int getTextureSize() {
        return this.textureSize;
    }

    public void setTextureSize(int size) {
        this.textureSize = size;
    }

    public boolean setupModel(boolean multithread) {
        try {
            setupModel_exe(multithread);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void setupModel_exe(boolean multithread) throws Exception {
        this.dirtyFlag = true;
        this.process1Finished = false;
        this.modelAvailable = false;
        if (this.kanojoModel == null) {
            this.kanojoModel = new KanojoModel(this);
            this.kanojoModel.setupAnimation(this);
        }
        if (!multithread) {
            this.kanojoModel.setupMotionData(this);
            this.kanojoModel.setupModel_process1(this);
            this.process1Finished = true;
            return;
        }
        Thread t = new Thread() {
            public void run() {
                try {
                    KanojoModel km = KanojoLive2D.this.kanojoModel;
                    if (km != null) {
                        km.setupModel_process1(KanojoLive2D.this);
                        KanojoLive2D.this.process1Finished = true;
                        Thread.sleep(1000);
                        if (KanojoLive2D.this.kanojoModel != null && km == KanojoLive2D.this.kanojoModel) {
                            km.setupMotionData(KanojoLive2D.this);
                        }
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        };
        t.setPriority(10);
        t.start();
    }

    private boolean setupModel_withGL(GL10 gl) throws Exception {
        if (!this.dirtyFlag) {
            return true;
        }
        if (!this.process1Finished) {
            return false;
        }
        if (this.modelUpdating) {
            return false;
        }
        this.modelUpdating = true;
        this.kanojoModel.setupModel_process2(gl);
        this.modelUpdating = false;
        this.dirtyFlag = false;
        this.modelAvailable = true;
        return true;
    }

    public void releaseModel() {
        this.modelAvailable = false;
        if (this.kanojoModel != null) {
            this.kanojoModel.releaseModel();
            this.kanojoModel = null;
        }
    }

    public Bitmap createIcon(int width, int height, float scale, float offset_x01, float offset_y01, int iconFlags) {
        return IconRenderer.createIcon(this, width, height, scale, offset_x01, offset_y01, iconFlags);
    }

    public void setPartsCacheDirectory(String path) {
        this.partsCacheDir = path;
    }

    public String getPartsCacheDirectory() {
        return this.partsCacheDir;
    }

    public boolean isAvailableParts(String partsID, int partsItemNo) {
        String bkPartsData = KanojoPartsItem.getBkPartsData_resource(KanojoPartsItem.getPartsDir(KanojoResource.AVATAR_DATA_DIR, partsID, partsItemNo));
        if (bkPartsData != null && this.fileManager.exists_resource(bkPartsData)) {
            return true;
        }
        if (this.partsCacheDir == null) {
            UtDebug.error("PartsCacheDir が設定されていませんsetPartsCacheDirectory()でフォルダを設定して下さい");
        } else {
            String bkPartsData2 = KanojoPartsItem.getBkPartsData_cache(KanojoPartsItem.getPartsDir(this.partsCacheDir, partsID, partsItemNo));
            if (bkPartsData2 != null && this.fileManager.exists_cache(bkPartsData2)) {
                return true;
            }
        }
        return false;
    }

    public boolean setBackgroundImage(String filepath, boolean isCache) {
        return setBackgroundImage(filepath, isCache, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f);
    }

    private boolean setBackgroundImage(String filepath, boolean isCache, float sx, float sy, float sw, float sh, float dx, float dy, float dw, float dh) {
        return this.glView.getMyRenderer().setBackgroundImage(filepath, isCache, sx, sy, sw, sh, dx, dy, dw, dh);
    }

    public void setInRoom(boolean _inRoom) {
        this.inRoom = _inRoom;
    }

    public boolean isInRoom() {
        return this.inRoom;
    }

    public int[] getUserActions() {
        int cnt = 0;
        for (int i = 0; i < 20; i++) {
            if (this.userActions[((this.curUserActionNo - i) + 20) % 20] < 0) {
                break;
            }
            cnt++;
        }
        int[] ret_array = new int[cnt];
        for (int i2 = 0; i2 < cnt; i2++) {
            ret_array[i2] = this.userActions[((this.curUserActionNo - i2) + 20) % 20];
        }
        for (int i3 = 0; i3 < 20; i3++) {
            this.userActions[i3] = -1;
        }
        return ret_array;
    }

    public void releaseActions(int[] actions) {
    }

    public boolean addUserAction_notForClientCall(int userAction) {
        int state = this.kanojoSetting.getKanojoState();
        if (!isInRoom()) {
            return false;
        }
		if (state == 1) {
			return false;
		}
		this.curUserActionNo = (this.curUserActionNo + 1) % 20;
		this.userActions[this.curUserActionNo] = userAction;
		if (this.kanojoRoomActivity != null) {
			this.kanojoRoomActivity.callUserAction(userAction);
		}
		return true;
	}

    public void addPositionTouch(float x, float y, int type) {
        if (this.kanojoRoomActivity != null) {
            this.kanojoRoomActivity.setAnimationTap(x, y, type);
        }
    }

    public KanojoFileManager getFileManager() {
        return this.fileManager;
    }

    public void releaseFileManager() {
        this.fileManager.release();
        this.fileManager = null;
    }
}
