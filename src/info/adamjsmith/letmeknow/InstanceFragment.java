package info.adamjsmith.letmeknow;

import java.util.UUID;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;

public class InstanceFragment extends Fragment {
	public static final String EXTRA_INSTANCE_ID = "info.adamjsmith.letmeknow.instance_id";

	private Instance mInstance;
	private MapView mapView;
	private GoogleMap map;
	private EditText message;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		UUID instanceId = (UUID)getArguments().getSerializable(EXTRA_INSTANCE_ID);
		mInstance = InstanceHolder.get(getActivity()).getInstance(instanceId);
		
		setHasOptionsMenu(true);
	}
	
	@Override
	public void onPause() {
		super.onPause();
		if (mInstance.getMessage() != null) {
			InstanceHolder.get(getActivity()).saveInstances();
		}
		mapView.onPause();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		mapView.onResume();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		mapView.onDestroy();
	}
	
	@Override
	public void onLowMemory() {
		super.onLowMemory();
		mapView.onLowMemory();
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_instance, parent, false);
		if(NavUtils.getParentActivityName(getActivity()) != null) {
			getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
		}
		
		mapView = (MapView) v.findViewById(R.id.instance_map);
		mapView.onCreate(savedInstanceState);
		
		map = mapView.getMap();
		map.getUiSettings().setMyLocationButtonEnabled(false);
		
		MapsInitializer.initialize(getActivity());
		
		CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(52.941128, -1.260106), 10);
		map.animateCamera(cameraUpdate);
		
		message = (EditText) v.findViewById(R.id.instance_message_input);
		
		Button selectMessage = (Button) v.findViewById(R.id.instance_message_button);
		selectMessage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivityForResult(new Intent("info.adamjsmith.letmeknow.MessageActivity"), 1);
			}
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
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 1) {
			UUID messageId = (UUID) data.getExtras().get(MessageFragment.EXTRA_MESSAGE_ID);
			Message lMessage = InstanceHolder.get(getActivity()).getMessage(messageId);
			message.setText(lMessage.getText());
			mInstance.setMessage(lMessage);
		} else {
			super.onActivityResult(requestCode, resultCode, data);
		}
	}
	
	public static Fragment newInstance(UUID instanceId) {
		Bundle args = new Bundle();
		args.putSerializable(EXTRA_INSTANCE_ID, instanceId);
		
		Fragment fragment = new InstanceFragment();
		fragment.setArguments(args);
		
		return fragment;
	}
	
	public void selectContact() {
		startActivityForResult(new Intent(), 0);
	}
	
}
