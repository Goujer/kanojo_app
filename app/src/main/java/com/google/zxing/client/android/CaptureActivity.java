/*
 * Copyright (C) 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.zxing.client.android;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.DecodeHintType;
import com.google.zxing.Result;
import com.google.zxing.ResultMetadataType;
import com.google.zxing.ResultPoint;
import com.google.zxing.client.android.camera.CameraManager;
import com.google.zxing.client.android.history.HistoryItem;
import com.google.zxing.client.android.history.HistoryManager;
import com.google.zxing.client.android.result.ResultButtonListener;
import com.google.zxing.client.android.result.ResultHandler;
import com.google.zxing.client.android.result.ResultHandlerFactory;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;

import android.view.KeyEvent;

import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Map;

import com.goujer.barcodekanojo.R;

public final class CaptureActivity extends Activity implements SurfaceHolder.Callback {

	private static final String TAG = CaptureActivity.class.getSimpleName();

	private static final long DEFAULT_INTENT_RESULT_DURATION_MS = 1500L;
	private static final long BULK_MODE_SCAN_DELAY_MS = 1000L;

	private static final String[] ZXING_URLS = { "http://zxing.appspot.com/scan", "zxing://scan/" };

	private static final int HISTORY_REQUEST_CODE = 0x0000bacc;

	private static final Collection<ResultMetadataType> DISPLAYABLE_METADATA_TYPES =
			EnumSet.of(ResultMetadataType.ISSUE_NUMBER,
					ResultMetadataType.SUGGESTED_PRICE,
					ResultMetadataType.ERROR_CORRECTION_LEVEL,
					ResultMetadataType.POSSIBLE_COUNTRY);

	private CameraManager cameraManager;
	private CaptureActivityHandler handler;
	private Result savedResultToShow;
	private ViewfinderView viewfinderView;
	private TextView statusView;
	private View resultView;
	private Result lastResult;
	private boolean hasSurface;
	private IntentSource source;
	private ScanFromWebPageManager scanFromWebPageManager;
	private Collection<BarcodeFormat> decodeFormats;
	private Map<DecodeHintType,?> decodeHints;
	private String characterSet;
	private HistoryManager historyManager;
	private InactivityTimer inactivityTimer;
	private BeepManager beepManager;
	private AmbientLightManager ambientLightManager;

	ViewfinderView getViewfinderView() {
		return viewfinderView;
	}

	public Handler getHandler() {
		return handler;
	}

	CameraManager getCameraManager() {
		return cameraManager;
	}

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		Window window = getWindow();
		window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.capture);

		hasSurface = false;
		inactivityTimer = new InactivityTimer(this);
		beepManager = new BeepManager(this);
		ambientLightManager = new AmbientLightManager(this);

		PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
	}

	@Override
	protected void onResume() {
		super.onResume();

		// historyManager must be initialized here to update the history preference
		historyManager = new HistoryManager(this);
		historyManager.trimHistory();

		// CameraManager must be initialized here, not in onCreate(). This is necessary because we don't
		// want to open the camera driver and measure the screen size if we're going to show the help on
		// first launch. That led to bugs where the scanning rectangle was the wrong size and partially
		// off screen.
		this.cameraManager = new CameraManager(getApplication());

		viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
		viewfinderView.setCameraManager(cameraManager);

		resultView = findViewById(R.id.result_view);
		statusView = (TextView) findViewById(R.id.status_view);

		this.handler = null;
		this.lastResult = null;

		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);

		resetStatusView();


		this.beepManager.updatePrefs();
		this.ambientLightManager.start(this.cameraManager);

        SurfaceHolder surfaceHolder = ((SurfaceView) findViewById(R.id.preview_view)).getHolder();
        if (this.hasSurface) {
            initCamera(surfaceHolder);
        } else {
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(3);
        }

		this.inactivityTimer.onResume();

		Intent intent = getIntent();

		this.source = IntentSource.NONE;

		this.decodeFormats = null;
		this.characterSet = null;

		if (intent != null) {
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
				statusView.setText(customPromptMessage);
			}
            this.characterSet = intent.getStringExtra(Intents.Scan.CHARACTER_SET);
        }
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

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
			case KeyEvent.KEYCODE_BACK:
				if (source == IntentSource.NATIVE_APP_INTENT) {
					setResult(RESULT_CANCELED);
					finish();
					return true;
				}
				if ((source == IntentSource.NONE || source == IntentSource.ZXING_LINK) && lastResult != null) {
					restartPreviewAfterDelay(0L);
					return true;
				}
				break;
			case KeyEvent.KEYCODE_FOCUS:
			case KeyEvent.KEYCODE_CAMERA:
				// Handle these events so they don't launch the Camera app
				return true;
			// Use volume up/down to turn on light
			case KeyEvent.KEYCODE_VOLUME_DOWN:
				cameraManager.setTorch(false);
				return true;
			case KeyEvent.KEYCODE_VOLUME_UP:
				cameraManager.setTorch(true);
				return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (resultCode == RESULT_OK && requestCode == HISTORY_REQUEST_CODE && historyManager != null) {
			int itemNumber = intent.getIntExtra(Intents.History.ITEM_NUMBER, -1);
			if (itemNumber >= 0) {
				HistoryItem historyItem = historyManager.buildHistoryItem(itemNumber);
				decodeOrStoreSavedBitmap(null, historyItem.getResult());
			}
		}
	}

	private void decodeOrStoreSavedBitmap(Bitmap bitmap, Result result) {
		// Bitmap isn't used yet -- will be used soon
		if (handler == null) {
			savedResultToShow = result;
		} else {
			if (result != null) {
				savedResultToShow = result;
			}
			if (savedResultToShow != null) {
				Message message = Message.obtain(handler, R.id.decode_succeeded, savedResultToShow);
				handler.sendMessage(message);
			}
			savedResultToShow = null;
		}
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


	/**
	 * A valid barcode has been found, so give an indication of success and show the results.
	 *
	 * @param rawResult The contents of the barcode.
	 * @param scaleFactor amount by which thumbnail was scaled
	 * @param barcode   A greyscale bitmap of the camera data which was decoded.
	 */
	public void handleDecode(Result rawResult, Bitmap barcode, float scaleFactor) {
		inactivityTimer.onActivity();
		lastResult = rawResult;
		ResultHandler resultHandler = ResultHandlerFactory.makeResultHandler(this, rawResult);

		boolean fromLiveScan = barcode != null;
		if (fromLiveScan) {
			// Beep/vibrate and we have an image to draw on
			beepManager.playBeepSoundAndVibrate();
			drawResultPoints(barcode, scaleFactor, rawResult);
		}

		switch (source) {
			case NATIVE_APP_INTENT:
			case PRODUCT_SEARCH_LINK:
				handleDecodeExternally(rawResult, resultHandler, barcode);
				break;
			case ZXING_LINK:
				if (scanFromWebPageManager == null || !scanFromWebPageManager.isScanFromWebPage()) {
					handleDecodeInternally(rawResult, resultHandler, barcode);
				} else {
					handleDecodeExternally(rawResult, resultHandler, barcode);
				}
				break;
			case NONE:
				SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
				if (fromLiveScan && prefs.getBoolean(PreferencesActivity.KEY_BULK_MODE, false)) {
					Toast.makeText(getApplicationContext(),
							getResources().getString(R.string.msg_bulk_mode_scanned) + " (" + rawResult.getText() + ')',
							Toast.LENGTH_SHORT).show();
					// Wait a moment or else it will scan the same barcode continuously about 3 times
					restartPreviewAfterDelay(BULK_MODE_SCAN_DELAY_MS);
				} else {
					handleDecodeInternally(rawResult, resultHandler, barcode);
				}
				break;
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
            barcodeImageView.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.icon_72));
        } else {
            barcodeImageView.setImageBitmap(barcode);
        }
        ((TextView) findViewById(R.id.format_text_view)).setText(rawResult.getBarcodeFormat().toString());
        ((TextView) findViewById(R.id.type_text_view)).setText(resultHandler.getType().toString());

		DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);
		TextView timeTextView = (TextView) findViewById(R.id.time_text_view);
		timeTextView.setText(formatter.format(rawResult.getTimestamp()));

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
    }

	// Briefly show the contents of the barcode, then handle the result outside Barcode Scanner.
	private void handleDecodeExternally(Result rawResult, ResultHandler resultHandler, Bitmap barcode) {

		if (barcode != null) {
			viewfinderView.drawResultBitmap(barcode);
		}

		long resultDurationMS;
		if (getIntent() == null) {
			resultDurationMS = DEFAULT_INTENT_RESULT_DURATION_MS;
		} else {
			resultDurationMS = getIntent().getLongExtra(Intents.Scan.RESULT_DISPLAY_DURATION_MS,
		                                                DEFAULT_INTENT_RESULT_DURATION_MS);
		}

		if (resultDurationMS > 0) {
			String rawResultString = String.valueOf(rawResult);
			if (rawResultString.length() > 32) {
				rawResultString = rawResultString.substring(0, 32) + " ...";
			}
			statusView.setText(getString(resultHandler.getDisplayTitle()) + " : " + rawResultString);
		}

		switch (source) {
			case NATIVE_APP_INTENT:
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
				break;

			case ZXING_LINK:
				if (scanFromWebPageManager != null && scanFromWebPageManager.isScanFromWebPage()) {
					String linkReplyURL = scanFromWebPageManager.buildReplyURL(rawResult, resultHandler);
					scanFromWebPageManager = null;
					sendReplyMessage(R.id.launch_product_query, linkReplyURL, resultDurationMS);
				}
				break;
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
		if (handler != null) {
			handler.sendEmptyMessageDelayed(R.id.restart_preview, delayMS);
		}
		resetStatusView();
	}

	private void resetStatusView() {
		resultView.setVisibility(View.GONE);
		statusView.setText(R.string.msg_default_status);
		statusView.setVisibility(View.VISIBLE);
		viewfinderView.setVisibility(View.VISIBLE);
		lastResult = null;
	}

	public void drawViewfinder() {
		viewfinderView.drawViewfinder();
	}
}
