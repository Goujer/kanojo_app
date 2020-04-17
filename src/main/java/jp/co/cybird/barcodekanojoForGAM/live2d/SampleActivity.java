package jp.co.cybird.barcodekanojoForGAM.live2d;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import java.util.Timer;
import java.util.TimerTask;
import jp.co.cybird.barcodekanojoForGAM.activity.base.BaseActivity;
import jp.co.cybird.barcodekanojoForGAM.live2d.view.AndroidEAGLView;

public class SampleActivity extends BaseActivity {
    KanojoLive2D mKanojoLive2D;
    int partsClothNo = 5;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        if (1 != 0) {
            mainSample();
        } else {
            iconSample();
        }
    }

    public View getClientView() {
        Log.w("LIVE2D SAMPLE", "getClientView: This method is not checked !  if occured error, fix this.");
        return new RelativeLayout(this);
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        System.out.printf("onResume\t\t\t\t\t@@SampleActivity\n", new Object[0]);
        KanojoLive2D kanojoLive2D = getLive2D();
        if (kanojoLive2D != null) {
            kanojoLive2D.startAnimation();
            super.onResume();
        }
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        System.out.printf("onPause\t\t\t\t\t@@SampleActivity\n", new Object[0]);
        KanojoLive2D kanojoLive2D = getLive2D();
        if (kanojoLive2D != null) {
            kanojoLive2D.stopAnimation();
            super.onPause();
        }
    }

    /* access modifiers changed from: package-private */
    public KanojoLive2D getLive2D() {
        if (this.mKanojoLive2D == null) {
            this.mKanojoLive2D = new KanojoLive2D(this);
        }
        return this.mKanojoLive2D;
    }

    private void iconSample() {
        KanojoLive2D kanojoLive2D = getLive2D();
        if (kanojoLive2D != null) {
            KanojoSetting setting = kanojoLive2D.getKanojoSetting();
            setLive2DKanojoParts(setting);
            setting.setLoveGage(75.0d);
            Bitmap bitmap = kanojoLive2D.createIcon(256, 256, 1.0f, 0.0f, 0.0f, 0);
            ImageView view = new ImageView(this);
            view.setBackgroundColor(-65536);
            view.setImageBitmap(bitmap);
            setContentView(view, new ViewGroup.LayoutParams(-2, -2));
        }
    }

    /* access modifiers changed from: package-private */
    public void mainSample() {
        final KanojoLive2D kanojoLive2D = getLive2D();
        if (kanojoLive2D != null) {
            AndroidEAGLView view = kanojoLive2D.createView(this, new Rect(0, 0, 320, 320));
            LinearLayout p1 = new LinearLayout(this);
            p1.setOrientation(1);
            LinearLayout center = new LinearLayout(this);
            center.setOrientation(0);
            center.addView(view, new LinearLayout.LayoutParams(-1, -2, 1.0f));
            p1.addView(center, new LinearLayout.LayoutParams(-1, -2, 1.0f));
            LinearLayout btns = new LinearLayout(this);
            Button b1 = new Button(this);
            b1.setText("<<");
            b1.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    SampleActivity sampleActivity = SampleActivity.this;
                    int no = sampleActivity.partsClothNo - 1;
                    sampleActivity.partsClothNo = no;
                    SampleActivity.this.change(no);
                }
            });
            btns.addView(b1, new LinearLayout.LayoutParams(-1, -2, 1.0f));
            Button b2 = new Button(this);
            b2.setText(">>");
            b2.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    SampleActivity sampleActivity = SampleActivity.this;
                    int no = sampleActivity.partsClothNo + 1;
                    sampleActivity.partsClothNo = no;
                    SampleActivity.this.change(no);
                }
            });
            btns.addView(b2, new LinearLayout.LayoutParams(-1, -2, 1.0f));
            p1.addView(btns, new ViewGroup.LayoutParams(-1, -2));
            setContentView(p1);
            KanojoSetting setting = kanojoLive2D.getKanojoSetting();
            setLive2DKanojoParts(setting);
            setting.setLoveGage(75.0d);
            kanojoLive2D.setupModel(true);
            kanojoLive2D.startAnimation();
            new Timer().schedule(new TimerTask() {
                public void run() {
                    System.out.printf("timer\t\t\t\t\t@@SampleActivity.onCreate(...).new TimerTask() {...}\n", new Object[0]);
                    int[] actions = kanojoLive2D.getUserActions();
                    String s = "actions[" + actions.length + "] = { ";
                    for (int i = 0; i < actions.length; i++) {
                        s = String.valueOf(s) + actions[i] + " ,";
                    }
                    System.out.printf(String.valueOf(s) + " } \n", new Object[0]);
                }
            }, 10000, 10000);
        }
    }

    private void setLive2DKanojoParts(KanojoSetting setting) {
        int no = this.partsClothNo;
        System.out.printf("cloth no :: %d\t\t\t\t\t@@SampleActivity\n", new Object[]{Integer.valueOf(no)});
        setting.setParts(KanojoSetting.PARTS_01_HAIR, 11);
        setting.setParts(KanojoSetting.PARTS_01_CLOTHES, no);
        setting.setColor(KanojoSetting.COLOR_01_HAIR, 19);
        setting.setColor(KanojoSetting.COLOR_01_SKIN, 11);
        setting.setColor(KanojoSetting.COLOR_01_EYE, 2);
        setting.setColor("COLOR_01_CLOTHES_A", 4);
        setting.setColor(KanojoSetting.COLOR_01_CLOTHES_B, 5);
    }

    /* access modifiers changed from: private */
    public void change(int no) {
        KanojoLive2D kanojoLive2D = getLive2D();
        if (kanojoLive2D != null) {
            kanojoLive2D.getKanojoSetting().setColor(KanojoSetting.COLOR_01_EYE, no);
            kanojoLive2D.releaseModel();
            kanojoLive2D.setupModel(true);
            System.out.printf("change cloth no :: %d\t\t\t\t\t@@SampleActivity\n", new Object[]{Integer.valueOf(no)});
        }
    }
}
