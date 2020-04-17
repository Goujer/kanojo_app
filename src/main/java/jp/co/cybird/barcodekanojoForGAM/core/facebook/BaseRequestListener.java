package jp.co.cybird.barcodekanojoForGAM.core.facebook;

import android.util.Log;
import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.FacebookError;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

public abstract class BaseRequestListener implements AsyncFacebookRunner.RequestListener {
    public void onFacebookError(FacebookError e, Object state) {
        Log.e("Facebook", e.getMessage());
        e.printStackTrace();
    }

    public void onFileNotFoundException(FileNotFoundException e, Object state) {
        Log.e("Facebook", e.getMessage());
        e.printStackTrace();
    }

    public void onIOException(IOException e, Object state) {
        Log.e("Facebook", e.getMessage());
        e.printStackTrace();
    }

    public void onMalformedURLException(MalformedURLException e, Object state) {
        Log.e("Facebook", e.getMessage());
        e.printStackTrace();
    }
}
