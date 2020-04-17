package jp.co.cybird.app.android.lib;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import jp.co.cybird.app.android.lib.applauncher.AppLauncherConsts;

public class InstallReferrerReceiver extends BroadcastReceiver {
    static final String CY_REFERRER_FILE = "cyInstallData";

    public void onReceive(Context context, Intent intent) {
        String campaign = intent.getStringExtra(AppLauncherConsts.REQUEST_PARAM_GENERAL_REFFERER);
        if ("com.android.vending.INSTALL_REFERRER".equals(intent.getAction()) && campaign != null) {
            try {
                OutputStream output = context.openFileOutput(CY_REFERRER_FILE, 0);
                output.write(campaign.getBytes());
                output.close();
                Bundle bundle = context.getPackageManager().getApplicationInfo(context.getPackageName(), 128).metaData;
                if (bundle != null) {
                    for (String key : bundle.keySet()) {
                        if (key.startsWith("receiver.")) {
                            Object obj = Class.forName(bundle.getString(key)).newInstance();
                            obj.getClass().getMethod("onReceive", new Class[]{Context.class, Intent.class}).invoke(obj, new Object[]{context, intent});
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (PackageManager.NameNotFoundException e2) {
                e2.printStackTrace();
            } catch (NullPointerException e3) {
                e3.printStackTrace();
            } catch (InstantiationException e4) {
                e4.printStackTrace();
            } catch (IllegalAccessException e5) {
                e5.printStackTrace();
            } catch (ClassNotFoundException e6) {
                e6.printStackTrace();
            } catch (NoSuchMethodException e7) {
                e7.printStackTrace();
            } catch (IllegalArgumentException e8) {
                e8.printStackTrace();
            } catch (InvocationTargetException e9) {
                e9.printStackTrace();
            } catch (Exception e10) {
                e10.printStackTrace();
            }
        }
    }

    public static String getAndClearCampaign(Context context) {
        if (context.getFileStreamPath(CY_REFERRER_FILE).exists()) {
            try {
                FileInputStream input = context.openFileInput(CY_REFERRER_FILE);
                byte[] inputBytes = new byte[8192];
                int readLen = input.read(inputBytes, 0, 8192);
                if (input.available() > 0) {
                    input.close();
                    context.deleteFile(CY_REFERRER_FILE);
                    return null;
                }
                input.close();
                context.deleteFile(CY_REFERRER_FILE);
                if (readLen <= 0) {
                    return null;
                }
                return new String(inputBytes, 0, readLen);
            } catch (FileNotFoundException e) {
                return null;
            } catch (IOException e2) {
                context.deleteFile(CY_REFERRER_FILE);
            } catch (Exception e3) {
                e3.printStackTrace();
            }
        }
        return null;
    }
}
