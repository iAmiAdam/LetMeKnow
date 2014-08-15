package info.adamjsmith.letmeknow;

import java.util.UUID;

public class Location {
	
	private UUID mId;
	private String mName;
	private Double mLatitude;
	private Double mLongitude;
	
	public Location() {
		this.mId = UUID.randomUUID();
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
