package info.adamjsmith.letmeknow;

import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

public class Instance {
	private static final String JSON_ID ="id";
	private static final String JSON_MESSAGE = "message";
	
	private UUID mId;
	private Message mMessage;
	
	public Instance() {
		this.mId = UUID.randomUUID();
	}
	
	public Instance(JSONObject json) throws JSONException {
		this.mId = UUID.fromString(json.getString(JSON_ID));
		this.mMessage = (Message)json.get(JSON_MESSAGE);
	}
	
	public JSONObject toJSON() throws JSONException {
		JSONObject json = new JSONObject();
		json.put(JSON_ID, mId);
		json.put(JSON_MESSAGE, mMessage);
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
}
