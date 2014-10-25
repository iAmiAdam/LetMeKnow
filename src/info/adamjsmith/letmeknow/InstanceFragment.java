package info.adamjsmith.letmeknow;

import java.util.UUID;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.TextWatcher;
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
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
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
	private TextView contactName;
	private Switch activeSwitch;
	private Message mMessage;
	private Location mLocation;	
	private Contact mContact;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//Fetch the instance id from the Extra that it's sent.	
		UUID instanceId = (UUID)getArguments().getSerializable(EXTRA_INSTANCE_ID);
		mInstance = InstanceHolder.get(getActivity()).getInstance(instanceId);	
		
		setHasOptionsMenu(true);
	}
	
	@Override
	public void onPause() {
		super.onPause();
		InstanceHolder.get(getActivity()).saveInstances();
		InstanceHolder.get(getActivity()).saveLocations();
		InstanceHolder.get(getActivity()).saveMessages();
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
		
		Double latitude;
		Double longitude;
		
		if(mInstance.getLocation() != null) {
			mLocation = InstanceHolder.get(getActivity()).getLocation(mInstance.getLocation());
			latitude = mLocation.getLatitude();
			longitude = mLocation.getLongitude();
		} else {
			latitude = LocationTools.getCurrentLocation(getActivity()).latitude;
			longitude = LocationTools.getCurrentLocation(getActivity()).longitude;
		}
		
		CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 10);
		map.animateCamera(cameraUpdate);
		
		map.setOnMapClickListener(new OnMapClickListener() {

			@Override
			public void onMapClick(LatLng point) {
				map.clear();
				Location mLocation = new Location();
				mLocation.setLatitude(point.latitude);
				mLocation.setLongitude(point.longitude);
				mLocation.setName("New Location");
				InstanceHolder.get(getActivity()).addLocation(mLocation);
				mInstance.setLocation(mLocation.getId());
				map.addMarker(new MarkerOptions()
						.position(point));
			}
		});
		
		contactName = (TextView) v.findViewById(R.id.instance_contact_name);
		
		if(mInstance.getContact() != null) {
			mContact = InstanceHolder.get(getActivity()).getContact(mInstance.getContact());
			contactName.setText("Sending message to " + mContact.getName());
		} else {
			contactName.setVisibility(View.GONE);
		}
		
		message = (EditText) v.findViewById(R.id.instance_message_input);
		
		if (mInstance.getMessage() != null) {
			mMessage = InstanceHolder.get(getActivity()).getMessage(mInstance.getMessage());
			message.setText(mMessage.getText());
		} else {
			mMessage = new Message();
		}
		
		message.addTextChangedListener(new TextWatcher() {
			public void afterTextChanged(Editable s) {
				mMessage.setText(String.valueOf(s));
				mInstance.setMessage(mMessage.getId());
				InstanceHolder.get(getActivity()).addMessage(mMessage);
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {}
		});
		
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
		
		persistenceBox.setChecked(mInstance.persist());
		
		persistenceBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				mInstance.setPersist();
			}
		});
		
		activeSwitch = (Switch) v.findViewById(R.id.instance_active);
		
		activeSwitch.setActivated(mInstance.state());
		
		activeSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				mInstance.setState();				
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
			if(resultCode == Activity.RESULT_OK) {
				UUID messageId = (UUID) data.getExtras().get(MessageFragment.EXTRA_MESSAGE_ID);
				Message lMessage = InstanceHolder.get(getActivity()).getMessage(messageId);
				message.setText(lMessage.getText());
				mInstance.setMessage(lMessage.getId());
				message.setEnabled(false);
				selectMessage.setVisibility(View.GONE);
				break;
			} else {
				break;
			}
		case 2:
			if(resultCode == Activity.RESULT_OK) {
				UUID locationId = (UUID) data.getExtras().get(LocationFragment.EXTRA_LOCATION_ID);
				Location lLocation = InstanceHolder.get(getActivity()).getLocation(locationId);
				mInstance.setLocation(lLocation.getId());
				selectLocation.setVisibility(View.GONE);
				CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(lLocation.getLatitude(), lLocation.getLongitude()), 14);
				map.animateCamera(cameraUpdate);
				map.addMarker(new MarkerOptions()
				.position(new LatLng(lLocation.getLatitude(), lLocation.getLongitude())));
				break;
			} else {
				break;
			}
		case 3:
			if(resultCode == Activity.RESULT_OK) {
				long contactId = data.getLongExtra("ID", 1);
				Contact lContact = InstanceHolder.get(getActivity()).getContact(contactId);
				mInstance.setContact(lContact.getId());
				mInstance.setNumber(data.getExtras().getString("number"));
				contactName.setVisibility(View.VISIBLE);
				contactName.setText("Sending a message to " + lContact.getName());
				break;
			} else {
				break;
			}
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
