package jp.co.cybird.barcodekanojoForGAM.live2d.view;

import android.opengl.GLSurfaceView;
import java.io.FileInputStream;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import jp.co.cybird.barcodekanojoForGAM.live2d.KanojoLive2D;
import jp.co.cybird.barcodekanojoForGAM.live2d.model.KanojoModel;
import jp.co.cybird.barcodekanojoForGAM.live2d.motion.KanojoAnimation;
import jp.live2d.android.Live2DModelAndroid;
import jp.live2d.android.UtOpenGL;
import jp.live2d.type.LDRect;
import jp.live2d.type.LDRectF;
import jp.live2d.util.UtSystem;

public class AndroidES1Renderer implements GLSurfaceView.Renderer {
    static final int BASE_MODEL_CANVAS_W = 1280;
    static final int DEFAULT_VISIBLE_HEIGHT = 1200;
    static final int DEFAULT_VISIBLE_OFFSET_Y = 0;
    static final int RENDER_SETUP_INTERVAL = 60;
    private static float acceleration_x = 0.0f;
    private static float acceleration_y = 0.0f;
    private static float acceleration_z = 0.0f;
    private static float dst_acceleration_x = 0.0f;
    private static float dst_acceleration_y = 0.0f;
    private static float dst_acceleration_z = 0.0f;
    private static float lastMove;
    private static long lastTimeMSec = -1;
    private static float last_dst_acceleration_x = 0.0f;
    private static float last_dst_acceleration_y = 0.0f;
    private static float last_dst_acceleration_z = 0.0f;
    private final float MAX_ACCEL_D = 0.04f;
    private float[] accel = new float[3];
    private LDRectF backDstR = new LDRectF(0.0f, 0.0f, 1.0f, 1.0f);
    private int backImage2;
    private boolean backImageIsCache = false;
    private String backImagePath;
    private boolean backImageUpdated;
    private LDRectF backSrcR = new LDRectF(0.0f, 0.0f, 1.0f, 1.0f);
    private int backingHeight;
    private int backingWidth;
    private GL10 gl;
    private KanojoLive2D kanojoLive2D;
    float[] lastAccel = new float[3];
    private float logicalH;
    private float logicalW;
    Live2DModelAndroid model;
    private int renderCount;
    float targetX;
    float targetY;
    private AndroidEAGLView view;
    private LDRect visibleRect = new LDRect();

    AndroidES1Renderer(KanojoLive2D kanojoLive2D2, AndroidEAGLView view2) {
        this.kanojoLive2D = kanojoLive2D2;
        this.view = view2;
        setBackgroundImage(null, false, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f);
    }

    private float fabs(float v) {
        return v > 0.0f ? v : -v;
    }

    private KanojoAnimation getAnimation() {
        if (this.kanojoLive2D == null) {
            return null;
        }
        return this.kanojoLive2D.getAnimation();
    }

    public void setCurAccel(float a1, float a2, float a3) {
        dst_acceleration_x = a1;
        dst_acceleration_y = a2;
        dst_acceleration_z = a3;
        lastMove = (lastMove * 0.7f) + (0.3f * (fabs(dst_acceleration_x - last_dst_acceleration_x) + fabs(dst_acceleration_y - last_dst_acceleration_y) + fabs(dst_acceleration_z - last_dst_acceleration_z)));
        last_dst_acceleration_x = dst_acceleration_x;
        last_dst_acceleration_y = dst_acceleration_y;
        last_dst_acceleration_z = dst_acceleration_z;
        if (lastMove > 1.5f) {
            KanojoAnimation ka = getAnimation();
            if (ka != null) {
                ka.shakeEvent();
            }
            lastMove = 0.0f;
        }
    }

	private void updateAccel() {
        float dx = dst_acceleration_x - acceleration_x;
        float dy = dst_acceleration_y - acceleration_y;
        float dz = dst_acceleration_z - acceleration_z;
        if (dx > 0.04f) {
            dx = 0.04f;
        }
        if (dx < -0.04f) {
            dx = -0.04f;
        }
        if (dy > 0.04f) {
            dy = 0.04f;
        }
        if (dy < -0.04f) {
            dy = -0.04f;
        }
        if (dz > 0.04f) {
            dz = 0.04f;
        }
        if (dz < -0.04f) {
            dz = -0.04f;
        }
        acceleration_x += dx;
        acceleration_y += dy;
        acceleration_z += dz;
        long time = UtSystem.getTimeMSec();
        lastTimeMSec = time;
        float scale = ((0.2f * ((float) (time - lastTimeMSec))) * 60.0f) / 1000.0f;
        if (scale > 0.5f) {
            scale = 0.5f;
        }
        this.accel[0] = (acceleration_x * scale) + (this.accel[0] * (1.0f - scale));
        this.accel[1] = (acceleration_y * scale) + (this.accel[1] * (1.0f - scale));
        this.accel[2] = (acceleration_z * scale) + (this.accel[2] * (1.0f - scale));
    }

