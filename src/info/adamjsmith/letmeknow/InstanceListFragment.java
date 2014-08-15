package info.adamjsmith.letmeknow;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

public class InstanceListFragment extends ListFragment {
	private ArrayList<Instance> mInstances;
	
	private int mCounter;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		
		mInstances = InstanceHolder.get(getActivity()).getInstances();
		
		mCounter = 0;
		
		InstanceAdapter adapter = new InstanceAdapter(mInstances);
		setListAdapter(adapter);
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
	
	@Override
	public void onResume() {
		super.onResume();
		((InstanceAdapter)getListAdapter()).notifyDataSetChanged();
	}
	
	private class InstanceAdapter extends ArrayAdapter<Instance> {
		
		public InstanceAdapter(ArrayList<Instance> instances){
			super(getActivity(), 0, instances);
		}
		
		public View getView(int position, View convertView, ViewGroup parent) {
			if(convertView == null) {
				if(mCounter % 2 == 0) {
					convertView = getActivity().getLayoutInflater()
							.inflate(R.layout.list_item_instance, null);
				} else {
					convertView = getActivity().getLayoutInflater()
							.inflate(R.layout.list_item_instance_right, null);
				}
			}
			
			Instance i = getItem(position);
			
			TextView contactName = (TextView) convertView.findViewById(R.id.instance_list_contact_name);
			contactName.setText("Contact Name");
			
			TextView message = (TextView) convertView.findViewById(R.id.instance_list_message_content);
			if (i.getMessage() != null) { 
				message.setText(i.getMessage().getText()); 
			} else { 
				message.setText("Message Text"); 
			}
			
			MapFragment mapView;
			GoogleMap mMap;
			
			mMap = ((SupportMapFragment) getFragmentManager().findFragmentById(R.id.instance_list_map)).getMap();
			
			mMap.getUiSettings().setAllGesturesEnabled(false);
			mMap.getUiSettings().setZoomControlsEnabled(false);
			
			CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(52.941128 , -1.260106), 10);
			mMap.animateCamera(cameraUpdate);
			
			return convertView;
		}
	}
}
