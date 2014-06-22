package info.adamjsmith.letmeknow;

import java.util.ArrayList;

import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;

public class CurrentMessages extends ListActivity {
	private	ArrayList<String> results = new ArrayList<String>();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getMessages();
		displayMessages();
	}
	
	private void displayMessages() {
		setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, results));
		getListView().setTextFilterEnabled(true);
	}
	
	private void getMessages() {
		DBAdapter db = new DBAdapter(this);
		db.open();
		Cursor c = db.getAllInstances();
		if (c.moveToFirst()) {
			do {
				Integer id = c.getInt(c.getColumnIndex("ID"));
				String name = c.getString(c.getColumnIndex("name"));
				String message = c.getString(c.getColumnIndex("message"));
				results.add(name + message);
			} while (c.moveToNext());
		} else {
			
		}
		db.close();
	}
}
