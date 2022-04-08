package jp.co.cybird.barcodekanojoForGAM.live2d.model;

import android.graphics.Bitmap;
import java.nio.IntBuffer;
import java.util.Random;
import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.egl.EGLSurface;
import javax.microedition.khronos.opengles.GL10;
import jp.co.cybird.barcodekanojoForGAM.live2d.KanojoLive2D;
import jp.co.cybird.barcodekanojoForGAM.live2d.KanojoSetting;
import jp.live2d.ALive2DModel;
import jp.live2d.util.UtDebug;

public class IconRenderer {
    static Random rand = new Random(System.currentTimeMillis());

    static float getRandF(float min, float max) {
        return ((max - min) * rand.nextFloat()) + min;
    }

    public static Bitmap createIcon(KanojoLive2D kanojoLive2D, int width, int height, float scale, float offset_x01, float offset_y01, int iconFlags) {
        try {
            return createIcon_exe(kanojoLive2D, width, height, scale, offset_x01, offset_y01, iconFlags);
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Bitmap createIcon_exe(KanojoLive2D kanojoLive2D, int width, int height, float scale, float offset_x01, float offset_y01, int iconFlags) {
        float back;
        ALive2DModel model;
        EGL10 egl = (EGL10) EGLContext.getEGL();
        EGLDisplay eglDisplay = egl.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);
        egl.eglInitialize(eglDisplay, new int[2]);
        int[] attribList2 = new int[13];
        attribList2[0] = 12325;
        attribList2[2] = 12326;
        attribList2[4] = 12324;
        attribList2[5] = 8;
        attribList2[6] = 12323;
        attribList2[7] = 8;
        attribList2[8] = 12322;
        attribList2[9] = 8;
        attribList2[10] = 12321;
        attribList2[11] = 8;
        attribList2[12] = 12344;
        int[] numConfig = new int[1];
        egl.eglChooseConfig(eglDisplay, attribList2, (EGLConfig[]) null, 0, numConfig);
        int configSize = numConfig[0];
        EGLConfig[] eglConfigs = new EGLConfig[configSize];
        egl.eglChooseConfig(eglDisplay, attribList2, eglConfigs, configSize, numConfig);
        EGLConfig eglConfig = eglConfigs[0];
        EGLContext eglContext = egl.eglCreateContext(eglDisplay, eglConfig, EGL10.EGL_NO_CONTEXT, (int[]) null);
        EGLSurface eglSurface = egl.eglCreatePbufferSurface(eglDisplay, eglConfig, new int[]{12375, width, 12374, height, 12344});
        egl.eglMakeCurrent(eglDisplay, eglSurface, eglSurface, eglContext);
        GL10 gl = (GL10) eglContext.getGL();
        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(5889);
        gl.glLoadIdentity();
        gl.glOrthof(0.0f, 1280.0f, (float) ((height * 1280) / width), 0.0f, 0.5f, -0.5f);
        gl.glMatrixMode(5888);
        gl.glLoadIdentity();
        if ((iconFlags & 1) != 0) {
            back = 0.5019608f;
        } else {
            back = 1.0f;
        }
        gl.glClearColor(back, back, back, 0.0f);
        gl.glClear(16384);
        KanojoSetting kanojoSetting = kanojoLive2D.getKanojoSetting();
        boolean lastSilhouetteMode = kanojoSetting.isSilhouetteMode();
        kanojoSetting.setSilhouetteMode_notForClientCall((iconFlags & 1) != 0);
        try {
            kanojoLive2D.setupModel_exe(false);
            try {
                KanojoModel kanojoModel = kanojoLive2D.getKanojoModel(gl);
                if (kanojoModel == null) {
                    UtDebug.error("kanojoModel not intiailzed at KanojoLive2D#createIcon()");
                    return null;
                }
                if ((iconFlags & 2) == 0 && (model = kanojoModel.getModel()) != null) {
                    model.setParamFloat("PARAM_ANGLE_X", getRandF(-30.0f, 30.0f));
                    model.setParamFloat("PARAM_ANGLE_Y", getRandF(-30.0f, 30.0f));
                    model.setParamFloat("PARAM_ANGLE_Z", getRandF(-30.0f, 30.0f));
                    model.setParamFloat("PARAM_BODY_ANGLE_X", getRandF(-10.0f, 10.0f));
                    model.setParamFloat("PARAM_EYE_BALL_X", getRandF(-0.8f, 0.8f));
                    model.setParamFloat("PARAM_EYE_BALL_Y", getRandF(-0.8f, 0.8f));
                }
                gl.glEnable(3042);
                gl.glDisable(2929);
                gl.glBlendFunc(1, 771);
                gl.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
                gl.glDisable(2884);
                gl.glMatrixMode(5888);
                gl.glLoadIdentity();
                gl.glEnable(3553);
                gl.glTexParameterx(3553, 10242, 10497);
                gl.glTexParameterx(3553, 10243, 10497);
                gl.glEnableClientState(32888);
                gl.glEnableClientState(32884);
                gl.glPushMatrix();
                gl.glTranslatef(640.0f * offset_x01, 640.0f * offset_y01, 0.0f);
                gl.glTranslatef(640.0f, 640.0f, 0.0f);
                gl.glScalef(scale * 1.5f, scale * 1.5f, 1.0f);
                gl.glTranslatef(-640.0f, -640.0f, 0.0f);
                gl.glTranslatef(0.0f, 150.0f, 0.0f);
                try {
                    kanojoModel.drawModel_core(gl);
                } catch (Exception e) {
                    UtDebug.error("failed to draw icon %s  @IconRenderer\n", e.getMessage());
                }
                gl.glPopMatrix();
                IntBuffer ib = IntBuffer.allocate(width * height);
                gl.glReadPixels(0, 0, width, height, 6408, 5121, ib);
                int[] colors = new int[(width * height)];
                for (int i = 0; i < height; i++) {
                    for (int j = 0; j < width; j++) {
                        int c = ib.get((i * width) + j);
                        colors[(((height - i) - 1) * width) + j] = (((c >>> 24) & 255) << 24) | (((c >>> 16) & 255) << 0) | (((c >>> 8) & 255) << 8) | (((c >>> 0) & 255) << 16);
                    }
                }
                ib.clear();
                egl.eglMakeCurrent(eglDisplay, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_CONTEXT);
                egl.eglDestroyContext(eglDisplay, eglContext);
                egl.eglDestroySurface(eglDisplay, eglSurface);
                egl.eglTerminate(eglDisplay);
                kanojoLive2D.releaseModel();
                kanojoLive2D.releaseFileManager();
                kanojoSetting.setSilhouetteMode_notForClientCall(lastSilhouetteMode);
                return Bitmap.createBitmap(colors, width, height, Bitmap.Config.ARGB_8888);
            } catch (Exception e1) {
                e1.printStackTrace();
                return null;
            }
        } catch (Exception e2) {
            UtDebug.error("setupModel throws exception :: %s @IconRenderer\n", e2.getMessage());
            return null;
        }
    }
}
