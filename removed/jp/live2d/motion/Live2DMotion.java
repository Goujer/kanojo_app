package jp.live2d.motion;

import java.util.ArrayList;
import jp.live2d.ALive2DModel;
import jp.live2d.motion.MotionQueueManager;
import jp.live2d.util.UtString;

public class Live2DMotion extends AMotion {
    static final int LINE_HEAD = 1;
    private static final String MTN_PREFIX_LAYOUT = "LAYOUT:";
    private static final String MTN_PREFIX_PARTS_VISIBLE = "VISIBLE:";
    private static int objectCount = 0;
    private static transient FloatArray tmpFloatArray = new FloatArray();
    private int durationMSec;
    private boolean loop;
    private int maxLength;
    private ArrayList<Motion> motions = new ArrayList<>();
	private float srcFps;

    static class Motion {
        public static final int MOTION_TYPE_LAYOUT_ANCHOR_X = 102;
        public static final int MOTION_TYPE_LAYOUT_ANCHOR_Y = 103;
        public static final int MOTION_TYPE_LAYOUT_SCALE_X = 104;
        public static final int MOTION_TYPE_LAYOUT_SCALE_Y = 105;
        public static final int MOTION_TYPE_LAYOUT_X = 100;
        public static final int MOTION_TYPE_LAYOUT_Y = 101;
        public static final int MOTION_TYPE_PARAM = 0;
        public static final int MOTION_TYPE_PARTS_VISIBLE = 1;
        int motionType;
        String paramIDStr = null;
        float[] values;
    }

    public Live2DMotion() {
        int i = objectCount;
        objectCount = i + 1;
		this.srcFps = 30.0f;
        this.maxLength = 0;
        this.loop = true;
        this.durationMSec = -1;
        reinit();
    }

    public static Live2DMotion loadMotion(byte[] str) {
        int i = 0;
        Live2DMotion ret = new Live2DMotion();
        int[] ret_endpos = new int[1];
        int bufSize = str.length;
        ret.maxLength = 0;
        while (i < bufSize) {
            char c = (char) (str[i] & 255);
            if (!(c == 10 || c == 13)) {
                if (c == '#') {
                    while (i < bufSize && str[i] != 10 && str[i] != 13) {
                        i++;
                    }
                } else if (c == '$') {
                    int head = i;
                    int eqpos = -1;
                    while (true) {
                        if (i >= bufSize) {
                            break;
                        }
                        char c2 = (char) (str[i] & 255);
                        if (c2 == 13 || c2 == 10) {
                            break;
                        } else if (c2 == '=') {
                            eqpos = i;
                            break;
                        } else {
                            i++;
                        }
                    }
                    boolean is_fps = false;
                    if (eqpos >= 0) {
                        if (eqpos == head + 4 && str[head + 1] == Motion.MOTION_TYPE_LAYOUT_ANCHOR_X && str[head + 2] == 112 && str[head + 3] == 115) {
                            is_fps = true;
                        }
                        i = eqpos + 1;
                        while (i < bufSize) {
                            char c3 = (char) (str[i] & 255);
                            if (c3 == 13 || c3 == 10) {
                                break;
                            }
                            if (!(c3 == ',' || c3 == ' ' || c3 == 9)) {
                                float f = (float) UtString.strToDouble(str, bufSize, i, ret_endpos);
                                if (ret_endpos[0] > 0 && is_fps && 5.0f < f && f < 121.0f) {
                                    ret.srcFps = f;
                                }
                                i = ret_endpos[0];
                            }
                            i++;
                        }
                    }
                    while (i < bufSize && str[i] != 10 && str[i] != 13) {
                        i++;
                    }
                } else if (('a' <= c && c <= 'z') || (('A' <= c && c <= 'Z') || c == '_')) {
                    int head2 = i;
                    int eqpos2 = -1;
                    while (true) {
                        if (i >= bufSize) {
                            break;
                        }
                        char c4 = (char) (str[i] & 255);
                        if (c4 == 13 || c4 == 10) {
                            break;
                        } else if (c4 == '=') {
                            eqpos2 = i;
                            break;
                        } else {
                            i++;
                        }
                    }
                    if (eqpos2 >= 0) {
                        Motion ent = new Motion();
                        if (UtString.startsWith(str, head2, MTN_PREFIX_PARTS_VISIBLE)) {
                            ent.motionType = Motion.MOTION_TYPE_PARTS_VISIBLE;
                            ent.paramIDStr = new String(str, head2, eqpos2 - head2);
                        } else if (UtString.startsWith(str, head2, MTN_PREFIX_LAYOUT)) {
                            ent.paramIDStr = new String(str, head2 + 7, (eqpos2 - head2) - 7);
                            if (UtString.startsWith(str, head2 + 7, "ANCHOR_X")) {
                                ent.motionType = Motion.MOTION_TYPE_LAYOUT_ANCHOR_X;
                            } else if (UtString.startsWith(str, head2 + 7, "ANCHOR_Y")) {
                                ent.motionType = Motion.MOTION_TYPE_LAYOUT_ANCHOR_Y;
                            } else if (UtString.startsWith(str, head2 + 7, "SCALE_X")) {
                                ent.motionType = Motion.MOTION_TYPE_LAYOUT_SCALE_X;
                            } else if (UtString.startsWith(str, head2 + 7, "SCALE_Y")) {
                                ent.motionType = Motion.MOTION_TYPE_LAYOUT_SCALE_Y;
                            } else if (UtString.startsWith(str, head2 + 7, "X")) {
                                ent.motionType = Motion.MOTION_TYPE_LAYOUT_X;
                            } else if (UtString.startsWith(str, head2 + 7, "Y")) {
                                ent.motionType = Motion.MOTION_TYPE_LAYOUT_Y;
                            }
                        } else {
                            ent.motionType = Motion.MOTION_TYPE_PARAM;
                            ent.paramIDStr = new String(str, head2, eqpos2 - head2);
                        }
                        ret.motions.add(ent);
                        int valueCount = 0;
                        tmpFloatArray.clear();
                        int i3 = eqpos2 + 1;
                        while (true) {
                            if (i >= bufSize) {
                                break;
                            }
                            char c5 = (char) (str[i] & 255);
                            if (c5 == 13 || c5 == 10) {
                                break;
                            }
                            if (!(c5 == ',' || c5 == ' ' || c5 == 9)) {
                                float f2 = (float) UtString.strToDouble(str, bufSize, i, ret_endpos);
                                if (ret_endpos[0] > 0) {
                                    tmpFloatArray.add(f2);
                                    valueCount++;
                                    int nexti = ret_endpos[0];
                                    if (nexti < i) {
                                        System.out.printf("Illegal state . @Live2DMotion loadMotion()\n");
                                        break;
                                    }
                                    i = nexti;
                                } else {
                                    continue;
                                }
                            }
                            i3 = i + 1;
                        }
                        ent.values = tmpFloatArray.toArray();
                        if (valueCount > ret.maxLength) {
                            ret.maxLength = valueCount;
                        }
                    }
                }
            }
            int i2 = i + 1;
        }
        ret.durationMSec = (int) (((float) (ret.maxLength * 1000)) / ret.srcFps);
        return ret;
    }

