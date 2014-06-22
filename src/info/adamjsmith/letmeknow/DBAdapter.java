package info.adamjsmith.letmeknow;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBAdapter {
	static final String KEY_ROWID = "_id";
	static final String KEY_NAME = "name";
	static final String KEY_NUMBER = "number";
	static final String KEY_MESSAGE = "message";
	static final String KEY_LAT = "latitude";
	static final String KEY_LONG = "longitude";
	static final String TAG = "DBAdapter";
	
	static final String DATABASE_NAME = "LetMeKnow";
	static final String DATABASE_TABLE = "instances";
	static final int DATABASE_VERSION = 6;
	
	static final String DATABASE_CREATE = 
			"create table instances (_id integer primary key autoincrement,"
			+ " name text not null, number text not null, message text not null,"
			+ " latitude text not null, longitude text not null)";
	
	final Context context;
	
	DatabaseHelper DBHelper;
	SQLiteDatabase db;
	
	public DBAdapter(Context ctx) {
		this.context = ctx;
		DBHelper = new DatabaseHelper(context);
	}
	
	private static class DatabaseHelper extends SQLiteOpenHelper {
		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}
		
		@Override
		public void onCreate(SQLiteDatabase db) {
			try {
				db.execSQL(DATABASE_CREATE);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
			db.execSQL("DROP TABLE IF EXISTS instances");
			onCreate(db);
		}
	}
	
	public DBAdapter open() throws SQLException {
		db = DBHelper.getWritableDatabase();
		return this;
	}
	
	public void close() {
		DBHelper.close();
	}
	
	public long insertInstance(String name, String number, String message, String latitude, String longitude) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_NAME, name);
		initialValues.put(KEY_NUMBER, number);
		initialValues.put(KEY_MESSAGE, message);
		initialValues.put(KEY_LAT, latitude);
		initialValues.put(KEY_LONG, longitude);
		return db.insert(DATABASE_TABLE, null, initialValues);
	}
	
	public boolean deleteInstance(long rowId) {
		return db.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
	}
	
	public Cursor getAllInstances() {
		return db.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_NAME, KEY_NUMBER, KEY_MESSAGE, KEY_LAT, KEY_LONG}, null, null, null, null, null);
	}
	
	public Cursor getInstance(long rowId) throws SQLException {
		Cursor mCursor = 
				db.query(true, DATABASE_TABLE, new String[] {KEY_ROWID, KEY_NAME, KEY_NUMBER, KEY_MESSAGE, KEY_LAT, KEY_LONG}, KEY_ROWID + "=" + rowId, null, null, null, null, null);
		if(mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}
}
