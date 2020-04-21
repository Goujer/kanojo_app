package jp.live2d.physics;

import java.util.ArrayList;
import jp.live2d.ALive2DModel;
import jp.live2d.util.UtMath;

public class PhysicsHair {
    float airResistance = 0.0f;
    float angleP1toP2Deg = 0.0f;
    float angleP1toP2Deg_v = 0.0f;
    float baseLengthM = 0.0f;
    float gravityAngleDeg = 0.0f;
    long lastTime = 0;
    float last_angleP1toP2Deg = 0.0f;
    PhysicsPoint p1;
    PhysicsPoint p2;
    ArrayList<IPhysicsSrc> srcList;
    long startTime = 0;
    ArrayList<IPhysicsTarget> targetList;

    enum Src {
        SRC_TO_X,
        SRC_TO_Y,
        SRC_TO_G_ANGLE
    }

    enum Target {
        TARGET_FROM_ANGLE,
        TARGET_FROM_ANGLE_V
    }

    public PhysicsHair() {
        setup(0.3f, 0.5f, 0.1f);
    }

    public PhysicsHair(float _baseLengthM, float _airRegistance, float _mass) {
        setup(_baseLengthM, _airRegistance, _mass);
    }

    public void setup(float _baseLengthM, float _airRegistance, float _mass) {
        this.baseLengthM = _baseLengthM;
        this.airResistance = _airRegistance;
        this.p1.mass = _mass;
        this.p2.mass = _mass;
        this.p2.y = _baseLengthM;
        setup();
    }

    /* access modifiers changed from: package-private */
    public PhysicsPoint getPhysicsPoint1() {
        return this.p1;
    }

    /* access modifiers changed from: package-private */
    public PhysicsPoint getPhysicsPoint2() {
        return this.p2;
    }

    /* access modifiers changed from: package-private */
    public float getGravityAngleDeg() {
        return this.gravityAngleDeg;
    }

    /* access modifiers changed from: package-private */
    public void setGravityAngleDeg(float angleDeg) {
        this.gravityAngleDeg = angleDeg;
    }

    /* access modifiers changed from: package-private */
    public float getAngleP1toP2Deg() {
        return this.angleP1toP2Deg;
    }

    /* access modifiers changed from: package-private */
    public float getAngleP1toP2Deg_velocity() {
        return this.angleP1toP2Deg_v;
    }

    /* access modifiers changed from: package-private */
    public float calc_angleP1toP2() {
        return (float) ((-180.0d * Math.atan2((double) (this.p1.x - this.p2.x), (double) (-(this.p1.y - this.p2.y)))) / 3.141592653589793d);
    }

    public void setup() {
        this.last_angleP1toP2Deg = calc_angleP1toP2();
        this.p2.setupLast();
    }

    public void addSrcParam(Src srcType, String paramID, float scale, float weight) {
        this.srcList.add(new PhysicsSrc(srcType, paramID, scale, weight));
    }

    public void addTargetParam(Target targetType, String paramID, float scale, float weight) {
        this.targetList.add(new PhysicsTarget(targetType, paramID, scale, weight));
    }

    public void update(ALive2DModel model, long time) {
        if (this.startTime == 0) {
            this.lastTime = time;
            this.startTime = time;
            this.baseLengthM = (float) Math.sqrt((double) (((this.p1.x - this.p2.x) * (this.p1.x - this.p2.x)) + ((this.p1.y - this.p2.y) * (this.p1.y - this.p2.y))));
            return;
        }
        float dt = ((float) (time - this.lastTime)) / 1000.0f;
        if (dt != 0.0f) {
            for (int i = this.srcList.size() - 1; i >= 0; i--) {
                this.srcList.get(i).updateSrc(model, this);
            }
            update_exe(model, dt);
            this.angleP1toP2Deg = calc_angleP1toP2();
            this.angleP1toP2Deg_v = (this.angleP1toP2Deg - this.last_angleP1toP2Deg) / dt;
            this.last_angleP1toP2Deg = this.angleP1toP2Deg;
        }
        for (int i2 = this.targetList.size() - 1; i2 >= 0; i2--) {
            this.targetList.get(i2).updateTarget(model, this);
        }
        this.lastTime = time;
    }

