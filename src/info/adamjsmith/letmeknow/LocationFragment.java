package info.adamjsmith.letmeknow;

import java.util.UUID;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;

public class LocationFragment extends Fragment {
	public static final String EXTRA_LOCATION_ID = "info.adamjsmith.letmeknow.location_id";
	
	private Location mLocation;
	private MapView mMapView;
	private GoogleMap mMap;
	
	public LocationFragment(UUID locationId) {
		mLocation = InstanceHolder.get(getActivity()).getLocation(locationId);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		getActivity().setTitle("New Location");
		
		if(mLocation == null) {
			UUID locationId = (UUID)getArguments().getSerializable(EXTRA_LOCATION_ID);
			mLocation = InstanceHolder.get(getActivity()).getLocation(locationId);
		}
		
		setHasOptionsMenu(true);
	}
	
	@Override
	public void onPause() {
		super.onPause();
		mMapView.onPause();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		mMapView.onResume();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		mMapView.onDestroy();
	}
	
	@Override
	public void onLowMemory() {
		super.onLowMemory();
		mMapView.onLowMemory();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_location, parent, false);
		if(NavUtils.getParentActivityName(getActivity()) != null) {
			getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
		}
		
		mMapView = (MapView) v.findViewById(R.id.location_map);
		mMapView.onCreate(savedInstanceState);
		
		mMap = mMapView.getMap();
		mMap.getUiSettings().setMyLocationButtonEnabled(false);
		
		MapsInitializer.initialize(getActivity());
		
		CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(52.941128, -1.260106), 10);
		mMap.animateCamera(cameraUpdate);
		
		return v;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			if(NavUtils.getParentActivityName(getActivity()) != null) {
				NavUtils.navigateUpFromSameTask(getActivity());
			}
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	public static Fragment newInstance(UUID locationId) {
		Bundle args = new Bundle();
		args.putSerializable(EXTRA_LOCATION_ID, locationId);
		
		Fragment fragment = new InstanceFragment();
		fragment.setArguments(args);
		
		return fragment;
	}

}
