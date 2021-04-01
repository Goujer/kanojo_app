package jp.co.cybird.barcodekanojoForGAM.activity.kanojo;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.http.SslError;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.goujer.barcodekanojo.activity.kanojo.KanojoInfoActivity;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Observable;
import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;
import jp.co.cybird.barcodekanojoForGAM.BarcodeKanojoApp;
import jp.co.cybird.barcodekanojoForGAM.Defs;
import jp.co.cybird.barcodekanojoForGAM.R;
import jp.co.cybird.barcodekanojoForGAM.activity.DashboardActivity;
import jp.co.cybird.barcodekanojoForGAM.activity.KanojosActivity;
import jp.co.cybird.barcodekanojoForGAM.activity.WebViewTabActivity;
import jp.co.cybird.barcodekanojoForGAM.activity.base.BaseActivity;
import jp.co.cybird.barcodekanojoForGAM.activity.base.BaseInterface;
import jp.co.cybird.barcodekanojoForGAM.core.BarcodeKanojo;
import jp.co.cybird.barcodekanojoForGAM.core.exception.BarcodeKanojoException;
import jp.co.cybird.barcodekanojoForGAM.core.model.BarcodeKanojoModel;
import jp.co.cybird.barcodekanojoForGAM.core.model.Kanojo;
import jp.co.cybird.barcodekanojoForGAM.core.model.KanojoItem;
import jp.co.cybird.barcodekanojoForGAM.core.model.KanojoMessage;
import jp.co.cybird.barcodekanojoForGAM.core.model.LoveIncrement;
import jp.co.cybird.barcodekanojoForGAM.core.model.MessageModel;
import jp.co.cybird.barcodekanojoForGAM.core.model.Product;
import jp.co.cybird.barcodekanojoForGAM.core.model.Response;
import jp.co.cybird.barcodekanojoForGAM.core.model.User;
import jp.co.cybird.barcodekanojoForGAM.core.model.WebViewData;
import jp.co.cybird.barcodekanojoForGAM.core.util.FileUtil;
import jp.co.cybird.barcodekanojoForGAM.core.util.FirstbootUtil;
import jp.co.cybird.barcodekanojoForGAM.core.util.Live2dUtil;
import jp.co.cybird.barcodekanojoForGAM.gree.core.GreeDefs;
import jp.co.cybird.barcodekanojoForGAM.live2d.KanojoLive2D;
import jp.co.cybird.barcodekanojoForGAM.live2d.KanojoSetting;
import jp.co.cybird.barcodekanojoForGAM.live2d.view.AndroidEAGLView;
import jp.co.cybird.barcodekanojoForGAM.view.CustomLoadingView;
import jp.co.cybird.barcodekanojoForGAM.view.DialogTextView;

@SuppressLint({"SetJavaScriptEnabled", "NewApi"})
public class KanojoRoomActivity extends BaseActivity implements View.OnClickListener, Observer {

    private static final String TAG = "KanojoRoomActivity";
    private Button btnClose;
    private Button btnDate;
    private ImageButton btnInfo;
    private Button btnItems;
    private ToggleButton btnLike;
    private ImageView btnStatusArrow;
    private Button btnTickets;
    private int dHeight;
    private int dWidth;
    private RelativeLayout dialoglayout;
    private ImageView dropImage;
    private String extraWebViewURL;
    private ImageView imageView01;
    private ImageView imageView02;
    private ImageView imageView03;
    private boolean isPrepared = false;