    public GL10 getGL() {
        return this.gl;
    }

    public void onDrawFrame(GL10 gl2) {
        if (this.logicalW > 0.0f && this.logicalH > 0.0f) {
            int i = this.renderCount;
            this.renderCount = i + 1;
            if (i % 60 == 0) {
                gl2.glViewport(0, 0, this.backingWidth, this.backingHeight);
                gl2.glMatrixMode(5889);
                gl2.glLoadIdentity();
                float marginW = 0.5f * (1280.0f - this.logicalW);
				this.visibleRect.setRect((int) marginW, 0, (int) this.logicalW, (int) this.logicalH);
                gl2.glOrthof(marginW, marginW + this.logicalW, 0.0f + this.logicalH, 0.0f, 0.5f, -0.5f);
                gl2.glMatrixMode(5888);
                gl2.glLoadIdentity();
                gl2.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
            }
            gl2.glClear(16384);
            renderMain(gl2);
        }
    }

	private void renderMain(GL10 gl2) {
        gl2.glEnable(3042);
        gl2.glDisable(2929);
        gl2.glBlendFunc(1, 771);
        gl2.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        gl2.glDisable(2884);
        gl2.glMatrixMode(5888);
        gl2.glLoadIdentity();
        gl2.glEnable(3553);
        gl2.glEnableClientState(32888);
        gl2.glEnableClientState(32884);
        if (this.backImageUpdated) {
            if (this.backImage2 > 0) {
                gl2.glDeleteTextures(1, new int[]{this.backImage2}, 0);
            }
            try {
                if (this.backImageIsCache) {
                    this.backImage2 = UtOpenGL.loadTexture(gl2, new FileInputStream(this.backImagePath), true);
                } else {
                    this.backImage2 = UtOpenGL.loadTexture(gl2, this.view.getContext(), this.backImagePath, true);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            this.backImageUpdated = false;
        }
        updateAccel();
        float ACCEL_PIX = this.logicalW / 6.0f;
        gl2.glPushMatrix();
        gl2.glTranslatef((-1.7f) * ACCEL_PIX * this.accel[0], 0.8f * ACCEL_PIX * this.accel[1], 0.0f);
        gl2.glTranslatef(-200.0f, -200.0f, 0.0f);
        gl2.glScalef(1700.0f, 1700.0f, 1.0f);
		UtOpenGL.drawImage(gl, this.backImage2, this.backDstR.a/*getX()*/, this.backDstR.b/*getY()*/, this.backDstR.c/*getWidth()*/, this.backDstR.d/*getHeight()*/, this.backSrcR.a/*getX()*/, this.backSrcR.b/*getY()*/, this.backSrcR.c/*getWidth()*/, this.backSrcR.d/*getHeight()*/);
        gl2.glPopMatrix();
        if (this.kanojoLive2D.isInRoom()) {
            gl2.glPushMatrix();
            try {
                KanojoModel kmodel = this.kanojoLive2D.getKanojoModel(gl2);
                if (kmodel != null && !this.kanojoLive2D.isModelUpdating() && kmodel.isModelInitialized()) {
                    kmodel.setAccelarationValue(this.accel);
                    try {
                        kmodel.drawModel(gl2);
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                }
            } catch (Exception e3) {
                e3.printStackTrace();
            }
            gl2.glPopMatrix();
        }
    }

    public void onSurfaceChanged(GL10 gl2, int width, int height) {
        this.backingWidth = width;
        this.backingHeight = height;
        this.logicalH = 1200.0f;
        this.logicalW = (this.logicalH * ((float) this.backingWidth)) / ((float) this.backingHeight);
        this.renderCount = 0;
    }

    public void onSurfaceCreated(GL10 gl2, EGLConfig arg1) {
        this.gl = gl2;
        this.backImageUpdated = true;
    }

    public boolean setBackgroundImage(String filepath, boolean isCache, float sx, float sy, float sw, float sh, float dx, float dy, float dw, float dh) {
		this.backSrcR.setRect(sx, sy, sw, sh);
		this.backDstR.setRect(sx, sy, sw, sh);	//TODO: This might not be the correct parameters
        if (filepath == null) {
            filepath = "back256.png";
        }
        this.backImagePath = filepath;
        this.backImageIsCache = isCache;
        this.backImageUpdated = true;
        return true;
    }

	float viewToLogicalX(float x) {
		return ((float) this.visibleRect.a/*getX()*/) + (((float) this.visibleRect.c/*getWidth()*/) * (x / ((float) this.backingWidth)));
	}

	float viewToLogicalY(float y) {
		return ((float) this.visibleRect.b/*getY()*/) + (((float) this.visibleRect.d/*getHeight()*/) * (y / ((float) this.backingHeight)));
	}
}
