package info.adamjsmith.letmeknow;

import java.io.IOException;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class LetMeKnowActivity extends Activity {
	String phoneNumber;
	String name;
	String message;
	Double latitude;
	Double longitude;
	ImageView contactTick;
	ImageView markerTick;
	EditText postCode;
	TextView contact;
	TextView location;
	TextView messageView;
	DBAdapter db;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);
        contactTick = (ImageView) findViewById(R.id.contactTick);
    	markerTick = (ImageView) findViewById(R.id.markerTick);
    	postCode = (EditText) findViewById(R.id.postCode);
    	contact = (TextView) findViewById(R.id.chosenContact);
    	location = (TextView) findViewById(R.id.location);
    	messageView = (TextView) findViewById(R.id.msgText);
    	PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
    	db = new DBAdapter(this);
    	
    	postCode.addTextChangedListener(new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				if (s.length() > 5) {
					if (getLongLat(s.toString())) {
						markerTick.setImageResource(R.drawable.tickfill);
						location.setText("Location Selected");
					} else {
						displayToast("Post Code not found");
						markerTick.setImageResource(R.drawable.tick);
						location.setText("");
					}
				} else {
					markerTick.setImageResource(R.drawable.tick);
					location.setText("");
				}
			}
    		
    	});
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
    	message = messageView.getText().toString(); 
    	if (longitude == null || latitude == null) {
    		getLongLat(postCode.getText().toString());
    	}
    	if (validate() == true) {
	    	db.open();
	    	db.insertInstance(name, phoneNumber, message, String.valueOf(latitude), String.valueOf(longitude));
	    	db.close();
	    	Toast.makeText(this, "Message saved", Toast.LENGTH_LONG).show();
	    	Intent i =  new Intent(this, LocationTools.class);
	    	startService(i);
	    	resetClick(null);
    	}
    }
    
    public void resetClick(View view) {
    	contact.setText("");
    	location.setText("");
    	TextView message = (TextView) findViewById(R.id.msgText);
    	message.setText("");
    	postCode.setText("");
    	postCode.setHint("Enter a post code");
    	contactTick.setImageResource(R.drawable.tick);
    	markerTick.setImageResource(R.drawable.tick);
    }
    
    public void messageSave(View view) {
    	message = messageView.getText().toString();
    	if (message != null) {
    		db.open();
    		db.insertMessage(message);
    		db.close();
    	}
    }    
    
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
    	
    	switch (requestCode) {
    	case 1:
    		if (resultCode == RESULT_OK) {
    			contact.setText(data.getData().toString());
    			name = data.getData().toString();
    			phoneNumber = (data.getStringExtra("number"));
    			contactTick.setImageResource(R.drawable.tickfill);
    		}
    		break;
    	case 2:
    		if (resultCode == RESULT_OK) {
    			location.setText("Location Selected");
    			latitude = data.getDoubleExtra("lat", 0);
    			longitude = data.getDoubleExtra("long", 0);
    			markerTick.setImageResource(R.drawable.tickfill);
    		}
    		break;
    	default:
    		break;
    	}
    }
	
	public boolean getLongLat(String input) {
		final Geocoder geocoder = new Geocoder(this);
		try {
			List<Address> addresses = geocoder.getFromLocationName(input,1);
			if (addresses != null && !addresses.isEmpty()) {
				Address address = addresses.get(0);
				latitude = address.getLatitude();
				longitude = address.getLongitude();
				if (latitude != null && longitude != null) {
					return true;
				} else {
					return false;
				}
				
			} else {
				return false;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
		
	}
	
	public boolean validate() {
		if (name == null || phoneNumber == null || message == null) {
			displayToast("Contact information missing");
			return false;
		} else if(longitude == null || latitude == null)  {
			displayToast("Location information missing");
			return false;
		} else {
			return true;
		}
	}
	
	public void displayToast(String text) {
		Toast.makeText(this, text, Toast.LENGTH_LONG).show();
	}
}
