package info.adamjsmith.letmeknow;

import android.telephony.SmsManager;

public class Messages {
	public boolean sendSMS(String phoneNumber,  String message){
		
		SmsManager sms = SmsManager.getDefault();
		sms.sendTextMessage(phoneNumber, null, message, null, null);
		return true;
	}
}