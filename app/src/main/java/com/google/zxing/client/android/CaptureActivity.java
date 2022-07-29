package com.google.zxing.client.android;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.text.ClipboardManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.DecodeHintType;
import com.google.zxing.Result;
import com.google.zxing.ResultMetadataType;
import com.google.zxing.ResultPoint;
import com.google.zxing.client.android.camera.CameraManager;
import com.google.zxing.client.android.history.HistoryActivity;
import com.google.zxing.client.android.history.HistoryManager;
import com.google.zxing.client.android.result.ResultButtonListener;
import com.google.zxing.client.android.result.ResultHandler;
import com.google.zxing.client.android.result.ResultHandlerFactory;
import com.google.zxing.client.android.result.supplement.SupplementalInfoRetriever;
import com.google.zxing.client.android.share.ShareActivity;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;
import com.goujer.barcodekanojo.R;

public final class CaptureActivity extends Activity implements SurfaceHolder.Callback {
    private static /* synthetic */ int[] $SWITCH_TABLE$com$google$zxing$client$android$IntentSource = null;
    private static final long BULK_MODE_SCAN_DELAY_MS = 1000;
    private static final long DEFAULT_INTENT_RESULT_DURATION_MS = 1500;
    private static final Set<ResultMetadataType> DISPLAYABLE_METADATA_TYPES = EnumSet.of(ResultMetadataType.ISSUE_NUMBER, ResultMetadataType.SUGGESTED_PRICE, ResultMetadataType.ERROR_CORRECTION_LEVEL, ResultMetadataType.POSSIBLE_COUNTRY);
    public static final int HISTORY_REQUEST_CODE = 47820;
    private static final String PACKAGE_NAME = "com.google.zxing.client.android";
    private static final String PRODUCT_SEARCH_URL_PREFIX = "http://www.google";
    private static final String PRODUCT_SEARCH_URL_SUFFIX = "/m/products/scan";
    private static final String TAG = CaptureActivity.class.getSimpleName();
    private static final String[] ZXING_URLS = {"http://zxing.appspot.com/scan", "zxing://scan/"};
    private AmbientLightManager ambientLightManager;
    private BeepManager beepManager;
    private CameraManager cameraManager;
    private String characterSet;
    private boolean copyToClipboard;
    private Collection<BarcodeFormat> decodeFormats;
    private Map<DecodeHintType, ?> decodeHints;
    private CaptureActivityHandler handler;
    private boolean hasSurface;
    private HistoryManager historyManager;
    private InactivityTimer inactivityTimer;
    private Result lastResult;
    private View resultView;
    private Result savedResultToShow;
    private ScanFromWebPageManager scanFromWebPageManager;
    private IntentSource source;
    private String sourceUrl;
    private TextView statusView;
    private ViewfinderView viewfinderView;

