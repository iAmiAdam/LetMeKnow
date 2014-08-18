package info.adamjsmith.letmeknow;

import java.util.UUID;

public class Contact {
	
	private UUID mId;
	private String[] mNumbers;
	private String mName;
	private String mPicture;
	
	public String getPicture() {
		return mPicture;
	}

	public void setPicture(String mPicture) {
		this.mPicture = mPicture;
	}

	public Contact() {
		this.mId = UUID.randomUUID();
	}
	
	public String getName() {
		return mName;
	}
	
	public void setName(String name) {
		this.mName = name;
	}
	
	public String[] getNumbers() {
		return mNumbers;
	}
	
	public void setNumbers(String[] numbers) {
		mNumbers = numbers;
	}

	public UUID getId() {
		return mId;
	}
	
}