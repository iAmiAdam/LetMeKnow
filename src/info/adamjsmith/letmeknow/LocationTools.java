package info.adamjsmith.letmeknow;

import android.app.PendingIntent;
import android.app.Service;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
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
	DBAdapter db;
	
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
	
	public int onStartCommand(Intent intent, int flags, int startId) {
		
		phoneNumber = intent.getStringExtra("number");
		message = intent.getStringExtra("message");
		lat = intent.getDoubleExtra("lat", 0);
		longitude = intent.getDoubleExtra("long", 0);
		db = new DBAdapter(this);
		
		
		lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		locationListener = new MyLocationListener();
		
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
		updateInterval = Integer.parseInt(sharedPref.getString("interval", "60000"));
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

		@SuppressWarnings("resource")
		public void onLocationChanged(Location loc) {
			if (loc != null) {
				latRemainder = lat - loc.getLatitude();
				longRemainder = longitude - loc.getLongitude();
							
				db.open();
				Cursor c = db.getAllInstances();
				if (c.moveToFirst()) {
					do {
						Integer id = c.getInt(0);
						lat = Double.parseDouble(c.getString(c.getColumnIndex("latitude")));
						longitude = Double.parseDouble(c.getString(c.getColumnIndex("longitude")));
						
						if(latRemainder < distance && latRemainder > -distance && longRemainder < distance && longRemainder > -distance){
							c = db.getInstance(id);
							phoneNumber = c.getString(c.getColumnIndex("number"));
							message = c.getString(c.getColumnIndex("message"));
							
							SmsManager sms = SmsManager.getDefault();
							sms.sendTextMessage(phoneNumber, null, message, null, null);
							
							pushNotification();
							db.deleteInstance(id);
							
							if (db.getAllInstances() == null) {
								stopSelf();
								lm.removeUpdates(locationListener);
							}
							
							c.close();
							db.close();
						}
					} while (c.moveToNext());
				} else {
					
				}
				db.close();
			}
		}
		
		public void onProviderDisabled(String provider) {
		}
		
		public void onProviderEnabled(String provider) {
		}
		
		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
	}
	
	public void pushNotification() {
		final Intent i = new Intent();
		PendingIntent pendingIntent = PendingIntent.getActivity(this,  0, i, PendingIntent.FLAG_UPDATE_CURRENT);
		NotificationCompat.Builder notifBuilder = new NotificationCompat.Builder(this)
		.setSmallIcon(R.drawable.app_icon)
		.setContentTitle("Text Message Sent")
		.setContentText("We've let them know you're safe")
		.setAutoCancel(true)
		.setContentIntent(pendingIntent);
		nm.notify(1, notifBuilder.build());
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
	}
}
