package info.adamjsmith.letmeknow;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.SmsManager;
import android.view.Window;

public class LocationTools extends Activity{
	LocationManager lm;
	LocationListener locationListener;
	String phoneNumber;
	String message;
	Double lat;
	Double longitude;
	Integer updateInterval;
	Double distance;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.location);
		
		phoneNumber = getIntent().getStringExtra("number");
		message = getIntent().getStringExtra("message");
		lat = getIntent().getDoubleExtra("lat", 0);
		longitude = getIntent().getDoubleExtra("long", 0);
		
		
		lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		locationListener = new MyLocationListener();
		
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
		updateInterval = Integer.parseInt(sharedPref.getString("interval", "4"));
		distance = Double.parseDouble(sharedPref.getString("distance", "0.01"));
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
		if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER) ) {
			lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, updateInterval, 0, locationListener);
		} else {
			lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, updateInterval, 0, locationListener);
		}
		
		
	}
	
	@Override
	public void onPause() {
		super.onPause();
		
		//lm.removeUpdates(locationListener);
	}
	
	private class MyLocationListener implements LocationListener {
		double latRemainder = 100;
		double longRemainder = 100;


		public void onLocationChanged(Location loc) {
			if (loc != null) {
				latRemainder = lat - loc.getLatitude();
				longRemainder = longitude - loc.getLongitude();
							
				
				if(latRemainder < distance && latRemainder > -distance && longRemainder < distance && longRemainder > -distance){
					SmsManager sms = SmsManager.getDefault();
					sms.sendTextMessage(phoneNumber, null, message, null, null);
					Intent data = new Intent();
					data.setData(Uri.parse("Success"));
					setResult(RESULT_OK, data);
					lm.removeUpdates(locationListener);
					finish();
				}
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
