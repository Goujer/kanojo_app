package com.google.zxing.client.android.result;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import com.google.zxing.Result;
import com.google.zxing.client.android.Contents;
import com.google.zxing.client.android.Intents;
import com.google.zxing.client.android.LocaleManager;
import com.google.zxing.client.android.PreferencesActivity;
import com.google.zxing.client.android.book.SearchBookContentsActivity;
import com.google.zxing.client.result.ParsedResult;
import com.google.zxing.client.result.ParsedResultType;
import com.google.zxing.client.result.ResultParser;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.Locale;
import com.goujer.barcodekanojo.R;

public abstract class ResultHandler {
    private static final String[] ADDRESS_TYPE_STRINGS = {"home", "work"};
    private static final int[] ADDRESS_TYPE_VALUES = {1, 2};
    private static final String[] EMAIL_TYPE_STRINGS = {"home", "work", "mobile"};
    private static final int[] EMAIL_TYPE_VALUES = {1, 2, 4};
    private static final String GOOGLE_SHOPPER_ACTIVITY = "com.google.android.apps.shopper.results.SearchResultsActivity";
    private static final String GOOGLE_SHOPPER_PACKAGE = "com.google.android.apps.shopper";
    private static final String MARKET_REFERRER_SUFFIX = "&referrer=utm_source%3Dbarcodescanner%26utm_medium%3Dapps%26utm_campaign%3Dscan";
    private static final String MARKET_URI_PREFIX = "market://details?id=";
    public static final int MAX_BUTTON_COUNT = 4;
    private static final int NO_TYPE = -1;
    private static final String[] PHONE_TYPE_STRINGS = {"home", "work", "mobile", "fax", "pager", "main"};
    private static final int[] PHONE_TYPE_VALUES = {1, 3, 2, 4, 6, 12};
    private static final String TAG = ResultHandler.class.getSimpleName();
    private final Activity activity;
    private final String customProductSearch;
    private final Result rawResult;
    private final ParsedResult result;
    private final DialogInterface.OnClickListener shopperMarketListener;

    public abstract int getButtonCount();

    public abstract int getButtonText(int i);

    public abstract int getDisplayTitle();

    public abstract void handleButtonPress(int i);

    ResultHandler(Activity activity2, ParsedResult result2) {
        this(activity2, result2, (Result) null);
    }

