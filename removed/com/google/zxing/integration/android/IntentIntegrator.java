package com.google.zxing.integration.android;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.accessibility.AccessibilityEventCompat;
import android.util.Log;
import com.google.zxing.client.android.Contents;
import com.google.zxing.client.android.Intents;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IntentIntegrator {
    public static final Collection<String> ALL_CODE_TYPES = null;
    private static final String BSPLUS_PACKAGE = "com.srowen.bs.android";
    private static final String BS_PACKAGE = "com.google.zxing.client.android";
    public static final Collection<String> DATA_MATRIX_TYPES = Collections.singleton("DATA_MATRIX");
    public static final String DEFAULT_MESSAGE = "This application requires Barcode Scanner. Would you like to install it?";
    public static final String DEFAULT_NO = "No";
    public static final String DEFAULT_TITLE = "Install Barcode Scanner?";
    public static final String DEFAULT_YES = "Yes";
    public static final Collection<String> ONE_D_CODE_TYPES = list("UPC_A", "UPC_E", "EAN_8", "EAN_13", "CODE_39", "CODE_93", "CODE_128", "ITF", "RSS_14", "RSS_EXPANDED");
    public static final Collection<String> PRODUCT_CODE_TYPES = list("UPC_A", "UPC_E", "EAN_8", "EAN_13", "RSS_14");
    public static final Collection<String> QR_CODE_TYPES = Collections.singleton("QR_CODE");
    public static final int REQUEST_CODE = 49374;
    /* access modifiers changed from: private */
    public static final String TAG = IntentIntegrator.class.getSimpleName();
    public static final List<String> TARGET_ALL_KNOWN = list(BS_PACKAGE, BSPLUS_PACKAGE, "com.srowen.bs.android.simple");
    public static final List<String> TARGET_BARCODE_SCANNER_ONLY = Collections.singletonList(BS_PACKAGE);
    /* access modifiers changed from: private */
    public final Activity activity;
    private String buttonNo = DEFAULT_NO;
    private String buttonYes = DEFAULT_YES;
    private String message = DEFAULT_MESSAGE;
    private final Map<String, Object> moreExtras = new HashMap(3);
    /* access modifiers changed from: private */
    public List<String> targetApplications = TARGET_ALL_KNOWN;
    private String title = DEFAULT_TITLE;

    public IntentIntegrator(Activity activity2) {
        this.activity = activity2;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title2) {
        this.title = title2;
    }

    public void setTitleByID(int titleID) {
        this.title = this.activity.getString(titleID);
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message2) {
        this.message = message2;
    }

    public void setMessageByID(int messageID) {
        this.message = this.activity.getString(messageID);
    }

    public String getButtonYes() {
        return this.buttonYes;
    }

    public void setButtonYes(String buttonYes2) {
        this.buttonYes = buttonYes2;
    }

    public void setButtonYesByID(int buttonYesID) {
        this.buttonYes = this.activity.getString(buttonYesID);
    }

    public String getButtonNo() {
        return this.buttonNo;
    }

    public void setButtonNo(String buttonNo2) {
        this.buttonNo = buttonNo2;
    }

    public void setButtonNoByID(int buttonNoID) {
        this.buttonNo = this.activity.getString(buttonNoID);
    }

    public Collection<String> getTargetApplications() {
        return this.targetApplications;
    }

    public final void setTargetApplications(List<String> targetApplications2) {
        if (targetApplications2.isEmpty()) {
            throw new IllegalArgumentException("No target applications");
        }
        this.targetApplications = targetApplications2;
    }

    public void setSingleTargetApplication(String targetApplication) {
        this.targetApplications = Collections.singletonList(targetApplication);
    }

    public Map<String, ?> getMoreExtras() {
        return this.moreExtras;
    }

    public final void addExtra(String key, Object value) {
        this.moreExtras.put(key, value);
    }

    public final AlertDialog initiateScan() {
        return initiateScan(ALL_CODE_TYPES);
    }

    public final AlertDialog initiateScan(Collection<String> desiredBarcodeFormats) {
        Intent intentScan = new Intent(Intents.Scan.ACTION);
        intentScan.addCategory("android.intent.category.DEFAULT");
        if (desiredBarcodeFormats != null) {
            StringBuilder joinedByComma = new StringBuilder();
            for (String format : desiredBarcodeFormats) {
                if (joinedByComma.length() > 0) {
                    joinedByComma.append(',');
                }
                joinedByComma.append(format);
            }
            intentScan.putExtra(Intents.Scan.FORMATS, joinedByComma.toString());
        }
        String targetAppPackage = findTargetAppPackage(intentScan);
        if (targetAppPackage == null) {
            return showDownloadDialog();
        }
        intentScan.setPackage(targetAppPackage);
        intentScan.addFlags(67108864);
        intentScan.addFlags(524288);
        attachMoreExtras(intentScan);
        startActivityForResult(intentScan, REQUEST_CODE);
        return null;
    }

    /* access modifiers changed from: protected */
    public void startActivityForResult(Intent intent, int code) {
        this.activity.startActivityForResult(intent, code);
    }

    private String findTargetAppPackage(Intent intent) {
        List<ResolveInfo> availableApps = this.activity.getPackageManager().queryIntentActivities(intent, AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
        if (availableApps != null) {
            for (ResolveInfo availableApp : availableApps) {
                String packageName = availableApp.activityInfo.packageName;
                if (this.targetApplications.contains(packageName)) {
                    return packageName;
                }
            }
        }
        return null;
    }

    private AlertDialog showDownloadDialog() {
        AlertDialog.Builder downloadDialog = new AlertDialog.Builder(this.activity);
        downloadDialog.setTitle(this.title);
        downloadDialog.setMessage(this.message);
        downloadDialog.setPositiveButton(this.buttonYes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                String packageName = (String) IntentIntegrator.this.targetApplications.get(0);
                try {
                    IntentIntegrator.this.activity.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=" + packageName)));
                } catch (ActivityNotFoundException e) {
                    Log.w(IntentIntegrator.TAG, "Google Play is not installed; cannot install " + packageName);
                }
            }
        });
        downloadDialog.setNegativeButton(this.buttonNo, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        return downloadDialog.show();
    }

    public static IntentResult parseActivityResult(int requestCode, int resultCode, Intent intent) {
        Integer orientation = null;
        if (requestCode != 49374) {
            return null;
        }
        if (resultCode != -1) {
            return new IntentResult();
        }
        String contents = intent.getStringExtra(Intents.Scan.RESULT);
        String formatName = intent.getStringExtra(Intents.Scan.RESULT_FORMAT);
        byte[] rawBytes = intent.getByteArrayExtra(Intents.Scan.RESULT_BYTES);
        int intentOrientation = intent.getIntExtra(Intents.Scan.RESULT_ORIENTATION, Integer.MIN_VALUE);
        if (intentOrientation != Integer.MIN_VALUE) {
            orientation = Integer.valueOf(intentOrientation);
        }
        return new IntentResult(contents, formatName, rawBytes, orientation, intent.getStringExtra(Intents.Scan.RESULT_ERROR_CORRECTION_LEVEL));
    }

    public final AlertDialog shareText(CharSequence text) {
        return shareText(text, Contents.Type.TEXT);
    }

    public final AlertDialog shareText(CharSequence text, CharSequence type) {
        Intent intent = new Intent();
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setAction(Intents.Encode.ACTION);
        intent.putExtra(Intents.Encode.TYPE, type);
        intent.putExtra(Intents.Encode.DATA, text);
        String targetAppPackage = findTargetAppPackage(intent);
        if (targetAppPackage == null) {
            return showDownloadDialog();
        }
        intent.setPackage(targetAppPackage);
        intent.addFlags(67108864);
        intent.addFlags(524288);
        attachMoreExtras(intent);
        this.activity.startActivity(intent);
        return null;
    }

    private static List<String> list(String... values) {
        return Collections.unmodifiableList(Arrays.asList(values));
    }

    private void attachMoreExtras(Intent intent) {
        for (Map.Entry<String, Object> entry : this.moreExtras.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (value instanceof Integer) {
                intent.putExtra(key, (Integer) value);
            } else if (value instanceof Long) {
                intent.putExtra(key, (Long) value);
            } else if (value instanceof Boolean) {
                intent.putExtra(key, (Boolean) value);
            } else if (value instanceof Double) {
                intent.putExtra(key, (Double) value);
            } else if (value instanceof Float) {
                intent.putExtra(key, (Float) value);
            } else if (value instanceof Bundle) {
                intent.putExtra(key, (Bundle) value);
            } else {
                intent.putExtra(key, value.toString());
            }
        }
    }
}