    static class FloatArray {
        float[] buf = new float[100];
        int size = 0;

        FloatArray() {
        }

        /* access modifiers changed from: package-private */
        public void clear() {
            this.size = 0;
        }

        /* access modifiers changed from: package-private */
        public void add(float f) {
            if (this.buf.length <= this.size) {
                float[] tmp = new float[(this.size * 2)];
                System.arraycopy(this.buf, 0, tmp, 0, this.size);
                this.buf = tmp;
            }
            float[] fArr = this.buf;
            int i = this.size;
            this.size = i + 1;
            fArr[i] = f;
        }

        /* access modifiers changed from: package-private */
        public float[] toArray() {
            float[] ret = new float[this.size];
            System.arraycopy(this.buf, 0, ret, 0, this.size);
            return ret;
        }
    }

    public int getDurationMSec() {
        return this.durationMSec;
    }

    public void dump() {
        for (int i = 0; i < this.motions.size(); i++) {
            Motion m = this.motions.get(i);
            System.out.printf("paramID[%s] [%d]. ", m.paramIDStr, m.values.length);
            int j = 0;
            while (j < m.values.length && j < 10) {
                System.out.printf("%5.2f ,", m.values[j]);
                j++;
            }
			System.out.print("\n");
        }
    }

    public void updateParamExe(ALive2DModel model, long timeMSec, float weight, MotionQueueManager.MotionQueueEnt motionQueueEnt) {
        int i;
        int i2;
        float indexF = (((float) (timeMSec - motionQueueEnt.startTimeMSec)) * this.srcFps) / 1000.0f;
        int index = (int) indexF;
        float t = indexF - ((float) index);
        for (int i3 = 0; i3 < this.motions.size(); i3++) {
            Motion m = this.motions.get(i3);
            int vsize = m.values.length;
            String id = m.paramIDStr;
            if (m.motionType == Motion.MOTION_TYPE_PARTS_VISIBLE) {
                float[] fArr = m.values;
                if (index >= vsize) {
                    i2 = vsize - 1;
                } else {
                    i2 = index;
                }
                model.setParamFloat(id, fArr[i2]);
            } else if (Motion.MOTION_TYPE_LAYOUT_X > m.motionType || m.motionType > Motion.MOTION_TYPE_LAYOUT_SCALE_Y) {
                float sv = model.getParamFloat(id);
                float[] fArr2 = m.values;
                if (index >= vsize) {
                    i = vsize - 1;
                } else {
                    i = index;
                }
                float cv1 = fArr2[i];
                model.setParamFloat(id, sv + (((cv1 + ((m.values[index + 1 >= vsize ? vsize - 1 : index + 1] - cv1) * t)) - sv) * weight));
            }
        }
        if (index < this.maxLength) {
            return;
        }
        if (this.loop) {
            motionQueueEnt.startTimeMSec = timeMSec;
            motionQueueEnt.fadeInStartTimeMSec = timeMSec;
            return;
        }
        motionQueueEnt.finished = true;
    }

    public boolean isLoop() {
        return this.loop;
    }

    public void setLoop(boolean loop2) {
        this.loop = loop2;
    }
}
