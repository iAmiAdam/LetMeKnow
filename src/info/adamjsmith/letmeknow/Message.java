package info.adamjsmith.letmeknow;

import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

public class Message {
	
	private static final String JSON_ID = "id";
	private static final String JSON_TEXT = "text";
	
	private UUID mId;
	private String mText;
	
	public Message(String message) {
		this.mId = UUID.randomUUID();
		this.mText = message;
	}
	
	public Message(JSONObject json) throws JSONException {
		mId = UUID.fromString(json.getString(JSON_ID));
		mText = json.getString(JSON_TEXT);		
	}
	
	public JSONObject toJSON() throws JSONException {
		JSONObject json = new JSONObject();
		json.put(JSON_ID, mId);
		json.put(JSON_TEXT, mText);
		return json;
	}

	public String getText() {
		return mText;
	}

	public void setText(String text) {
		this.mText = text;
	}

	public UUID getId() {
		return mId;
	}
}
