package info.adamjsmith.letmeknow;

import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

public class Instance {
	private static final String JSON_ID ="id";
	private static final String JSON_MESSAGE = "message";
	
	private UUID mId;
	private Message mMessage;
	
	public Instance(Message m) {
		this.mId = UUID.randomUUID();
		this.mMessage = m;
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

	public Message getMessage() {
		return mMessage;
	}
}
