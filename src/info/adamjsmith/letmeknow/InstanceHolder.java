package info.adamjsmith.letmeknow;

import java.util.ArrayList;
import java.util.UUID;

import android.content.Context;

public class InstanceHolder {
	private static final String INSTANCESFILE = "instances.json";
	private static final String MESSAGESFILE = "messages.json";
	private static final String LOCATIONSFILE = "locations.json";
	
	private ArrayList<Instance> mInstances;
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
		
		try {
			mSerializer.setFile(INSTANCESFILE);
			mInstances = mSerializer.loadInstances();
		} catch (Exception e) {
			mInstances = new ArrayList<Instance>();
		}
	}
	
	public static InstanceHolder get(Context c) {
		if (sInstanceHolder == null) {
			sInstanceHolder = new InstanceHolder(c);
		}
		return sInstanceHolder;
	}
	
	public ArrayList<Instance> getInstances() {
		return mInstances;
	}
	
	public Instance getInstance(UUID id) {
		for(Instance i: mInstances) {
			if (i.getId().equals(id)) {
				return i;
			}
		}
		return null;
	}
	
	public void addInstance(Instance i) {
		mInstances.add(i);
	}
	
	public boolean saveInstances() {
		try {
			mSerializer.setFile(INSTANCESFILE);
			mSerializer.saveInstances(mInstances);
			return true;
		} catch (Exception e) {
			return false;
		}
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
