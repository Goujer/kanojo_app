package jp.co.cybird.barcodekanojoForGAM.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.goujer.barcodekanojo.R;
import com.goujer.barcodekanojo.core.model.Kanojo;

import org.osmdroid.api.IMapController;
import org.osmdroid.api.IMapView;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedOverlay;
import org.osmdroid.views.overlay.OverlayItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jp.co.cybird.barcodekanojoForGAM.Defs;

//TODO: Get this working and put it somewhere.
public class MapKanojosActivity extends /*Fragment*/Activity implements View.OnClickListener {
	private final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;
    private Location lastKnownLoc;
    //private GoogleMap googleMap;
    IMapController mapController;
    private MapView mapView;
    //private MyLocationOverlay overlay;

    /* JADX WARNING: type inference failed for: r6v0, types: [android.content.Context, jp.co.cybird.barcodekanojoForGAM.activity.MapKanojosActivity, com.google.android.maps.MapActivity] */
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);

	    Context ctx = getApplicationContext();
	    Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));

		setContentView(R.layout.activity_map_kanojos);

		mapView = findViewById(R.id.mapview);
		mapView.setTileSource(TileSourceFactory.MAPNIK);

	    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
		    requestPermissionsIfNecessary(new String[]{
				    // if you need to show the current location, uncomment the line below
				    Manifest.permission.ACCESS_COARSE_LOCATION,
				    // WRITE_EXTERNAL_STORAGE is required in order to show the map
				    Manifest.permission.WRITE_EXTERNAL_STORAGE
		    });
	    }

		//if (googleMap == null) {
			//googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();

			// check if map is created successfully or not
			//if (googleMap == null) {
			//	Toast.makeText(getApplicationContext(), "Sorry! unable to create maps", Toast.LENGTH_LONG).show();
			//}
		//}
//		savedInstanceState.
		KanojoItemizedOverlay kanojoOverlay = new KanojoItemizedOverlay(resizeIcon(getResources().getDrawable(R.drawable.dummy_face_90)));
		kanojoOverlay.setDataList(null);
        this.mapView.getOverlays().add(kanojoOverlay);
        this.mapController = this.mapView.getController();

	    //Set on map on User location
	    this.mapController.setZoom(9.5);
	    LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
	    try {
		    lastKnownLoc = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		    if (lastKnownLoc != null) {
			    mapController.setCenter(new GeoPoint(lastKnownLoc));
		    }
	    } catch (Exception e) {
		    if (Defs.DEBUG) {
			    e.printStackTrace();
		    }
		    Toast.makeText(this,"Failed to get Location", Toast.LENGTH_LONG).show();
	    }

//        this.mapView = new MapView(this, getResources().getString(R.string.map_key));
//        this.mapView.setEnabled(true);
//        this.mapView.setClickable(true);

	    //Enable touch zoom and zoom buttons
	    mapView.setBuiltInZoomControls(true);
	    mapView.setMultiTouchControls(true);
    }

    protected void onResume() {
        super.onResume();
	    mapView.onResume(); //needed for compass, my location overlays, v6.0.0 and up
//        this.overlay = new MyLocationOverlay(getApplicationContext(), this.mapView);
//        this.overlay.onProviderEnabled("gps");
//		googleMap.setMyLocationEnabled(false);
//        this.overlay.runOnFirstFix(new Runnable() {
//            public void run() {
//                lastKnownLoc = overlay.getMyLocation();
//                mapView.getController().animateTo(lastKnownLoc);
//            }
//        });
//        googleMap.addMarker();
//        this.mapView.getOverlays().add(this.overlay);
//        this.mapView.invalidate();
    }

    protected void onPause() {
	    super.onPause();
	    mapView.onPause();  //needed for compass, my location overlays, v6.0.0 and up
        //this.overlay.disableMyLocation();
		//googleMap.setMyLocationEnabled(false);
		//googleMap.clear();	//this.mapView.getOverlays().remove(this.overlay);
    }

    protected void onDestroy() {
        MapKanojosActivity.super.onDestroy();
    }

    public void onClick(View v) {
    }

    private class KanojoItemizedOverlay extends ItemizedOverlay<KanojoOverlayItem> {
        private List<Kanojo> list = null;
        Paint mPaint = new Paint();

        public KanojoItemizedOverlay(Drawable defaultMarker) {
            //super(boundCenterBottom(defaultMarker));
	        super(defaultMarker);
            this.mPaint.setColor(-16777216);
        }

        public void setDataList(List<Kanojo> list2) {
            this.list = list2;
            populate();
        }

        protected KanojoOverlayItem createItem(int i) {
            return new KanojoOverlayItem(this.list.get(i).getGeo());
        }

		public int size() {
			if (this.list != null) {
				return this.list.size();
			} else {
				return 0;
			}
		}

        public void draw(Canvas canvas, MapView mapView, boolean isShadow) {
            if (!isShadow) {
                super.draw(canvas, mapView, isShadow);
            }
        }

	    @Override
	    public boolean onSnapToItem(int x, int y, Point snapPoint, IMapView mapView) {
		    return false;
	    }
    }

    private class KanojoOverlayItem extends OverlayItem {
        public KanojoOverlayItem(GeoPoint point) {
            super("", "", point);
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

	@RequiresApi(api = Build.VERSION_CODES.M)
	private void requestPermissionsIfNecessary(String[] permissions) {
		ArrayList<String> permissionsToRequest = new ArrayList<>();
		for (String permission : permissions) {
			if (checkSelfPermission(permission)
					!= PackageManager.PERMISSION_GRANTED) {
				// Permission is not granted
				permissionsToRequest.add(permission);
			}
		}
		if (permissionsToRequest.size() > 0) {
			requestPermissions(
					permissionsToRequest.toArray(new String[0]),
					REQUEST_PERMISSIONS_REQUEST_CODE);
		}
	}

	@RequiresApi(api = Build.VERSION_CODES.M)
	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, int[] grantResults) {
		ArrayList<String> permissionsToRequest = new ArrayList<>(Arrays.asList(permissions).subList(0, grantResults.length));
		if (permissionsToRequest.size() > 0) {
			requestPermissions(
					permissionsToRequest.toArray(new String[0]),
					REQUEST_PERMISSIONS_REQUEST_CODE);
		}
	}
}
