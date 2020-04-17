package com.google.zxing.client.result;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import java.util.HashMap;
import jp.co.cybird.barcodekanojoForGAM.gree.core.GreeDefs;

public final class ExpandedProductResultParser extends ResultParser {
    public ExpandedProductParsedResult parse(Result result) {
        String rawText;
        if (result.getBarcodeFormat() != BarcodeFormat.RSS_EXPANDED || (rawText = getMassagedText(result)) == null) {
            return null;
        }
        String productID = null;
        String sscc = null;
        String lotNumber = null;
        String productionDate = null;
        String packagingDate = null;
        String bestBeforeDate = null;
        String expirationDate = null;
        String weight = null;
        String weightType = null;
        String weightIncrement = null;
        String price = null;
        String priceIncrement = null;
        String priceCurrency = null;
        HashMap hashMap = new HashMap();
        int i = 0;
        while (i < rawText.length()) {
            String ai = findAIvalue(i, rawText);
            if (ai == null) {
                return null;
            }
            int i2 = i + ai.length() + 2;
            String value = findValue(i2, rawText);
            i = i2 + value.length();
            if ("00".equals(ai)) {
                sscc = value;
            } else if ("01".equals(ai)) {
                productID = value;
            } else if (GreeDefs.GEOPOINT.equals(ai)) {
                lotNumber = value;
            } else if (GreeDefs.USER_NAME.equals(ai)) {
                productionDate = value;
            } else if ("13".equals(ai)) {
                packagingDate = value;
            } else if ("15".equals(ai)) {
                bestBeforeDate = value;
            } else if ("17".equals(ai)) {
                expirationDate = value;
            } else if ("3100".equals(ai) || "3101".equals(ai) || "3102".equals(ai) || "3103".equals(ai) || "3104".equals(ai) || "3105".equals(ai) || "3106".equals(ai) || "3107".equals(ai) || "3108".equals(ai) || "3109".equals(ai)) {
                weight = value;
                weightType = ExpandedProductParsedResult.KILOGRAM;
                weightIncrement = ai.substring(3);
            } else if ("3200".equals(ai) || "3201".equals(ai) || "3202".equals(ai) || "3203".equals(ai) || "3204".equals(ai) || "3205".equals(ai) || "3206".equals(ai) || "3207".equals(ai) || "3208".equals(ai) || "3209".equals(ai)) {
                weight = value;
                weightType = ExpandedProductParsedResult.POUND;
                weightIncrement = ai.substring(3);
            } else if ("3920".equals(ai) || "3921".equals(ai) || "3922".equals(ai) || "3923".equals(ai)) {
                price = value;
                priceIncrement = ai.substring(3);
            } else if (!"3930".equals(ai) && !"3931".equals(ai) && !"3932".equals(ai) && !"3933".equals(ai)) {
                hashMap.put(ai, value);
            } else if (value.length() < 4) {
                return null;
            } else {
                price = value.substring(3);
                priceCurrency = value.substring(0, 3);
                priceIncrement = ai.substring(3);
            }
        }
        return new ExpandedProductParsedResult(rawText, productID, sscc, lotNumber, productionDate, packagingDate, bestBeforeDate, expirationDate, weight, weightType, weightIncrement, price, priceIncrement, priceCurrency, hashMap);
    }

    private static String findAIvalue(int i, String rawText) {
        if (rawText.charAt(i) != '(') {
            return null;
        }
        String rawTextAux = rawText.substring(i + 1);
        StringBuilder buf = new StringBuilder();
        for (int index = 0; index < rawTextAux.length(); index++) {
            char currentChar = rawTextAux.charAt(index);
            if (currentChar == ')') {
                return buf.toString();
            }
            if (currentChar < '0' || currentChar > '9') {
                return null;
            }
            buf.append(currentChar);
        }
        return buf.toString();
    }

    private static String findValue(int i, String rawText) {
        StringBuilder buf = new StringBuilder();
        String rawTextAux = rawText.substring(i);
        for (int index = 0; index < rawTextAux.length(); index++) {
            char c = rawTextAux.charAt(index);
            if (c == '(') {
                if (findAIvalue(index, rawTextAux) != null) {
                    break;
                }
                buf.append('(');
            } else {
                buf.append(c);
            }
        }
        return buf.toString();
    }
}
