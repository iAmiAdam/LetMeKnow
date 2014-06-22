package info.adamjsmith.letmeknow;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

public class LetMeKnowActivity extends Activity {
	String phoneNumber;
	String name;
	double latitude;
	double longitude;
	ImageView contactTick;
	ImageView markerTick;
	NotificationManager nm;
	DBAdapter db;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);
        contactTick = (ImageView) findViewById(R.id.contactTick);
    	markerTick = (ImageView) findViewById(R.id.markerTick);
    	nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    	PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
    	db = new DBAdapter(this);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	super.onCreateOptionsMenu(menu);
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.options, menu);
    	return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
    	case R.id.settings:
    		startActivity(new Intent("info.adamjsmith.letmeknow.Preferences"));
    		return true;
    	}
    	return false;
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
    
    public void messagesClick(View view) {
    	startActivity(new Intent("info.adamjsmith.letmeknow.CurrentMessages"));
    }
    
    public void confirmClick(View view) {
    	TextView textView = (TextView) findViewById(R.id.msgText);
    	String message = textView.getText().toString(); 
    	db.open();
    	db.insertInstance(name, phoneNumber, message, String.valueOf(latitude), String.valueOf(longitude));
    	db.close();
    	Intent i =  new Intent(this, LocationTools.class);
    	startService(i);
    	resetClick(null);
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
    
    
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
    	
    	switch (requestCode) {
    	case 1:
    		if (resultCode == RESULT_OK) {
    			TextView contact = (TextView)findViewById(R.id.chosenContact);
    			contact.setText(data.getData().toString());
    			name = data.getData().toString();
    			phoneNumber = (data.getStringExtra("number"));
    			contactTick.setImageResource(R.drawable.tickgreen);
    		}
    		break;
    	case 2:
    		if (resultCode == RESULT_OK) {
    			TextView location = (TextView)findViewById(R.id.location);
    			location.setText("Location Selected");
    			latitude = data.getDoubleExtra("lat", 0);
    			longitude = data.getDoubleExtra("long", 0);
    			markerTick.setImageResource(R.drawable.tickgreen);
    		}
    		break;
    	default:
    		break;
    	}
    }
}
