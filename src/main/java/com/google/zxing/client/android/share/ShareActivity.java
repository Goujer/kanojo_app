package com.google.zxing.client.android.share;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.ClipboardManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.android.Contents;
import com.google.zxing.client.android.Intents;
import jp.co.cybird.app.android.lib.commons.log.ApplicationLogDB;
import jp.co.cybird.barcodekanojoForGAM.R;

public final class ShareActivity extends Activity {
    private static final int PICK_APP = 2;
    private static final int PICK_BOOKMARK = 0;
    private static final int PICK_CONTACT = 1;
    private static final String TAG = ShareActivity.class.getSimpleName();
    private final View.OnClickListener appListener = new View.OnClickListener() {
        public void onClick(View v) {
            Intent intent = new Intent("android.intent.action.PICK");
            intent.addFlags(524288);
            intent.setClassName(ShareActivity.this, AppPickerActivity.class.getName());
            ShareActivity.this.startActivityForResult(intent, 2);
        }
    };
    private final View.OnClickListener bookmarkListener = new View.OnClickListener() {
        public void onClick(View v) {
            Intent intent = new Intent("android.intent.action.PICK");
            intent.addFlags(524288);
            intent.setClassName(ShareActivity.this, BookmarkPickerActivity.class.getName());
            ShareActivity.this.startActivityForResult(intent, 0);
        }
    };
    private Button clipboardButton;
    private final View.OnClickListener clipboardListener = new View.OnClickListener() {
        public void onClick(View v) {
            ClipboardManager clipboard = (ClipboardManager) ShareActivity.this.getSystemService("clipboard");
            if (clipboard.hasText()) {
                ShareActivity.this.launchSearch(clipboard.getText().toString());
            }
        }
    };
    private final View.OnClickListener contactListener = new View.OnClickListener() {
        public void onClick(View v) {
            Intent intent = new Intent("android.intent.action.PICK", ContactsContract.Contacts.CONTENT_URI);
            intent.addFlags(524288);
            ShareActivity.this.startActivityForResult(intent, 1);
        }
    };
    private final View.OnKeyListener textListener = new View.OnKeyListener() {
        public boolean onKey(View view, int keyCode, KeyEvent event) {
            if (keyCode != 66 || event.getAction() != 0) {
                return false;
            }
            String text = ((TextView) view).getText().toString();
            if (text != null && text.length() > 0) {
                ShareActivity.this.launchSearch(text);
            }
            return true;
        }
    };

