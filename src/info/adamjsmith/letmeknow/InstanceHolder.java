package info.adamjsmith.letmeknow;

import java.util.ArrayList;
import java.util.UUID;

import android.content.Context;

public class InstanceHolder {
	private static final String MESSAGESFILE = "messages.json";
	
	private ArrayList<Message> mMessages;
	private LetMeKnowJSONSerializer mSerializer;
	
	private static InstanceHolder sInstanceHolder;
	private Context mContext;
	
	private InstanceHolder(Context appContext) {
		this.mContext = appContext;
		this.mSerializer = new LetMeKnowJSONSerializer(mContext);
		
		try {
			mSerializer.setFile(MESSAGESFILE);
			mMessages = mSerializer.loadMessages();
		} catch (Exception e) {
			mMessages = new ArrayList<Message>();
		}
	}
	
	public static InstanceHolder get(Context c) {
		if (sInstanceHolder == null) {
			sInstanceHolder = new InstanceHolder(c);
		}
		return sInstanceHolder;
	}
	
	public ArrayList<Message> getMessages() {
		return mMessages;
	}
	
	public Message getMessage(UUID id) {
		for(Message m: mMessages) {
			if (m.getId().equals(id)) {
				return m;
			}
		}
		return null;
	}
	
	public void addMessage(Message m) {
		mMessages.add(m);
	}
	
	public boolean saveMessages() {
		try {
			mSerializer.setFile(MESSAGESFILE);
			mSerializer.saveMessages(mMessages);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
