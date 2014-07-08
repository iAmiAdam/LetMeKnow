package info.adamjsmith.letmeknow;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ListActivity;
import android.content.Context;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class CurrentMessages extends ListActivity {
	SimpleCursorAdapter adapter;
	DBAdapter db;
	ListView LV = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contactlist);
		db = new DBAdapter(this);
		db.open();
		getMessages();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		db.close();
	}
	
	@SuppressLint("InlinedApi") 
	public void getMessages() {
		Cursor c = db.getAllInstances();
		if (c.moveToFirst()) {
			do {
				String[] columns = new String[] {"_id", "name", "message"};
		        int[] views = new int[] {R.id.id, R.id.name, R.id.message};   
		        
		        if(android.os.Build.VERSION.SDK_INT < 11) {
		        	adapter = new oldCursorAdapter(this, R.layout.messageitem, c, columns, views);
		        } else {
		        	adapter = new  MySimpleCursorAdapter(this, R.layout.messageitem, c, columns, views, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
		        }	
		        this.setListAdapter(adapter);
			} while (c.moveToNext());
		} else {
			
		}
	}
	
	public class oldCursorAdapter extends SimpleCursorAdapter {

		@SuppressWarnings("deprecation")
		public oldCursorAdapter(Context context, int layout, Cursor c,
				String[] from, int[] to) {
			super(context, layout, c, from, to);
		}
		
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
					db.deleteInstance(Integer.parseInt(id));
					getMessages();
				}
			});
		}
		
	}

	public class MySimpleCursorAdapter extends SimpleCursorAdapter {
		@TargetApi(Build.VERSION_CODES.HONEYCOMB) public MySimpleCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flagRegisterContentObserver) {
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
					db.deleteInstance(Integer.parseInt(id));
					getMessages();
				}
			});
		}
	}
}