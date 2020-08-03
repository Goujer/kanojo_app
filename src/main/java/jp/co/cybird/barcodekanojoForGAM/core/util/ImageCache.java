package jp.co.cybird.barcodekanojoForGAM.core.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Observable;
import jp.co.cybird.barcodekanojoForGAM.core.util.RemoteResourceManager;

public class ImageCache {

    private static final String TAG = "ImageCache";
    private static HashMap<String, SoftReference<Bitmap>> cache = new HashMap<>();

    public static Bitmap getImage(String key) {
        SoftReference<Bitmap> ref;
        if (!cache.containsKey(key) || (ref = cache.get(key)) == null) {
            return null;
        }
        return ref.get();
    }

    public static void setImage(String key, Bitmap image) {
        cache.put(key, new SoftReference(image));
    }

    public static boolean hasImage(String key) {
        return cache.containsKey(key);
    }

    public static void clear() {
        for (String key : cache.keySet()) {
            cache.get(key).get().recycle();
        }
        cache.clear();
    }

    public static File saveImageBitmap(RemoteResourceManager rrm, String key) {
        try {
            Bitmap bitmap = (Bitmap) new SoftReference(BitmapFactory.decodeStream(rrm.getInputStream(Uri.parse(key)))).get();
            if (bitmap == null) {
                return null;
            }
            File file = new File(String.valueOf(Environment.getExternalStorageDirectory().getPath()) + "/barcodekanojo/", "temp.jpg");
            if (file.exists()) {
                file.delete();
            }
            try {
                FileOutputStream out = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
                out.flush();
                out.close();
                return file;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } catch (IOException e2) {
            return null;
        } catch (OutOfMemoryError e3) {
            return null;
        }
    }

    public static void setImage(ImageView imageView, String key, RemoteResourceManager rrm, int default_rid) {
        if (imageView != null) {
            if (key == null || key.equals("null") || rrm == null) {
                imageView.setImageResource(default_rid);
                return;
            }
            Uri photoUri = Uri.parse(key);
            File file = rrm.getFile(photoUri);
            if (file == null) {
                imageView.setImageResource(default_rid);
            } else if (!file.exists()) {
                imageView.setImageResource(default_rid);
            } else {
                try {
                    Bitmap bitmap = (Bitmap) new SoftReference(BitmapFactory.decodeStream(rrm.getInputStream(photoUri))).get();
                    if (bitmap != null) {
                        imageView.setImageBitmap(bitmap);
                    } else {
                        imageView.setImageResource(default_rid);
                    }
                } catch (IOException e) {
                    imageView.setImageResource(default_rid);
                } catch (OutOfMemoryError e2) {
                    imageView.setImageResource(default_rid);
                }
            }
        }
    }

    public static void setImageAndRequest(Context context, ImageView imageView, String key, RemoteResourceManager rrm, int default_rid) {
        if (imageView != null) {
            if (key == null || rrm == null) {
                imageView.setImageResource(default_rid);
                return;
            }
            Uri photoUri = Uri.parse(key);
            try {
                Bitmap bitmap = BitmapFactory.decodeStream(rrm.getInputStream(photoUri));
                if (bitmap != null) {
                    imageView.setImageBitmap(bitmap);
                } else {
                    imageView.setImageResource(default_rid);
                }
            } catch (IOException e) {
                imageView.setImageResource(default_rid);
                final Context context2 = context;
                final ImageView imageView2 = imageView;
                final RemoteResourceManager remoteResourceManager = rrm;
                final int i = default_rid;
                rrm.addObserver(new RemoteResourceManager.ResourceRequestObserver(photoUri) {
                    public void requestReceived(Observable observable, Uri uri) {
                        observable.deleteObserver(this);
                        ImageCache.updateImageView(context2, imageView2, uri, remoteResourceManager, i);
                    }
                });
                rrm.request(photoUri);
            }
        }
    }

    /* access modifiers changed from: private */
    public static void updateImageView(Context context, final ImageView imageView, final Uri uri, final RemoteResourceManager rrm, final int default_rid) {
        ((Activity) context).runOnUiThread(new Runnable() {
            public void run() {
                try {
                    imageView.setImageBitmap((Bitmap) new SoftReference(BitmapFactory.decodeStream(rrm.getInputStream(uri))).get());
                } catch (IOException e) {
                    imageView.setImageResource(default_rid);
                } catch (Exception e2) {
                    Log.d(ImageCache.TAG, "updateImageView: error??? ", e2);
                }
            }
        });
    }

    public static void requestImage(String key, RemoteResourceManager rrm) {
        if (key != null && rrm != null) {
            Uri uri = Uri.parse(key);
            try {
                File file = rrm.getFile(uri);
                if (file == null) {
                    rrm.request(uri);
                } else if (!file.exists()) {
                    rrm.addObserver(new RemoteResourceManager.ResourceRequestObserver(uri) {
                        public void requestReceived(Observable observable, Uri uri) {
                            observable.deleteObserver(this);
                        }
                    });
                    rrm.request(uri);
                }
            } catch (Exception e) {
                rrm.request(uri);
            }
        }
    }

    public static boolean hasImageAndRequest(String key, RemoteResourceManager rrm) {
        if (key == null || rrm == null) {
            return false;
        }
        Uri uri = Uri.parse(key);
        if (rrm.getFile(uri) != null) {
            return cache.containsKey(key);
        }
        rrm.request(uri);
        return false;
    }
}
