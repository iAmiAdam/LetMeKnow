package info.adamjsmith.letmeknow;

import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

public class Instance {
	private static final String JSON_ID ="id";
	private static final String JSON_MESSAGE = "message";
	private static final String JSON_LOCATION = "location";
	private static final String JSON_CONTACT = "contact";
	
	private UUID mId;
	private Message mMessage;
	private Location mLocation;
	private Contact mContact;
	private String mNumber;
	
	public Instance() {
		this.mId = UUID.randomUUID();
	}
	
	public Instance(JSONObject json) throws JSONException {
		this.mId = UUID.fromString(json.getString(JSON_ID));
		this.mMessage = (Message)json.get(JSON_MESSAGE);
		this.mLocation = (Location)json.get(JSON_LOCATION);
		this.mContact = (Contact)json.get(JSON_CONTACT);
	}
	
	public JSONObject toJSON() throws JSONException {
		JSONObject json = new JSONObject();
		json.put(JSON_ID, mId);
		json.put(JSON_MESSAGE, mMessage);
		json.put(JSON_LOCATION, mLocation);
		json.put(JSON_CONTACT, mContact);
		return json;
	}
	
	public UUID getId() {
		return mId;
	}
	
	public void setMessage(Message m) {
		this.mMessage = m;
	}

	public Message getMessage() {
		return mMessage;
	}
	
	public void setLocation(Location l) {
		this.mLocation = l;
	}
	
	public Location getLocation() {
		return mLocation;
	}
	
	public void setContact(Contact c) {
		this.mContact = c;
	}
	
	public Contact getContact() {
		return mContact;
	}
	
	public void setNumber(String number) {
		this.mNumber = number;
	}
	
	public String getNumber() {
		return mNumber;
	}
}
