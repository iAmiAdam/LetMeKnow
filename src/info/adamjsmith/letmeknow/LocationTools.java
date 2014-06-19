package info.adamjsmith.letmeknow;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.Window;
import android.widget.TextView;

public class LocationTools extends Activity{
	LocationManager lm;
	LocationListener locationListener;
	String phoneNumber;
	String message;
	double lat;
	double longitude;
	
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
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 4, 0, locationListener);
	}
	
	@Override
	public void onPause() {
		super.onPause();
		
		//lm.removeUpdates(locationListener);
	}
	
	private class MyLocationListener implements LocationListener {
		double rem1 = 100;
		double rem2 = 100;
		String text;
		TextView latView = (TextView) findViewById(R.id.Lat);
		TextView longView = (TextView) findViewById(R.id.Long);
		TextView rem1View =  (TextView) findViewById(R.id.rem1);
		TextView rem2View = (TextView) findViewById(R.id.rem2);
		public void onLocationChanged(Location loc) {
			if (loc != null) {
				rem1 = lat - loc.getLatitude();
				rem2 = longitude - loc.getLongitude();
				text = Double.toString(loc.getLatitude());
				latView.setText(text);
				text = Double.toString(loc.getLongitude());
				longView.setText(text); 
				text = Double.toString(rem1);
				rem1View.setText(text);
				text = Double.toString(rem2);
				rem2View.setText(text);
				
				if(rem1 < 0.01 && rem1 > -0.01 && rem2 < 0.01 && rem2 > -0.01){
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
