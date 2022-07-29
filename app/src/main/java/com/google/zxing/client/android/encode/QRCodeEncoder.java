package com.google.zxing.client.android.encode;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.Result;
import com.google.zxing.ResultPoint;
import com.google.zxing.WriterException;
import com.google.zxing.client.android.Contents;
import com.google.zxing.client.android.Intents;
import com.google.zxing.client.result.AddressBookParsedResult;
import com.google.zxing.client.result.ParsedResult;
import com.google.zxing.client.result.ResultParser;
import com.google.zxing.common.BitMatrix;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import com.goujer.barcodekanojo.R;

final class QRCodeEncoder {
    private static final int BLACK = -16777216;
    private static final String TAG = QRCodeEncoder.class.getSimpleName();
    private static final int WHITE = -1;
    private final Activity activity;
    private String contents;
    private final int dimension;
    private String displayContents;
    private BarcodeFormat format;
    private String title;
    private final boolean useVCard;

    QRCodeEncoder(Activity activity2, Intent intent, int dimension2, boolean useVCard2) throws WriterException {
        this.activity = activity2;
        this.dimension = dimension2;
        this.useVCard = useVCard2;
        String action = intent.getAction();
        if (action.equals(Intents.Encode.ACTION)) {
            encodeContentsFromZXingIntent(intent);
        } else if (action.equals("android.intent.action.SEND")) {
            encodeContentsFromShareIntent(intent);
        }
    }

    String getContents() {
        return this.contents;
    }

    String getDisplayContents() {
        return this.displayContents;
    }

    String getTitle() {
        return this.title;
    }

    boolean isUseVCard() {
        return this.useVCard;
    }

    private boolean encodeContentsFromZXingIntent(Intent intent) {
        String formatString = intent.getStringExtra(Intents.Encode.FORMAT);
        this.format = null;
        if (formatString != null) {
            try {
                this.format = BarcodeFormat.valueOf(formatString);
            } catch (IllegalArgumentException e) {
            }
        }
        if (this.format == null || this.format == BarcodeFormat.QR_CODE) {
            String type = intent.getStringExtra(Intents.Encode.TYPE);
            if (type == null || type.length() == 0) {
                return false;
            }
            this.format = BarcodeFormat.QR_CODE;
            encodeQRCodeContents(intent, type);
        } else {
            String data = intent.getStringExtra(Intents.Encode.DATA);
            if (data != null && data.length() > 0) {
                this.contents = data;
                this.displayContents = data;
                this.title = this.activity.getString(R.string.contents_text);
            }
        }
        if (this.contents == null || this.contents.length() <= 0) {
            return false;
        }
        return true;
    }

    private void encodeContentsFromShareIntent(Intent intent) throws WriterException {
        if (intent.hasExtra("android.intent.extra.STREAM")) {
            encodeFromStreamExtra(intent);
        } else {
            encodeFromTextExtras(intent);
        }
    }

    private void encodeFromTextExtras(Intent intent) throws WriterException {
        String theContents = ContactEncoder.trim(intent.getStringExtra("android.intent.extra.TEXT"));
        if (theContents == null && (theContents = ContactEncoder.trim(intent.getStringExtra("android.intent.extra.HTML_TEXT"))) == null && (theContents = ContactEncoder.trim(intent.getStringExtra("android.intent.extra.SUBJECT"))) == null) {
            String[] emails = intent.getStringArrayExtra("android.intent.extra.EMAIL");
            if (emails != null) {
                theContents = ContactEncoder.trim(emails[0]);
            } else {
                theContents = "?";
            }
        }
        if (theContents == null || theContents.length() == 0) {
            throw new WriterException("Empty EXTRA_TEXT");
        }
        this.contents = theContents;
        this.format = BarcodeFormat.QR_CODE;
        if (intent.hasExtra("android.intent.extra.SUBJECT")) {
            this.displayContents = intent.getStringExtra("android.intent.extra.SUBJECT");
        } else if (intent.hasExtra("android.intent.extra.TITLE")) {
            this.displayContents = intent.getStringExtra("android.intent.extra.TITLE");
        } else {
            this.displayContents = this.contents;
        }
        this.title = this.activity.getString(R.string.contents_text);
    }

    private void encodeFromStreamExtra(Intent intent) throws WriterException {
        this.format = BarcodeFormat.QR_CODE;
        Bundle bundle = intent.getExtras();
        if (bundle == null) {
            throw new WriterException("No extras");
        }
        Uri uri = (Uri) bundle.getParcelable("android.intent.extra.STREAM");
        if (uri == null) {
            throw new WriterException("No EXTRA_STREAM");
        }
        try {
            InputStream stream = this.activity.getContentResolver().openInputStream(uri);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[2048];
            while (true) {
                int bytesRead = stream.read(buffer);
                if (bytesRead <= 0) {
                    break;
                }
                baos.write(buffer, 0, bytesRead);
            }
            byte[] vcard = baos.toByteArray();
            String vcardString = new String(vcard, 0, vcard.length, "UTF-8");
            Log.d(TAG, "Encoding share intent content:");
            Log.d(TAG, vcardString);
            ParsedResult parsedResult = ResultParser.parseResult(new Result(vcardString, vcard, (ResultPoint[]) null, BarcodeFormat.QR_CODE));
            if (!(parsedResult instanceof AddressBookParsedResult)) {
                throw new WriterException("Result was not an address");
            }
            encodeQRCodeContents((AddressBookParsedResult) parsedResult);
            if (this.contents == null || this.contents.length() == 0) {
                throw new WriterException("No content to encode");
            }
        } catch (IOException ioe) {
            throw new WriterException((Throwable) ioe);
        }
    }

