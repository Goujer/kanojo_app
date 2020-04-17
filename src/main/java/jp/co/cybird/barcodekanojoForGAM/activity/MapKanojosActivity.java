package jp.co.cybird.barcodekanojoForGAM.activity;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.OverlayItem;
import java.util.List;
import jp.co.cybird.barcodekanojoForGAM.R;
import jp.co.cybird.barcodekanojoForGAM.core.model.Kanojo;

public class MapKanojosActivity extends MapActivity implements View.OnClickListener {
    GeoPoint here;
    MapController mapController;
    /* access modifiers changed from: private */
    public MapView mapView;
    /* access modifiers changed from: private */
    public MyLocationOverlay overlay;

    /* JADX WARNING: type inference failed for: r6v0, types: [android.content.Context, jp.co.cybird.barcodekanojoForGAM.activity.MapKanojosActivity, com.google.android.maps.MapActivity] */
    public void onCreate(Bundle savedInstanceState) {
        MapKanojosActivity.super.onCreate(savedInstanceState);
        this.mapView = new MapView(this, getResources().getString(R.string.map_key));
        this.mapView.setEnabled(true);
        this.mapView.setClickable(true);
        this.mapView.setBuiltInZoomControls(true);
        setContentView(this.mapView);
        KanojoItemizedOverlay kanojoOverlay = new KanojoItemizedOverlay(resizeIcon(getResources().getDrawable(R.drawable.dummy_face_90)));
        kanojoOverlay.setDataList((List<Kanojo>) null);
        this.mapView.getOverlays().add(kanojoOverlay);
        this.mapController = this.mapView.getController();
        this.mapController.setZoom(13);
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        MapKanojosActivity.super.onResume();
        this.overlay = new MyLocationOverlay(getApplicationContext(), this.mapView);
        this.overlay.onProviderEnabled("gps");
        this.overlay.enableMyLocation();
        this.overlay.runOnFirstFix(new Runnable() {
            public void run() {
                MapKanojosActivity.this.here = MapKanojosActivity.this.overlay.getMyLocation();
                MapKanojosActivity.this.mapView.getController().animateTo(MapKanojosActivity.this.here);
            }
        });
        this.mapView.getOverlays().add(this.overlay);
        this.mapView.invalidate();
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        this.overlay.disableMyLocation();
        this.mapView.getOverlays().remove(this.overlay);
        MapKanojosActivity.super.onPause();
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        MapKanojosActivity.super.onDestroy();
    }

    /* access modifiers changed from: protected */
    public boolean isRouteDisplayed() {
        return false;
    }

    public void onClick(View v) {
    }

    private class KanojoItemizedOverlay extends ItemizedOverlay<KanojoOverlayItem> {
        private List<Kanojo> list = null;
        Paint mPaint = new Paint();

        public KanojoItemizedOverlay(Drawable defaultMarker) {
            super(boundCenterBottom(defaultMarker));
            this.mPaint.setColor(-16777216);
        }

        public void setDataList(List<Kanojo> list2) {
            this.list = list2;
            populate();
        }

        /* access modifiers changed from: protected */
        public KanojoOverlayItem createItem(int i) {
            return new KanojoOverlayItem(this.list.get(i).getGeo());
        }

        public int size() {
            return this.list.size();
        }

        public void draw(Canvas canvas, MapView mapView, boolean isShadow) {
            if (!isShadow) {
                MapKanojosActivity.super.draw(canvas, mapView, isShadow);
            }
        }
    }

    private class KanojoOverlayItem extends OverlayItem {
        public KanojoOverlayItem(GeoPoint point) {
            super(point, "", "");
        }
    }

    private Drawable resizeIcon(Drawable icon) {
        Resources res = getResources();
        int width = (int) res.getDimension(R.dimen.map_kanojos_pin_width);
        int height = (int) res.getDimension(R.dimen.map_kanojos_pin_height);
        int iconWidth = icon.getIntrinsicWidth();
        int iconHeight = icon.getIntrinsicHeight();
        if (width <= 0 || height <= 0) {
            return icon;
        }
        if (width >= iconWidth && height >= iconHeight) {
            return icon;
        }
        float ratio = ((float) iconWidth) / ((float) iconHeight);
        if (iconWidth > iconHeight) {
            height = (int) (((float) width) / ratio);
        } else if (iconHeight > iconWidth) {
            width = (int) (((float) height) * ratio);
        }
        Bitmap thumb = Bitmap.createBitmap(width, height, icon.getOpacity() != -1 ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(thumb);
        canvas.setDrawFilter(new PaintFlagsDrawFilter(4, 0));
        Rect oldBounds = new Rect();
        oldBounds.set(icon.getBounds());
        icon.setBounds(0, 0, width, height);
        icon.draw(canvas);
        icon.setBounds(oldBounds);
        return new BitmapDrawable(thumb);
    }
}
