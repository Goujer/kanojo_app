package jp.live2d.android;

import java.io.InputStream;
import javax.microedition.khronos.opengles.GL10;
import jp.live2d.ALive2DModel;
import jp.live2d.util.UtDebug;

public class Live2DModelAndroid extends ALive2DModel {
    DrawParam_Android drawParamAndroid = new DrawParam_Android();

    Live2DModelAndroid() {
    }

    public void setGL(GL10 gl) {
        this.drawParamAndroid.setGL(gl);
    }

    public void draw() {
        this.modelContext.draw(this.drawParamAndroid);
    }

    public void setTexture(int textureNo, int openGLTextureNo) {
        if (this.drawParamAndroid == null) {
            UtDebug.error("LIVE2D for QT ERROR / setQtWidget() is not called!!", new Object[0]);
        }
        this.drawParamAndroid.setTexture(textureNo, openGLTextureNo);
    }

    public int generateModelTextureNo() {
        return this.drawParamAndroid.generateModelTextureNo();
    }

    public void releaseModelTextureNo(int no) {
        this.drawParamAndroid.releaseModelTextureNo(no);
    }

    public static Live2DModelAndroid loadModel(String filepath) throws Exception {
        Live2DModelAndroid model = new Live2DModelAndroid();
        ALive2DModel.loadModel_exe((ALive2DModel) model, filepath);
        return model;
    }

    public static Live2DModelAndroid loadModel(InputStream bin) throws Exception {
        Live2DModelAndroid model = new Live2DModelAndroid();
        ALive2DModel.loadModel_exe((ALive2DModel) model, bin);
        return model;
    }
}
