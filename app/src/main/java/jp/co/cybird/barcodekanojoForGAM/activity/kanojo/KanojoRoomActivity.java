package jp.co.cybird.barcodekanojoForGAM.activity.kanojo;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.goujer.barcodekanojo.BarcodeKanojoApp;
import com.goujer.barcodekanojo.R;
import com.goujer.barcodekanojo.activity.kanojo.KanojoInfoActivity;
import com.goujer.barcodekanojo.core.model.Kanojo;
import com.goujer.barcodekanojo.databinding.ActivityKanojoRoomBinding;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import jp.co.cybird.barcodekanojoForGAM.Defs;
import jp.co.cybird.barcodekanojoForGAM.activity.base.BaseActivity;
import jp.co.cybird.barcodekanojoForGAM.activity.base.BaseInterface;
import com.goujer.barcodekanojo.core.BarcodeKanojo;
import com.goujer.utils.TimeUtilKt;

import jp.co.cybird.barcodekanojoForGAM.core.exception.BarcodeKanojoException;
import jp.co.cybird.barcodekanojoForGAM.core.model.BarcodeKanojoModel;
import jp.co.cybird.barcodekanojoForGAM.core.model.KanojoItem;
import jp.co.cybird.barcodekanojoForGAM.core.model.KanojoMessage;
import jp.co.cybird.barcodekanojoForGAM.core.model.LoveIncrement;
import jp.co.cybird.barcodekanojoForGAM.core.model.MessageModel;
import jp.co.cybird.barcodekanojoForGAM.core.model.Product;
import jp.co.cybird.barcodekanojoForGAM.core.model.Response;
import jp.co.cybird.barcodekanojoForGAM.core.util.FirstbootUtil;
import jp.co.cybird.barcodekanojoForGAM.core.util.Live2dUtil;
import jp.co.cybird.barcodekanojoForGAM.gree.core.GreeDefs;
import jp.co.cybird.barcodekanojoForGAM.live2d.KanojoLive2D;
import jp.co.cybird.barcodekanojoForGAM.live2d.KanojoSetting;
import jp.co.cybird.barcodekanojoForGAM.live2d.view.KanojoGLSurfaceView;
import jp.co.cybird.barcodekanojoForGAM.view.DialogTextView;

@SuppressLint({"SetJavaScriptEnabled", "NewApi"})
public class KanojoRoomActivity extends BaseActivity implements View.OnClickListener, Observer {

    private Button btnClose;
    private Button btnDate;
    private ImageButton btnInfo;
    private Button btnItems;
    private ToggleButton btnLike;
    private ImageView btnStatusArrow;
    //private Button btnTickets;
    private ImageView dropImage;
    private ImageView imageView01;
    private ImageView imageView02;
    private ImageView imageView03;
    private RelativeLayout layoutBGActionLoveGauge;
    private RelativeLayout layoutTagLoveBar;
    private RelativeLayout mFirstBoot;
    private Handler mHandler = new Handler();
    private DialogTextView mKanojoDialogMessage;
    private RelativeLayout mKanojoLayout;
    private KanojoLive2D mKanojoLive2D;
    private KanojoMessage mKanojoMessage;
    private Live2dUtil mLive2dUtil;
    private ProgressBar mLoveBar;
    private LoveIncrement mLoveIncrement;
    private MessageModel mMessage;
    private LinearLayout mStatusLayout;
    private LinearLayout statusBarLayout;

	private KanojoDialogTask mKanojoDialogTask;
	private KanojoRoomTask mKanojoRoomTask;
	private Live2dTask mLive2dTask;

	private boolean mIsFirstBoot = false;
	private int mLastAction = 0;

	private Timer mTimerCallAPI;

	private Kanojo mKanojo;
	private Product mProduct;

	private int dHeight;
	private int dWidth;

	public boolean isPrepared = false;

	private ActivityKanojoRoomBinding binding;

	private Runnable mProgressThread = () -> {
		if (getLive2D().isModelAvailable()) {
			dismissProgressDialog();
			mHandler.removeCallbacks(KanojoRoomActivity.this.mProgressThread);
			return;
		}
		mHandler.postDelayed(KanojoRoomActivity.this.mProgressThread, 100);
	};

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityKanojoRoomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        this.dWidth = displayMetrics.widthPixels;
        this.dHeight = displayMetrics.heightPixels;

	    this.statusBarLayout = findViewById(R.id.kanojo_room_status_bar_layout);

        this.btnClose = findViewById(R.id.kanojo_room_close);

        this.btnInfo = findViewById(R.id.kanojo_room_info);

        this.btnLike = findViewById(R.id.kanojo_room_like_btn);

        this.btnDate = findViewById(R.id.kanojo_room_date_btn);
	    this.btnDate.setVisibility(View.GONE);
	    this.btnDate.setEnabled(false);

        this.btnItems = findViewById(R.id.kanojo_room_items_btn);
	    this.btnItems.setVisibility(View.GONE);
	    this.btnItems.setEnabled(false);

        //this.btnTickets = findViewById(R.id.kanojo_room_ticket_btn);
	    //this.btnTickets.setVisibility(View.GONE);
	    //this.btnTickets.setEnabled(false);

	    this.dropImage = findViewById(R.id.dropdown_img);

        this.mStatusLayout = findViewById(R.id.kanojo_room_status_txt_layout);

