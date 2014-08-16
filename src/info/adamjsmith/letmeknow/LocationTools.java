package info.adamjsmith.letmeknow;

import java.io.IOException;
import java.util.List;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;

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
			List<Address> addresses = geocoder.getFromLocationName(input,1);
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
}
