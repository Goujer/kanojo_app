package jp.live2d.io;

import android.support.v4.view.MotionEventCompat;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import jp.live2d.base.LDAffineTransform;
import jp.live2d.id.BaseDataID;
import jp.live2d.id.DrawDataID;
import jp.live2d.id.ParamID;
import jp.live2d.id.PartsDataID;
import jp.live2d.type.LDColor;
import jp.live2d.type.LDPoint;
import jp.live2d.type.LDPointF;
import jp.live2d.type.LDRect;
import jp.live2d.type.LDRectF;

public class BReader {
    InputStream bin;
    int bitBuff = 0;
    int bitCount = 0;
    byte[] buf = new byte[8];
    byte[] cache = new byte[1000];
    int formatVersion = 0;
    ArrayList<Object> loadObjects = new ArrayList<>();
    ByteBuffer tmpBuf = ByteBuffer.wrap(this.buf);

    public BReader(InputStream in) {
        this.bin = in;
    }

    public BReader(InputStream in, int version) {
        this.formatVersion = version;
        this.bin = in;
    }

    public int getFormatVersion() {
        return this.formatVersion;
    }

    public void setFormatVersion(int version) {
        this.formatVersion = version;
    }

    public int readNum() throws Exception {
        return bytesToNum(this.bin);
    }

    public static int bytesToNum(InputStream bin2) throws Exception {
        int b1 = bin2.read();
        if ((b1 & 128) == 0) {
            return b1 & MotionEventCompat.ACTION_MASK;
        }
        int b2 = bin2.read();
        if ((b2 & 128) == 0) {
            return ((b1 & 127) << 7) | (b2 & 127);
        }
        int b3 = bin2.read();
        if ((b3 & 128) == 0) {
            return ((b1 & 127) << 14) | ((b2 & 127) << 7) | (b3 & MotionEventCompat.ACTION_MASK);
        }
        int b4 = bin2.read();
        if ((b4 & 128) == 0) {
            return ((b1 & 127) << 21) | ((b2 & 127) << 14) | ((b3 & 127) << 7) | (b4 & MotionEventCompat.ACTION_MASK);
        }
        throw new Exception("未対応 _");
    }

    public double readDouble() throws Exception {
        checkBits();
        this.bin.read(this.buf, 0, 8);
        this.tmpBuf.position(0);
        return this.tmpBuf.getDouble();
    }

    public float readFloat() throws Exception {
        checkBits();
        this.bin.read(this.buf, 0, 4);
        this.tmpBuf.position(0);
        return this.tmpBuf.getFloat();
    }

    public int readInt() throws Exception {
        checkBits();
        this.bin.read(this.buf, 0, 4);
        this.tmpBuf.position(0);
        return this.tmpBuf.getInt();
    }

    public byte readByte() throws Exception {
        checkBits();
        return (byte) this.bin.read();
    }

    public short readShort() throws Exception {
        checkBits();
        this.bin.read(this.buf, 0, 2);
        this.tmpBuf.position(0);
        return this.tmpBuf.getShort();
    }

    public long readLong() throws Exception {
        checkBits();
        this.bin.read(this.buf, 0, 8);
        this.tmpBuf.position(0);
        return this.tmpBuf.getLong();
    }

    public boolean readBoolean() throws Exception {
        checkBits();
        return this.bin.read() != 0;
    }

    public String readUTF8() throws Exception {
        checkBits();
        int bytes = readNum();
        if (this.cache.length < bytes) {
            this.cache = new byte[bytes];
        }
        this.bin.read(this.cache, 0, bytes);
        return new String(this.cache, 0, bytes, "UTF-8");
    }

    public int[] readArrayInt() throws Exception {
        checkBits();
        int num = readNum();
        int[] ret = new int[num];
        for (int i = 0; i < num; i++) {
            ret[i] = readInt();
        }
        return ret;
    }

    public float[] readArrayFloat() throws Exception {
        checkBits();
        int num = readNum();
        float[] ret = new float[num];
        for (int i = 0; i < num; i++) {
            ret[i] = readFloat();
        }
        return ret;
    }

    public double[] readArrayDouble() throws Exception {
        checkBits();
        int num = readNum();
        double[] ret = new double[num];
        for (int i = 0; i < num; i++) {
            ret[i] = readDouble();
        }
        return ret;
    }

    public Object readObject() throws Exception {
        return readObject_exe1(-1);
    }

    public Object readObject_exe1(int cno) throws Exception {
        checkBits();
        if (cno < 0) {
            cno = readNum();
        }
        if (cno == 33) {
            int refNo = readInt();
            if (refNo >= 0 && refNo < this.loadObjects.size()) {
                return this.loadObjects.get(refNo);
            }
            throw new Exception("illegal refNo @Breader");
        }
        Object o = readObject_exe2(cno);
        this.loadObjects.add(o);
        return o;
    }

    public Object readObject_exe2(int cno) throws Exception {
        if (cno == 0) {
            return null;
        }
        if (cno == 50) {
            return DrawDataID.getID(readUTF8());
        }
        if (cno == 51) {
            return BaseDataID.getID(readUTF8());
        }
        if (cno == 134) {
            return PartsDataID.getID(readUTF8());
        }
        if (cno == 60) {
            return ParamID.getID(readUTF8());
        }
        if (cno >= 48) {
            ISerializableV2 e = (ISerializableV2) FileFormat2.newInstance(cno);
            if (e == null) {
                return null;
            }
            e.readV2(this);
            return e;
        }
        switch (cno) {
            case 1:
                return readUTF8();
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 18:
            case 19:
            case 20:
            case 24:
            case 28:
                throw new Exception("not impl : readObject() no is " + cno + " of {2-9 ,18,19,20,24,28}");
            case 10:
                return new LDColor(readInt(), true);
            case 11:
                return new LDRectF((float) readDouble(), (float) readDouble(), (float) readDouble(), (float) readDouble());
            case 12:
                return new LDRectF(readFloat(), readFloat(), readFloat(), readFloat());
            case 13:
                return new LDPointF((float) readDouble(), (float) readDouble());
            case 14:
                return new LDPointF(readFloat(), readFloat());
            case 15:
                int num = readNum();
                ArrayList<Object> ret = new ArrayList<>(num);
                for (int i = 0; i < num; i++) {
                    ret.add(readObject());
                }
                return ret;
            case 16:
            case 25:
                return readArrayInt();
            case 17:
                return new LDAffineTransform((float) readDouble(), (float) readDouble(), (float) readDouble(), (float) readDouble(), (float) readDouble(), (float) readDouble());
            case 21:
                return new LDRect(readInt(), readInt(), readInt(), readInt());
            case 22:
                return new LDPoint(readInt(), readInt());
            case 23:
                throw new RuntimeException("未実装 _");
            case 26:
                return readArrayDouble();
            case 27:
                return readArrayFloat();
            default:
                throw new Exception("not impl : readObject() NO DEF");
        }
    }

    public boolean readBit() throws Exception {
        if (this.bitCount == 0) {
            this.bitBuff = this.bin.read();
        } else if (this.bitCount == 8) {
            this.bitBuff = this.bin.read();
            this.bitCount = 0;
        }
        int i = this.bitBuff;
        int i2 = this.bitCount;
        this.bitCount = i2 + 1;
        if (((i >> (7 - i2)) & 1) == 1) {
            return true;
        }
        return false;
    }

    private void checkBits() {
        if (this.bitCount != 0) {
            this.bitCount = 0;
        }
    }
}
