package info.adamjsmith.letmeknow;

public class Contact {
	
	private long mId;
	private String[] mNumbers;
	private String mName;
	private String mPicture;
	
	public String getPicture() {
		return mPicture;
	}

	public void setPicture(String mPicture) {
		this.mPicture = mPicture;
	}

	public Contact(long id) {
		this.mId = id;
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

	public long getId() {
		return mId;
	}
	
}