package jp.co.cybird.barcodekanojoForGAM.live2d.view;

import android.content.Context;
import android.graphics.Canvas;
import android.opengl.GLSurfaceView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import androidx.annotation.NonNull;

import jp.co.cybird.barcodekanojoForGAM.activity.util.KanojoScreenShot;
import jp.co.cybird.barcodekanojoForGAM.live2d.KanojoLive2D;
import jp.co.cybird.barcodekanojoForGAM.live2d.motion.KanojoAnimation;

import jp.live2d.type.LDPointF;

public class KanojoGLSurfaceView extends GLSurfaceView {
    private final GestureDetector gestureDetector;
    KanojoLive2D kanojoLive2D;
    private float lastD = -1.0f;
    private float last_p1x;
    private float last_p1y;
    private float last_p2x;
    private float last_p2y;
    private float lastx;
    private float lasty;
    private boolean multitouch = false;
    AndroidES1Renderer renderer;

	public KanojoGLSurfaceView(KanojoLive2D kanojoLive2D2, Context context) {
        super(context);
        setFocusable(true);
        this.kanojoLive2D = kanojoLive2D2;
		renderer = new AndroidES1Renderer(kanojoLive2D2, this);
        setRenderer(renderer);
		GestureDetector.SimpleOnGestureListener simpleOnGestureListener = new GestureDetector.SimpleOnGestureListener() {
			public boolean onDoubleTap(@NonNull MotionEvent event) {
				return tapEvent(2) | super.onDoubleTap(event);
			}

			public boolean onDown(@NonNull MotionEvent event) {
				super.onDown(event);
				return true;
			}

			public boolean onSingleTapConfirmed(@NonNull MotionEvent event) {
				return tapEvent(1) | super.onSingleTapUp(event);
			}

			public boolean onSingleTapUp(@NonNull MotionEvent event) {
				return super.onSingleTapUp(event);
			}

			private boolean tapEvent(int tapCount) {
				float logicalX = KanojoGLSurfaceView.this.renderer.viewToLogicalX(KanojoGLSurfaceView.this.lastx);
				float logicalY = KanojoGLSurfaceView.this.renderer.viewToLogicalY(KanojoGLSurfaceView.this.lasty);
				KanojoAnimation kanim = KanojoGLSurfaceView.this.getKanojoAnimation();
				if (kanim == null) {
					return false;
				}
				kanim.tapEvent(tapCount, logicalX, logicalY);
				return true;
			}
		};
		this.gestureDetector = new GestureDetector(getContext(), simpleOnGestureListener);
    }

    public AndroidES1Renderer getMyRenderer() {
        return this.renderer;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        super.surfaceDestroyed(holder);
    }

    public void startAnimation() {
    }

    public void stopAnimation() {
    }

//    public AndroidES1Renderer getRenderer() {
//        return this.renderer;
//    }

