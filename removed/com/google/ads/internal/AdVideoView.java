package com.google.ads.internal;

import android.app.Activity;
import android.media.MediaPlayer;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import android.widget.MediaController;
import android.widget.VideoView;
import com.google.ads.m;
import com.google.ads.util.b;
import java.lang.ref.WeakReference;

public class AdVideoView extends FrameLayout implements MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener, MediaPlayer.OnPreparedListener {
    private static final a b = a.a.b();
    public MediaController a = null;
    private final WeakReference<Activity> c;
    private final AdWebView d;
    private long e = 0;
    private final VideoView f;
    private String g = null;

    private static class a implements Runnable {
        private final WeakReference<AdVideoView> a;

        public a(AdVideoView adVideoView) {
            this.a = new WeakReference<>(adVideoView);
        }

        public void run() {
            AdVideoView adVideoView = (AdVideoView) this.a.get();
            if (adVideoView == null) {
                b.d("The video must be gone, so cancelling the timeupdate task.");
                return;
            }
            adVideoView.f();
            m.a().c.a().postDelayed(this, 250);
        }

        public void a() {
            m.a().c.a().postDelayed(this, 250);
        }
    }

    public AdVideoView(Activity adActivity, AdWebView adWebView) {
        super(adActivity);
        this.c = new WeakReference<>(adActivity);
        this.d = adWebView;
        this.f = new VideoView(adActivity);
        addView(this.f, new FrameLayout.LayoutParams(-1, -1, 17));
        a();
        this.f.setOnCompletionListener(this);
        this.f.setOnPreparedListener(this);
        this.f.setOnErrorListener(this);
    }

    /* access modifiers changed from: protected */
    public void a() {
        new a(this).a();
    }

    public void b() {
        if (!TextUtils.isEmpty(this.g)) {
            this.f.setVideoPath(this.g);
        } else {
            b.a(this.d, "onVideoEvent", "{'event': 'error', 'what': 'no_src'}");
        }
    }

    public void setMediaControllerEnabled(boolean enabled) {
        Activity activity = (Activity) this.c.get();
        if (activity == null) {
            b.e("adActivity was null while trying to enable controls on a video.");
        } else if (enabled) {
            if (this.a == null) {
                this.a = new MediaController(activity);
            }
            this.f.setMediaController(this.a);
        } else {
            if (this.a != null) {
                this.a.hide();
            }
            this.f.setMediaController((MediaController) null);
        }
    }

    public void setSrc(String src) {
        this.g = src;
    }

    public void onCompletion(MediaPlayer mp) {
        b.a(this.d, "onVideoEvent", "{'event': 'ended'}");
    }

    public boolean onError(MediaPlayer mp, int what, int extra) {
        b.e("Video threw error! <what:" + what + ", extra:" + extra + ">");
        b.a(this.d, "onVideoEvent", "{'event': 'error', 'what': '" + what + "', 'extra': '" + extra + "'}");
        return true;
    }

    public void onPrepared(MediaPlayer mp) {
        b.a(this.d, "onVideoEvent", "{'event': 'canplaythrough', 'duration': '" + (((float) this.f.getDuration()) / 1000.0f) + "'}");
    }

    public void c() {
        this.f.pause();
    }

    public void d() {
        this.f.start();
    }

    public void a(int i) {
        this.f.seekTo(i);
    }

    public void a(MotionEvent motionEvent) {
        this.f.onTouchEvent(motionEvent);
    }

    public void e() {
        this.f.stopPlayback();
    }

    /* access modifiers changed from: package-private */
    public void f() {
        long currentPosition = (long) this.f.getCurrentPosition();
        if (this.e != currentPosition) {
            b.a(this.d, "onVideoEvent", "{'event': 'timeupdate', 'time': " + (((float) currentPosition) / 1000.0f) + "}");
            this.e = currentPosition;
        }
    }
}
