package info.adamjsmith.letmeknow;

import java.util.ArrayList;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.view.Window;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class SelectContact extends ListActivity {
	String selectedNumber;
	String name;
	Intent data = new Intent();
	@SuppressLint("InlinedApi") @SuppressWarnings("deprecation")
	public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
	        setContentView(R.layout.contactlist);
	        
	        Uri allContacts = ContactsContract.Contacts.CONTENT_URI;
	        
	        Cursor c;
	        
	        if (android.os.Build.VERSION.SDK_INT < 11) {
	        	c = managedQuery(allContacts, null, ContactsContract.Contacts.HAS_PHONE_NUMBER + " = 1", null, ContactsContract.Contacts.DISPLAY_NAME + " ASC");
	        } else {
	        	CursorLoader cursorLoader = new CursorLoader(this, allContacts, null, ContactsContract.Contacts.HAS_PHONE_NUMBER + " = 1", null, ContactsContract.Contacts.DISPLAY_NAME + " ASC");
	        	c = cursorLoader.loadInBackground();
	        }
	        String[] columns;
	        int[] views;
	        if(android.os.Build.VERSION.SDK_INT < 11){
	        	columns = new String[] {ContactsContract.Contacts.DISPLAY_NAME, ContactsContract.Contacts._ID};
	        	views = new int[] {R.id.contactName, R.id.id};   
	        } else {
	        	columns = new String[] {ContactsContract.CommonDataKinds.Photo.PHOTO_URI, ContactsContract.Contacts.DISPLAY_NAME, ContactsContract.Contacts._ID};
	        	views = new int[] {R.id.contactPicture, R.id.contactName, R.id.id};   
	        }	        
	        
	        SimpleCursorAdapter adapter;
	        
	        if(android.os.Build.VERSION.SDK_INT < 11) {
	        	adapter = new SimpleCursorAdapter(this, R.layout.contactlist, c, columns, views);
	        } else {
	        	adapter = new  MySimpleCursorAdapter(this, R.layout.contactlist, c, columns, views, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
	        }	
	        	
	        this.setListAdapter(adapter);
	        
	    }
	
	@Override
	protected void onListItemClick(ListView list, View view, int position, long id){
		super.onListItemClick(list, view, position, id);
		
		TextView tv = (TextView)view.findViewById(R.id.contactName);
		data.setData(Uri.parse(tv.getText().toString()));
		name = tv.getText().toString();
		
		tv = (TextView)view.findViewById(R.id.id);
		String contactId = tv.getText().toString();
		
		Cursor phoneCursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
		final ArrayList<String> phonesList = new ArrayList<String>();
		while (phoneCursor.moveToNext()) {
			String phone = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DATA));
			phonesList.add(phone);
		}
		phoneCursor.close();

		if (phonesList.size() == 1) {
			selectedNumber = phonesList.get(0);
			data.putExtra("number", selectedNumber);
			setResult(RESULT_OK, data);
			finish();
		} else {
			final String[] phonesArr = new String[phonesList.size()];
			for (int i = 0; i < phonesList.size(); i++) {
				phonesArr[i] = phonesList.get(i);
			}
			
			AlertDialog.Builder dialog = new AlertDialog.Builder(this);
			dialog.setTitle("Choose a number for " + name);
			dialog.setItems(phonesArr, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					selectedNumber = phonesArr[which];
					data.putExtra("number", selectedNumber);
					setResult(RESULT_OK, data);
					finish();
				}
			}).create();
			dialog.show();
		}

	}
	
	
	public class MySimpleCursorAdapter extends SimpleCursorAdapter {

		public MySimpleCursorAdapter(Context context, int layout, Cursor c,
				String[] from, int[] to, int flagRegisterContentObserver) {
			super(context, layout, c, from, to, flagRegisterContentObserver);
		}
		
		@Override
		public void setViewImage(ImageView iv, String text) {
			if (text == "") {
				iv.setImageResource(R.drawable.app_icon);
			} else {
				iv.setImageURI(Uri.parse(text));
			}
			
		}
		
	}
}