    /* access modifiers changed from: package-private */
    public void update_exe(ALive2DModel model, float dt) {
        float dt_div = 1.0f / dt;
        this.p1.vx = (this.p1.x - this.p1.last_x) * dt_div;
        this.p1.vy = (this.p1.y - this.p1.last_y) * dt_div;
        this.p1.ax = (this.p1.vx - this.p1.last_vx) * dt_div;
        this.p1.ay = (this.p1.vy - this.p1.last_vy) * dt_div;
        this.p1.fx = this.p1.ax * this.p1.mass;
        this.p1.fy = this.p1.ay * this.p1.mass;
        this.p1.setupLast();
        float Q = -((float) Math.atan2((double) (this.p1.y - this.p2.y), (double) (this.p1.x - this.p2.x)));
        float cosQ = (float) Math.cos((double) Q);
        float sinQ = (float) Math.sin((double) Q);
        float fn = (float) (((double) (9.8f * this.p2.mass)) * Math.cos((double) (Q - (this.gravityAngleDeg * UtMath.DEG_TO_RAD_F))));
        float fx2 = (-this.p1.fx) * sinQ * sinQ;
        float fy2 = (-this.p1.fy) * sinQ * cosQ;
        float fx3 = (-this.p2.vx) * this.airResistance;
        float fy3 = (-this.p2.vy) * this.airResistance;
        this.p2.fx = (fn * sinQ) + fx2 + fx3;
        this.p2.fy = (fn * cosQ) + fy2 + fy3;
        this.p2.ax = this.p2.fx / this.p2.mass;
        this.p2.ay = this.p2.fy / this.p2.mass;
        this.p2.vx += this.p2.ax * dt;
        this.p2.vy += this.p2.ay * dt;
        this.p2.x += this.p2.vx * dt;
        this.p2.y += this.p2.vy * dt;
        float len = (float) Math.sqrt((double) (((this.p1.x - this.p2.x) * (this.p1.x - this.p2.x)) + ((this.p1.y - this.p2.y) * (this.p1.y - this.p2.y))));
        this.p2.x = this.p1.x + ((this.baseLengthM * (this.p2.x - this.p1.x)) / len);
        this.p2.y = this.p1.y + ((this.baseLengthM * (this.p2.y - this.p1.y)) / len);
        this.p2.vx = (this.p2.x - this.p2.last_x) * dt_div;
        this.p2.vy = (this.p2.y - this.p2.last_y) * dt_div;
        this.p2.setupLast();
    }

    static class PhysicsPoint {
        float ax = 0.0f;
        float ay = 0.0f;
        float fx = 0.0f;
        float fy = 0.0f;
        float last_vx = 0.0f;
        float last_vy = 0.0f;
        float last_x = 0.0f;
        float last_y = 0.0f;
        float mass = 1.0f;
        float vx = 0.0f;
        float vy = 0.0f;
        float x = 0.0f;
        float y = 0.0f;

        PhysicsPoint() {
        }

        /* access modifiers changed from: package-private */
        public void setupLast() {
            this.last_x = this.x;
            this.last_y = this.y;
            this.last_vx = this.vx;
            this.last_vy = this.vy;
        }
    }

    static class IPhysicsSrc {
        String paramID;
        float scale;
        float weight;

        IPhysicsSrc(String _paramID, float _scale, float _weight) {
            this.paramID = _paramID;
            this.scale = _scale;
            this.weight = _weight;
        }

        /* access modifiers changed from: package-private */
        public void updateSrc(ALive2DModel model, PhysicsHair hair) {
        }
    }

