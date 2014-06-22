package info.adamjsmith.letmeknow;

import android.app.Service;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.telephony.SmsManager;

public class LocationTools extends Service {
	LocationManager lm;
	LocationListener locationListener;
	String phoneNumber;
	String message;
	Double lat;
	Double longitude;
	Integer updateInterval;
	Double distance;
	NotificationManager nm;
	
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
	
	public int onStartCommand(Intent intent, int flags, int startId) {
		
		phoneNumber = intent.getStringExtra("number");
		message = intent.getStringExtra("message");
		lat = intent.getDoubleExtra("lat", 0);
		longitude = intent.getDoubleExtra("long", 0);
		
		
		lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		locationListener = new MyLocationListener();
		
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
		updateInterval = Integer.parseInt(sharedPref.getString("interval", "4"));
		distance = Double.parseDouble(sharedPref.getString("distance", "0.01"));
		
		nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		
		if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER) ) {
			lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, updateInterval, 0, locationListener);
		} else {
			lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, updateInterval, 0, locationListener);
		}
		
		return START_STICKY;
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
					lm.removeUpdates(locationListener);
					stopSelf();
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
	
	@Override
	public void onDestroy() {
		super.onDestroy();
	}
}
