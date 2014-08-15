package info.adamjsmith.letmeknow;

import java.io.InputStream;
import java.util.ArrayList;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class LocationListFragment extends ListFragment {
	private ArrayList<Location> mLocations;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		
		mLocations = InstanceHolder.get(getActivity()).getLocations();		
		
		LocationAdapter adapter = new LocationAdapter(mLocations);
		setListAdapter(adapter);
	}
	
	private class LocationAdapter extends ArrayAdapter<Location> {
		
		public LocationAdapter(ArrayList<Location> locations) {
			super(getActivity(), 0, locations);
		}
		
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = getActivity().getLayoutInflater()
						.inflate(R.layout.list_item_location, null);
			}
			
			Location l = getItem(position);
			
			TextView name = (TextView) convertView.findViewById(R.id.location_list_name);
			name.setText(l.getName());
			
			String getMapURL = "http://maps.googleapis.com/maps/api/staticmap?zoom=14&size=500x250&markers=size:mid|color:red|52.941128,-1.260106"; 
			
			new DownloadImageTask((ImageView) convertView.findViewById(R.id.location_list_map))
            .execute(getMapURL);
			
			return convertView;
		}
	}
	
	private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
	    ImageView bmImage;

	    public DownloadImageTask(ImageView bmImage) {
	        this.bmImage = bmImage;
	    }

	    protected Bitmap doInBackground(String... urls) {
	        String urldisplay = urls[0];
	        Bitmap mIcon11 = null;
	        try {
	            InputStream in = new java.net.URL(urldisplay).openStream();
	            mIcon11 = BitmapFactory.decodeStream(in);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return mIcon11;
	    }

	    protected void onPostExecute(Bitmap result) {
	        bmImage.setImageBitmap(result);
	    }
	}
}
