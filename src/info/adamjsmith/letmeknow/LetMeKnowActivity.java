package info.adamjsmith.letmeknow;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

public class LetMeKnowActivity extends Activity {
	String phoneNumber;
	double lat;
	double longitude;
	ImageView contactTick;
	ImageView markerTick;
	NotificationManager nm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);
        contactTick = (ImageView) findViewById(R.id.contactTick);
    	markerTick = (ImageView) findViewById(R.id.markerTick);
    	nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    	PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	super.onCreateOptionsMenu(menu);
    	CreateMenu(menu);
    	return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	return MenuChoice(item);
    }
    
    public void onResume() {
    	super.onResume();
    }
    
    public void contactClick(View view) {
    	int request_Code = 1;
    	startActivityForResult(new Intent("info.adamjsmith.letmeknow.SelectContact"), request_Code);
    }
    
    public void mapClick(View view) {
    	int request_Code = 2;
    	startActivityForResult(new Intent("info.adamjsmith.letmeknow.MapActivity"), request_Code);
    }
    
    public void confirmClick(View view) {
    	int request_Code = 3;
    	TextView textView = (TextView) findViewById(R.id.msgText);
    	String message = textView.getText().toString(); 
    	Intent i =  new Intent("info.adamjsmith.letmeknow.LocationTools");
    	i.putExtra("message", message);
    	i.putExtra("number", phoneNumber);
    	i.putExtra("lat", lat);
    	i.putExtra("long", longitude);
    	startActivityForResult(i, request_Code);
    }
    
    public void resetClick(View view) {
    	TextView contact = (TextView) findViewById(R.id.chosenContact);
    	contact.setText("");
    	TextView location = (TextView) findViewById(R.id.location);
    	location.setText("");
    	TextView message = (TextView) findViewById(R.id.msgText);
    	message.setText("");
    	contactTick.setImageResource(R.drawable.tickgrey);
    	markerTick.setImageResource(R.drawable.tickgrey);
    }
    
    private void CreateMenu(Menu menu) {
    	MenuItem settings = menu.add(0, 0, 0, "Settings"); {
    	}
    }
    
    private boolean MenuChoice(MenuItem item) {
    	switch(item.getItemId()) {
    	case 0:
    		startActivity(new Intent("info.adamjsmith.letmeknow.Preferences"));
    		return true;
    	}
    	return false;
    }
    
    
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
    	
    	switch (requestCode) {
    	case 1:
    		if (resultCode == RESULT_OK) {
    			TextView contact = (TextView)findViewById(R.id.chosenContact);
    			contact.setText(data.getData().toString());
    			//contact.setText(data.getData().toString());
    			phoneNumber = (data.getStringExtra("number"));
    			contactTick.setImageResource(R.drawable.tickgreen);
    		}
    		break;
    	case 2:
    		if (resultCode == RESULT_OK) {
    			TextView location = (TextView)findViewById(R.id.location);
    			location.setText("Location Selected");
    			lat = data.getDoubleExtra("lat", 0);
    			longitude = data.getDoubleExtra("long", 0);
    			markerTick.setImageResource(R.drawable.tickgreen);
    		}
    		break;
    	case 3:
    		if (resultCode == RESULT_OK) {
    			final Intent i = new Intent();
    			PendingIntent pendingIntent = PendingIntent.getActivity(this,  0, i, PendingIntent.FLAG_UPDATE_CURRENT);
    			NotificationCompat.Builder notifBuilder = new NotificationCompat.Builder(this)
    			.setSmallIcon(R.drawable.app_icon)
    			.setContentTitle("Text Message Sent")
    			.setContentText("We've let them know you're safe")
    			.setAutoCancel(true)
    			.setContentIntent(pendingIntent);
    			nm.notify(1, notifBuilder.build());
    			resetClick(null);
    		}
    		break;
    	default:
    		break;
    	}
    }
}
