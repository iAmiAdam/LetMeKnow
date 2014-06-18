package info.adamjsmith.letmeknow;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class LocationTools extends Activity{
	LocationManager lm;
	LocationListener locationListener;
	
	public void onCreate(Bundle savedInstanceState) {
		lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		locationListener = new MyLocationListener();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
		lm.requestLocationUpdates(
				LocationManager.GPS_PROVIDER, 0, 0, locationListener);
	}
	
	@Override
	public void onPause() {
		super.onPause();
		
		lm.removeUpdates(locationListener);
	}
	
	private class MyLocationListener implements LocationListener {
		public void onLocationChanged(Location loc) {
			if (loc != null) {
				
			}
		}
		
		public void onProviderDisabled(String provider) {
			
		}
		
		public void onProviderEnabled(String provider) {
			
		}
		
		public void onStatusChanged(String provider, int status, Bundle extras) {
			
		}
	}
}
