package jp.live2d;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import jp.live2d.id.ParamID;
import jp.live2d.io.BReader;
import jp.live2d.model.ModelImpl;
import jp.live2d.util.UtDebug;

public abstract class ALive2DModel {
    public static final int FILE_LOAD_EOF_ERROR = 1;
    public static final int FILE_LOAD_VERSION_ERROR = 2;
    protected static int INSTANCE_COUNT = 0;
    protected int errorFlags = 0;
    protected ModelContext modelContext = null;
    protected ModelImpl modelImpl = null;

    public abstract void draw();

    public ALive2DModel() {
        INSTANCE_COUNT++;
        this.modelContext = new ModelContext(this);
    }

    public void setModelImpl(ModelImpl m) {
        this.modelImpl = m;
    }

    public ModelImpl getModelImpl() {
        if (this.modelImpl == null) {
            this.modelImpl = new ModelImpl();
            this.modelImpl.initDirect();
        }
        return this.modelImpl;
    }

    public float getCanvasWidth() {
        if (this.modelImpl == null) {
            return 0.0f;
        }
        return this.modelImpl.getCanvasWidth();
    }

    public float getCanvasHeight() {
        if (this.modelImpl == null) {
            return 0.0f;
        }
        return this.modelImpl.getCanvasHeight();
    }

    public float getParamFloat(String paramID) {
        return this.modelContext.getParamFloat(this.modelContext.getParamIndex(ParamID.getID(paramID)));
    }

    public void setParamFloat(String paramID, float value) {
        this.modelContext.setParamFloat(this.modelContext.getParamIndex(ParamID.getID(paramID)), value);
    }

    public void setParamFloat(String paramID, float value, float weight) {
        setParamFloat(this.modelContext.getParamIndex(ParamID.getID(paramID)), value, weight);
    }

    public void addToParamFloat(String paramID, float value, float weight) {
        addToParamFloat(this.modelContext.getParamIndex(ParamID.getID(paramID)), value, weight);
    }

    public void multParamFloat(String paramID, float mult, float weight) {
        multParamFloat(this.modelContext.getParamIndex(ParamID.getID(paramID)), mult, weight);
    }

    public void loadParam() {
        this.modelContext.loadParam();
    }

    public void saveParam() {
        this.modelContext.saveParam();
    }

    public void init() {
        this.modelContext.init();
    }

    public void update() throws Exception {
        this.modelContext.update();
    }

    public int generateModelTextureNo() {
        UtDebug.error("please override generateModelTextureNo()", new Object[0]);
        return -1;
    }

    public void releaseModelTextureNo(int no) {
        UtDebug.error("please override ALive2DModel#releaseModelTextureNo() \n", new Object[0]);
    }

    public static void loadModel_exe(ALive2DModel ret, String filepath) throws Exception {
        BufferedInputStream bin = new BufferedInputStream(new FileInputStream(filepath), 8192);
        loadModel_exe(ret, (InputStream) bin);
        bin.close();
    }

    public static void loadModel_exe(ALive2DModel ret, InputStream bin) throws Exception {
        BReader br = new BReader(bin);
        byte c0 = br.readByte();
        byte c1 = br.readByte();
        byte c2 = br.readByte();
        if (c0 == 109 && c1 == 111 && c2 == 99) {
            int formatVersion = br.readByte();
            br.setFormatVersion(formatVersion);
            if (formatVersion > 9) {
                ret.errorFlags |= 2;
                System.out.printf("Illegal data version ( available : %d < loaded . %d )@ALive2DModel#loadModel()\n", new Object[]{9, Integer.valueOf(formatVersion)});
                return;
            }
            ModelImpl model = (ModelImpl) br.readObject();
            if (formatVersion < 8 || br.readInt() == -2004318072) {
                ret.setModelImpl(model);
                ret.getModelContext().init();
                return;
            }
            ret.errorFlags |= 1;
            System.out.printf("Assert @ALive2DModel#loadModel() / EOF value check\n", new Object[0]);
            return;
        }
        throw new Exception("未対応バージョン");
    }

    public int getParamIndex(String paramID) {
        return this.modelContext.getParamIndex(ParamID.getID(paramID));
    }

    public float getParamFloat(int paramIndex) {
        return this.modelContext.getParamFloat(paramIndex);
    }

    public void setParamFloat(int paramIndex, float value) {
        this.modelContext.setParamFloat(paramIndex, value);
    }

    public void setParamFloat(int paramIndex, float value, float weight) {
        this.modelContext.setParamFloat(paramIndex, (this.modelContext.getParamFloat(paramIndex) * (1.0f - weight)) + (value * weight));
    }

    public void addToParamFloat(int paramIndex, float value, float weight) {
        this.modelContext.setParamFloat(paramIndex, this.modelContext.getParamFloat(paramIndex) + (value * weight));
    }

    public void multParamFloat(int paramIndex, float mult, float weight) {
        this.modelContext.setParamFloat(paramIndex, this.modelContext.getParamFloat(paramIndex) * (((mult - 1.0f) * weight) + 1.0f));
    }

    public ModelContext getModelContext() {
        return this.modelContext;
    }

    public int getErrorFlags() {
        return this.errorFlags;
    }
}
