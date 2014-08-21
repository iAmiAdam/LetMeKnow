package info.adamjsmith.letmeknow;

import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

public class Instance {
	private static final String JSON_ID ="id";
	private static final String JSON_MESSAGE = "message";
	private static final String JSON_LOCATION = "location";
	private static final String JSON_CONTACT = "contact";
	private static final String JSON_NUMBER = "number";
	private static final String JSON_ALERT = "alert";
	
	private UUID mId;
	private UUID mMessage;
	private UUID mLocation;
	private long mContact;
	private String mNumber;
	private Boolean mAlert = false;
	
	public Instance() {
		this.mId = UUID.randomUUID();
	}
	
	public Instance(JSONObject json) throws JSONException {
		this.mId = UUID.fromString(json.getString(JSON_ID));
		this.mMessage = UUID.fromString(json.getString(JSON_MESSAGE));
		this.mLocation = UUID.fromString(json.getString(JSON_LOCATION));
		this.mContact = json.getLong(JSON_CONTACT);
		this.mNumber = json.getString(JSON_NUMBER);
		this.mAlert = json.getBoolean(JSON_ALERT);
	}
	
	public JSONObject toJSON() throws JSONException {
		JSONObject json = new JSONObject();
		json.put(JSON_ID, mId);
		json.put(JSON_MESSAGE, mMessage);
		json.put(JSON_LOCATION, mLocation);
		json.put(JSON_CONTACT, mContact);
		json.put(JSON_NUMBER, mNumber);
		json.put(JSON_ALERT, mAlert);
		return json;
	}
	
	public UUID getId() {
		return mId;
	}
	
	public void setMessage(UUID m) {
		this.mMessage = m;
	}

	public UUID getMessage() {
		return mMessage;
	}
	
	public void setLocation(UUID l) {
		this.mLocation = l;
	}
	
	public UUID getLocation() {
		return mLocation;
	}
	
	public void setContact(long c) {
		this.mContact = c;
	}
	
	public Long getContact() {
		return mContact;
	}
	
	public void setNumber(String number) {
		this.mNumber = number;
	}
	
	public String getNumber() {
		return mNumber;
	}
	
	public void setAlert() {
		if (mAlert == false) {
			mAlert = true;
		} else {
			mAlert = false;
		}
	}
}
