package info.adamjsmith.letmeknow;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.TextView;

public class LetMeKnowActivity extends Activity {
	String phoneNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
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
    	TextView textView = (TextView) findViewById(R.id.msgText);
    	String message = textView.getText().toString(); 
    	SmsManager sms = SmsManager.getDefault();
		sms.sendTextMessage(phoneNumber, null, message, null, null);
    }
    
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    	
    	switch (requestCode) {
    	case 1:
    		if (resultCode == RESULT_OK) {
    			TextView contact = (TextView)findViewById(R.id.chosenContact);
    			contact.setText(data.getData().toString());
    			//contact.setText(data.getData().toString());
    			phoneNumber = (data.getStringExtra("number"));
    		}
    		break;
    	case 2:
    		if (resultCode == RESULT_OK) {
    			TextView location = (TextView)findViewById(R.id.location);
    			location.setText(data.getData().toString());
    		}
    		break;
    	default:
    		break;
    	}
    }
}
