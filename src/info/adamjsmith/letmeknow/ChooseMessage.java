package info.adamjsmith.letmeknow;

import info.adamjsmith.letmeknow.R.color;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class ChooseMessage extends ListActivity{
	SimpleCursorAdapter adapter;
	DBAdapter db;
	ListView LV = null;
	Intent data = new Intent();
	String message;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		db = new DBAdapter(this);
		db.open();
		getMessages();
		LV = getListView();
		LV.setBackgroundColor(color.blue);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		db.close();
	}
	
	@SuppressLint("InlinedApi") 
	public void getMessages() {
		Cursor c = db.getAllMessages();
		if (c.moveToFirst()) {
			do {
				String[] columns = new String[] {"_id", "message"};
		        int[] views = new int[] {R.id.id, R.id.message};   
		        
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
			String message = cursor.getString(1);
			
			TextView idView = (TextView) view.findViewById(R.id.id);
			idView.setText(id);
			
			TextView messageView = (TextView) view.findViewById(R.id.message);
			messageView.setText(message);
			
			Button delete = (Button) view.findViewById(R.id.delete);
			delete.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					db.deleteMessage(Integer.parseInt(id));
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
			String message = cursor.getString(1);
			
			TextView idView = (TextView) view.findViewById(R.id.id);
			idView.setText(id);
			
			TextView messageView = (TextView) view.findViewById(R.id.message);
			messageView.setText(message);
			
			Button delete = (Button) view.findViewById(R.id.delete);
			delete.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					db.deleteMessage(Integer.parseInt(id));
					getMessages();
				}
			});
		}
	}
	
	@Override
	protected void onListItemClick(ListView list, View view, int position, long id) {
		super.onListItemClick(list, view, position, id);
		
		TextView tv = (TextView)view.findViewById(R.id.contactName);
		message = tv.getText().toString();
		
		data.putExtra("message", message);
		setResult(RESULT_OK, data);
		finish();
	}
}
