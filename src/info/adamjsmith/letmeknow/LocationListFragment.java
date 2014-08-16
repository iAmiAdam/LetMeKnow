package info.adamjsmith.letmeknow;

import java.io.InputStream;
import java.util.ArrayList;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
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
	
	@Override
	public void onResume() {
		super.onResume();
		if(getListAdapter() != null) {
			((LocationAdapter)getListAdapter()).notifyDataSetChanged();
		}
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		ListView listView = getListView();
		listView.setBackgroundColor(getResources().getColor(R.color.background));
		listView.setDividerHeight(0);
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		Location loc = ((LocationAdapter)getListAdapter()).getItem(position);

		Intent i = new Intent(getActivity(), LocationActivity.class);
		i.putExtra(LocationFragment.EXTRA_LOCATION_ID, loc.getId());
		startActivity(i);
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.fragment_message_list, menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_item_new_message:
			Location location = new Location();
			InstanceHolder.get(getActivity()).addLocation(location);
			Intent i = new Intent(getActivity(), LocationActivity.class);
			i.putExtra(LocationFragment.EXTRA_LOCATION_ID, location.getId());
			startActivityForResult(i, 0);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	class LocationAdapter extends ArrayAdapter<Location> {
		
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
			
			String getMapURL = "http://maps.googleapis.com/maps/api/staticmap?zoom=14&size=500x250&markers=size:mid|color:red|"; 
			getMapURL += l.getLatitude() + "," + l.getLongitude();
			
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
