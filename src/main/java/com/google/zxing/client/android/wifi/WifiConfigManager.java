package com.google.zxing.client.android.wifi;

import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.util.Log;
import com.google.zxing.client.result.WifiParsedResult;
import java.util.regex.Pattern;

public final class WifiConfigManager extends AsyncTask<WifiParsedResult, Object, Object> {
    private static final Pattern HEX_DIGITS = Pattern.compile("[0-9A-Fa-f]+");
    private static final String TAG = WifiConfigManager.class.getSimpleName();
    private final WifiManager wifiManager;

    public WifiConfigManager(WifiManager wifiManager2) {
        this.wifiManager = wifiManager2;
    }

    /* access modifiers changed from: protected */
    public Object doInBackground(WifiParsedResult... args) {
        WifiParsedResult theWifiResult = args[0];
        if (!this.wifiManager.isWifiEnabled()) {
            Log.i(TAG, "Enabling wi-fi...");
            if (this.wifiManager.setWifiEnabled(true)) {
                Log.i(TAG, "Wi-fi enabled");
                int count = 0;
                while (true) {
                    if (this.wifiManager.isWifiEnabled()) {
                        break;
                    } else if (count >= 10) {
                        Log.i(TAG, "Took too long to enable wi-fi, quitting");
                        break;
                    } else {
                        Log.i(TAG, "Still waiting for wi-fi to enable...");
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                        }
                        count++;
                    }
                }
            } else {
                Log.w(TAG, "Wi-fi could not be enabled!");
            }
            return null;
        }
        String networkTypeString = theWifiResult.getNetworkEncryption();
        try {
            NetworkType networkType = NetworkType.forIntentValue(networkTypeString);
            if (networkType == NetworkType.NO_PASSWORD) {
                changeNetworkUnEncrypted(this.wifiManager, theWifiResult);
            } else {
                String password = theWifiResult.getPassword();
                if (password != null && password.length() != 0) {
                    if (networkType == NetworkType.WEP) {
                        changeNetworkWEP(this.wifiManager, theWifiResult);
                    } else if (networkType == NetworkType.WPA) {
                        changeNetworkWPA(this.wifiManager, theWifiResult);
                    }
                }
            }
        } catch (IllegalArgumentException e2) {
            Log.w(TAG, "Bad network type; see NetworkType values: " + networkTypeString);
        }
        return null;
    }

    private static void updateNetwork(WifiManager wifiManager2, WifiConfiguration config) {
        Integer foundNetworkID = findNetworkInExistingConfig(wifiManager2, config.SSID);
        if (foundNetworkID != null) {
            Log.i(TAG, "Removing old configuration for network " + config.SSID);
            wifiManager2.removeNetwork(foundNetworkID.intValue());
            wifiManager2.saveConfiguration();
        }
        int networkId = wifiManager2.addNetwork(config);
        if (networkId < 0) {
            Log.w(TAG, "Unable to add network " + config.SSID);
        } else if (wifiManager2.enableNetwork(networkId, true)) {
            Log.i(TAG, "Associating to network " + config.SSID);
            wifiManager2.saveConfiguration();
        } else {
            Log.w(TAG, "Failed to enable network " + config.SSID);
        }
    }

    private static WifiConfiguration changeNetworkCommon(WifiParsedResult wifiResult) {
        WifiConfiguration config = new WifiConfiguration();
        config.allowedAuthAlgorithms.clear();
        config.allowedGroupCiphers.clear();
        config.allowedKeyManagement.clear();
        config.allowedPairwiseCiphers.clear();
        config.allowedProtocols.clear();
        config.SSID = quoteNonHex(wifiResult.getSsid(), new int[0]);
        config.hiddenSSID = wifiResult.isHidden();
        return config;
    }

    private static void changeNetworkWEP(WifiManager wifiManager2, WifiParsedResult wifiResult) {
        WifiConfiguration config = changeNetworkCommon(wifiResult);
        config.wepKeys[0] = quoteNonHex(wifiResult.getPassword(), 10, 26, 58);
        config.wepTxKeyIndex = 0;
        config.allowedAuthAlgorithms.set(1);
        config.allowedKeyManagement.set(0);
        config.allowedGroupCiphers.set(2);
        config.allowedGroupCiphers.set(3);
        config.allowedGroupCiphers.set(0);
        config.allowedGroupCiphers.set(1);
        updateNetwork(wifiManager2, config);
    }

    private static void changeNetworkWPA(WifiManager wifiManager2, WifiParsedResult wifiResult) {
        WifiConfiguration config = changeNetworkCommon(wifiResult);
        config.preSharedKey = quoteNonHex(wifiResult.getPassword(), 64);
        config.allowedAuthAlgorithms.set(0);
        config.allowedProtocols.set(0);
        config.allowedProtocols.set(1);
        config.allowedKeyManagement.set(1);
        config.allowedKeyManagement.set(2);
        config.allowedPairwiseCiphers.set(1);
        config.allowedPairwiseCiphers.set(2);
        config.allowedGroupCiphers.set(2);
        config.allowedGroupCiphers.set(3);
        updateNetwork(wifiManager2, config);
    }

    private static void changeNetworkUnEncrypted(WifiManager wifiManager2, WifiParsedResult wifiResult) {
        WifiConfiguration config = changeNetworkCommon(wifiResult);
        config.allowedKeyManagement.set(0);
        updateNetwork(wifiManager2, config);
    }

    private static Integer findNetworkInExistingConfig(WifiManager wifiManager2, String ssid) {
        for (WifiConfiguration existingConfig : wifiManager2.getConfiguredNetworks()) {
            if (existingConfig.SSID.equals(ssid)) {
                return Integer.valueOf(existingConfig.networkId);
            }
        }
        return null;
    }

    private static String quoteNonHex(String value, int... allowedLengths) {
        return isHexOfLength(value, allowedLengths) ? value : convertToQuotedString(value);
    }

    private static String convertToQuotedString(String string) {
        if (string == null || string.length() == 0) {
            return null;
        }
        return (string.charAt(0) == '\"' && string.charAt(string.length() + -1) == '\"') ? string : String.valueOf('\"') + string + '\"';
    }

    private static boolean isHexOfLength(CharSequence value, int... allowedLengths) {
        if (value == null || !HEX_DIGITS.matcher(value).matches()) {
            return false;
        }
        if (allowedLengths.length == 0) {
            return true;
        }
        for (int length : allowedLengths) {
            if (value.length() == length) {
                return true;
            }
        }
        return false;
    }
}
