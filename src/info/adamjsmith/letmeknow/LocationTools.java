package info.adamjsmith.letmeknow;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

public class LocationTools {
	
	public static LatLng getCurrentLocation(Context context) {
		LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		
		android.location.Location location;
		location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		
		return new LatLng(location.getLatitude(), location.getLongitude());
	}
	
	public static LatLng getLatLong(Context context, String input) {
		final Geocoder geocoder = new Geocoder(context);
		Double latitude;
		Double longitude;
		try {
			List<Address> addresses = geocoder.getFromLocationName(input, 1);
			if (addresses != null && !addresses.isEmpty()) {
				Address address = addresses.get(0);
				latitude = address.getLatitude();
				longitude = address.getLongitude();
				if (latitude != null && longitude != null) {
					return new LatLng(latitude, longitude);
				} else {
					return new LatLng(0, 0);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new LatLng(0, 0);		
	}
	
	public static void addAlert(Context context, double latitude, double longitude, String number, UUID id) {
		LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
		float radius = Float.parseFloat(sharedPref.getString("distance", "1609")); 
		
		Intent notification = new Intent("info.adamjsmith.letmeknow.NotificationTools");
		notification.putExtra(InstanceFragment.EXTRA_INSTANCE_ID, id);
		Instance i = InstanceHolder.get(context).getInstance(id);
		PendingIntent wrapper = PendingIntent.getBroadcast(context, 0, notification, PendingIntent.FLAG_ONE_SHOT);
		locationManager.addProximityAlert(latitude, longitude, radius, -1, wrapper);
		
		IntentFilter filter = new IntentFilter("info.adamjsmith.letmeknow.NotificationTools");
		context.getApplicationContext().registerReceiver(new NotificationTools(), filter);
	}
}
