package com.google.zxing.client.android;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.PlanarYUVLuminanceSource;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import java.io.ByteArrayOutputStream;
import java.util.Map;
import com.goujer.barcodekanojo.R;

final class DecodeHandler extends Handler {
    private static final String TAG = DecodeHandler.class.getSimpleName();
    private final CaptureActivity activity;
    private final MultiFormatReader multiFormatReader = new MultiFormatReader();
    private boolean running = true;

    DecodeHandler(CaptureActivity activity2, Map<DecodeHintType, Object> hints) {
        this.multiFormatReader.setHints(hints);
        this.activity = activity2;
    }

    public void handleMessage(Message message) {
        if (this.running) {
			if (message.what == R.id.decode) {
				decode((byte[]) message.obj, message.arg1, message.arg2);
				return;
			} else if (message.what == R.id.quit) {
				this.running = false;
				Looper.myLooper().quit();
				return;
			}
			return;
		}
    }

    private void decode(byte[] data, int width, int height) {
        long start = System.currentTimeMillis();
        Result rawResult = null;
        PlanarYUVLuminanceSource source = this.activity.getCameraManager().buildLuminanceSource(data, width, height);
        if (source != null) {
            try {
                rawResult = this.multiFormatReader.decodeWithState(new BinaryBitmap(new HybridBinarizer(source)));
            } catch (ReaderException e) {
            } finally {
                this.multiFormatReader.reset();
            }
        }
        Handler handler = this.activity.getHandler();
        if (rawResult != null) {
            Log.d(TAG, "Found barcode in " + (System.currentTimeMillis() - start) + " ms");
            if (handler != null) {
                Message message = Message.obtain(handler, R.id.decode_succeeded, rawResult);
                Bundle bundle = new Bundle();
                bundleThumbnail(source, bundle);
                message.setData(bundle);
                message.sendToTarget();
            }
        } else if (handler != null) {
            Message.obtain(handler, R.id.decode_failed).sendToTarget();
        }
    }

    private static void bundleThumbnail(PlanarYUVLuminanceSource source, Bundle bundle) {
        int[] pixels = source.renderThumbnail();
        int width = source.getThumbnailWidth();
        Bitmap bitmap = Bitmap.createBitmap(pixels, 0, width, width, source.getThumbnailHeight(), Bitmap.Config.ARGB_8888);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, out);
        bundle.putByteArray(DecodeThread.BARCODE_BITMAP, out.toByteArray());
        bundle.putFloat(DecodeThread.BARCODE_SCALED_FACTOR, ((float) width) / ((float) source.getWidth()));
    }
}
