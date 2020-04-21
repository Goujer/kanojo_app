package jp.live2d.base;

import jp.live2d.io.BReader;
import jp.live2d.io.ISerializableV2;

public class AffineEnt implements ISerializableV2 {
    float originX;
    float originY;
    float rotateDeg;
    float scaleX;
    float scaleY;

    public AffineEnt() {
        this.originX = 0.0f;
        this.originY = 0.0f;
        this.scaleX = 1.0f;
        this.scaleY = 1.0f;
        this.rotateDeg = 0.0f;
    }

    public AffineEnt(float _originx, float _originy, float _scalex, float _scaley, float _rotateDeg) {
        this.originX = _originx;
        this.originY = _originy;
        this.scaleX = _scalex;
        this.scaleY = _scaley;
        this.rotateDeg = _rotateDeg;
    }

    /* access modifiers changed from: package-private */
    public void init(AffineEnt ent) {
        this.originX = ent.originX;
        this.originY = ent.originY;
        this.scaleX = ent.scaleX;
        this.scaleY = ent.scaleY;
        this.rotateDeg = ent.rotateDeg;
    }

    public void readV2(BReader br) throws Exception {
        this.originX = br.readFloat();
        this.originY = br.readFloat();
        this.scaleX = br.readFloat();
        this.scaleY = br.readFloat();
        this.rotateDeg = br.readFloat();
    }

    /* access modifiers changed from: package-private */
    public void DUMP() {
        System.out.printf("原点(%7.2f,%7.2f) 拡大(%7.2f,%7.2f) 回転(%5.1f度)\n", new Object[]{Float.valueOf(this.originX), Float.valueOf(this.originY), Float.valueOf(this.scaleX), Float.valueOf(this.scaleY), Float.valueOf(this.rotateDeg)});
    }
}