    static class PhysicsSrc extends IPhysicsSrc {
        private static /* synthetic */ int[] $SWITCH_TABLE$jp$live2d$physics$PhysicsHair$Src;
        Src srcType;

        static /* synthetic */ int[] $SWITCH_TABLE$jp$live2d$physics$PhysicsHair$Src() {
            int[] iArr = $SWITCH_TABLE$jp$live2d$physics$PhysicsHair$Src;
            if (iArr == null) {
                iArr = new int[Src.values().length];
                try {
                    iArr[Src.SRC_TO_G_ANGLE.ordinal()] = 3;
                } catch (NoSuchFieldError e) {
                }
                try {
                    iArr[Src.SRC_TO_X.ordinal()] = 1;
                } catch (NoSuchFieldError e2) {
                }
                try {
                    iArr[Src.SRC_TO_Y.ordinal()] = 2;
                } catch (NoSuchFieldError e3) {
                }
                $SWITCH_TABLE$jp$live2d$physics$PhysicsHair$Src = iArr;
            }
            return iArr;
        }

        PhysicsSrc(Src _srcType, String _paramID, float _scale, float _weight) {
            super(_paramID, _scale, _weight);
            this.srcType = _srcType;
        }

        /* access modifiers changed from: package-private */
        public void updateSrc(ALive2DModel model, PhysicsHair hair) {
            float value = this.scale * model.getParamFloat(this.paramID);
            PhysicsPoint p1 = hair.getPhysicsPoint1();
            switch ($SWITCH_TABLE$jp$live2d$physics$PhysicsHair$Src()[this.srcType.ordinal()]) {
                case 2:
                    p1.y += (value - p1.y) * this.weight;
                    return;
                case 3:
                    float gAngle = hair.getGravityAngleDeg();
                    hair.setGravityAngleDeg(gAngle + ((value - gAngle) * this.weight));
                    return;
                default:
                    p1.x += (value - p1.x) * this.weight;
                    return;
            }
        }
    }

    static class IPhysicsTarget {
        String paramID;
        float scale;
        float weight;

        IPhysicsTarget(String _paramID, float _scale, float _weight) {
            this.paramID = _paramID;
            this.scale = _scale;
            this.weight = _weight;
        }

        /* access modifiers changed from: package-private */
        public void updateTarget(ALive2DModel model, PhysicsHair hair) {
        }
    }

    static class PhysicsTarget extends IPhysicsTarget {
        private static /* synthetic */ int[] $SWITCH_TABLE$jp$live2d$physics$PhysicsHair$Target;
        Target targetType;

        static /* synthetic */ int[] $SWITCH_TABLE$jp$live2d$physics$PhysicsHair$Target() {
            int[] iArr = $SWITCH_TABLE$jp$live2d$physics$PhysicsHair$Target;
            if (iArr == null) {
                iArr = new int[Target.values().length];
                try {
                    iArr[Target.TARGET_FROM_ANGLE.ordinal()] = 1;
                } catch (NoSuchFieldError e) {
                }
                try {
                    iArr[Target.TARGET_FROM_ANGLE_V.ordinal()] = 2;
                } catch (NoSuchFieldError e2) {
                }
                $SWITCH_TABLE$jp$live2d$physics$PhysicsHair$Target = iArr;
            }
            return iArr;
        }

        PhysicsTarget(Target _targetType, String _paramID, float _scale, float _weight) {
            super(_paramID, _scale, _weight);
        }

        /* access modifiers changed from: package-private */
        public void updateTarget(ALive2DModel model, PhysicsHair hair) {
            switch ($SWITCH_TABLE$jp$live2d$physics$PhysicsHair$Target()[this.targetType.ordinal()]) {
                case 2:
                    model.setParamFloat(this.paramID, this.scale * hair.getAngleP1toP2Deg_velocity(), this.weight);
                    return;
                default:
                    model.setParamFloat(this.paramID, this.scale * hair.getAngleP1toP2Deg(), this.weight);
                    return;
            }
        }
    }
}
