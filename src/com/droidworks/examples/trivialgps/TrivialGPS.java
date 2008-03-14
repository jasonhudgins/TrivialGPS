package com.droidworks.examples.trivialgps;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentReceiver;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Point;

/**
* Sample Application to demonstrate how to use some of the
* android Location API's
* 
* Author: Jason Hudgins <jason@droidworks.com>
*/
public class TrivialGPS extends MapActivity {
	
	public static final String UPDATE_LOC = "com.test.TrivialGPS.LOC_UPDATE";
	
	private MapController mapController;
	private MapView mapView;
	private LocationManager locationManager;
	
    /** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle icicle) {
	   super.onCreate(icicle);

	   // create a map view
	   mapView = new MapView(this);
	   mapController = mapView.getController();
	   mapController.zoomTo(22);
	   setContentView(mapView);

	   // get a hangle on the location manager
	   locationManager =
	     (LocationManager) getSystemService(Context.LOCATION_SERVICE);

	   // get the "gps" mock provider
	   LocationProvider provider = locationManager.getProvider("gps");

	   // register our IntentReceiver as an observer.
	   registerReceiver(new handleLocationUpdate(), new IntentFilter(UPDATE_LOC));

	   // instruct the location manager to broad cast the UPDATE_LOC intent
	   // continually as we move.
	   Intent intent = new Intent(UPDATE_LOC);
	   locationManager.requestUpdates(provider, 0, 0, intent);
	}
        
    // this inner class is the intent reciever that recives notifcations
    // from the location provider about position updates, and then redraws
    // the MapView with the new location centered.
    public class handleLocationUpdate extends IntentReceiver {
    	public void onReceiveIntent(Context context, Intent intent) {
    		// getExtra is deprecated, but I currently see no alternative.
    		@SuppressWarnings("deprecation")
    		Location loc = (Location) intent.getExtra("location");
        	
        	Double lat = loc.getLatitude()*1E6;
        	Double lng = loc.getLongitude()*1E6;
        	Point point = new Point(lat.intValue(), lng.intValue());
        	mapController.centerMapTo(point, false);
        	
            setContentView(mapView);
    	}
    }
}
