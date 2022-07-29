package com.google.zxing.client.android.result;

import android.app.Activity;
import android.telephony.PhoneNumberUtils;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import com.google.zxing.client.result.AddressBookParsedResult;
import com.google.zxing.client.result.ParsedResult;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import com.goujer.barcodekanojo.R;

public final class AddressBookResultHandler extends ResultHandler {
    private static final int[] BUTTON_TEXTS = {R.string.button_add_contact, R.string.button_show_map, R.string.button_dial, R.string.button_email};
    private static final DateFormat[] DATE_FORMATS = {new SimpleDateFormat("yyyyMMdd", Locale.ENGLISH), new SimpleDateFormat("yyyyMMdd'T'HHmmss", Locale.ENGLISH), new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH), new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH)};
    private int buttonCount;
    private final boolean[] fields;

    static {
        for (DateFormat format : DATE_FORMATS) {
            format.setLenient(false);
        }
    }

    private int mapIndexToAction(int index) {
        if (index < this.buttonCount) {
            int count = -1;
            for (int x = 0; x < 4; x++) {
                if (this.fields[x]) {
                    count++;
                }
                if (count == index) {
                    return x;
                }
            }
        }
        return -1;
    }

    public AddressBookResultHandler(Activity activity, ParsedResult result) {
        super(activity, result);
        boolean hasAddress;
        boolean hasPhoneNumber;
        boolean hasEmailAddress;
        AddressBookParsedResult addressResult = (AddressBookParsedResult) result;
        String[] addresses = addressResult.getAddresses();
        if (addresses == null || addresses.length <= 0 || addresses[0] == null || addresses[0].length() <= 0) {
            hasAddress = false;
        } else {
            hasAddress = true;
        }
        String[] phoneNumbers = addressResult.getPhoneNumbers();
        if (phoneNumbers == null || phoneNumbers.length <= 0) {
            hasPhoneNumber = false;
        } else {
            hasPhoneNumber = true;
        }
        String[] emails = addressResult.getEmails();
        if (emails == null || emails.length <= 0) {
            hasEmailAddress = false;
        } else {
            hasEmailAddress = true;
        }
        this.fields = new boolean[4];
        this.fields[0] = true;
        this.fields[1] = hasAddress;
        this.fields[2] = hasPhoneNumber;
        this.fields[3] = hasEmailAddress;
        this.buttonCount = 0;
        for (int x = 0; x < 4; x++) {
            if (this.fields[x]) {
                this.buttonCount++;
            }
        }
    }

    public int getButtonCount() {
        return this.buttonCount;
    }

    public int getButtonText(int index) {
        return BUTTON_TEXTS[mapIndexToAction(index)];
    }

    public void handleButtonPress(int index) {
        AddressBookParsedResult addressResult = (AddressBookParsedResult) getResult();
        String[] addresses = addressResult.getAddresses();
        String address1 = (addresses == null || addresses.length < 1) ? null : addresses[0];
        String[] addressTypes = addressResult.getAddressTypes();
        String address1Type = (addressTypes == null || addressTypes.length < 1) ? null : addressTypes[0];
        switch (mapIndexToAction(index)) {
            case 0:
                addContact(addressResult.getNames(), addressResult.getNicknames(), addressResult.getPronunciation(), addressResult.getPhoneNumbers(), addressResult.getPhoneTypes(), addressResult.getEmails(), addressResult.getEmailTypes(), addressResult.getNote(), addressResult.getInstantMessenger(), address1, address1Type, addressResult.getOrg(), addressResult.getTitle(), addressResult.getURLs(), addressResult.getBirthday(), addressResult.getGeo());
                return;
            case 1:
                String[] names = addressResult.getNames();
                searchMap(address1, names != null ? names[0] : null);
                return;
            case 2:
                dialPhone(addressResult.getPhoneNumbers()[0]);
                return;
            case 3:
                sendEmail(addressResult.getEmails()[0], (String) null, (String) null);
                return;
            default:
                return;
        }
    }

    private static Date parseDate(String s) {
        DateFormat[] dateFormatArr = DATE_FORMATS;
        int i = 0;
        while (i < dateFormatArr.length) {
            try {
                return dateFormatArr[i].parse(s);
            } catch (ParseException e) {
                i++;
            }
        }
        return null;
    }

    public CharSequence getDisplayContents() {
        Date date;
        AddressBookParsedResult result = (AddressBookParsedResult) getResult();
        StringBuilder contents = new StringBuilder(100);
        ParsedResult.maybeAppend(result.getNames(), contents);
        int namesLength = contents.length();
        String pronunciation = result.getPronunciation();
        if (pronunciation != null && pronunciation.length() > 0) {
            contents.append("\n(");
            contents.append(pronunciation);
            contents.append(')');
        }
        ParsedResult.maybeAppend(result.getTitle(), contents);
        ParsedResult.maybeAppend(result.getOrg(), contents);
        ParsedResult.maybeAppend(result.getAddresses(), contents);
        String[] numbers = result.getPhoneNumbers();
        if (numbers != null) {
            for (String number : numbers) {
                ParsedResult.maybeAppend(PhoneNumberUtils.formatNumber(number), contents);
            }
        }
        ParsedResult.maybeAppend(result.getEmails(), contents);
        ParsedResult.maybeAppend(result.getURLs(), contents);
        String birthday = result.getBirthday();
        if (!(birthday == null || birthday.length() <= 0 || (date = parseDate(birthday)) == null)) {
            ParsedResult.maybeAppend(DateFormat.getDateInstance(2).format(Long.valueOf(date.getTime())), contents);
        }
        ParsedResult.maybeAppend(result.getNote(), contents);
        if (namesLength <= 0) {
            return contents.toString();
        }
        Spannable styled = new SpannableString(contents.toString());
        styled.setSpan(new StyleSpan(1), 0, namesLength, 0);
        return styled;
    }

    public int getDisplayTitle() {
        return R.string.result_address_book;
    }
}
