package info.adamjsmith.letmeknow;

import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

public class Location {
	
	private static final String JSON_ID = "id";
	private static final String JSON_NAME = "name";
	private static final String JSON_LATITUDE = "latitude";
	private static final String JSON_LONGITUDE = "longitude";
	
	private UUID mId;
	private String mName;
	private Double mLatitude;
	private Double mLongitude;
	
	public Location() {
		this.mId = UUID.randomUUID();
	}
	
	public Location(JSONObject json) throws JSONException {
		this.mId = UUID.fromString(json.getString(JSON_ID));
		this.mName = json.getString(JSON_NAME);
		this.mLatitude = json.getDouble(JSON_LATITUDE);
		this.mLongitude = json.getDouble(JSON_LONGITUDE);
	}
	
	public JSONObject toJSON() throws JSONException {
		JSONObject json = new JSONObject();
		json.put(JSON_ID, this.mId);
		json.put(JSON_NAME, this.mName);
		json.put(JSON_LATITUDE, this.mLatitude);
		json.put(JSON_LONGITUDE, this.mLongitude);
		return json;
	}
	
	public String getName() {
		return mName;
	}
	public void setName(String mName) {
		this.mName = mName;
	}
	public Double getLatitude() {
		return mLatitude;
	}
	public void setLatitude(Double mLatitude) {
		this.mLatitude = mLatitude;
	}
	public Double getLongitude() {
		return mLongitude;
	}
	public void setLongitude(Double mLongitude) {
		this.mLongitude = mLongitude;
	}
	public UUID getId() {
		return mId;
	}
	
	

}