	private WebView kanojoMessage;
    private RelativeLayout layoutBGActionLoveGauge;
    private RelativeLayout layoutTagLoveBar;
    private RelativeLayout mFirstBoot;
    private Handler mHandler = new Handler();
    private boolean mIsFirstBoot = false;
    private Kanojo mKanojo;
    private DialogTextView mKanojoDialogMessage;
    private KanojoDialogTask mKanojoDialogTask;
    private RelativeLayout mKanojoLayout;
    private KanojoLive2D mKanojoLive2D;
    private KanojoMessage mKanojoMessage;
    private KanojoRoomTask mKanojoRoomTask;
	private Live2dTask mLive2dTask;
    private Live2dUtil mLive2dUtil;
    private CustomLoadingView mLoadingView;
    private ProgressBar mLoveBar;
    private LoveIncrement mLoveIncremen;
    private MessageModel mMessage;
    private Product mProduct;
    private RelativeLayout mProgressBar;
    private Runnable mProgressThread = new Runnable() {
        public void run() {
            if (KanojoRoomActivity.this.getLive2D().isModelAvailable()) {
                KanojoRoomActivity.this.dismissProgressDialog();
                KanojoRoomActivity.this.mHandler.removeCallbacks(KanojoRoomActivity.this.mProgressThread);
                return;
            }
            KanojoRoomActivity.this.mHandler.postDelayed(KanojoRoomActivity.this.mProgressThread, 100);
        }
    };
    private LinearLayout mStatusLayout;
    private Timer mTimerCallAPI;
    private User mUser;
    private LinearLayout statusBarLayout;
    private WebView webview;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kanojo_room);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        this.dWidth = displayMetrics.widthPixels;
        this.dHeight = displayMetrics.heightPixels;
        this.btnClose = findViewById(R.id.kanojo_room_close);
        this.btnClose.setOnClickListener(this);
        this.btnInfo = findViewById(R.id.kanojo_room_info);
        this.btnInfo.setOnClickListener(this);
        this.btnLike = findViewById(R.id.kanojo_room_like_btn);
        this.btnLike.setOnClickListener(this);
        this.btnDate = findViewById(R.id.kanojo_room_date_btn);
        this.btnDate.setOnClickListener(this);
        this.btnItems = findViewById(R.id.kanojo_room_items_btn);
        this.btnItems.setOnClickListener(this);
        this.btnTickets = findViewById(R.id.kanojo_room_ticket_btn);
        this.btnTickets.setOnClickListener(this);
        this.btnDate.setVisibility(View.GONE);
        this.btnDate.setEnabled(false);
        this.btnItems.setVisibility(View.GONE);
        this.btnItems.setEnabled(false);
        this.btnTickets.setVisibility(View.GONE);
        this.btnTickets.setEnabled(false);
        this.mStatusLayout = findViewById(R.id.kanojo_room_status_txt_layout);
        this.statusBarLayout = findViewById(R.id.kanojo_room_status_bar_layout);
        this.statusBarLayout.setOnClickListener(this);
        TextView txtStatus = findViewById(R.id.kanojo_room_status_txt);
        this.mLoveBar = findViewById(R.id.kanojo_room_status_bar);
        this.btnStatusArrow = findViewById(R.id.kanojo_room_arrow);
        this.mKanojoLayout = findViewById(R.id.kanojo_room_live2d);
        this.mLoadingView = findViewById(R.id.loadingView);
        this.kanojoMessage = findViewById(R.id.kanojo_room_message_banner);
        this.kanojoMessage.setBackgroundColor(0);
        if (Build.VERSION.SDK_INT >= 11) {
            this.kanojoMessage.setLayerType(1, null);
        }
        this.dropImage = findViewById(R.id.dropdown_img);
        this.dropImage.setOnClickListener(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            this.mKanojo = (Kanojo) bundle.get(BaseInterface.EXTRA_KANOJO);
        }
        if (this.mKanojo != null) {
            this.webview = findViewById(R.id.radar_webview);
            this.webview.setWebViewClient(new MyWebViewClient(this, null));
            this.webview.getSettings().setJavaScriptEnabled(true);
            this.webview.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
            this.webview.setOnTouchListener(new View.OnTouchListener() {
                @Override
            	public boolean onTouch(View v, MotionEvent event) {
                    return event.getAction() == 2;
                }
            });
            this.webview.setBackgroundColor(0);
            if (Build.VERSION.SDK_INT >= 11) {
                this.webview.setLayerType(1, null);
            }
            this.webview.setInitialScale(1);
            this.webview.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
            this.mProgressBar = findViewById(R.id.progressbar_radar_view);
            txtStatus.setText(this.mKanojo.getStatus());
            this.btnLike.setChecked(this.mKanojo.isVoted_like());
            TextView txtName = findViewById(R.id.kanojo_room_name);
            if (txtName != null) {
                txtName.setText(this.mKanojo.getName());
            }
            this.mKanojoDialogMessage = findViewById(R.id.dialog_message);
            this.dialoglayout = findViewById(R.id.dialog_frame_all);
            this.dialoglayout.setVisibility(View.INVISIBLE);
            this.dialoglayout.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                }
            });
            getResources().getDrawable(R.drawable.dialog_frame).setAlpha(180);
            findViewById(R.id.dialog_frame).setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    KanojoRoomActivity.this.dialoglayout.setVisibility(View.GONE);
                }
            });
			DialogTextView.OnDismissListener mListener = new DialogTextView.OnDismissListener() {
				public void onSiteClick(String url) {
					Log.d(TAG, "mKanojoMessage.getNextScreen(): " + KanojoRoomActivity.this.mKanojoMessage.getNextScreen());
					if (KanojoRoomActivity.this.mKanojoMessage.getNextScreen().equalsIgnoreCase("webview")) {
						Intent intent = new Intent(KanojoRoomActivity.this, WebViewTabActivity.class);
						intent.putExtra(BaseInterface.EXTRA_WEBVIEW_URL, url);
						KanojoRoomActivity.this.startActivity(intent);
						KanojoRoomActivity.this.finish();
					} else if (KanojoRoomActivity.this.mKanojoMessage.getNextScreen().equalsIgnoreCase("dashboard")) {
						KanojoRoomActivity.this.startActivity(new Intent(KanojoRoomActivity.this, DashboardActivity.class));
						KanojoRoomActivity.this.finish();
					} else if (KanojoRoomActivity.this.mKanojoMessage.getNextScreen().equalsIgnoreCase("kanojo")) {
						Intent intent2 = new Intent(KanojoRoomActivity.this, KanojosActivity.class);
						intent2.putExtra(BaseInterface.EXTRA_KANOJO_ITEM, KanojoRoomActivity.this.mKanojoMessage.getKanojo_id());
						KanojoRoomActivity.this.setResult(BaseInterface.RESULT_KANOJO_MESSAGE_DIALOG, intent2);
						KanojoRoomActivity.this.finish();
					}
				}

				public void OnCloseClick() {
					KanojoRoomActivity.this.dialoglayout.setVisibility(View.GONE);
				}
			};
            this.mKanojoDialogMessage.setListener(mListener);
            executeKanojoRoomTask();
            this.mFirstBoot = findViewById(R.id.kanojo_room_firstboot);
            if (this.mKanojo.getRelation_status() == 1) {
                this.mIsFirstBoot = false;
                this.mFirstBoot.setVisibility(View.GONE);
                cleanupView(this.mFirstBoot);
            } else if (!FirstbootUtil.isShowed(this, "kanojo_room_firstboot")) {
                this.mIsFirstBoot = true;
                this.mFirstBoot.setVisibility(View.VISIBLE);
                this.mFirstBoot.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        KanojoRoomActivity.this.mFirstBoot.setVisibility(View.GONE);
                        KanojoRoomActivity.this.cleanupView(KanojoRoomActivity.this.mFirstBoot);
                    }
                });
            } else {
                this.mIsFirstBoot = false;
                this.mFirstBoot.setVisibility(View.GONE);
                cleanupView(this.mFirstBoot);
            }
            if (this.mKanojo.getRelation_status() == Kanojo.RELATION_KANOJO) {
                this.kanojoMessage.setVisibility(View.VISIBLE);
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) this.btnItems.getLayoutParams();
                params.addRule(RelativeLayout.ABOVE, R.id.kanojo_room_message_banner);
                this.btnItems.setLayoutParams(params);
                RelativeLayout.LayoutParams params2 = (RelativeLayout.LayoutParams) this.dialoglayout.getLayoutParams();
                params2.addRule(RelativeLayout.ABOVE, R.id.kanojo_room_message_banner);
                this.dialoglayout.setLayoutParams(params2);
                if (this.mKanojo.getAdvertising_banner_url() == null || this.mKanojo.getAdvertising_banner_url().equals("null")) {
                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) this.btnItems.getLayoutParams();
                    layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                    this.btnItems.setLayoutParams(layoutParams);
                    this.kanojoMessage.setVisibility(View.GONE);
                } else {
                    addReactionWord(this.mKanojo.getAdvertising_banner_url());
                }
            } else {
				RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) this.btnItems.getLayoutParams();
				layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
				this.btnItems.setLayoutParams(layoutParams);
                this.kanojoMessage.setVisibility(View.GONE);
            }
            executeKanojoShowDialogTask();
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
            if (this.mKanojo.getRelation_status() == Kanojo.RELATION_OTHER) {
                this.btnDate.setVisibility(View.GONE);
                this.btnDate.setEnabled(false);
                this.btnItems.setVisibility(View.GONE);
                this.btnItems.setEnabled(false);
                this.btnTickets.setVisibility(View.GONE);
                this.btnTickets.setEnabled(false);
                this.mFirstBoot.setVisibility(View.GONE);
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

//    public static final String md5(String s) {
//        try {
//            MessageDigest digest = MessageDigest.getInstance(Digest.MD5);
//            digest.update(s.getBytes());
//            byte[] messageDigest = digest.digest();
//            StringBuffer hexString = new StringBuffer();
//            for (byte b : messageDigest) {
//                String h = Integer.toHexString(b & 255);
//                while (h.length() < 2) {
//                    h = GreeDefs.BARCODE + h;
//                }
//                hexString.append(h);
//            }
//            return hexString.toString();
//        } catch (NoSuchAlgorithmException e) {
//            Log.v(TAG, e.toString());
//            return "";
//        }
//    }

    public void bindEvent() {
        this.statusBarLayout.setOnClickListener(this);
        this.btnClose.setOnClickListener(this);
        this.btnInfo.setOnClickListener(this);
        this.btnLike.setOnClickListener(this);
        this.btnDate.setOnClickListener(this);
        this.btnItems.setOnClickListener(this);
        this.btnTickets.setOnClickListener(this);
        this.dropImage.setOnClickListener(this);
    }

    public void unBindEvent() {
        this.statusBarLayout.setOnClickListener(null);
        this.btnClose.setOnClickListener(null);
        this.btnInfo.setOnClickListener(null);
        this.btnLike.setOnClickListener(null);
        this.btnDate.setOnClickListener(null);
        this.btnItems.setOnClickListener(null);
        this.btnTickets.setOnClickListener(null);
        this.dropImage.setOnClickListener(null);
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
        this.statusBarLayout.setOnClickListener(null);
        this.btnClose.setOnClickListener(null);
        this.btnInfo.setOnClickListener(null);
        this.btnLike.setOnClickListener(null);
        this.btnDate.setOnClickListener(null);
        this.btnItems.setOnClickListener(null);
        super.onDestroy();
    }

    static class GetURLWebView extends AsyncTask<Void, Void, Response<?>> {
        WeakReference<KanojoRoomActivity> contextRef;

    	GetURLWebView(KanojoRoomActivity context) {
    		contextRef = new WeakReference<>(context);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            contextRef.get().showProgressDialog();
        }

        @Override
        protected Response<?> doInBackground(Void... params) {
            try {
                getURLRadarWebView();
                return null;
            } catch (BarcodeKanojoException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Response<?> result) {
			contextRef.get().dismissProgressDialog();
            super.onPostExecute(result);
        }

        private void getURLRadarWebView() throws BarcodeKanojoException {
            BarcodeKanojo barcodeKanojo = ((BarcodeKanojoApp) contextRef.get().getApplication()).getBarcodeKanojo();
			contextRef.get().mUser = barcodeKanojo.getUser();
            final Response<BarcodeKanojoModel> uRLRadarWebView = barcodeKanojo.getURLRadarWebView(contextRef.get().mKanojo.getId());
            if (uRLRadarWebView == null) {
                throw new BarcodeKanojoException("Error: URL webview not found");
            }
            int code = uRLRadarWebView.getCode();
            switch (code) {
                case 200:
					contextRef.get().runOnUiThread(new Runnable() {
						@Override
						public void run() {
							contextRef.get().webview.loadUrl(((WebViewData) uRLRadarWebView.get(WebViewData.class)).getUrl());
						}
					});
                    return;
                case 400:
                case 401:
                case 403:
                case 404:
                case 500:
                case 503:
					contextRef.get().runOnUiThread(new Runnable() {
						@Override
						public void run() {
							contextRef.get().dismissProgressDialog();
						}
					});
                    throw new BarcodeKanojoException("Error: Code: " + code + " WebView not initialized!");
                default:
            }
        }
    }

    private class MyWebViewClient extends WebViewClient {
        private MyWebViewClient() {
        }

        /* synthetic */ MyWebViewClient(KanojoRoomActivity kanojoRoomActivity, MyWebViewClient myWebViewClient) {
            this();
        }

        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed();
        }

        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            KanojoRoomActivity.this.mProgressBar.setVisibility(View.GONE);
            KanojoRoomActivity.this.webview.setVisibility(View.VISIBLE);
            view.clearCache(true);
        }
    }

    @Override
    public void onClick(View v) {
        unBindEvent();
        new Intent();
        switch (v.getId()) {
            case R.id.kanojo_room_close:
                setResult(BaseInterface.RESULT_KANOJO_ROOM_EXIT);
                close();
                return;
            case R.id.kanojo_room_info:
                this.webview.setVisibility(View.GONE);
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
                return;
            case R.id.kanojo_room_like_btn:
                this.mKanojo.setVoted_like(this.btnLike.isChecked());
                bindEvent();
                return;
            case R.id.kanojo_room_items_btn:
                this.webview.setVisibility(View.GONE);
                Intent intent2 = new Intent(this, KanojoItemsActivity.class);
                if (this.mKanojo != null) {
                    intent2.putExtra(BaseInterface.EXTRA_KANOJO, this.mKanojo);
                }
                intent2.putExtra(BaseInterface.EXTRA_KANOJO_ITEM_MODE, 2);
                startActivityForResult(intent2, BaseInterface.REQUEST_KANOJO_ITEMS);
                return;
            case R.id.kanojo_room_date_btn:
                this.webview.setVisibility(View.GONE);
                Intent intent3 = new Intent(this, KanojoItemsActivity.class);
                if (this.mKanojo != null) {
                    intent3.putExtra(BaseInterface.EXTRA_KANOJO, this.mKanojo);
                }
                intent3.putExtra(BaseInterface.EXTRA_KANOJO_ITEM_MODE, 1);
                startActivityForResult(intent3, BaseInterface.REQUEST_KANOJO_ITEMS);
                return;
            case R.id.kanojo_room_ticket_btn:
                this.webview.setVisibility(View.GONE);
                Intent intent4 = new Intent(this, KanojoItemsActivity.class);
                if (this.mKanojo != null) {
                    intent4.putExtra(BaseInterface.EXTRA_KANOJO, this.mKanojo);
                }
                KanojoItem item = new KanojoItem(3);
                item.setTitle(getResources().getString(R.string.kanojo_items_store));
                intent4.putExtra(BaseInterface.EXTRA_KANOJO_ITEM, item);
                intent4.putExtra(BaseInterface.EXTRA_KANOJO_ITEM_MODE, 3);
                startActivityForResult(intent4, BaseInterface.REQUEST_KANOJO_TICKETS);
                return;
            case R.id.kanojo_room_status_bar_layout:
            case R.id.dropdown_img:
                if (this.mStatusLayout.getVisibility() == View.VISIBLE) {
                    this.webview.setVisibility(View.GONE);
                    this.mStatusLayout.setVisibility(View.GONE);
                    this.btnStatusArrow.setImageResource(R.drawable.kanojolovebararrowdown);
                } else {
                    this.mStatusLayout.setVisibility(View.VISIBLE);
                    this.webview.setVisibility(View.VISIBLE);
                    this.btnStatusArrow.setImageResource(R.drawable.kanojolovebararrowup);
                    if (this.extraWebViewURL == null) {
                        new GetURLWebView(this).execute();
                    } else {
                        this.webview.loadUrl(this.extraWebViewURL);
                    }
                }
                bindEvent();
                return;
            default:
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bundle bundle;
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 101) {
            executeKanojoRoomTask();
            return;
        }
        if (!(resultCode != 204 || data == null || (bundle = data.getExtras()) == null)) {
            Kanojo kanojo = (Kanojo) bundle.get(BaseInterface.EXTRA_KANOJO);
            LoveIncrement loveIncrement = (LoveIncrement) bundle.get(BaseInterface.EXTRA_LOVE_INCREMENT);
            if (kanojo != null) {
                this.mKanojo = kanojo;
                if (loveIncrement != null) {
                    this.mLoveIncremen = loveIncrement;
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
            if (barcodeKanojo != null) {
                barcodeKanojo.setPlayLive2d(this.mKanojo, kanojoLive2D.getUserActions());
            }
            kanojoLive2D.stopAnimation();
            kanojoLive2D.releaseView();
            kanojoLive2D.releaseModel();
        }
    }

    private void startLive2D() {
        KanojoLive2D kanojoLive2D = getLive2D();
        if (kanojoLive2D != null) {
            AndroidEAGLView view = kanojoLive2D.createView(getApplicationContext(), new Rect(0, 0, 320, 320));
            this.mKanojoLayout.removeAllViews();
            this.mKanojoLayout.addView(view, new LinearLayout.LayoutParams(-1, -2, 1.0f));
            if (this.mKanojo.getMascotEnable() == 1) {
                kanojoLive2D.setBackgroundImage("bg_permanent_kanojo.png", false);
            } else {
                kanojoLive2D.setBackgroundImage("back256.png", false);
            }
            kanojoLive2D.setKanojoRoomActivity(this);
        }
    }

    private void settingLive2D() {
        if (FileUtil.isAvailableExternalSDMemory() || FileUtil.isAvailableInternalSDMemory()) {
            KanojoLive2D kanojoLive2D = getLive2D();
            if (kanojoLive2D != null && this.isPrepared) {
                if (this.mKanojo != null) {
                    this.mLoveBar.setProgress(this.mKanojo.getLove_gauge());
                    this.btnLike.setChecked(this.mKanojo.isVoted_like());
                }
                if (!this.mIsFirstBoot) {
                }
                boolean in_room = this.mKanojo.isIn_room();
                kanojoLive2D.setInRoom(in_room);
                if (in_room && this.mKanojo.getRelation_status() != Kanojo.RELATION_OTHER) {
                    this.btnDate.setVisibility(View.VISIBLE);
                    this.btnDate.setEnabled(true);
                    this.btnItems.setVisibility(View.VISIBLE);
                    this.btnItems.setEnabled(true);
                    this.btnTickets.setVisibility(View.VISIBLE);
                    this.btnTickets.setEnabled(true);
                }
                if (!in_room) {
                    dismissProgressDialog();
                    this.mHandler.removeCallbacks(this.mProgressThread);
                }
                KanojoSetting setting = kanojoLive2D.getKanojoSetting();
                setting.setKanojoState(this.mKanojo.getRelation_status());
                if (getLive2DUtil().setLive2DKanojoPartsAndRequest(setting, this.mKanojo)) {
                    if (getLive2DUtil().setLive2DKanojoBackground(this.mKanojo)) {
                        setting.setLoveGage(this.mKanojo.getLove_gauge());
                        kanojoLive2D.setupModel(true);
                        kanojoLive2D.startAnimation();
                        if (this.mLoveIncremen != null) {
                            new Handler().postDelayed(new Runnable() {
                                public void run() {
                                    KanojoRoomActivity.this.addTagLoveGauge();
                                }
                            }, 2000);
                        }
                    }
                } else if (this.mKanojo.getMascotEnable() == 1) {
                    kanojoLive2D.setBackgroundImage("bg_permanent_kanojo.png", false);
                } else {
                    kanojoLive2D.setBackgroundImage("back256.png", false);
                }
            }
        } else {
            sendBroadcast(new Intent(BarcodeKanojoApp.INTENT_ACTION_FULL_STORAGE));
        }
    }

    private void executeKanojoRoomTask() {
        this.isPrepared = false;
        if (this.mKanojoRoomTask == null || this.mKanojoRoomTask.getStatus() == AsyncTask.Status.FINISHED || this.mKanojoRoomTask.cancel(true) || this.mKanojoRoomTask.isCancelled()) {
            this.mKanojoRoomTask = (KanojoRoomTask) new KanojoRoomTask().execute(new Void[0]);
        }
    }

    class KanojoRoomTask extends AsyncTask<Void, Void, Response<?>> {
        private Exception mReason = null;

        KanojoRoomTask() {
        }

        public void onPreExecute() {
        }

        public Response<?> doInBackground(Void... params) {
            try {
                return process();
            } catch (Exception e) {
                this.mReason = e;
                return null;
            }
        }

        public void onPostExecute(Response<?> response) {
            try {
				if (KanojoRoomActivity.this.getCodeAndShowAlert(response, this.mReason) == 200) {
					KanojoRoomActivity.this.mKanojo = (Kanojo) response.get(Kanojo.class);
					KanojoRoomActivity.this.mProduct = (Product) response.get(Product.class);
					KanojoRoomActivity.this.mMessage = (MessageModel) response.get(MessageModel.class);
					if (KanojoRoomActivity.this.mKanojo != null && KanojoRoomActivity.this.mProduct != null) {
						KanojoRoomActivity.this.isPrepared = true;
						KanojoRoomActivity.this.settingLive2D();
					} else {
						throw new BarcodeKanojoException("Response Error: Data is null");
					}
				}
                if (!KanojoRoomActivity.this.isPrepared) {
                    KanojoRoomActivity.this.dismissProgressDialog();
                    KanojoRoomActivity.this.btnInfo.setVisibility(View.INVISIBLE);
                } else {
                    KanojoRoomActivity.this.btnInfo.setVisibility(View.VISIBLE);
                }
                if (KanojoRoomActivity.this.mStatusLayout.getVisibility() == View.VISIBLE) {
                    KanojoRoomActivity.this.mStatusLayout.setVisibility(View.GONE);
                    KanojoRoomActivity.this.btnStatusArrow.setImageResource(R.drawable.kanojolovebararrowdown);
                }
            } catch (BarcodeKanojoException e) {
                if (!KanojoRoomActivity.this.isPrepared) {
                    KanojoRoomActivity.this.dismissProgressDialog();
                    KanojoRoomActivity.this.btnInfo.setVisibility(View.INVISIBLE);
                } else {
                    KanojoRoomActivity.this.btnInfo.setVisibility(View.VISIBLE);
                }
                if (KanojoRoomActivity.this.mStatusLayout.getVisibility() == View.VISIBLE) {
                    KanojoRoomActivity.this.mStatusLayout.setVisibility(View.GONE);
                    KanojoRoomActivity.this.btnStatusArrow.setImageResource(R.drawable.kanojolovebararrowdown);
                }
            } catch (Throwable th) {
                if (!KanojoRoomActivity.this.isPrepared) {
                    KanojoRoomActivity.this.dismissProgressDialog();
                    KanojoRoomActivity.this.btnInfo.setVisibility(View.INVISIBLE);
                } else {
                    KanojoRoomActivity.this.btnInfo.setVisibility(View.VISIBLE);
                }
                if (KanojoRoomActivity.this.mStatusLayout.getVisibility() == View.VISIBLE) {
                    KanojoRoomActivity.this.mStatusLayout.setVisibility(View.GONE);
                    KanojoRoomActivity.this.btnStatusArrow.setImageResource(R.drawable.kanojolovebararrowdown);
                }
                throw th;
            }
        }

        protected void onCancelled() {
        }

        Response<?> process() throws BarcodeKanojoException, IllegalStateException, IOException {
            return ((BarcodeKanojoApp) KanojoRoomActivity.this.getApplication()).getBarcodeKanojo().show(KanojoRoomActivity.this.mKanojo.getId(), true);
        }
    }

    public void update(Observable observable, Object data) {
        this.mHandler.post(new Runnable() {
            public void run() {
                KanojoRoomActivity.this.settingLive2D();
            }
        });
    }

    private void executeKanojoShowDialogTask() {
        this.isPrepared = false;
        if (this.mKanojoDialogTask == null || this.mKanojoDialogTask.getStatus() == AsyncTask.Status.FINISHED || this.mKanojoDialogTask.cancel(true) || this.mKanojoDialogTask.isCancelled()) {
            this.mKanojoDialogTask = (KanojoDialogTask) new KanojoDialogTask().execute(new Void[0]);
        }
    }

    class KanojoDialogTask extends AsyncTask<Void, Void, Response<?>> {
        private Exception mReason = null;

        KanojoDialogTask() {
        }

        @Override
        public void onPreExecute() {
        }

        @Override
        public Response<?> doInBackground(Void... params) {
            try {
                return process();
            } catch (Exception e) {
                this.mReason = e;
                return null;
            }
        }

        @Override
        public void onPostExecute(Response<?> response) {
            try {
				if (KanojoRoomActivity.this.getCodeAndShowAlert(response, this.mReason) == 200) {
					KanojoRoomActivity.this.mKanojoMessage = (KanojoMessage) response.get(KanojoMessage.class);
					if (KanojoRoomActivity.this.mKanojoMessage == null) {
						KanojoRoomActivity.this.dialoglayout.setVisibility(View.GONE);
					} else if (KanojoRoomActivity.this.mKanojoMessage.getMessage() == null || KanojoRoomActivity.this.mKanojoMessage.getMessage().equals("")) {
						KanojoRoomActivity.this.dialoglayout.setVisibility(View.GONE);
					} else {
						KanojoRoomActivity.this.mKanojoDialogMessage.initDialogMessage(KanojoRoomActivity.this.mKanojoMessage.getMessage(), KanojoRoomActivity.this.mKanojoMessage.getSiteURL(), KanojoRoomActivity.this.mKanojoMessage.getButtonText());
						KanojoRoomActivity.this.dialoglayout.setVisibility(View.VISIBLE);
					}
				} else if (Defs.DEBUG) {
					mReason.printStackTrace();
				}
			} catch (BarcodeKanojoException e) {
            	if (Defs.DEBUG) {
            		e.printStackTrace();
				}
            }
        }

        @Override
        protected void onCancelled() {
        }

        Response<?> process() throws BarcodeKanojoException, IllegalStateException, IOException {
            return ((BarcodeKanojoApp) KanojoRoomActivity.this.getApplication()).getBarcodeKanojo().show_dialog();
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode != 4 || !this.mLoadingView.isShow()) {
            return super.onKeyDown(keyCode, event);
        }
        this.mLoadingView.setMessage(getString(R.string.requesting_cant_cancel));
        return true;
    }

    @Override
    public ProgressDialog showProgressDialog() {
        this.mLoadingView.show();
        return new ProgressDialog(this);
    }

    @Override
    protected void dismissProgressDialog() {
        this.mLoadingView.dismiss();
    }

    class MyJavaScriptInterface {
        MyJavaScriptInterface() {
        }

        @JavascriptInterface
        public void getValue(String wordCount) {
            new LongOperation(this, null).execute(wordCount);
        }

        private class LongOperation extends AsyncTask<String, Void, String> {
            private LongOperation() {
            }

            /* synthetic */ LongOperation(MyJavaScriptInterface myJavaScriptInterface, LongOperation longOperation) {
                this();
            }

            protected String doInBackground(String... params) {
                try {
                    Thread.sleep(Integer.parseInt(params[0]) * 5000);
                    return null;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return null;
                }
            }

            protected void onPostExecute(String result) {
                if (KanojoRoomActivity.this.mKanojo.getRelation_status() == Kanojo.RELATION_KANOJO) {
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) KanojoRoomActivity.this.btnItems.getLayoutParams();
                    params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
                    KanojoRoomActivity.this.btnItems.setLayoutParams(params);
                }
                KanojoRoomActivity.this.kanojoMessage.setVisibility(View.GONE);
            }
        }
    }

    public void setAnimationTap(float x, float y, int type) {
        ImageView imageViewAction = new ImageView(this);
        if (12 == type) {
            System.out.println("USER_ACTION_TSUTSUKU ");
            imageViewAction.setImageResource(R.drawable.hand_image);
        } else if (20 == type) {
            System.out.println("USER_ACTION_KISS ");
            imageViewAction.setImageResource(R.drawable.kiss_image);
        } else if (21 == type) {
            System.out.println("USER_ACTION_MUNE ");
            imageViewAction.setImageResource(R.drawable.hand_image);
        }
        addImageAnimationTap(imageViewAction, x, y);
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
                handler.post(new Runnable() {
                    public void run() {
                        imageView.clearAnimation();
                        imageView.setImageBitmap(null);
                        KanojoRoomActivity.this.mKanojoLayout.removeView(imageView);
                    }
                });
            }
        });
        this.mKanojoLayout.addView(imageView);
        animationActionTap.startNow();
    }

    public void callUserAction(int userAction) {
        if (10 == userAction || 11 == userAction || 12 == userAction || 20 != userAction) {
        }
        if (this.mTimerCallAPI != null) {
            this.mTimerCallAPI.cancel();
            this.mTimerCallAPI.purge();
        }
        this.mTimerCallAPI = new Timer();
        final Handler mTimerCallAPIHandler = new Handler();
        this.mTimerCallAPI.schedule(new TimerTask() {
            public void run() {
                mTimerCallAPIHandler.post(new Runnable() {
                    public void run() {
                        KanojoRoomActivity.this.executeLive2dTask();
                    }
                });
            }
        }, 2000);
    }

    private void executeLive2dTask() {
        if (this.mLive2dTask == null || this.mLive2dTask.getStatus() == AsyncTask.Status.FINISHED || this.mLive2dTask.cancel(true) || this.mLive2dTask.isCancelled()) {
            this.mLive2dTask = new Live2dTask();
            this.mLive2dTask.execute();
        }
    }

    class Live2dTask extends AsyncTask<Void, Void, Response<BarcodeKanojoModel>> {
        private Exception mReason = null;

        Live2dTask() {
        }

        public void onPreExecute() {
            KanojoLive2D kanojoLive2D = KanojoRoomActivity.this.getLive2D();
            BarcodeKanojo barcodeKanojo = ((BarcodeKanojoApp) KanojoRoomActivity.this.getApplication()).getBarcodeKanojo();
            if (barcodeKanojo != null) {
                barcodeKanojo.setPlayLive2d(KanojoRoomActivity.this.mKanojo, kanojoLive2D.getUserActions());
            }
        }

        public Response<BarcodeKanojoModel> doInBackground(Void... params) {
            try {
                return process();
            } catch (Exception e) {
                this.mReason = e;
                return null;
            }
        }

        public void onPostExecute(Response<BarcodeKanojoModel> response) {
            if (response != null) {
                int code = response.getCode();
                KanojoRoomActivity.this.mLoveIncremen = (LoveIncrement) response.get(LoveIncrement.class);
                if (KanojoRoomActivity.this.mLoveIncremen != null && KanojoRoomActivity.this.mLoveIncremen.getAlertShow().equals(GreeDefs.KANOJO_NAME)) {
                    try {
                        int unused = KanojoRoomActivity.this.getCodeAndShowAlert(response, this.mReason);
                    } catch (BarcodeKanojoException e) {
                        e.printStackTrace();
                    }
                }
				if (code == 200) {
					KanojoRoomActivity.this.mKanojo = (Kanojo) response.get(Kanojo.class);
					KanojoRoomActivity.this.updateUser(response);
					KanojoRoomActivity.this.setAutoRefreshLoveGageAction();
				}
			}
        }

        Response<BarcodeKanojoModel> process() throws BarcodeKanojoException, IllegalStateException, IOException {
            return ((BarcodeKanojoApp) KanojoRoomActivity.this.getApplication()).getBarcodeKanojo().play_on_live2d();
        }
    }

    private void setAutoRefreshLoveGageAction() {
        if (!FileUtil.isAvailableExternalSDMemory() && !FileUtil.isAvailableInternalSDMemory()) {
            sendBroadcast(new Intent(BarcodeKanojoApp.INTENT_ACTION_FULL_STORAGE));
        } else if (getLive2D() != null && this.isPrepared && this.mKanojo != null) {
            this.mLoveBar.setProgress(this.mKanojo.getLove_gauge());
            addTagLoveGauge();
        }
    }

    private void addTagLoveGauge() {
        Rect rectLoveBar = new Rect();
        this.mLoveBar.getLocalVisibleRect(rectLoveBar);
        int xLoveBar = rectLoveBar.left;
        int wLoveBar = rectLoveBar.width();
        this.layoutTagLoveBar = findViewById(R.id.kanojo_tag_love_gauge);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) this.layoutTagLoveBar.getLayoutParams();
        params.leftMargin = ((wLoveBar / 100) * this.mKanojo.getLove_gauge()) + xLoveBar;
        this.layoutTagLoveBar.setLayoutParams(params);
        TextView textTagLoveBar = findViewById(R.id.kanojo_tag_love_gauge_text);
        if (this.mLoveIncremen != null) {
            if (!this.mLoveIncremen.getIncrease_love().equals(GreeDefs.BARCODE)) {
                textTagLoveBar.setText("+" + this.mLoveIncremen.getIncrease_love());
                startLayoutTagLoveBarAnimation();
                setBackGroundLoveAnimation(this.mLoveIncremen.getIncrease_love());
            } else if (!this.mLoveIncremen.getDecrement_love().equals(GreeDefs.BARCODE)) {
                textTagLoveBar.setText("-" + this.mLoveIncremen.getDecrement_love());
                startLayoutTagLoveBarAnimation();
                setBackGroundLoveAnimation(this.mLoveIncremen.getDecrement_love());
            } else {
                this.layoutTagLoveBar.setVisibility(View.GONE);
            }
            addReactionWord(this.mLoveIncremen.getReaction_word());
            this.mLoveIncremen.clearValueAll();
            this.mLoveIncremen = null;
            return;
        }
        this.layoutTagLoveBar.setVisibility(View.GONE);
    }

    private void startLayoutTagLoveBarAnimation() {
        this.layoutTagLoveBar.setVisibility(View.VISIBLE);
        Animation fadeOutAnimation = AnimationUtils.loadAnimation(this, R.anim.anim_touch_fade_out);
        fadeOutAnimation.setStartOffset(3000);
        fadeOutAnimation.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationStart(Animation animation) {
            }

            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationEnd(Animation animation) {
                KanojoRoomActivity.this.layoutTagLoveBar.setVisibility(View.GONE);
            }
        });
        this.layoutTagLoveBar.setAnimation(fadeOutAnimation);
        fadeOutAnimation.start();
    }

    @SuppressLint({"SetJavaScriptEnabled"})
    private void addReactionWord(String reactionWord) {
		String url = reactionWord;
		if (url.equals("null") || url.equals("")) {
			this.kanojoMessage.setVisibility(View.GONE);
			return;
		}
		this.kanojoMessage.loadUrl(url);
		this.kanojoMessage.setVisibility(View.VISIBLE);

		RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) this.btnItems.getLayoutParams();
		params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 0);
		params.addRule(RelativeLayout.ABOVE, R.id.kanojo_room_message_banner);
		this.btnItems.setLayoutParams(params);

		//TODO: Figure out what to do for earlier versions. (Go through all methods and variables and restrict as much as possible?)
		this.kanojoMessage.addJavascriptInterface(new MyJavaScriptInterface(), "Android");
        this.kanojoMessage.getSettings().setJavaScriptEnabled(true);
        this.kanojoMessage.setWebViewClient(new WebViewClient() {
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
            }

            public boolean shouldOverrideUrlLoading(WebView webView, String urlNewString) {
                webView.loadUrl(urlNewString);
                return true;
            }

            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            public void onPageFinished(WebView lwebView, String urlPageFinished) {
                lwebView.loadUrl("javascript:( function () { var resultSrc = slen.length ; Android.getValue(resultSrc); } ) ()");
            }
        });
    }

    private void setBackGroundLoveAnimation(String love) {
        final int loveValue = Integer.parseInt(love);
        this.layoutBGActionLoveGauge = findViewById(R.id.kanojo_bg_action_love_gauge);
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
        this.layoutBGActionLoveGauge.setAnimation(fadeInAnimation);
        fadeInAnimation.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationStart(Animation animation) {
                KanojoRoomActivity.this.layoutBGActionLoveGauge.setVisibility(View.VISIBLE);
            }

            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationEnd(Animation animation) {
                KanojoRoomActivity.this.setAnimationImageLoveBackGround(loveValue);
            }
        });
        fadeInAnimation.start();
    }

    private void setAnimationImageLoveBackGround(int loveValue) {
        if (loveValue > 0 && loveValue < 20) {
            Animation rotateLeftAnimation = AnimationUtils.loadAnimation(this, R.anim.anim_rotate_music_bg);
            rotateLeftAnimation.setRepeatMode(2);
            rotateLeftAnimation.setRepeatCount(2);
            rotateLeftAnimation.setAnimationListener(new Animation.AnimationListener() {
                public void onAnimationStart(Animation animation) {
                }

                public void onAnimationRepeat(Animation animation) {
                }

                public void onAnimationEnd(Animation animation) {
                    KanojoRoomActivity.this.closeBackGroundLoveAnimation();
                }
            });
            this.imageView01.setAnimation(rotateLeftAnimation);
            this.imageView02.setAnimation(rotateLeftAnimation);
            this.imageView03.setAnimation(rotateLeftAnimation);
            rotateLeftAnimation.start();
        } else if (loveValue >= 20) {
            Animation scaleAnimation = AnimationUtils.loadAnimation(this, R.anim.anim_scale_heart_bg);
            scaleAnimation.setRepeatMode(2);
            scaleAnimation.setRepeatCount(2);
            scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
                public void onAnimationStart(Animation animation) {
                }

                public void onAnimationRepeat(Animation animation) {
                }

                public void onAnimationEnd(Animation animation) {
                    KanojoRoomActivity.this.closeBackGroundLoveAnimation();
                }
            });
            this.imageView01.setAnimation(scaleAnimation);
            this.imageView02.setAnimation(scaleAnimation);
            this.imageView03.setAnimation(scaleAnimation);
            scaleAnimation.start();
        }
    }

    private void closeBackGroundLoveAnimation() {
        Animation fadeOutAnimation = AnimationUtils.loadAnimation(this, R.anim.anim_touch_fade_out);
        fadeOutAnimation.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationStart(Animation animation) {
            }

            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationEnd(Animation animation) {
                KanojoRoomActivity.this.layoutBGActionLoveGauge.setVisibility(View.GONE);
            }
        });
        this.layoutBGActionLoveGauge.setAnimation(fadeOutAnimation);
        fadeOutAnimation.start();
    }
}