    /* access modifiers changed from: private */
    public void launchSearch(String text) {
        Intent intent = new Intent(Intents.Encode.ACTION);
        intent.addFlags(524288);
        intent.putExtra(Intents.Encode.TYPE, Contents.Type.TEXT);
        intent.putExtra(Intents.Encode.DATA, text);
        intent.putExtra(Intents.Encode.FORMAT, BarcodeFormat.QR_CODE.toString());
        startActivity(intent);
    }

    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.share);
        findViewById(R.id.share_contact_button).setOnClickListener(this.contactListener);
        findViewById(R.id.share_bookmark_button).setOnClickListener(this.bookmarkListener);
        findViewById(R.id.share_app_button).setOnClickListener(this.appListener);
        this.clipboardButton = (Button) findViewById(R.id.share_clipboard_button);
        this.clipboardButton.setOnClickListener(this.clipboardListener);
        findViewById(R.id.share_text_view).setOnKeyListener(this.textListener);
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        this.clipboardButton.setEnabled(((ClipboardManager) getSystemService("clipboard")).hasText());
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (resultCode == -1) {
            switch (requestCode) {
                case 0:
                case 2:
                    showTextAsBarcode(intent.getStringExtra("url"));
                    return;
                case 1:
                    showContactAsBarcode(intent.getData());
                    return;
                default:
                    return;
            }
        }
    }

    private void showTextAsBarcode(String text) {
        Log.i(TAG, "Showing text as barcode: " + text);
        if (text != null) {
            Intent intent = new Intent(Intents.Encode.ACTION);
            intent.addFlags(524288);
            intent.putExtra(Intents.Encode.TYPE, Contents.Type.TEXT);
            intent.putExtra(Intents.Encode.DATA, text);
            intent.putExtra(Intents.Encode.FORMAT, BarcodeFormat.QR_CODE.toString());
            startActivity(intent);
        }
    }

    private void showContactAsBarcode(Uri contactUri) {
        String data;
        Cursor phonesCursor;
        Log.i(TAG, "Showing contact URI as barcode: " + contactUri);
        if (contactUri != null) {
            ContentResolver resolver = getContentResolver();
            try {
                Cursor cursor = resolver.query(contactUri, (String[]) null, (String) null, (String[]) null, (String) null);
                if (cursor != null) {
                    try {
                        if (cursor.moveToFirst()) {
                            String id = cursor.getString(cursor.getColumnIndex(ApplicationLogDB.COLUMN__ID));
                            String name = cursor.getString(cursor.getColumnIndex("display_name"));
                            boolean hasPhone = cursor.getInt(cursor.getColumnIndex("has_phone_number")) > 0;
                            cursor.close();
                            Bundle bundle = new Bundle();
                            if (name != null && name.length() > 0) {
                                bundle.putString("name", massageContactData(name));
                            }
                            if (hasPhone && (phonesCursor = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, (String[]) null, "contact_id=" + id, (String[]) null, (String) null)) != null) {
                                int foundPhone = 0;
                                try {
                                    int phonesNumberColumn = phonesCursor.getColumnIndex("data1");
                                    while (phonesCursor.moveToNext() && foundPhone < Contents.PHONE_KEYS.length) {
                                        String number = phonesCursor.getString(phonesNumberColumn);
                                        if (number != null && number.length() > 0) {
                                            bundle.putString(Contents.PHONE_KEYS[foundPhone], massageContactData(number));
                                        }
                                        foundPhone++;
                                    }
                                } finally {
                                    phonesCursor.close();
                                }
                            }
                            Cursor methodsCursor = resolver.query(ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_URI, (String[]) null, "contact_id=" + id, (String[]) null, (String) null);
                            if (methodsCursor != null) {
                                try {
                                    if (methodsCursor.moveToNext() && (data = methodsCursor.getString(methodsCursor.getColumnIndex("data1"))) != null && data.length() > 0) {
                                        bundle.putString("postal", massageContactData(data));
                                    }
                                } finally {
                                    methodsCursor.close();
                                }
                            }
                            Cursor emailCursor = resolver.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, (String[]) null, "contact_id=" + id, (String[]) null, (String) null);
                            if (emailCursor != null) {
                                int foundEmail = 0;
                                try {
                                    int emailColumn = emailCursor.getColumnIndex("data1");
                                    while (emailCursor.moveToNext() && foundEmail < Contents.EMAIL_KEYS.length) {
                                        String email = emailCursor.getString(emailColumn);
                                        if (email != null && email.length() > 0) {
                                            bundle.putString(Contents.EMAIL_KEYS[foundEmail], massageContactData(email));
                                        }
                                        foundEmail++;
                                    }
                                } finally {
                                    emailCursor.close();
                                }
                            }
                            Intent intent = new Intent(Intents.Encode.ACTION);
                            intent.addFlags(524288);
                            intent.putExtra(Intents.Encode.TYPE, Contents.Type.CONTACT);
                            intent.putExtra(Intents.Encode.DATA, bundle);
                            intent.putExtra(Intents.Encode.FORMAT, BarcodeFormat.QR_CODE.toString());
                            Log.i(TAG, "Sending bundle for encoding: " + bundle);
                            startActivity(intent);
                        }
                    } finally {
                        cursor.close();
                    }
                }
            } catch (IllegalArgumentException e) {
            }
        }
    }

    private static String massageContactData(String data) {
        if (data.indexOf(10) >= 0) {
            data = data.replace("\n", " ");
        }
        if (data.indexOf(13) >= 0) {
            return data.replace("\r", " ");
        }
        return data;
    }
}