    static /* synthetic */ int[] $SWITCH_TABLE$com$google$zxing$client$android$IntentSource() {
        int[] iArr = $SWITCH_TABLE$com$google$zxing$client$android$IntentSource;
        if (iArr == null) {
            iArr = new int[IntentSource.values().length];
            try {
                iArr[IntentSource.NATIVE_APP_INTENT.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[IntentSource.NONE.ordinal()] = 4;
            } catch (NoSuchFieldError e2) {
            }
            try {
                iArr[IntentSource.PRODUCT_SEARCH_LINK.ordinal()] = 2;
            } catch (NoSuchFieldError e3) {
            }
            try {
                iArr[IntentSource.ZXING_LINK.ordinal()] = 3;
            } catch (NoSuchFieldError e4) {
            }
            $SWITCH_TABLE$com$google$zxing$client$android$IntentSource = iArr;
        }
        return iArr;
    }

    ViewfinderView getViewfinderView() {
        return this.viewfinderView;
    }

    public Handler getHandler() {
        return this.handler;
    }

    CameraManager getCameraManager() {
        return this.cameraManager;
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        getWindow().addFlags(128);
        setContentView(R.layout.capture);
        this.hasSurface = false;
        this.historyManager = new HistoryManager(this);
        this.historyManager.trimHistory();
        this.inactivityTimer = new InactivityTimer(this);
        this.beepManager = new BeepManager(this);
        this.ambientLightManager = new AmbientLightManager(this);
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
    }

    @Override
    protected void onResume() {
        boolean z;
        super.onResume();
        this.cameraManager = new CameraManager(getApplication());
        this.viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
        this.viewfinderView.setCameraManager(this.cameraManager);
        this.resultView = findViewById(R.id.result_view);
        this.statusView = (TextView) findViewById(R.id.status_view);
        this.handler = null;
        this.lastResult = null;
        resetStatusView();
        SurfaceHolder surfaceHolder = ((SurfaceView) findViewById(R.id.preview_view)).getHolder();
        if (this.hasSurface) {
            initCamera(surfaceHolder);
        } else {
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(3);
        }
        this.beepManager.updatePrefs();
        this.ambientLightManager.start(this.cameraManager);
        this.inactivityTimer.onResume();
        Intent intent = getIntent();
        if (!PreferenceManager.getDefaultSharedPreferences(this).getBoolean(PreferencesActivity.KEY_COPY_TO_CLIPBOARD, true) || (intent != null && !intent.getBooleanExtra(Intents.Scan.SAVE_HISTORY, true))) {
            z = false;
        } else {
            z = true;
        }
        this.copyToClipboard = z;
        this.source = IntentSource.NONE;
        this.decodeFormats = null;
        this.characterSet = null;
        if (intent != null) {
            String action = intent.getAction();
            String dataString = intent.getDataString();
            if (Intents.Scan.ACTION.equals(action)) {
                this.source = IntentSource.NATIVE_APP_INTENT;
                this.decodeFormats = DecodeFormatManager.parseDecodeFormats(intent);
                this.decodeHints = DecodeHintManager.parseDecodeHints(intent);
                if (intent.hasExtra(Intents.Scan.WIDTH) && intent.hasExtra(Intents.Scan.HEIGHT)) {
                    int width = intent.getIntExtra(Intents.Scan.WIDTH, 0);
                    int height = intent.getIntExtra(Intents.Scan.HEIGHT, 0);
                    if (width > 0 && height > 0) {
                        this.cameraManager.setManualFramingRect(width, height);
                    }
                }
                String customPromptMessage = intent.getStringExtra(Intents.Scan.PROMPT_MESSAGE);
                if (customPromptMessage != null) {
                    this.statusView.setText(customPromptMessage);
                }
            } else if (dataString != null && dataString.contains(PRODUCT_SEARCH_URL_PREFIX) && dataString.contains(PRODUCT_SEARCH_URL_SUFFIX)) {
                this.source = IntentSource.PRODUCT_SEARCH_LINK;
                this.sourceUrl = dataString;
                this.decodeFormats = DecodeFormatManager.PRODUCT_FORMATS;
            } else if (isZXingURL(dataString)) {
                this.source = IntentSource.ZXING_LINK;
                this.sourceUrl = dataString;
                Uri inputUri = Uri.parse(dataString);
                this.scanFromWebPageManager = new ScanFromWebPageManager(inputUri);
                this.decodeFormats = DecodeFormatManager.parseDecodeFormats(inputUri);
                this.decodeHints = DecodeHintManager.parseDecodeHints(inputUri);
            }
            this.characterSet = intent.getStringExtra(Intents.Scan.CHARACTER_SET);
        }
    }

    private static boolean isZXingURL(String dataString) {
        if (dataString == null) {
            return false;
        }
        for (String url : ZXING_URLS) {
            if (dataString.startsWith(url)) {
                return true;
            }
        }
        return false;
    }

    protected void onPause() {
        if (this.handler != null) {
            this.handler.quitSynchronously();
            this.handler = null;
        }
        this.inactivityTimer.onPause();
        this.ambientLightManager.stop();
        this.cameraManager.closeDriver();
        if (!this.hasSurface) {
            ((SurfaceView) findViewById(R.id.preview_view)).getHolder().removeCallback(this);
        }
        super.onPause();
    }

    protected void onDestroy() {
        this.inactivityTimer.shutdown();
        super.onDestroy();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case 4:
                if (this.source == IntentSource.NATIVE_APP_INTENT) {
                    setResult(0);
                    finish();
                    return true;
                } else if ((this.source == IntentSource.NONE || this.source == IntentSource.ZXING_LINK) && this.lastResult != null) {
                    restartPreviewAfterDelay(0);
                    return true;
                }
            case 24:
                this.cameraManager.setTorch(true);
                return true;
            case 25:
                this.cameraManager.setTorch(false);
                return true;
            case 27:
            case 80:
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.capture, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.addFlags(Intents.FLAG_NEW_DOC);
		int itemId = item.getItemId();
		if (itemId == R.id.menu_share) {
			intent.setClassName(this, ShareActivity.class.getName());
			startActivity(intent);
		} else if (itemId == R.id.menu_history) {
			intent.setClassName(this, HistoryActivity.class.getName());
			startActivityForResult(intent, HISTORY_REQUEST_CODE);
		} else if (itemId == R.id.menu_settings) {
			intent.setClassName(this, PreferencesActivity.class.getName());
			startActivity(intent);
		} else if (itemId == R.id.menu_help) {
			intent.setClassName(this, HelpActivity.class.getName());
			startActivity(intent);
		} else {
			return super.onOptionsItemSelected(item);
		}
        return true;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        int itemNumber;
        if (resultCode == -1 && requestCode == 47820 && (itemNumber = intent.getIntExtra(Intents.History.ITEM_NUMBER, -1)) >= 0) {
            decodeOrStoreSavedBitmap(null, this.historyManager.buildHistoryItem(itemNumber).getResult());
        }
    }

    private void decodeOrStoreSavedBitmap(Bitmap bitmap, Result result) {
        if (this.handler == null) {
            this.savedResultToShow = result;
            return;
        }
        if (result != null) {
            this.savedResultToShow = result;
        }
        if (this.savedResultToShow != null) {
            this.handler.sendMessage(Message.obtain(this.handler, R.id.decode_succeeded, this.savedResultToShow));
        }
        this.savedResultToShow = null;
    }

    public void surfaceCreated(SurfaceHolder holder) {
        if (holder == null) {
            Log.e(TAG, "*** WARNING *** surfaceCreated() gave us a null surface!");
        }
        if (!this.hasSurface) {
            this.hasSurface = true;
            initCamera(holder);
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        this.hasSurface = false;
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    public void handleDecode(Result rawResult, Bitmap barcode, float scaleFactor) {
        boolean fromLiveScan;
        this.inactivityTimer.onActivity();
        this.lastResult = rawResult;
        ResultHandler resultHandler = ResultHandlerFactory.makeResultHandler(this, rawResult);
        if (barcode != null) {
            fromLiveScan = true;
        } else {
            fromLiveScan = false;
        }
        if (fromLiveScan) {
            this.historyManager.addHistoryItem(rawResult, resultHandler);
            this.beepManager.playBeepSoundAndVibrate();
            drawResultPoints(barcode, scaleFactor, rawResult);
        }
        switch ($SWITCH_TABLE$com$google$zxing$client$android$IntentSource()[this.source.ordinal()]) {
            case 1:
            case 2:
                handleDecodeExternally(rawResult, resultHandler, barcode);
                return;
            case 3:
                if (this.scanFromWebPageManager == null || !this.scanFromWebPageManager.isScanFromWebPage()) {
                    handleDecodeInternally(rawResult, resultHandler, barcode);
                    return;
                } else {
                    handleDecodeExternally(rawResult, resultHandler, barcode);
                    return;
                }
            case 4:
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
                if (!fromLiveScan || !prefs.getBoolean(PreferencesActivity.KEY_BULK_MODE, false)) {
                    handleDecodeInternally(rawResult, resultHandler, barcode);
                    return;
                }
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.msg_bulk_mode_scanned) + " (" + rawResult.getText() + ')', Toast.LENGTH_SHORT).show();
                restartPreviewAfterDelay(BULK_MODE_SCAN_DELAY_MS);
                return;
            default:
		}
    }

	/**
	 * Superimpose a line for 1D or dots for 2D to highlight the key features of the barcode.
	 *
	 * @param barcode   A bitmap of the captured image.
	 * @param scaleFactor amount by which thumbnail was scaled
	 * @param rawResult The decoded results which contains the points to draw.
	 */
	private void drawResultPoints(Bitmap barcode, float scaleFactor, Result rawResult) {
		ResultPoint[] points = rawResult.getResultPoints();
		if (points != null && points.length > 0) {
			Canvas canvas = new Canvas(barcode);
			Paint paint = new Paint();
			paint.setColor(getResources().getColor(R.color.result_points));
			if (points.length == 2) {
				paint.setStrokeWidth(4.0f);
				drawLine(canvas, paint, points[0], points[1], scaleFactor);
			} else if (points.length == 4 &&
					(rawResult.getBarcodeFormat() == BarcodeFormat.UPC_A ||
							rawResult.getBarcodeFormat() == BarcodeFormat.EAN_13)) {
				// Hacky special case -- draw two lines, for the barcode and metadata
				drawLine(canvas, paint, points[0], points[1], scaleFactor);
				drawLine(canvas, paint, points[2], points[3], scaleFactor);
			} else {
				paint.setStrokeWidth(10.0f);
				for (ResultPoint point : points) {
					if (point != null) {
						canvas.drawPoint(scaleFactor * point.getX(), scaleFactor * point.getY(), paint);
					}
				}
			}
		}
	}

    private static void drawLine(Canvas canvas, Paint paint, ResultPoint a, ResultPoint b, float scaleFactor) {
        if (a != null && b != null) {
            canvas.drawLine(scaleFactor * a.getX(), scaleFactor * a.getY(), scaleFactor * b.getX(), scaleFactor * b.getY(), paint);
        }
    }

    private void handleDecodeInternally(Result rawResult, ResultHandler resultHandler, Bitmap barcode) {
        this.statusView.setVisibility(View.GONE);
        this.viewfinderView.setVisibility(View.GONE);
        this.resultView.setVisibility(View.VISIBLE);
        ImageView barcodeImageView = findViewById(R.id.barcode_image_view);
        if (barcode == null) {
            barcodeImageView.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.launcher_icon));
        } else {
            barcodeImageView.setImageBitmap(barcode);
        }
        ((TextView) findViewById(R.id.format_text_view)).setText(rawResult.getBarcodeFormat().toString());
        ((TextView) findViewById(R.id.type_text_view)).setText(resultHandler.getType().toString());
        ((TextView) findViewById(R.id.time_text_view)).setText(DateFormat.getDateTimeInstance(3, 3).format(new Date(rawResult.getTimestamp())));
        TextView metaTextView = findViewById(R.id.meta_text_view);
        View metaTextViewLabel = findViewById(R.id.meta_text_view_label);
        metaTextView.setVisibility(View.GONE);
        metaTextViewLabel.setVisibility(View.GONE);
        Map<ResultMetadataType, Object> metadata = rawResult.getResultMetadata();
        if (metadata != null) {
            StringBuilder sb = new StringBuilder(20);
            for (Map.Entry<ResultMetadataType, Object> entry : metadata.entrySet()) {
                if (DISPLAYABLE_METADATA_TYPES.contains(entry.getKey())) {
                    sb.append(entry.getValue()).append(10);
                }
            }
            if (sb.length() > 0) {
                sb.setLength(sb.length() - 1);
                metaTextView.setText(sb);
                metaTextView.setVisibility(View.VISIBLE);
                metaTextViewLabel.setVisibility(View.VISIBLE);
            }
        }
        TextView contentsTextView = findViewById(R.id.contents_text_view);
        CharSequence displayContents = resultHandler.getDisplayContents();
        contentsTextView.setText(displayContents);
        contentsTextView.setTextSize(2, (float) Math.max(22, 32 - (displayContents.length() / 4)));
        TextView supplementTextView = findViewById(R.id.contents_supplement_text_view);
        supplementTextView.setText("");
        supplementTextView.setOnClickListener(null);
        if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean(PreferencesActivity.KEY_SUPPLEMENTAL, true)) {
            SupplementalInfoRetriever.maybeInvokeRetrieval(supplementTextView, resultHandler.getResult(), this.historyManager, this);
        }
        int buttonCount = resultHandler.getButtonCount();
        ViewGroup buttonView = findViewById(R.id.result_button_view);
        buttonView.requestFocus();
        for (int x = 0; x < 4; x++) {
            TextView button = (TextView) buttonView.getChildAt(x);
            if (x < buttonCount) {
                button.setVisibility(View.VISIBLE);
                button.setText(resultHandler.getButtonText(x));
                button.setOnClickListener(new ResultButtonListener(resultHandler, x));
            } else {
                button.setVisibility(View.GONE);
            }
        }
        if (this.copyToClipboard && !resultHandler.areContentsSecure()) {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
            if (displayContents != null) {
                try {
                    clipboard.setText(displayContents);
                } catch (NullPointerException npe) {
                    Log.w(TAG, "Clipboard bug", npe);
                }
            }
        }
    }

    private void handleDecodeExternally(Result rawResult, ResultHandler resultHandler, Bitmap barcode) {
        long resultDurationMS;
        if (barcode != null) {
            this.viewfinderView.drawResultBitmap(barcode);
        }
        if (getIntent() == null) {
            resultDurationMS = DEFAULT_INTENT_RESULT_DURATION_MS;
        } else {
            resultDurationMS = getIntent().getLongExtra(Intents.Scan.RESULT_DISPLAY_DURATION_MS, DEFAULT_INTENT_RESULT_DURATION_MS);
        }
        if (resultDurationMS > 0) {
            String rawResultString = String.valueOf(rawResult);
            if (rawResultString.length() > 32) {
                rawResultString = rawResultString.substring(0, 32) + " ...";
            }
            this.statusView.setText(getString(resultHandler.getDisplayTitle()) + " : " + rawResultString);
        }
        if (this.copyToClipboard && !resultHandler.areContentsSecure()) {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
            CharSequence text = resultHandler.getDisplayContents();
            if (text != null) {
                try {
                    clipboard.setText(text);
                } catch (NullPointerException npe) {
                    Log.w(TAG, "Clipboard bug", npe);
                }
            }
        }
        if (this.source == IntentSource.NATIVE_APP_INTENT) {
			// Hand back whatever action they requested - this can be changed to Intents.Scan.ACTION when
			// the deprecated intent is retired.
			Intent intent = new Intent(getIntent().getAction());
			intent.addFlags(Intents.FLAG_NEW_DOC);
            intent.putExtra(Intents.Scan.RESULT, rawResult.toString());
            intent.putExtra(Intents.Scan.RESULT_FORMAT, rawResult.getBarcodeFormat().toString());
            byte[] rawBytes = rawResult.getRawBytes();
            if (rawBytes != null && rawBytes.length > 0) {
                intent.putExtra(Intents.Scan.RESULT_BYTES, rawBytes);
            }
            Map<ResultMetadataType, ?> resultMetadata = rawResult.getResultMetadata();
            if (resultMetadata != null) {
                if (resultMetadata.containsKey(ResultMetadataType.UPC_EAN_EXTENSION)) {
                    intent.putExtra(Intents.Scan.RESULT_UPC_EAN_EXTENSION, resultMetadata.get(ResultMetadataType.UPC_EAN_EXTENSION).toString());
                }
                Integer orientation = (Integer) resultMetadata.get(ResultMetadataType.ORIENTATION);
                if (orientation != null) {
                    intent.putExtra(Intents.Scan.RESULT_ORIENTATION, orientation.intValue());
                }
                String ecLevel = (String) resultMetadata.get(ResultMetadataType.ERROR_CORRECTION_LEVEL);
                if (ecLevel != null) {
                    intent.putExtra(Intents.Scan.RESULT_ERROR_CORRECTION_LEVEL, ecLevel);
                }
                Iterable<byte[]> byteSegments = (Iterable<byte[]>) resultMetadata.get(ResultMetadataType.BYTE_SEGMENTS);
                if (byteSegments != null) {
                    int i = 0;
                    for (byte[] byteSegment : byteSegments) {
                        intent.putExtra(Intents.Scan.RESULT_BYTE_SEGMENTS_PREFIX + i, byteSegment);
                        i++;
                    }
                }
            }
            sendReplyMessage(R.id.return_scan_result, intent, resultDurationMS);
        } else if (this.source == IntentSource.PRODUCT_SEARCH_LINK) {
            sendReplyMessage(R.id.launch_product_query, this.sourceUrl.substring(0, this.sourceUrl.lastIndexOf("/scan")) + "?q=" + resultHandler.getDisplayContents() + "&source=zxing", resultDurationMS);
        } else if (this.source == IntentSource.ZXING_LINK && this.scanFromWebPageManager != null && this.scanFromWebPageManager.isScanFromWebPage()) {
            sendReplyMessage(R.id.launch_product_query, this.scanFromWebPageManager.buildReplyURL(rawResult, resultHandler), resultDurationMS);
        }
    }

    private void sendReplyMessage(int id, Object arg, long delayMS) {
        Message message = Message.obtain(this.handler, id, arg);
        if (delayMS > 0) {
            this.handler.sendMessageDelayed(message, delayMS);
        } else {
            this.handler.sendMessage(message);
        }
    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        if (surfaceHolder == null) {
            throw new IllegalStateException("No SurfaceHolder provided");
        } else if (this.cameraManager.isOpen()) {
            Log.w(TAG, "initCamera() while already open -- late SurfaceView callback?");
        } else {
            try {
                this.cameraManager.openDriver(surfaceHolder);
                if (this.handler == null) {
                    this.handler = new CaptureActivityHandler(this, this.decodeFormats, this.decodeHints, this.characterSet, this.cameraManager);
                }
                decodeOrStoreSavedBitmap((Bitmap) null, (Result) null);
            } catch (IOException ioe) {
                Log.w(TAG, ioe);
                displayFrameworkBugMessageAndExit();
            } catch (RuntimeException e) {
                Log.w(TAG, "Unexpected error initializing camera", e);
                displayFrameworkBugMessageAndExit();
            }
        }
    }

    private void displayFrameworkBugMessageAndExit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.app_name));
        builder.setMessage(getString(R.string.msg_camera_framework_bug));
        builder.setPositiveButton(R.string.button_ok, new FinishListener(this));
        builder.setOnCancelListener(new FinishListener(this));
        builder.show();
    }

    public void restartPreviewAfterDelay(long delayMS) {
        if (this.handler != null) {
            this.handler.sendEmptyMessageDelayed(R.id.restart_preview, delayMS);
        }
        resetStatusView();
    }

    private void resetStatusView() {
        this.resultView.setVisibility(View.GONE);
        this.statusView.setText(R.string.msg_default_status);
        this.statusView.setVisibility(View.VISIBLE);
        this.viewfinderView.setVisibility(View.VISIBLE);
        this.lastResult = null;
    }

    public void drawViewfinder() {
        this.viewfinderView.drawViewfinder();
    }
}
