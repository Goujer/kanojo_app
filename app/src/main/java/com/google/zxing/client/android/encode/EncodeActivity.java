package com.google.zxing.client.android.encode;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.zxing.WriterException;
import com.google.zxing.client.android.Contents;
import com.google.zxing.client.android.FinishListener;
import com.google.zxing.client.android.Intents;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.regex.Pattern;
import com.goujer.barcodekanojo.R;

public final class EncodeActivity extends Activity {
    private static final int MAX_BARCODE_FILENAME_LENGTH = 24;
    private static final Pattern NOT_ALPHANUMERIC = Pattern.compile("[^A-Za-z0-9]");
    private static final String TAG = EncodeActivity.class.getSimpleName();
    private static final String USE_VCARD_KEY = "USE_VCARD";
    private QRCodeEncoder qrCodeEncoder;

    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        Intent intent = getIntent();
        if (intent == null) {
            finish();
            return;
        }
        String action = intent.getAction();
        if (Intents.Encode.ACTION.equals(action) || "android.intent.action.SEND".equals(action)) {
            setContentView(R.layout.encode);
        } else {
            finish();
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.encode, menu);
        int encodeNameResource = this.qrCodeEncoder != null && this.qrCodeEncoder.isUseVCard() ? R.string.menu_encode_mecard : R.string.menu_encode_vcard;
        MenuItem encodeItem = menu.findItem(R.id.menu_encode);
        encodeItem.setTitle(encodeNameResource);
        Intent intent = getIntent();
        if (intent != null) {
            encodeItem.setVisible(Contents.Type.CONTACT.equals(intent.getStringExtra(Intents.Encode.TYPE)));
        }
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        boolean z = false;
		int itemId = item.getItemId();
		if (itemId == R.id.menu_share) {
			share();
			return true;
		} else if (itemId == R.id.menu_encode) {
			Intent intent = getIntent();
			if (intent == null) {
				return false;
			}
			if (!this.qrCodeEncoder.isUseVCard()) {
				z = true;
			}
			intent.putExtra(USE_VCARD_KEY, z);
			startActivity(intent);
			finish();
			return true;
		}
		return false;
	}

    private void share() {
        QRCodeEncoder encoder = qrCodeEncoder;
        if (encoder == null) { // Odd
            Log.w(TAG, "No existing barcode to send?");
            return;
        }

        String contents = encoder.getContents();
        if (contents == null) {
            Log.w(TAG, "No existing barcode to send?");
            return;
        }

        Bitmap bitmap;
        try {
            bitmap = encoder.encodeAsBitmap();
        } catch (WriterException we) {
            Log.w(TAG, we);
            return;
        }
        if (bitmap == null) {
            return;
        }

        File bsRoot = new File(Environment.getExternalStorageDirectory(), "BarcodeScanner");
        File barcodesRoot = new File(bsRoot, "Barcodes");
        if (!barcodesRoot.exists() && !barcodesRoot.mkdirs()) {
            Log.w(TAG, "Couldn't make dir " + barcodesRoot);
            showErrorMessage(R.string.msg_unmount_usb);
            return;
        }
        File barcodeFile = new File(barcodesRoot, makeBarcodeFileName(contents) + ".png");
        if (!barcodeFile.delete()) {
            Log.w(TAG, "Could not delete " + barcodeFile);
            // continue anyway
        }
        try {
            FileOutputStream fos = new FileOutputStream(barcodeFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, fos);
        } catch (IOException ioe) {
            Log.w(TAG, "Couldn't access file " + barcodeFile + " due to " + ioe);
            showErrorMessage(R.string.msg_unmount_usb);
            return;
        }

        Intent intent = new Intent(Intent.ACTION_SEND, Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name) + " - " + encoder.getTitle());
        intent.putExtra(Intent.EXTRA_TEXT, contents);
        intent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + barcodeFile.getAbsolutePath()));
        intent.setType("image/png");
        intent.addFlags(Intents.FLAG_NEW_DOC);
        startActivity(Intent.createChooser(intent, null));
    }

    private static CharSequence makeBarcodeFileName(CharSequence contents) {
        String fileName = NOT_ALPHANUMERIC.matcher(contents).replaceAll("_");
        if (fileName.length() > 24) {
            return fileName.substring(0, 24);
        }
        return fileName;
    }

    @Override
    protected void onResume() {
        int smallerDimension;
        super.onResume();
        Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int width = display.getWidth();
        int height = display.getHeight();
        if (width < height) {
            smallerDimension = width;
        } else {
            smallerDimension = height;
        }
        int smallerDimension2 = (smallerDimension * 7) / 8;
        Intent intent = getIntent();
        if (intent != null) {
            try {
                this.qrCodeEncoder = new QRCodeEncoder(this, intent, smallerDimension2, intent.getBooleanExtra(USE_VCARD_KEY, false));
                Bitmap bitmap = this.qrCodeEncoder.encodeAsBitmap();
                if (bitmap == null) {
                    Log.w(TAG, "Could not encode barcode");
                    showErrorMessage(R.string.msg_encode_contents_failed);
                    this.qrCodeEncoder = null;
                    return;
                }
                ((ImageView) findViewById(R.id.image_view)).setImageBitmap(bitmap);
                TextView contents = (TextView) findViewById(R.id.contents_text_view);
                if (intent.getBooleanExtra(Intents.Encode.SHOW_CONTENTS, true)) {
                    contents.setText(this.qrCodeEncoder.getDisplayContents());
                    setTitle(this.qrCodeEncoder.getTitle());
                    return;
                }
                contents.setText("");
                setTitle("");
            } catch (WriterException e) {
                Log.w(TAG, "Could not encode barcode", e);
                showErrorMessage(R.string.msg_encode_contents_failed);
                this.qrCodeEncoder = null;
            }
        }
    }

    private void showErrorMessage(int message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message);
        builder.setPositiveButton(R.string.button_ok, new FinishListener(this));
        builder.setOnCancelListener(new FinishListener(this));
        builder.show();
    }
}