	    this.layoutBGActionLoveGauge = findViewById(R.id.kanojo_bg_action_love_gauge);

        this.mLoveBar = findViewById(R.id.kanojo_room_status_bar);

        this.btnStatusArrow = findViewById(R.id.kanojo_room_arrow);

        this.mKanojoLayout = findViewById(R.id.kanojo_room_live2d);


	    this.layoutTagLoveBar = findViewById(R.id.kanojo_tag_love_gauge);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            this.mKanojo = (Kanojo) bundle.get(BaseInterface.EXTRA_KANOJO);
        }
        if (this.mKanojo != null) {
	        TextView txtStatus = findViewById(R.id.kanojo_room_status_txt);
            txtStatus.setText(this.mKanojo.getStatus());

            this.btnLike.setChecked(this.mKanojo.isVoted_like());
            TextView txtName = findViewById(R.id.kanojo_room_name);
            if (txtName != null) {
                txtName.setText(this.mKanojo.getName());
            }
            this.mKanojoDialogMessage = findViewById(R.id.dialog_message);
			this.mKanojoDialogMessage.setVisibility(View.GONE);

			int nightModeFlags = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
			if (nightModeFlags != Configuration.UI_MODE_NIGHT_YES) {
				getResources().getDrawable(R.drawable.dialog_frame).setAlpha(180);  //Light mode dialog box is not transparent
			}
			DialogTextView.OnDismissListener mListener = new DialogTextView.OnDismissListener() {
				public void onNextClick() {}

				public void OnCloseClick() {
					KanojoRoomActivity.this.mKanojoDialogMessage.setVisibility(View.GONE);
					KanojoRoomActivity.this.isPrepared = true;
				}
			};
            this.mKanojoDialogMessage.setListener(mListener);
            executeKanojoRoomTask();
            this.mFirstBoot = findViewById(R.id.kanojo_room_firstboot);
            if (mKanojo.isIn_room() || this.mKanojo.getRelation_status() == Kanojo.RELATION_OTHER) {
                this.mIsFirstBoot = false;
                this.mFirstBoot.setVisibility(View.GONE);
                cleanupView(this.mFirstBoot);
            } else if (!FirstbootUtil.isShowed(this, "kanojo_room_firstboot")) {
                this.mIsFirstBoot = true;
                this.mFirstBoot.setVisibility(View.VISIBLE);
                this.mFirstBoot.setOnClickListener(v -> {
                    this.mFirstBoot.setVisibility(View.GONE);
                    this.cleanupView(this.mFirstBoot);
                });
            } else {
                this.mIsFirstBoot = false;
                this.mFirstBoot.setVisibility(View.GONE);
                cleanupView(this.mFirstBoot);
            }
        }
    }

    public View getClientView() {
        View layout = getLayoutInflater().inflate(R.layout.activity_kanojo_room, null);
        LinearLayout appLayoutRoot = new LinearLayout(this);
        appLayoutRoot.addView(layout);
        return appLayoutRoot;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (this.mKanojo != null) {
            this.mLoveBar.setProgress(this.mKanojo.getLove_gauge());
            this.btnLike.setChecked(this.mKanojo.isVoted_like());
            if (this.mKanojo.isIn_room() && this.mKanojo.getRelation_status() != Kanojo.RELATION_OTHER) {
				if (!this.mKanojo.isOnDate()) {
					this.btnDate.setVisibility(View.VISIBLE);
					this.btnDate.setEnabled(true);
				}
                this.btnItems.setVisibility(View.VISIBLE);
                this.btnItems.setEnabled(true);
                //this.btnTickets.setVisibility(View.VISIBLE);
                //this.btnTickets.setEnabled(true);
            }
        }
        showProgressDialog();
        this.mHandler.post(this.mProgressThread);
        startLive2D();
        if (!this.isPrepared) {
            executeKanojoRoomTask();
        } else {
            settingLive2D();
        }
        bindEvent();
    }

    @Override
    protected void onPause() {
        if (this.mLive2dTask != null) {
            this.mLive2dTask.cancel(true);
            this.mLive2dTask = null;
        }
        dismissProgressDialog();
        this.mHandler.removeCallbacks(this.mProgressThread);
        stopLive2D();
        if (isFinishing()) {
            getLive2DUtil().removeObserver();
        }
        super.onPause();
    }

	@Override
	protected void onDestroy() {
		KanojoLive2D kanojoLive2D = getLive2D();
		if (kanojoLive2D != null) {
			kanojoLive2D.stopAnimation();
			kanojoLive2D.releaseView();
			kanojoLive2D.releaseModel();
			kanojoLive2D.releaseFileManager();
		}
		this.mLive2dUtil.setOnRrObserver(null);
		this.mLive2dUtil = null;
		this.mKanojoLive2D = null;
		this.mHandler = null;
		if (this.mKanojoRoomTask != null) {
			this.mKanojoRoomTask.cancel(true);
			this.mKanojoRoomTask = null;
		}
		this.mProgressThread = null;
		unBindEvent();
		super.onDestroy();
	}

    public void bindEvent() {
        this.statusBarLayout.setOnClickListener(this);
        this.btnClose.setOnClickListener(this);
        this.btnInfo.setOnClickListener(this);
        this.btnLike.setOnClickListener(this);
	    if (!this.mKanojo.isOnDate()) {
		    this.btnDate.setOnClickListener(this);
	    }
        this.btnItems.setOnClickListener(this);
        //this.btnTickets.setOnClickListener(this);
        this.dropImage.setOnClickListener(this);
    }

    public void unBindEvent() {
        this.statusBarLayout.setOnClickListener(null);
        this.btnClose.setOnClickListener(null);
        this.btnInfo.setOnClickListener(null);
        this.btnLike.setOnClickListener(null);
        this.btnDate.setOnClickListener(null);
        this.btnItems.setOnClickListener(null);
        //this.btnTickets.setOnClickListener(null);
        this.dropImage.setOnClickListener(null);
    }

	private void radarChartUpdate() {
		List<RadarEntry> entries = new ArrayList<>();
		entries.add(new RadarEntry(mKanojo.getConsumption()));
		entries.add(new RadarEntry(mKanojo.getPossession()));
		entries.add(new RadarEntry(mKanojo.getRecognition()));
		entries.add(new RadarEntry(mKanojo.getSexual()));
		entries.add(new RadarEntry(mKanojo.getFlirtable()));

		RadarDataSet radarDataSet = new RadarDataSet(entries, getString(R.string.stats));
		//radarDataSet.setLineWidth(0.2f);
		radarDataSet.setColor(0x7FFF0000);
		radarDataSet.setDrawValues(false);
		//radarDataSet.setHighlightLineWidth(0.0f);
		//radarDataSet.setDrawHighlightCircleEnabled(true);

		radarDataSet.setDrawFilled(true);
		//if (Build.VERSION.SDK_INT >= 23) {
		//	radarDataSet.setFillColor(getColor(android.R.color.holo_red_dark));
		//} else {
		//	radarDataSet.setFillColor(getResources().getColor(android.R.color.holo_red_dark));
		//}
		radarDataSet.setFillColor(0xFFFF0000);

		RadarData radarData = new RadarData(radarDataSet);
		//radarData.setLabels("Flirtable", "Sexual", "Possession", "Recognition", "Consumption");
		radarData.setHighlightEnabled(false);

		RadarChart radarView = binding.radarView;

		radarView.setRotationEnabled(false);

		radarView.getLegend().setEnabled(false);
		radarView.getDescription().setEnabled(false);

		radarView.getYAxis().setAxisMinimum(0.0f);
		radarView.getYAxis().setAxisMaximum(100.0f);
		radarView.getYAxis().setLabelCount(6, true);
		radarView.getYAxis().setDrawLabels(false);
		radarView.getYAxis().setTextSize(6f);

		radarView.getXAxis().setValueFormatter(new IndexAxisValueFormatter(new String[]{getString(R.string.stat_consumption), getString(R.string.stat_possession), getString(R.string.stat_recognition), getString(R.string.stat_sexual), getString(R.string.stat_flirtable)}));
		radarView.getXAxis().setTextColor(0xFFFFFFFF);
		radarView.getXAxis().setTextSize(10f);

		radarView.setWebColor(0xFF9F9F9F);
		radarView.setWebColorInner(0xFFFFFFFF);
		radarView.setWebLineWidthInner(1.5f);

		radarView.setData(radarData);
		radarView.invalidate();
	}

    @Override
    public void onClick(View v) {
        unBindEvent();
        //new Intent();
		int id = v.getId();
		if (id == R.id.kanojo_room_close) {
			setResult(BaseInterface.RESULT_KANOJO_ROOM_EXIT);
			close();

		} else if (id == R.id.kanojo_room_info) {
			binding.radarView.setVisibility(View.GONE);
			Intent intent = new Intent(this, KanojoInfoActivity.class);
			if (this.mKanojo != null) {
				intent.putExtra(BaseInterface.EXTRA_KANOJO, this.mKanojo);
			}
			if (this.mProduct != null) {
				intent.putExtra(BaseInterface.EXTRA_PRODUCT, this.mProduct);
			}
			if (this.mMessage != null) {
				intent.putExtra(MessageModel.NOTIFY_AMENDMENT_INFORMATION, this.mMessage.get(MessageModel.NOTIFY_AMENDMENT_INFORMATION));
			}
			startActivityForResult(intent, BaseInterface.REQUEST_KANOJO_INFO);

		} else if (id == R.id.kanojo_room_like_btn) {
			this.mKanojo.setVoted_like(this.btnLike.isChecked());
			bindEvent();

		} else if (id == R.id.kanojo_room_items_btn) {
			binding.radarView.setVisibility(View.GONE);
			Intent intent2 = new Intent(this, KanojoItemsActivity.class);
			if (this.mKanojo != null) {
				intent2.putExtra(BaseInterface.EXTRA_KANOJO, this.mKanojo);
			}
			intent2.putExtra(BaseInterface.EXTRA_KANOJO_ITEM_MODE, KanojoItemsActivity.MODE_GIFT);
			startActivityForResult(intent2, BaseInterface.REQUEST_KANOJO_ITEMS);

		} else if (id == R.id.kanojo_room_date_btn) {
			binding.radarView.setVisibility(View.GONE);
			Intent intent = new Intent(this, KanojoItemsActivity.class);
			if (this.mKanojo != null) {
				intent.putExtra(BaseInterface.EXTRA_KANOJO, this.mKanojo);
			}
			intent.putExtra(BaseInterface.EXTRA_KANOJO_ITEM_MODE, KanojoItemsActivity.MODE_DATE);
			startActivityForResult(intent, BaseInterface.REQUEST_KANOJO_ITEMS);

		} else if (id == R.id.kanojo_room_ticket_btn) {
			binding.radarView.setVisibility(View.GONE);
			Intent intent4 = new Intent(this, KanojoItemsActivity.class);
			if (this.mKanojo != null) {
				intent4.putExtra(BaseInterface.EXTRA_KANOJO, this.mKanojo);
			}
			KanojoItem item = new KanojoItem(KanojoItem.GIFT_ITEM_CLASS);
			item.setTitle(getResources().getString(R.string.kanojo_items_store));
			intent4.putExtra(BaseInterface.EXTRA_KANOJO_ITEM, item);
			intent4.putExtra(BaseInterface.EXTRA_KANOJO_ITEM_MODE, KanojoItemsActivity.MODE_TICKET);
			startActivityForResult(intent4, BaseInterface.REQUEST_KANOJO_TICKETS);
		} else if (id == R.id.kanojo_room_status_bar_layout || id == R.id.dropdown_img) {
			if (this.mStatusLayout.getVisibility() == View.VISIBLE) {
				binding.radarView.setVisibility(View.GONE);
				this.mStatusLayout.setVisibility(View.GONE);
				this.btnStatusArrow.setImageResource(R.drawable.kanojolovebararrowdown);
			} else {
				this.mStatusLayout.setVisibility(View.VISIBLE);
				binding.radarView.setVisibility(View.VISIBLE);
				this.btnStatusArrow.setImageResource(R.drawable.kanojolovebararrowup);
				radarChartUpdate();
			}
			bindEvent();
		}
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bundle bundle;
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == BaseInterface.RESULT_SAVE_PRODUCT_INFO) {
            executeKanojoRoomTask();
            return;
        } else if (resultCode == BaseInterface.RESULT_KANOJO_ITEM_USED
		        && data != null && (bundle = data.getExtras()) != null) {
			//Item has been used
            Kanojo kanojo = (Kanojo) bundle.get(BaseInterface.EXTRA_KANOJO);
            LoveIncrement loveIncrement = (LoveIncrement) bundle.get(BaseInterface.EXTRA_LOVE_INCREMENT);

			displayKanojoDialog((KanojoMessage) bundle.get(BaseInterface.EXTRA_MESSAGES));
            if (kanojo != null) {
                this.mKanojo = kanojo;
                if (loveIncrement != null) {
                    this.mLoveIncrement = loveIncrement;
                }
            } else {
                this.isPrepared = false;
                setResult(BaseInterface.RESULT_KANOJO_GOOD_BYE);
                close();
                return;
            }
        }
        this.isPrepared = true;
    }

    private Live2dUtil getLive2DUtil() {
        if (this.mLive2dUtil == null) {
            this.mLive2dUtil = new Live2dUtil(getLive2D(), getApplicationContext());
            this.mLive2dUtil.setOnRrObserver(this);
        }
        return this.mLive2dUtil;
    }

    private KanojoLive2D getLive2D() {
        if (this.mKanojoLive2D == null) {
            this.mKanojoLive2D = new KanojoLive2D(getApplicationContext());
        }
        return this.mKanojoLive2D;
    }

    private void stopLive2D() {
        this.mKanojoLayout.removeAllViews();
        KanojoLive2D kanojoLive2D = getLive2D();
        if (kanojoLive2D != null) {
            BarcodeKanojo barcodeKanojo = ((BarcodeKanojoApp) getApplication()).getBarcodeKanojo();
	        barcodeKanojo.setPlayLive2d(this.mKanojo, kanojoLive2D.getUserActions());
	        kanojoLive2D.stopAnimation();
            kanojoLive2D.releaseView();
            kanojoLive2D.releaseModel();
        }
    }

    private void startLive2D() {
        KanojoLive2D kanojoLive2D = getLive2D();
        if (kanojoLive2D != null) {
            KanojoGLSurfaceView view = kanojoLive2D.createView(getApplicationContext(), new Rect(0, 0, 320, 320));
            this.mKanojoLayout.removeAllViews();
            this.mKanojoLayout.addView(view, new LinearLayout.LayoutParams(-1, -2, 1.0f));
			setBackground(kanojoLive2D);
        }
    }

    private void settingLive2D() {
		KanojoLive2D kanojoLive2D = getLive2D();
		if (kanojoLive2D != null && this.isPrepared) {
			if (this.mKanojo != null) {
				this.mLoveBar.setProgress(this.mKanojo.getLove_gauge());
				this.btnLike.setChecked(this.mKanojo.isVoted_like());
			}
			boolean in_room = this.mKanojo.isIn_room();
			kanojoLive2D.setInRoom(in_room);
			if (!in_room) {
				dismissProgressDialog();
				this.mHandler.removeCallbacks(this.mProgressThread);
			}

			KanojoSetting setting = kanojoLive2D.getKanojoSetting();
			setting.setKanojoState(this.mKanojo.getRelation_status());
			if (getLive2DUtil().setLive2DKanojoPartsAndRequest(setting, this.mKanojo)) {
				setting.setLoveGage(this.mKanojo.getLove_gauge());
				kanojoLive2D.setupModel(true);
				kanojoLive2D.startAnimation();
				if (this.mLoveIncrement != null) {
					new Handler().postDelayed(KanojoRoomActivity.this::addTagLoveGauge, 2000);
				}
			} else {
				setBackground(kanojoLive2D);
			}
		}
    }

	private void setBackground(KanojoLive2D kanojoLive2D) {    //Sets background based on Kanojo outfit and marriage status.
		int displayMax = Math.max(this.dHeight, this.dWidth);
		int clothesType = mKanojo.getClothes_type();
		int pod = TimeUtilKt.getPartOfDay();


		//if (this.mKanojo.isOnDate()) {
			//If on date we should do a background for that date if there is one
		//}

		switch (clothesType) {
			case 2:
				if (this.mKanojo.getMascotEnable() == 1) {
					if (displayMax >= 1024) {
						kanojoLive2D.setBackgroundImage("class_permanent1024.png", false);
					} else if (displayMax >= 512) {
						kanojoLive2D.setBackgroundImage("class_permanent512.png", false);
					} else {
						kanojoLive2D.setBackgroundImage("class_permanent256.png", false);
					}
				} else {
					if (pod == 0) {
						kanojoLive2D.setBackgroundImage("class_night256.png", false);
					} else if (pod == 3) {
						kanojoLive2D.setBackgroundImage("class_evening256.png", false);
					} else {
						if (displayMax >= 512) {
							kanojoLive2D.setBackgroundImage("class_morning512.png", false);
						} else {
							kanojoLive2D.setBackgroundImage("class_morning256.png", false);
						}
					}
				}
				break;
			case 901:
			case 902:
			case 903:
			case 904:
			case 905:
			case 906:
			case 907:
				kanojoLive2D.setBackgroundImage("resort.jpg", false);
				break;
			case 908:
			case 909:
			case 910:
			case 911:
				if (new Random().nextInt(2) == 0) {
					kanojoLive2D.setBackgroundImage("hotspring.png", false);
				} else {
					kanojoLive2D.setBackgroundImage("shrine.png", false);
				}
				break;
			case 912:
			case 913:
				kanojoLive2D.setBackgroundImage("studio.png", false);
				break;
			default:
				if (this.mKanojo.getMascotEnable() == 1) {
					if (displayMax >= 960) {
						kanojoLive2D.setBackgroundImage("back_permanent960.png", false);
					} else {
						kanojoLive2D.setBackgroundImage("back_permanent256.png", false);
					}
				} else {
					if (displayMax >= 1024) {
						kanojoLive2D.setBackgroundImage("back1024.png", false);
					} else if (displayMax >= 512) {
						kanojoLive2D.setBackgroundImage("back512.png", false);
					} else {
						kanojoLive2D.setBackgroundImage("back256.png", false);
					}
				}
				kanojoLive2D.setKanojoRoomActivity(this);
		}
	}

    private void executeKanojoRoomTask() {
        this.isPrepared = false;
        if (this.mKanojoRoomTask == null || this.mKanojoRoomTask.getStatus() == AsyncTask.Status.FINISHED || this.mKanojoRoomTask.cancel(true) || this.mKanojoRoomTask.isCancelled()) {
            this.mKanojoRoomTask = (KanojoRoomTask) new KanojoRoomTask(this).execute();
        }
    }

    static class KanojoRoomTask extends AsyncTask<Void, Void, Response<?>> {
	    private final WeakReference<KanojoRoomActivity> activityRef;
        private Exception mReason = null;

        KanojoRoomTask(KanojoRoomActivity activity) {
			super();
	        activityRef = new WeakReference<>(activity);
        }

        public Response<?> doInBackground(Void... params) {
	        KanojoRoomActivity activity = activityRef.get();
	        if (activity == null || activity.isFinishing()) {
		        return null;
	        }
            try {
	            return ((BarcodeKanojoApp) activity.getApplication()).getBarcodeKanojo().show(activity.mKanojo.getId(), true);
            } catch (Exception e) {
                this.mReason = e;
                return null;
            }
        }

        public void onPostExecute(Response<?> response) {
	        KanojoRoomActivity activity = activityRef.get();
	        if (activity == null || activity.isFinishing()) {
		        return;
	        }

            try {
				if (activity.getCodeAndShowAlert(response, this.mReason) == Response.CODE_SUCCESS) {
					activity.mKanojo = (Kanojo) response.get(Kanojo.class);
					activity.mProduct = (Product) response.get(Product.class);
					activity.mMessage = (MessageModel) response.get(MessageModel.class);
					if (activity.mKanojo != null && activity.mProduct != null) {
						activity.isPrepared = true;
						activity.settingLive2D();
					} else {
						throw new BarcodeKanojoException("Response Error: Data is null");
					}
				}

                if (!activity.isPrepared) {
                    activity.dismissProgressDialog();
                    activity.btnInfo.setVisibility(View.INVISIBLE);
                } else {
	                activity.btnInfo.setVisibility(View.VISIBLE);
                }

                if (activity.mStatusLayout.getVisibility() == View.VISIBLE) {
                    activity.mStatusLayout.setVisibility(View.GONE);
                    activity.btnStatusArrow.setImageResource(R.drawable.kanojolovebararrowdown);
                }
            } catch (BarcodeKanojoException e) {
                if (!activity.isPrepared) {
                    activity.dismissProgressDialog();
                    activity.btnInfo.setVisibility(View.INVISIBLE);
                } else {
	                activity.btnInfo.setVisibility(View.VISIBLE);
                }

                if (activity.mStatusLayout.getVisibility() == View.VISIBLE) {
                    activity.mStatusLayout.setVisibility(View.GONE);
                    activity.btnStatusArrow.setImageResource(R.drawable.kanojolovebararrowdown);
                }
            } catch (Throwable th) {
                if (!activity.isPrepared) {
                    activity.dismissProgressDialog();
                    activity.btnInfo.setVisibility(View.INVISIBLE);
                } else {
	                activity.btnInfo.setVisibility(View.VISIBLE);
                }
                if (activity.mStatusLayout.getVisibility() == View.VISIBLE) {
                    activity.mStatusLayout.setVisibility(View.GONE);
                    activity.btnStatusArrow.setImageResource(R.drawable.kanojolovebararrowdown);
                }
                throw th;
            }
        }
    }

    public void update(Observable observable, Object data) {
        this.mHandler.post(KanojoRoomActivity.this::settingLive2D);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode != KeyEvent.KEYCODE_BACK || !binding.loadingView.isShow()) {
            return super.onKeyDown(keyCode, event);
        }
        binding.loadingView.setMessage(getString(R.string.requesting_cant_cancel));
        return true;
    }

    @Override
    public void showProgressDialog() {
		binding.loadingView.show();
    }

    @Override
    protected void dismissProgressDialog() {
        binding.loadingView.dismiss();
    }

    public void setAnimationTap(float x, float y, int type) {
		if (mKanojo.isIn_room() && mKanojo.getRelation_status() != Kanojo.RELATION_OTHER && isPrepared) {
			mLastAction = type;
			ImageView imageViewAction = new ImageView(this);
			if (KanojoLive2D.USER_ACTION_HEADPAT == type) {
				System.out.println("USER_ACTION_HEADPAT");
				imageViewAction.setImageResource(R.drawable.hand_image);
			} else if (KanojoLive2D.USER_ACTION_KISS == type) {
				System.out.println("USER_ACTION_KISS");
				imageViewAction.setImageResource(R.drawable.kiss_image);
			} else if (KanojoLive2D.USER_ACTION_BREAST == type) {
				System.out.println("USER_ACTION_BREAST");
				imageViewAction.setImageResource(R.drawable.hand_image);
			}
			addImageAnimationTap(imageViewAction, x, y);
		}
    }

    private void addImageAnimationTap(final ImageView imageView, float x, float y) {
        AnimationUtils.loadAnimation(this, R.anim.anim_touch_fade_in).setDuration(1000);
        Animation fadeOutAnimation = AnimationUtils.loadAnimation(this, R.anim.anim_touch_fade_out);
        fadeOutAnimation.setDuration(1000);
        fadeOutAnimation.setStartOffset(1000);
        AnimationSet animationActionTap = new AnimationSet(false);
        int imageCenter = (int) (0.075d * ((double) this.dWidth));
        animationActionTap.addAnimation(fadeOutAnimation);
        animationActionTap.setFillEnabled(true);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(-2, -2);
        params.leftMargin = ((int) ((x / 1280.0f) * ((float) this.dWidth))) - imageCenter;
        params.topMargin = (((int) ((y / 1280.0f) * ((float) this.dHeight))) - imageCenter) - 60;
        imageView.setLayoutParams(params);
        imageView.setAnimation(animationActionTap);
        animationActionTap.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationStart(Animation animation) {
            }

            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationEnd(Animation animation) {
                Handler handler = new Handler();
                handler.post(() -> {
                    imageView.clearAnimation();
                    imageView.setImageBitmap(null);
                    KanojoRoomActivity.this.mKanojoLayout.removeView(imageView);
                });
            }
        });
        this.mKanojoLayout.addView(imageView);
        animationActionTap.startNow();
    }

    public void callUserAction(int userAction) {
		if (KanojoLive2D.USER_ACTION_SWIPE == userAction || KanojoLive2D.USER_ACTION_SHAKE == userAction || KanojoLive2D.USER_ACTION_HEADPAT == userAction || KanojoLive2D.USER_ACTION_KISS != userAction) {
		}
		if (this.mTimerCallAPI != null) {
			this.mTimerCallAPI.cancel();
			this.mTimerCallAPI.purge();
		}
		this.mTimerCallAPI = new Timer();
		final Handler mTimerCallAPIHandler = new Handler();
		this.mTimerCallAPI.schedule(new TimerTask() {
			public void run() {
				mTimerCallAPIHandler.post(KanojoRoomActivity.this::executeLive2dTask);
			}
		}, 2000);
	}

    private void executeLive2dTask() {
        if (this.mLive2dTask == null || this.mLive2dTask.getStatus() == AsyncTask.Status.FINISHED || this.mLive2dTask.cancel(true) || this.mLive2dTask.isCancelled()) {
            this.mLive2dTask = new Live2dTask(this);
            this.mLive2dTask.execute();
        }
    }

    static class Live2dTask extends AsyncTask<Void, Void, Response<BarcodeKanojoModel>> {
		WeakReference<KanojoRoomActivity> activityRef;
        private Exception mReason = null;

		Live2dTask(KanojoRoomActivity activity) {
			super();
			activityRef = new WeakReference<>(activity);
		}

        public void onPreExecute() {
	        KanojoRoomActivity activity = activityRef.get();
	        if (activity == null || activity.isFinishing()) {
		        return;
	        }

            KanojoLive2D kanojoLive2D = activity.getLive2D();
            BarcodeKanojo barcodeKanojo = ((BarcodeKanojoApp) activity.getApplication()).getBarcodeKanojo();
			barcodeKanojo.setPlayLive2d(activity.mKanojo, kanojoLive2D.getUserActions());
		}

        public Response<BarcodeKanojoModel> doInBackground(Void... params) {
	        KanojoRoomActivity activity = activityRef.get();
	        if (activity == null || activity.isFinishing()) {
		        return null;
	        }

            try {
	            return ((BarcodeKanojoApp) activity.getApplication()).getBarcodeKanojo().play_on_live2d();
            } catch (Exception e) {
                this.mReason = e;
                return null;
            }
        }

        public void onPostExecute(Response<BarcodeKanojoModel> response) {
			KanojoRoomActivity activity = activityRef.get();
			if (activity == null || activity.isFinishing()) {
				return;
			}

            if (response != null) {
                int code = response.getCode();
				activity.mLoveIncrement = (LoveIncrement) response.get(LoveIncrement.class);

				if (activity.mLoveIncrement != null && activity.mLoveIncrement.getAlertShow().equals(GreeDefs.KANOJO_NAME)) {
                    try {
						int unused = activity.getCodeAndShowAlert(response, this.mReason);
                    } catch (BarcodeKanojoException e) {
                        e.printStackTrace();
                    }
                }

				if (code == Response.CODE_SUCCESS) {
					activity.mKanojo = (Kanojo) response.get(Kanojo.class);
					activity.updateUser(response);
					activity.setAutoRefreshLoveGageAction();
					activity.displayKanojoDialog((KanojoMessage) response.get(KanojoMessage.class));
				}
			}
        }
    }

    private void setAutoRefreshLoveGageAction() {
        if (getLive2D() != null && this.isPrepared && this.mKanojo != null) {
            this.mLoveBar.setProgress(this.mKanojo.getLove_gauge());
            addTagLoveGauge();
        }
    }

    private void addTagLoveGauge() {
		//Determine where popup animation needs to appear top mark love increment or decrement
        Rect rectLoveBar = new Rect();
        this.mLoveBar.getLocalVisibleRect(rectLoveBar);
        int xLoveBar = rectLoveBar.left;
        int wLoveBar = rectLoveBar.width();
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) this.layoutTagLoveBar.getLayoutParams();
        params.leftMargin = ((wLoveBar / 100) * this.mKanojo.getLove_gauge()) + xLoveBar;
        this.layoutTagLoveBar.setLayoutParams(params);

		//Determine what to display in increment/decrement text
        TextView textTagLoveBar = findViewById(R.id.kanojo_tag_love_gauge_text);
        if (this.mLoveIncrement != null) {
            if (!this.mLoveIncrement.getIncrease_love().equals("0")) {
                textTagLoveBar.setText("+" + this.mLoveIncrement.getIncrease_love());
                startLayoutTagLoveBarAnimation();
                setBackGroundLoveAnimation(this.mLoveIncrement.getIncrease_love());
            } else if (!this.mLoveIncrement.getDecrement_love().equals("0")) {
                textTagLoveBar.setText("-" + this.mLoveIncrement.getDecrement_love());
                startLayoutTagLoveBarAnimation();
                setBackGroundLoveAnimation(this.mLoveIncrement.getDecrement_love());
            } else {
                this.layoutTagLoveBar.setVisibility(View.GONE);
            }

            this.mLoveIncrement.clearValueAll();
            this.mLoveIncrement = null;
            return;
        }
        this.layoutTagLoveBar.setVisibility(View.GONE);
    }

    private void startLayoutTagLoveBarAnimation() {
        this.layoutTagLoveBar.setVisibility(View.VISIBLE);
        Animation fadeOutAnimation = AnimationUtils.loadAnimation(this, R.anim.anim_touch_fade_out);
        fadeOutAnimation.setStartOffset(3000);
        fadeOutAnimation.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationStart(Animation animation) {}

            public void onAnimationRepeat(Animation animation) {}

            public void onAnimationEnd(Animation animation) {
                KanojoRoomActivity.this.layoutTagLoveBar.setVisibility(View.GONE);
            }
        });
        this.layoutTagLoveBar.setAnimation(fadeOutAnimation);
        fadeOutAnimation.start();
    }

	private void displayKanojoDialog(KanojoMessage kanojoMessage) {
		if (kanojoMessage == null || kanojoMessage.getMessages() == null || kanojoMessage.getMessages().length == 0) {
			mKanojoDialogMessage.setVisibility(View.GONE);
		} else {
			mKanojoDialogMessage.initDialogMessage(kanojoMessage);
			mKanojoDialogMessage.setVisibility(View.VISIBLE);
		}
	}

	static class KanojoDialogTask extends AsyncTask<Integer, Void, Response<?>> {
		private final WeakReference<KanojoRoomActivity> activityRef;
		private Exception mReason = null;

		KanojoDialogTask(KanojoRoomActivity activity) {
			super();
			activityRef = new WeakReference<>(activity);
		}

		@Override
		public Response<?> doInBackground(Integer... params) {
			KanojoRoomActivity activity = activityRef.get();
			if (activity == null || activity.isFinishing()) {
				return null;
			}

			try {
				return ((BarcodeKanojoApp) activity.getApplication()).getBarcodeKanojo().show_dialog(params[0]);
			} catch (Exception e) {
				this.mReason = e;
				return null;
			}
		}

		@Override
		public void onPostExecute(Response<?> response) {
			KanojoRoomActivity activity = activityRef.get();
			if (activity == null || activity.isFinishing()) {
				return;
			}
			try {
				if (activity.getCodeAndShowAlert(response, this.mReason) == Response.CODE_SUCCESS) {
					activity.mKanojoMessage = (KanojoMessage) response.get(KanojoMessage.class);
					activity.displayKanojoDialog(activity.mKanojoMessage);
				} else if (Defs.DEBUG) {
					if (mReason != null) {
						mReason.printStackTrace();
					}
				}
			} catch (BarcodeKanojoException e) {
				if (Defs.DEBUG) {
					e.printStackTrace();
				}
			}
		}
	}

    private void setBackGroundLoveAnimation(String love) {
        final int loveValue = Integer.parseInt(love);
        if (loveValue == 0) {
            this.layoutBGActionLoveGauge.setVisibility(View.GONE);
            return;
        }
        this.imageView01 = findViewById(R.id.imageView01);
        this.imageView02 = findViewById(R.id.imageView02);
        this.imageView03 = findViewById(R.id.imageView03);
        if (loveValue > 0 && loveValue < 20) {
            this.imageView01.setImageResource(R.drawable.music_image);
            this.imageView02.setImageResource(R.drawable.music_image);
            this.imageView03.setImageResource(R.drawable.music_image);
        } else if (loveValue >= 20) {
            this.imageView01.setImageResource(R.drawable.heart_image);
            this.imageView02.setImageResource(R.drawable.heart_image);
            this.imageView03.setImageResource(R.drawable.heart_image);
        }

        Animation fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.anim_touch_fade_in);
        fadeInAnimation.setAnimationListener(new Animation.AnimationListener() {
	        @Override
			public void onAnimationStart(Animation animation) {
                KanojoRoomActivity.this.layoutBGActionLoveGauge.setVisibility(View.VISIBLE);
            }

	        @Override
            public void onAnimationRepeat(Animation animation) {}

			@Override
            public void onAnimationEnd(Animation animation) {
                KanojoRoomActivity.this.setAnimationImageLoveBackGround(loveValue);
            }
        });
	    this.layoutBGActionLoveGauge.startAnimation(fadeInAnimation);
    }

    private void setAnimationImageLoveBackGround(int loveValue) {
        if (loveValue > 0 && loveValue < 20) {
            Animation rotateLeftAnimation = AnimationUtils.loadAnimation(this, R.anim.anim_rotate_music_bg);
            rotateLeftAnimation.setRepeatMode(2);
            rotateLeftAnimation.setRepeatCount(2);
            rotateLeftAnimation.setAnimationListener(new Animation.AnimationListener() {
	            @Override
				public void onAnimationStart(Animation animation) {}

	            @Override
				public void onAnimationRepeat(Animation animation) {}

	            @Override
                public void onAnimationEnd(Animation animation) {
                    KanojoRoomActivity.this.closeBackGroundLoveAnimation();
                }
            });
            this.imageView01.startAnimation(rotateLeftAnimation);
            this.imageView02.startAnimation(rotateLeftAnimation);
            this.imageView03.startAnimation(rotateLeftAnimation);
	        //rotateLeftAnimation.start();
        } else if (loveValue >= 20) {
            Animation scaleAnimation = AnimationUtils.loadAnimation(this, R.anim.anim_scale_heart_bg);
            scaleAnimation.setRepeatMode(2);
            scaleAnimation.setRepeatCount(2);
            scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
	            @Override
				public void onAnimationStart(Animation animation) {}

	            @Override
                public void onAnimationRepeat(Animation animation) {}

	            @Override
                public void onAnimationEnd(Animation animation) {
                    KanojoRoomActivity.this.closeBackGroundLoveAnimation();
                }
            });
            this.imageView01.startAnimation(scaleAnimation);
            this.imageView02.startAnimation(scaleAnimation);
            this.imageView03.startAnimation(scaleAnimation);
            //scaleAnimation.start();
        }
    }

    private void closeBackGroundLoveAnimation() {
        Animation fadeOutAnimation = AnimationUtils.loadAnimation(this, R.anim.anim_touch_fade_out);
        fadeOutAnimation.setAnimationListener(new Animation.AnimationListener() {
	        @Override
			public void onAnimationStart(Animation animation) {}

	        @Override
            public void onAnimationRepeat(Animation animation) {}

	        @Override
            public void onAnimationEnd(Animation animation) {
                KanojoRoomActivity.this.layoutBGActionLoveGauge.setVisibility(View.GONE);
            }
        });
        this.layoutBGActionLoveGauge.startAnimation(fadeOutAnimation);
        //fadeOutAnimation.start();
    }
}
