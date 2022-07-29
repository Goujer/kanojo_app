package jp.co.cybird.barcodekanojoForGAM.activity;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
//import android.support.v4.app.FragmentActivity;
import android.view.View;

//import com.google.android.gms.maps.CameraUpdateFactory;
//import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.MapFragment;
//import com.google.android.gms.maps.MapView;
//import com.google.android.gms.maps.model.CameraPosition;
//import com.google.android.gms.maps.model.LatLng;
import com.goujer.barcodekanojo.R;

//TODO: Get this working and put it somewhere.
public class MapKanojosActivity extends /*Fragment*/Activity implements View.OnClickListener {
    //private LatLng here;
    //private GoogleMap googleMap;
    //MapController mapController;
    //private MapView mapView;
    //private MyLocationOverlay overlay;

    /* JADX WARNING: type inference failed for: r6v0, types: [android.content.Context, jp.co.cybird.barcodekanojoForGAM.activity.MapKanojosActivity, com.google.android.maps.MapActivity] */
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
		//if (googleMap == null) {
			//googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();

			// check if map is created successfully or not
			//if (googleMap == null) {
			//	Toast.makeText(getApplicationContext(), "Sorry! unable to create maps", Toast.LENGTH_LONG).show();
			//}
		//}
//		savedInstanceState.
//		KanojoItemizedOverlay kanojoOverlay = new KanojoItemizedOverlay(resizeIcon(getResources().getDrawable(R.drawable.dummy_face_90)));
//		kanojoOverlay.setDataList(null);
//        this.mapView.getOverlays().add(kanojoOverlay);
//        this.mapController = this.mapView.getController();
//
//        Location myLocation = googleMap.getMyLocation();
//		CameraPosition cameraPosition = CameraPosition.fromLatLngZoom(new LatLng(myLocation.getLatitude(), myLocation.getLongitude()), 13);
//		googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
//
//        this.mapView = new MapView(this, getResources().getString(R.string.map_key));
//        this.mapView.setEnabled(true);
//        this.mapView.setClickable(true);
//		googleMap.getUiSettings().setZoomControlsEnabled(true);
//        setContentView(this.mapView);
    }

    protected void onResume() {
        super.onResume();
//        this.overlay = new MyLocationOverlay(getApplicationContext(), this.mapView);
//        this.overlay.onProviderEnabled("gps");
//		googleMap.setMyLocationEnabled(false);
//        this.overlay.runOnFirstFix(new Runnable() {
//            public void run() {
//                here = overlay.getMyLocation();
//                mapView.getController().animateTo(here);
//            }
//        });
//        googleMap.addMarker();
//        this.mapView.getOverlays().add(this.overlay);
//        this.mapView.invalidate();
    }

    protected void onPause() {
        //this.overlay.disableMyLocation();
		//googleMap.setMyLocationEnabled(false);
		//googleMap.clear();	//this.mapView.getOverlays().remove(this.overlay);
		super.onPause();
    }

    protected void onDestroy() {
        MapKanojosActivity.super.onDestroy();
    }

    protected boolean isRouteDisplayed() {
        return false;
    }

    public void onClick(View v) {
    }

//    private class KanojoItemizedOverlay extends ItemizedOverlay<KanojoOverlayItem> {
//        private List<Kanojo> list = null;
//        Paint mPaint = new Paint();
//
//        public KanojoItemizedOverlay(Drawable defaultMarker) {
//            super(boundCenterBottom(defaultMarker));
//            this.mPaint.setColor(-16777216);
//        }
//
//        public void setDataList(List<Kanojo> list2) {
//            this.list = list2;
//            populate();
//        }
//
//        protected KanojoOverlayItem createItem(int i) {
//            return new KanojoOverlayItem(this.list.get(i).getGeo());
//        }
//
//        public int size() {
//            return this.list.size();
//        }
//
//        public void draw(Canvas canvas, MapView mapView, boolean isShadow) {
//            if (!isShadow) {
//                super.draw(canvas, mapView, isShadow);
//            }
//        }
//    }

//    private class KanojoOverlayItem extends OverlayItem {
//        public KanojoOverlayItem(LatLng point) {
//            super(point, "", "");
//        }
//    }

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
        Bitmap thumb = Bitmap.createBitmap(width, height, icon.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
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