	@Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean ret = false;
		int action;
		action = event.getActionMasked();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                ret = true;
                touchesBegan(event);
                break;
			case MotionEvent.ACTION_UP:
                touchesEnded(event);
                break;
			case MotionEvent.ACTION_MOVE:
                touchesMoved(event);
                break;
        }
        return ret | this.gestureDetector.onTouchEvent(event);
    }

    void touchesBegan(MotionEvent event) {
        int touchNum = event.getPointerCount();
        if (touchNum == 1) {
            this.lastx = event.getX();
            this.lasty = event.getY();
            this.lastD = -1.0f;
            this.multitouch = false;
        } else if (touchNum >= 2) {
            this.last_p1x = event.getX(0);
            this.last_p1y = event.getY(0);
            this.last_p2x = event.getX(1);
            this.last_p2y = event.getY(1);
            float dist = (float) Math.sqrt(((this.last_p1x - this.last_p2x) * (this.last_p1x - this.last_p2x)) + ((this.last_p1y - this.last_p2y) * (this.last_p1y - this.last_p2y)));
            this.lastx = (this.last_p1x + this.last_p2x) * 0.5f;
            this.lasty = (this.last_p1y + this.last_p2y) * 0.5f;
            this.lastD = dist;
            this.multitouch = true;
        }
        KanojoAnimation anim = getKanojoAnimation();
        if (anim != null) {
            anim.touchesBegan(this.renderer.viewToLogicalX(this.lastx), this.renderer.viewToLogicalY(this.lasty), touchNum);
        }
    }

    float calcShift(float v1, float v2) {
        boolean z = false;
        int i = 1;
        boolean z2 = v1 > 0.0f;
        if (v2 > 0.0f) {
            z = true;
        }
        if (z2 != z) {
            return 0.0f;
        }
        if (v1 <= 0.0f) {
            i = -1;
        }
        float fugou = (float) i;
        float a1 = Math.abs(v1);
        float a2 = Math.abs(v2);
        if (a1 >= a2) {
            a1 = a2;
        }
        return fugou * a1;
    }

    void touchesMoved(MotionEvent event) {
        int touchNum = event.getPointerCount();
        if (touchNum == 1) {
            LDPointF lDPointF = new LDPointF(event.getX(), event.getY());
			this.lastx = lDPointF.a;//getX();
			this.lasty = lDPointF.b;//getY();
            this.lastD = -1.0f;
        } else if (touchNum >= 2) {
            int index1 = 0;
            int index2 = 0;
            int minDist2 = 999999999;
            for (int i1 = 0; i1 < touchNum; i1++) {
                LDPointF pp1 = new LDPointF(event.getX(i1), event.getY(i1));
                for (int i2 = 0; i2 < touchNum; i2++) {
                    if (i1 != i2) {
                        LDPointF lDPointF = new LDPointF(event.getX(i2), event.getY(i2));
						int distTotal = (int) ((((this.last_p1x - pp1.a/*getX()*/) * (this.last_p1x - pp1.a/*getX()*/)) + ((this.last_p1y - pp1.b/*getY()*/) * (this.last_p1y - pp1.b/*getY()*/))) + (((this.last_p2x - lDPointF.a/*getX()*/) * (this.last_p2x - lDPointF.a/*getX()*/)) + ((this.last_p2y - lDPointF.b/*getY()*/) * (this.last_p2y - lDPointF.b/*getY()*/))));
                        if (distTotal < minDist2) {
                            minDist2 = distTotal;
                            index1 = i1;
                            index2 = i2;
                        }
                    }
                }
            }
            if (minDist2 <= 9800 || touchNum <= 2) {
				LDPointF lDPointF = new LDPointF(event.getX(index1), event.getY(index1));
				LDPointF lDPointF2 = new LDPointF(event.getX(index2), event.getY(index2));
				float dist = (float) Math.sqrt(((lDPointF.a/*getX()*/ - lDPointF2.a/*getX()*/) * (lDPointF.a/*getX()*/ - lDPointF2.a/*getX()*/)) + ((lDPointF.b/*getY()*/ - lDPointF2.b/*getY()*/) * (lDPointF.b/*getY()*/ - lDPointF2.b/*getY()*/)));
				float cy = (lDPointF.b/*getY()*/ + lDPointF2.b/*getY()*/) * 0.5f;
				this.lastx = (lDPointF.a/*getX()*/ + lDPointF2.a/*getX()*/) * 0.5f;
				this.lasty = cy;
				this.last_p1x = lDPointF.a/*getX()*/;
				this.last_p1y = lDPointF.b/*getY()*/;
				this.last_p2x = lDPointF2.a/*getX()*/;
				this.last_p2y = lDPointF2.b/*getY()*/;
                this.lastD = dist;
                this.multitouch = true;
            } else {
                return;
            }
        }
        KanojoAnimation kanim = getKanojoAnimation();
        if (kanim != null) {
            kanim.touchesMoved(this.renderer.viewToLogicalX(this.lastx), this.renderer.viewToLogicalY(this.lasty), touchNum);
        }
    }

    void touchesEnded(MotionEvent event) {
        KanojoAnimation kanim = getKanojoAnimation();
        if (kanim != null) {
            kanim.touchesEnded();
        }
    }

	@Override
	protected void dispatchDraw(Canvas canvas) {
		KanojoScreenShot.GLSurface.dispatchDraw(canvas, KanojoGLSurfaceView.super::dispatchDraw);
	}

    KanojoAnimation getKanojoAnimation() {
        if (this.kanojoLive2D == null) {
            return null;
        }
        return this.kanojoLive2D.getAnimation();
    }
}