    private void encodeQRCodeContents(Intent intent, String type) {
        Bundle bundle;
        ContactEncoder mecardEncoder;
        if (type.equals(Contents.Type.TEXT)) {
            String data = intent.getStringExtra(Intents.Encode.DATA);
            if (data != null && data.length() > 0) {
                this.contents = data;
                this.displayContents = data;
                this.title = this.activity.getString(R.string.contents_text);
            }
        } else if (type.equals(Contents.Type.EMAIL)) {
            String data2 = ContactEncoder.trim(intent.getStringExtra(Intents.Encode.DATA));
            if (data2 != null) {
                this.contents = "mailto:" + data2;
                this.displayContents = data2;
                this.title = this.activity.getString(R.string.contents_email);
            }
        } else if (type.equals(Contents.Type.PHONE)) {
            String data3 = ContactEncoder.trim(intent.getStringExtra(Intents.Encode.DATA));
            if (data3 != null) {
                this.contents = "tel:" + data3;
                this.displayContents = PhoneNumberUtils.formatNumber(data3);
                this.title = this.activity.getString(R.string.contents_phone);
            }
        } else if (type.equals(Contents.Type.SMS)) {
            String data4 = ContactEncoder.trim(intent.getStringExtra(Intents.Encode.DATA));
            if (data4 != null) {
                this.contents = "sms:" + data4;
                this.displayContents = PhoneNumberUtils.formatNumber(data4);
                this.title = this.activity.getString(R.string.contents_sms);
            }
        } else if (type.equals(Contents.Type.CONTACT)) {
            Bundle bundle2 = intent.getBundleExtra(Intents.Encode.DATA);
            if (bundle2 != null) {
                String name = bundle2.getString("name");
                String organization = bundle2.getString("company");
                String address = bundle2.getString("postal");
                Collection<String> phones = new ArrayList<>(Contents.PHONE_KEYS.length);
                for (int x = 0; x < Contents.PHONE_KEYS.length; x++) {
                    phones.add(bundle2.getString(Contents.PHONE_KEYS[x]));
                }
                Collection<String> emails = new ArrayList<>(Contents.EMAIL_KEYS.length);
                for (int x2 = 0; x2 < Contents.EMAIL_KEYS.length; x2++) {
                    emails.add(bundle2.getString(Contents.EMAIL_KEYS[x2]));
                }
                String url = bundle2.getString(Contents.URL_KEY);
                Collection<String> urls = url == null ? null : Collections.singletonList(url);
                String note = bundle2.getString(Contents.NOTE_KEY);
                if (this.useVCard) {
                    mecardEncoder = new VCardContactEncoder();
                } else {
                    mecardEncoder = new MECARDContactEncoder();
                }
                String[] encoded = mecardEncoder.encode(Collections.singleton(name), organization, Collections.singleton(address), phones, emails, urls, note);
                if (encoded[1].length() > 0) {
                    this.contents = encoded[0];
                    this.displayContents = encoded[1];
                    this.title = this.activity.getString(R.string.contents_contact);
                }
            }
        } else if (type.equals(Contents.Type.LOCATION) && (bundle = intent.getBundleExtra(Intents.Encode.DATA)) != null) {
            float latitude = bundle.getFloat("LAT", Float.MAX_VALUE);
            float longitude = bundle.getFloat("LONG", Float.MAX_VALUE);
            if (latitude != Float.MAX_VALUE && longitude != Float.MAX_VALUE) {
                this.contents = "geo:" + latitude + ',' + longitude;
                this.displayContents = String.valueOf(latitude) + "," + longitude;
                this.title = this.activity.getString(R.string.contents_location);
            }
        }
    }

    private void encodeQRCodeContents(AddressBookParsedResult contact) {
        String[] encoded = (this.useVCard ? new VCardContactEncoder() : new MECARDContactEncoder()).encode(toIterable(contact.getNames()), contact.getOrg(), toIterable(contact.getAddresses()), toIterable(contact.getPhoneNumbers()), toIterable(contact.getEmails()), toIterable(contact.getURLs()), (String) null);
        if (encoded[1].length() > 0) {
            this.contents = encoded[0];
            this.displayContents = encoded[1];
            this.title = this.activity.getString(R.string.contents_contact);
        }
    }

    private static Iterable<String> toIterable(String[] values) {
        if (values == null) {
            return null;
        }
        return Arrays.asList(values);
    }

    Bitmap encodeAsBitmap() throws WriterException {
        String contentsToEncode = this.contents;
        if (contentsToEncode == null) {
            return null;
        }
        Map<EncodeHintType, Object> hints = null;
        String encoding = guessAppropriateEncoding(contentsToEncode);
        if (encoding != null) {
            hints = new EnumMap<>(EncodeHintType.class);
            hints.put(EncodeHintType.CHARACTER_SET, encoding);
        }
        try {
            BitMatrix result = new MultiFormatWriter().encode(contentsToEncode, this.format, this.dimension, this.dimension, hints);
            int width = result.getWidth();
            int height = result.getHeight();
            int[] pixels = new int[(width * height)];
            for (int y = 0; y < height; y++) {
                int offset = y * width;
                for (int x = 0; x < width; x++) {
                    pixels[offset + x] = result.get(x, y) ? BLACK : -1;
                }
            }
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
            return bitmap;
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private static String guessAppropriateEncoding(CharSequence contents2) {
        for (int i = 0; i < contents2.length(); i++) {
            if (contents2.charAt(i) > 255) {
                return "UTF-8";
            }
        }
        return null;
    }
}
