package info.adamjsmith.letmeknow;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import android.content.Context;

public class LetMeKnowJSONSerializer {
	private Context mContext;
	private String mFilename;
	
	public LetMeKnowJSONSerializer(Context c, String f) {
		this.mContext = c;
		this.mFilename = f;
	}
	
	public ArrayList<Message> loadMessages() throws IOException, JSONException {
		ArrayList<Message> messages = new ArrayList<Message>();
		BufferedReader reader = null;
		try {
			InputStream in = mContext.openFileInput(mFilename);
			reader = new BufferedReader(new InputStreamReader(in));
			StringBuilder jsonString = new StringBuilder();
			String line  = null;
			while ((line = reader.readLine()) != null) {
				jsonString.append(line);
			}
			
			JSONArray array = (JSONArray) new JSONTokener(jsonString.toString()).nextValue();
			for (int i = 0; i < array.length(); i++) {
				messages.add(new Message(array.getJSONObject(i)));
			}
		} catch (FileNotFoundException e) {
			
		} finally {
			if (reader != null)
				reader.close();
		}
		return messages;
	}
	
	public void saveMessage(ArrayList<Message> messages) throws IOException, JSONException {
		JSONArray array = new JSONArray();
		for (Message m : messages) 
			array.put(m.toJSON());
		
		Writer writer = null;
		try {
			OutputStream out = mContext.openFileOutput(mFilename, Context.MODE_PRIVATE); 
			writer = new OutputStreamWriter(out);
			writer.write(array.toString());
		} finally {
			if(writer != null)
				writer.close();
		}
	}
}
