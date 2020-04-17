package jp.live2d.id;

public class ID {
    protected String id;

    public String toString() {
        return this.id;
    }

    public static void releaseStored_notForClientCall() {
        ParamID.releaseStored_exe_notForClientCall();
        BaseDataID.releaseStored_exe_notForClientCall();
        DrawDataID.releaseStored_exe_notForClientCall();
        PartsDataID.releaseStored_exe_notForClientCall();
    }
}
