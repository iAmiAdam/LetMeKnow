package info.adamjsmith.letmeknow;

import android.app.ListActivity;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.view.View;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class SelectContact extends ListActivity {
	
	public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.contactlist);
	        
	        Uri allContacts = Uri.parse("content://contacts/people");
	        
	        Cursor c;
	        
	        if (android.os.Build.VERSION.SDK_INT < 11) {
	        	c = managedQuery(allContacts, null, null, null, ContactsContract.Contacts.DISPLAY_NAME + " ASC");
	        } else {
	        	CursorLoader cursorLoader = new CursorLoader(this, allContacts, null, null, null, ContactsContract.Contacts.DISPLAY_NAME + " ASC");
	        	c = cursorLoader.loadInBackground();
	        }
	                
	        String[] columns = new String[] {ContactsContract.Contacts.DISPLAY_NAME };
	        
	        int[] views = new int[] {R.id.contactName};        
	        
	        ImageView imageView = (ImageView) findViewById(R.id.contactPicture);
	        
	        if (ContactsContract.Contacts.Photo.PHOTO_ID != null) {
	        	Uri photoUri = Uri.withAppendedPath(Contacts.CONTENT_URI, ContactsContract.Contacts.Photo.PHOTO_ID);
	        	imageView.setImageURI(photoUri);
	        } else {
	        	imageView.setImageResource(R.drawable.app_icon);
	        }
	        
	        
	        SimpleCursorAdapter adapter;
	        
	        if (android.os.Build.VERSION.SDK_INT <11) {
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
		String name = tv.getText().toString();
	}
}
