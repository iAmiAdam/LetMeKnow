package info.adamjsmith.letmeknow;

import android.app.ListActivity;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class SelectContact extends ListActivity {
	
	public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.contactlist);
	        
	        Uri allContacts = ContactsContract.Contacts.CONTENT_URI;
	        
	        Cursor c;
	        
	        if (android.os.Build.VERSION.SDK_INT < 11) {
	        	c = managedQuery(allContacts, null, ContactsContract.Contacts.HAS_PHONE_NUMBER + " = 1", null, ContactsContract.Contacts.DISPLAY_NAME + " ASC");
	        } else {
	        	CursorLoader cursorLoader = new CursorLoader(this, allContacts, null, ContactsContract.Contacts.HAS_PHONE_NUMBER + " = 1", null, ContactsContract.Contacts.DISPLAY_NAME + " ASC");
	        	c = cursorLoader.loadInBackground();
	        }
	        
	        String[] columns = new String[] {ContactsContract.CommonDataKinds.Photo.PHOTO_URI, ContactsContract.Contacts.DISPLAY_NAME, ContactsContract.Contacts._ID};
	        int[] views = new int[] {R.id.contactPicture, R.id.contactName, R.id.id};   
	        
	        
	        SimpleCursorAdapter adapter;
	        
	        if (android.os.Build.VERSION.SDK_INT < 11) {
	        	adapter = new SimpleCursorAdapter(this, R.layout.contactlist, c, columns, views);
	        } else {
	        	adapter = new SimpleCursorAdapter(this, R.layout.contactlist, c, columns, views, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
	        }
	        this.setListAdapter(adapter);
	        
	    }
	
	@Override
	protected void onListItemClick(ListView list, View view, int position, long id){
		super.onListItemClick(list, view, position, id);
		
		TextView tv = (TextView)view.findViewById(R.id.contactName);
		Intent data = new Intent();
		data.setData(Uri.parse(tv.getText().toString()));
		
		tv = (TextView)view.findViewById(R.id.id);
		String contactId = tv.getText().toString();
		
		Cursor phoneCursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
		String number = "test";
		while (phoneCursor.moveToNext()) {
			number = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
		}
		phoneCursor.close();
		
		data.putExtra("number", number);
		setResult(RESULT_OK, data);
		finish();
	}
}
