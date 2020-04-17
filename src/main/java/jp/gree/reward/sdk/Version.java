package jp.gree.reward.sdk;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.jar.Manifest;

public class Version {
    public static void main(String[] strArr) {
        InputStream inputStream = null;
        try {
            String externalForm = Version.class.getResource(String.valueOf(Version.class.getSimpleName()) + ".class").toExternalForm();
            inputStream = new URL(String.valueOf(externalForm.substring(0, externalForm.lastIndexOf(Version.class.getName().replace(".", "/")))) + "META-INF/MANIFEST.MF").openStream();
            String value = new Manifest(inputStream).getMainAttributes().getValue("Built-Date");
            System.out.println("Version : " + Version.class.getPackage().getImplementationVersion());
            System.out.println("Built-Date: " + value);
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e2) {
            e2.printStackTrace();
            try {
                inputStream.close();
            } catch (IOException e3) {
                e3.printStackTrace();
            }
        } catch (MalformedURLException e4) {
            e4.printStackTrace();
            try {
                inputStream.close();
            } catch (IOException e5) {
                e5.printStackTrace();
            }
        } catch (IOException e6) {
            e6.printStackTrace();
            try {
                inputStream.close();
            } catch (IOException e7) {
                e7.printStackTrace();
            }
        } catch (Throwable th) {
            try {
                inputStream.close();
            } catch (IOException e8) {
                e8.printStackTrace();
            }
            throw th;
        }
    }
}
