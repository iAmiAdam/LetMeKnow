package info.adamjsmith.letmeknow;

import java.util.ArrayList;
import java.util.UUID;

import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;

public class InstanceHolder {
	private static final String INSTANCESFILE = "instances.json";
	private static final String MESSAGESFILE = "messages.json";
	private static final String LOCATIONSFILE = "locations.json";
	
	private ArrayList<Instance> mInstances;
	private ArrayList<Message> mMessages;
	private ArrayList<Location> mLocations;
	private ArrayList<Contact> mContacts;
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
		
		try {
			mSerializer.setFile(LOCATIONSFILE);
			mLocations = mSerializer.loadLocations();
		} catch (Exception e) {
			mLocations = new ArrayList<Location>();
		}
		
		mContacts = loadContacts();
		
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
	
	public void deleteInstance(Instance i) {
		mInstances.remove(i);
	}
	
	public boolean saveInstances() {
		try {
			mSerializer.setFile(INSTANCESFILE);
			mSerializer.saveInstances(mInstances);
			for (Instance i: mInstances) {
				if (i.getContact() != null && i.getLocation() != null && i.getMessage() != null) {
					LocationTools.addAlert(mContext, getLocation(i.getLocation()).getLatitude(), getLocation(i.getLocation()).getLongitude(), i.getNumber(), i.getId());
					Log.d("Alert", "set");
				}
			}
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
	
	public void deleteMessage(Message m) {
		mMessages.remove(m);
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
	
	public ArrayList<Location> getLocations() {
		return mLocations;
	}
	
	public Location getLocation(UUID id) {
		for(Location l: mLocations) {
			if (l.getId().equals(id)) {
				return l;
			}
		}
		return null;
	}
	
	public void addLocation(Location l) {
		mLocations.add(l);
	}
	
	public void deleteLocation(Location l) {
		mLocations.remove(l);
	}
	
	public boolean saveLocations() {
		try {
			mSerializer.setFile(LOCATIONSFILE);
			mSerializer.saveLocations(mLocations);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public ArrayList<Contact> getContacts() {
		return mContacts;
	}
	
	public Contact getContact(long id) {
		for(Contact c: mContacts) {
			if (c.getId() == id) {
				return c;
			}
		}
		return null;
	}
	
	public ArrayList<Contact> loadContacts() {
		ArrayList<Contact> contacts = new ArrayList<Contact>();
		
		Uri allContacts = ContactsContract.Contacts.CONTENT_URI;
		Cursor c;
		CursorLoader contactLoader = new CursorLoader(mContext, allContacts, null, ContactsContract.Contacts.HAS_PHONE_NUMBER + " = 1", null, ContactsContract.Contacts.DISPLAY_NAME + " ASC");
    	c = contactLoader.loadInBackground();
    	
    	while(c.moveToNext()) {
    		
    		Contact contact = new Contact(c.getLong(c.getColumnIndex("_ID")));
    		contact.setName(c.getString(c.getColumnIndex("DISPLAY_NAME")));
    		
    		Cursor p = mContext.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, 
    				ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + c.getLong(c.getColumnIndex("_ID")), null, null);
    		String[] numbers = new String[p.getCount()];
    		Integer i = 0;
    		
    		while(p.moveToNext()) {
    			numbers[i] = p.getString(p.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DATA));
    			i++;
    		}
    		p.close();
    		
    		contact.setNumbers(numbers);
    		
    		contact.setPicture(c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Photo.PHOTO_URI)));
    		
    		contacts.add(contact);
    	}
    	
    	c.close();
    	
    	return contacts;
	}
}
