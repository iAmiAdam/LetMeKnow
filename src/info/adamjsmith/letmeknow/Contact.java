package info.adamjsmith.letmeknow;

public class Contact {
	
	private Integer mId;
	private Integer[] mNumbers;
	private String mName;
	
	public void Contact(Integer numbers) {
		mNumbers = new Integer[numbers];
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
	
	public void addNumber(Integer number) {
		mNumbers[mNumbers.length + 1] = number;
	}

	public Integer getId() {
		return mId;
	}

	public void setId(Integer id) {
		this.mId = id;
	}
	
}