package info.adamjsmith.letmeknow;

import java.io.InputStream;
import java.util.ArrayList;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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

public class InstanceListFragment extends ListFragment {
	private ArrayList<Instance> mInstances;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		
		mInstances = InstanceHolder.get(getActivity()).getInstances();
		
		InstanceAdapter adapter = new InstanceAdapter(mInstances);
		setListAdapter(adapter);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		((InstanceAdapter)getListAdapter()).notifyDataSetChanged();
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		ListView listView = getListView();
		listView.setBackgroundColor(getResources().getColor(R.color.background));
		listView.setDividerHeight(0);
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.fragment_instance_list, menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_item_new_instance:
			Instance instance = new Instance();
			InstanceHolder.get(getActivity()).addInstance(instance);
			Intent i = new Intent(getActivity(), InstancePagerActivity.class);
			i.putExtra(InstanceFragment.EXTRA_INSTANCE_ID, instance.getId());
			startActivityForResult(i, 0);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	private class InstanceAdapter extends ArrayAdapter<Instance> {
		
		public InstanceAdapter(ArrayList<Instance> instances){
			super(getActivity(), 0, instances);
		}
		
		public View getView(int position, View convertView, ViewGroup parent) {
			if(convertView == null) {
				if(position % 2 == 0) {
					convertView = getActivity().getLayoutInflater()
							.inflate(R.layout.list_item_instance, null);
				} else {
					convertView = getActivity().getLayoutInflater()
							.inflate(R.layout.list_item_instance_right, null);
				}
			}
			
			Instance i = getItem(position);
			
			TextView message = (TextView) convertView.findViewById(R.id.instance_list_message_content);
			if (i.getMessage() != null) { 
				message.setText(InstanceHolder.get(getActivity()).getMessage(i.getMessage()).getText()); 
			} else { 
				message.setText("Assign a message..."); 
			}
			
			String getMapURL = "http://maps.googleapis.com/maps/api/staticmap?zoom=12&size=150x150&markers=size:mid|color:red|"; 
			
			if (i.getLocation() != null) {
				getMapURL += InstanceHolder.get(getActivity()).getLocation(i.getLocation()).getLatitude() + "," + InstanceHolder.get(getActivity()).getLocation(i.getLocation()).getLongitude(); 
			} else {
				getMapURL += LocationTools.getCurrentLocation(getActivity()).latitude + "," + LocationTools.getCurrentLocation(getActivity()).longitude;
			}
			
			new DownloadImageTask((ImageView) convertView.findViewById(R.id.instance_list_map))
            .execute(getMapURL);
			
			if(i.getContact() != null) {
				TextView contactName = (TextView) convertView.findViewById(R.id.instance_list_name);
				String name = InstanceHolder.get(getActivity()).getContact(i.getContact()).getName();
				contactName.setText(name);
				if (InstanceHolder.get(getActivity()).getContact(i.getContact()).getPicture() != null) {
					ImageView contactPicture = (ImageView) convertView.findViewById(R.id.instance_list_contact_picture);
					contactPicture.setImageURI(Uri.parse(InstanceHolder.get(getActivity()).getContact(i.getContact()).getPicture()));
				}
			}
			
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
	        Bitmap map = null;
	        try {
	            InputStream in = new java.net.URL(urldisplay).openStream();
	            map = BitmapFactory.decodeStream(in);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return map;
	    }

	    protected void onPostExecute(Bitmap result) {
	        bmImage.setImageBitmap(result);
	    }
	}
	
}
