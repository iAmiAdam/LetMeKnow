package info.adamjsmith.letmeknow;

import java.util.UUID;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

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
		
		if(mLocation.getName() == null) {
			getActivity().setTitle("New Location");
		} else {
			getActivity().setTitle(mLocation.getName());
		}
		
		setHasOptionsMenu(true);

	}
	
	@Override
	public void onPause() {
		super.onPause();
		mMapView.onPause();
		InstanceHolder.get(getActivity()).saveLocations();
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
		
		mMap.setOnMapClickListener(new OnMapClickListener() {

			@Override
			public void onMapClick(LatLng point) {
				mMap.clear();
				mLocation.setLatitude(point.latitude);
				mLocation.setLongitude(point.longitude);
				mMap.addMarker(new MarkerOptions()
						.position(point));
				
			}
			
		});
		
		MapsInitializer.initialize(getActivity());
		
		LatLng location;
		
		if (mLocation.getLatitude() != null) {
			location = new LatLng(mLocation.getLatitude(), mLocation.getLongitude());
			mMap.addMarker(new MarkerOptions() 
				.position(new LatLng(mLocation.getLatitude(), mLocation.getLongitude())));
		} else {			
			location = LocationTools.getCurrentLocation(getActivity());
			mLocation.setLatitude(LocationTools.getCurrentLocation(getActivity()).latitude);
			mLocation.setLongitude(LocationTools.getCurrentLocation(getActivity()).longitude);
		}

		CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(location, 10);
		mMap.animateCamera(cameraUpdate);
		
		EditText name = (EditText) v.findViewById(R.id.location_name);
		name.setText(mLocation.getName());
		name.addTextChangedListener(new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				mLocation.setName(String.valueOf(s));
			}

			@Override
			public void afterTextChanged(Editable s) {}
			
			
		});
		
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
