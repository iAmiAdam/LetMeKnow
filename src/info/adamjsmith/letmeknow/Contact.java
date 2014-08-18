package info.adamjsmith.letmeknow;

public class Contact {
	
	private long mId;
	private Integer[] mNumbers;
	private String mName;
	
	public Contact(long id) {
		this.mId = id;
	}
	
	public String getName() {
		return mName;
	}
	
	public void setName(String name) {
		this.mName = name;
	}
	
	public Integer[] getNumbers() {
		return mNumbers;
	}
	
	public void setNumbers(Integer[] numbers) {
		mNumbers = numbers;
	}

	public long getId() {
		return mId;
	}
	
}