package info.adamjsmith.letmeknow;

import android.app.ListActivity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.CursorAdapter;
import android.widget.SimpleCursorAdapter;

public class CurrentMessages extends ListActivity {
	SimpleCursorAdapter adapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getMessages();
		displayMessages();
	}
	
	private void displayMessages() {
		//setListAdapter(adapter);
		//getListView().setTextFilterEnabled(true);
	}
	
	public void getMessages() {
		DBAdapter db = new DBAdapter(this);
		db.open();
		Cursor c = db.getAllInstances();
		if (c.moveToFirst()) {
			do {
				String[] columns = new String[] {"_id", "name", "message"};
		        int[] views = new int[] {R.id.id, R.id.name, R.id.message};   
		        
		        adapter = new  MySimpleCursorAdapter(this, R.layout.messageslist, c, columns, views, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
		        	
		        this.setListAdapter(adapter);
			} while (c.moveToNext());
		} else {
			
		}
		db.close();
	}

	public class MySimpleCursorAdapter extends SimpleCursorAdapter {
		public MySimpleCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flagRegisterContentObserver) {
			super(context, layout, c, from, to, flagRegisterContentObserver);
		}	
	}
	
}