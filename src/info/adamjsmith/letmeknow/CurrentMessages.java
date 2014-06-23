package info.adamjsmith.letmeknow;

import android.app.ListActivity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class CurrentMessages extends ListActivity {
	SimpleCursorAdapter adapter;
	DBAdapter db;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getMessages();
		db = new DBAdapter(this);
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
		
		@Override
		public void bindView(View view, Context context, Cursor cursor) {
			final String id = String.valueOf(cursor.getString(0));
			String name = cursor.getString(1);
			String message = cursor.getString(3);
			
			TextView nameView = (TextView) view.findViewById(R.id.name);
			nameView.setText(name);
			
			TextView idView = (TextView) view.findViewById(R.id.id);
			idView.setText(id);
			
			TextView messageView = (TextView) view.findViewById(R.id.message);
			messageView.setText(message);
			
			Button delete = (Button) view.findViewById(R.id.delete);
			delete.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					db.open();
					db.deleteInstance(Integer.parseInt(id));
					db.close();	
					getMessages();
				}
			});
		}
	}
}