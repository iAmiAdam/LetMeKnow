package info.adamjsmith.letmeknow;

import android.app.ListActivity;
import android.content.CursorLoader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.CursorAdapter;
import android.widget.SimpleCursorAdapter;

public class SelectContact extends ListActivity {
	
	public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.contactlist);
	        
	        Uri allContacts = Uri.parse("content://contacts/people");
	        
	        Cursor c;
	        
	        if (android.os.Build.VERSION.SDK_INT < 11) {
	        	c = managedQuery(allContacts, null, null, null, null);
	        } else {
	        	CursorLoader cursorLoader = new CursorLoader(this, allContacts, null, null, null, null);
	        	c = cursorLoader.loadInBackground();
	        }
	                
	        String[] columns = new String[] {ContactsContract.Contacts.DISPLAY_NAME};
	        
	        int[] views = new int[] {R.id.contactName};
	        
	        SimpleCursorAdapter adapter;
	        
	        if (android.os.Build.VERSION.SDK_INT <11) {
	        	adapter = new SimpleCursorAdapter(this, R.layout.contactlist, c, columns, views);
	        } else {
	        	adapter = new SimpleCursorAdapter(this, R.layout.contactlist, c, columns, views, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
	        }
	        this.setListAdapter(adapter);
	    }
}