    ResultHandler(Activity activity2, ParsedResult result2, Result rawResult2) {
        this.shopperMarketListener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int which) {
                ResultHandler.this.launchIntent(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=com.google.android.apps.shopper&referrer=utm_source%3Dbarcodescanner%26utm_medium%3Dapps%26utm_campaign%3Dscan")));
            }
        };
        this.result = result2;
        this.activity = activity2;
        this.rawResult = rawResult2;
        this.customProductSearch = parseCustomSearchURL();
    }

    public final ParsedResult getResult() {
        return this.result;
    }

    final boolean hasCustomProductSearch() {
        return this.customProductSearch != null;
    }

    final Activity getActivity() {
        return this.activity;
    }

    public boolean areContentsSecure() {
        return false;
    }

    public CharSequence getDisplayContents() {
        return this.result.getDisplayResult().replace("\r", "");
    }

    public final ParsedResultType getType() {
        return this.result.getType();
    }

    final void addPhoneOnlyContact(String[] phoneNumbers, String[] phoneTypes) {
        addContact((String[]) null, (String[]) null, (String) null, phoneNumbers, phoneTypes, (String[]) null, (String[]) null, (String) null, (String) null, (String) null, (String) null, (String) null, (String) null, (String[]) null, (String) null, (String[]) null);
    }

    final void addEmailOnlyContact(String[] emails, String[] emailTypes) {
        addContact((String[]) null, (String[]) null, (String) null, (String[]) null, (String[]) null, emails, emailTypes, (String) null, (String) null, (String) null, (String) null, (String) null, (String) null, (String[]) null, (String) null, (String[]) null);
    }

    final void addContact(String[] names, String[] nicknames, String pronunciation, String[] phoneNumbers, String[] phoneTypes, String[] emails, String[] emailTypes, String note, String instantMessenger, String address, String addressType, String org2, String title, String[] urls, String birthday, String[] geo) {
        int type;
        int type2;
        int type3;
        Intent intent = new Intent("android.intent.action.INSERT_OR_EDIT", ContactsContract.Contacts.CONTENT_URI);
        intent.setType("vnd.android.cursor.item/contact");
        putExtra(intent, "name", names != null ? names[0] : null);
        putExtra(intent, "phonetic_name", pronunciation);
        int phoneCount = Math.min(phoneNumbers != null ? phoneNumbers.length : 0, Contents.PHONE_KEYS.length);
        for (int x = 0; x < phoneCount; x++) {
            putExtra(intent, Contents.PHONE_KEYS[x], phoneNumbers[x]);
            if (phoneTypes != null && x < phoneTypes.length && (type3 = toPhoneContractType(phoneTypes[x])) >= 0) {
                intent.putExtra(Contents.PHONE_TYPE_KEYS[x], type3);
            }
        }
        int emailCount = Math.min(emails != null ? emails.length : 0, Contents.EMAIL_KEYS.length);
        for (int x2 = 0; x2 < emailCount; x2++) {
            putExtra(intent, Contents.EMAIL_KEYS[x2], emails[x2]);
            if (emailTypes != null && x2 < emailTypes.length && (type2 = toEmailContractType(emailTypes[x2])) >= 0) {
                intent.putExtra(Contents.EMAIL_TYPE_KEYS[x2], type2);
            }
        }
        StringBuilder aggregatedNotes = new StringBuilder();
        if (urls != null) {
            for (String url : urls) {
                if (url != null && url.length() > 0) {
                    aggregatedNotes.append(10).append(url);
                }
            }
        }
        for (String aNote : new String[]{birthday, note}) {
            if (aNote != null) {
                aggregatedNotes.append(10).append(aNote);
            }
        }
        if (nicknames != null) {
            for (String nickname : nicknames) {
                if (nickname != null && nickname.length() > 0) {
                    aggregatedNotes.append(10).append(nickname);
                }
            }
        }
        if (geo != null) {
            aggregatedNotes.append(10).append(geo[0]).append(',').append(geo[1]);
        }
        if (aggregatedNotes.length() > 0) {
            putExtra(intent, "notes", aggregatedNotes.substring(1));
        }
        putExtra(intent, "im_handle", instantMessenger);
        putExtra(intent, "postal", address);
        if (addressType != null && (type = toAddressContractType(addressType)) >= 0) {
            intent.putExtra("postal_type", type);
        }
        putExtra(intent, "company", org2);
        putExtra(intent, "job_title", title);
        launchIntent(intent);
    }

    private static int toEmailContractType(String typeString) {
        return doToContractType(typeString, EMAIL_TYPE_STRINGS, EMAIL_TYPE_VALUES);
    }

    private static int toPhoneContractType(String typeString) {
        return doToContractType(typeString, PHONE_TYPE_STRINGS, PHONE_TYPE_VALUES);
    }

    private static int toAddressContractType(String typeString) {
        return doToContractType(typeString, ADDRESS_TYPE_STRINGS, ADDRESS_TYPE_VALUES);
    }

    private static int doToContractType(String typeString, String[] types, int[] values) {
        if (typeString == null) {
            return NO_TYPE;
        }
        for (int i = 0; i < types.length; i++) {
            String type = types[i];
            if (typeString.startsWith(type) || typeString.startsWith(type.toUpperCase(Locale.ENGLISH))) {
                return values[i];
            }
        }
        return NO_TYPE;
    }

    final void shareByEmail(String contents) {
        sendEmailFromUri("mailto:", (String) null, this.activity.getString(R.string.msg_share_subject_line), contents);
    }

    final void sendEmail(String address, String subject, String body) {
        sendEmailFromUri("mailto:" + address, address, subject, body);
    }

    final void sendEmailFromUri(String uri, String email, String subject, String body) {
        Intent intent = new Intent("android.intent.action.SEND", Uri.parse(uri));
        if (email != null) {
            intent.putExtra("android.intent.extra.EMAIL", new String[]{email});
        }
        putExtra(intent, "android.intent.extra.SUBJECT", subject);
        putExtra(intent, "android.intent.extra.TEXT", body);
        intent.setType("text/plain");
        launchIntent(intent);
    }

	final void shareBySMS(String contents) {
        sendSMSFromUri("smsto:", String.valueOf(this.activity.getString(R.string.msg_share_subject_line)) + ":\n" + contents);
    }

	final void sendSMS(String phoneNumber, String body) {
        sendSMSFromUri("smsto:" + phoneNumber, body);
    }

    private final void sendSMSFromUri(String uri, String body) {
        Intent intent = new Intent("android.intent.action.SENDTO", Uri.parse(uri));
        putExtra(intent, "sms_body", body);
        intent.putExtra("compose_mode", true);
        launchIntent(intent);
    }

    final void sendMMS(String phoneNumber, String subject, String body) {
        sendMMSFromUri("mmsto:" + phoneNumber, subject, body);
    }

    private final void sendMMSFromUri(String uri, String subject, String body) {
        Intent intent = new Intent("android.intent.action.SENDTO", Uri.parse(uri));
        if (subject == null || subject.length() == 0) {
            putExtra(intent, "subject", this.activity.getString(R.string.msg_default_mms_subject));
        } else {
            putExtra(intent, "subject", subject);
        }
        putExtra(intent, "sms_body", body);
        intent.putExtra("compose_mode", true);
        launchIntent(intent);
    }

    final void dialPhone(String phoneNumber) {
        launchIntent(new Intent("android.intent.action.DIAL", Uri.parse("tel:" + phoneNumber)));
    }

    final void dialPhoneFromUri(String uri) {
        launchIntent(new Intent("android.intent.action.DIAL", Uri.parse(uri)));
    }

    final void openMap(String geoURI) {
        launchIntent(new Intent("android.intent.action.VIEW", Uri.parse(geoURI)));
    }

    final void searchMap(String address, CharSequence title) {
        String query = address;
        if (title != null && title.length() > 0) {
            query = String.valueOf(query) + " (" + title + ')';
        }
        launchIntent(new Intent("android.intent.action.VIEW", Uri.parse("geo:0,0?q=" + Uri.encode(query))));
    }

    final void getDirections(double latitude, double longitude) {
        launchIntent(new Intent("android.intent.action.VIEW", Uri.parse("http://maps.google." + LocaleManager.getCountryTLD(this.activity) + "/maps?f=d&daddr=" + latitude + ',' + longitude)));
    }

    final void openProductSearch(String upc) {
        launchIntent(new Intent("android.intent.action.VIEW", Uri.parse("http://www.google." + LocaleManager.getProductSearchCountryTLD(this.activity) + "/m/products?q=" + upc + "&source=zxing")));
    }

    final void openBookSearch(String isbn) {
        launchIntent(new Intent("android.intent.action.VIEW", Uri.parse("http://books.google." + LocaleManager.getBookSearchCountryTLD(this.activity) + "/books?vid=isbn" + isbn)));
    }

    final void searchBookContents(String isbnOrUrl) {
        Intent intent = new Intent(Intents.SearchBookContents.ACTION);
        intent.setClassName(this.activity, SearchBookContentsActivity.class.getName());
        putExtra(intent, Intents.SearchBookContents.ISBN, isbnOrUrl);
        launchIntent(intent);
    }

    final void openURL(String url) {
        if (url.startsWith("HTTP://")) {
            url = "http" + url.substring(4);
        } else if (url.startsWith("HTTPS://")) {
            url = "https" + url.substring(5);
        }
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(url));
        try {
            launchIntent(intent);
        } catch (ActivityNotFoundException e) {
            Log.w(TAG, "Nothing available to handle " + intent);
        }
    }

    final void webSearch(String query) {
        Intent intent = new Intent("android.intent.action.WEB_SEARCH");
        intent.putExtra("query", query);
        launchIntent(intent);
    }

    final void rawLaunchIntent(Intent intent) {
        if (intent != null) {
        	if (Build.VERSION.SDK_INT < 21) {
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
			} else {
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
        	}
            Log.d(TAG, "Launching intent: " + intent + " with extras: " + intent.getExtras());
            this.activity.startActivity(intent);
        }
    }

    final void launchIntent(Intent intent) {
        try {
            rawLaunchIntent(intent);
        } catch (ActivityNotFoundException e) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this.activity);
            builder.setTitle(R.string.app_name);
            builder.setMessage(R.string.msg_intent_failed);
            builder.setPositiveButton(R.string.button_ok, (DialogInterface.OnClickListener) null);
            builder.show();
        }
    }

    private static void putExtra(Intent intent, String key, String value) {
        if (value != null && value.length() > 0) {
            intent.putExtra(key, value);
        }
    }

    private String parseCustomSearchURL() {
        String customProductSearch2 = PreferenceManager.getDefaultSharedPreferences(this.activity).getString(PreferencesActivity.KEY_CUSTOM_PRODUCT_SEARCH, null);
        if (customProductSearch2 == null || customProductSearch2.trim().length() != 0) {
            return customProductSearch2;
        }
        return null;
    }

    final String fillInCustomSearchURL(String text) {
        if (this.customProductSearch == null) {
            return text;
        }
        try {
            text = URLEncoder.encode(text, "UTF-8");
        } catch (UnsupportedEncodingException e) {
        }
        String url = this.customProductSearch.replace("%s", text);
        if (this.rawResult == null) {
            return url;
        }
        String url2 = url.replace("%f", this.rawResult.getBarcodeFormat().toString());
        if (url2.contains("%t")) {
            return url2.replace("%t", ResultParser.parseResult(this.rawResult).getType().toString());
        }
        return url2;
    }
}
