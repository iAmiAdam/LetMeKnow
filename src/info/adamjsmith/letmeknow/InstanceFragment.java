package info.adamjsmith.letmeknow;

import java.util.UUID;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class InstanceFragment extends Fragment {
	public static final String EXTRA_INSTANCE_ID = "info.adamjsmith.letmeknow.instance_id";

	private Instance mInstance;
	private MapView mapView;
	private GoogleMap map;
	private EditText message;
	private Button selectMessage;
	private Button selectLocation;
	private Button selectContact;
	private CheckBox persistenceBox;
	
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
		InstanceHolder.get(getActivity()).saveInstances();
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
		
		selectMessage = (Button) v.findViewById(R.id.instance_message_button);
		selectMessage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivityForResult(new Intent("info.adamjsmith.letmeknow.MessageActivity"), 1);
			}
		});
		
		selectLocation = (Button) v.findViewById(R.id.instance_location_button);
		selectLocation.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivityForResult(new Intent("info.adamjsmith.letmeknow.LocationChoiceActivity"), 2);
			}
		});
		
		selectContact = (Button) v.findViewById(R.id.instance_contact_button);
		selectContact.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivityForResult(new Intent("info.adamjsmith.letmeknow.ContactListActivity"), 3);
			}
		});
		
		persistenceBox = (CheckBox) v.findViewById(R.id.instance_persistence);
		persistenceBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				mInstance.setPersist();
			}
		});
		
		
		return v;
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.fragment_instance, menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			if(NavUtils.getParentActivityName(getActivity()) != null) {
				NavUtils.navigateUpFromSameTask(getActivity());
			}
			return true;
		case R.id.menu_item_save:
			InstanceHolder.get(getActivity()).saveInstances();
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
		switch (requestCode) {
		case 1:
			UUID messageId = (UUID) data.getExtras().get(MessageFragment.EXTRA_MESSAGE_ID);
			Message lMessage = InstanceHolder.get(getActivity()).getMessage(messageId);
			message.setText(lMessage.getText());
			mInstance.setMessage(lMessage.getId());
			message.setEnabled(false);
			selectMessage.setVisibility(View.GONE);
			break;
		case 2:
			UUID locationId = (UUID) data.getExtras().get(LocationFragment.EXTRA_LOCATION_ID);
			Location lLocation = InstanceHolder.get(getActivity()).getLocation(locationId);
			mInstance.setLocation(lLocation.getId());
			selectLocation.setVisibility(View.GONE);
			CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(lLocation.getLatitude(), lLocation.getLongitude()), 14);
			map.animateCamera(cameraUpdate);
			map.addMarker(new MarkerOptions()
			.position(new LatLng(lLocation.getLatitude(), lLocation.getLongitude())));
			break;
		case 3:
			long contactId = data.getLongExtra("ID", 1);
			Contact lContact = InstanceHolder.get(getActivity()).getContact(contactId);
			mInstance.setContact(lContact.getId());
			mInstance.setNumber(data.getExtras().getString("number"));
			selectContact.setVisibility(View.GONE);
			break;
		default:
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
