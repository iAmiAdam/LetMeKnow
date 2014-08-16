package info.adamjsmith.letmeknow;

import android.content.Context;
import android.location.LocationManager;

import com.google.android.gms.maps.model.LatLng;

public class LocationTools {
	
	public static LatLng getCurrentLocation(Context context) {
		LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		
		android.location.Location location;
		location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		
		return new LatLng(location.getLatitude(), location.getLongitude());
	}
}
