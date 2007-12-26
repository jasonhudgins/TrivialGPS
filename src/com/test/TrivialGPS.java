package com.test;

import java.util.List;

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

public class TrivialGPS extends MapActivity {
	
	public static final String UPDATE_LOC = "com.test.TrivialGPS.LOC_UPDATE";
	
	private MapController mapController;
	private MapView mapView;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        
        // create a map view
        mapView = new MapView(this);
        mapController = mapView.getController();
        mapController.zoomTo(22);
        
        // register our IntentReceiver to recieve notifications about location updates.
        registerReceiver(new handleLocationUpdate(), new IntentFilter(UPDATE_LOC));
        doGPS();
        setContentView(mapView);
    }
    
    private void doGPS() {
        // get a hangle on the location manager
		LocationManager locationManager = 
			(LocationManager) getSystemService(Context.LOCATION_SERVICE);

		// get the "gps" mock provider
    	LocationProvider provider = getProviderByName("gps");
		
		// listen for updates
		Intent intent = new Intent(UPDATE_LOC);
		locationManager.requestUpdates(provider, 0, 0, intent);
    }

    // utility method that returns a named location provider
    private LocationProvider getProviderByName(String name) {
    	
        // get a hangle on the location manager
		LocationManager locationManager = 
			(LocationManager) getSystemService(Context.LOCATION_SERVICE);
		
		List<LocationProvider> providers = locationManager.getProviders();
    	
    	LocationProvider provider = null;
    	for (LocationProvider p : providers) {
    		if (p.getName().equalsIgnoreCase(name)) {
    			provider = p;
    		}
    	}
    	
    	assert provider != null : "Provider " + name + " does not exist";
    	return provider;
    }
    
    // this inner class is the intent reciever that recives notifcations
    // from the location provider about position updates, and then redraws
    // the MapView with the new location centered.
    public class handleLocationUpdate extends IntentReceiver {
    	public void onReceiveIntent(Context context, Intent intent) {
    		Location loc = (Location) intent.getExtra("location");
        	android.util.Log.i("TrivialGPS", "now at lat: " + loc.getLatitude() + 
        			" lng: " + loc.getLongitude());
        	
        	Double lat = loc.getLatitude()*1E6;
        	Double lng = loc.getLongitude()*1E6;
        	Point point = new Point(lat.intValue(), lng.intValue());
        	mapController.centerMapTo(point, false);
        	
            setContentView(mapView);
    	}
    }
}