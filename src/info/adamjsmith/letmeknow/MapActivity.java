package info.adamjsmith.letmeknow;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

public class MapActivity extends Activity {
	MapView mapView;
	GoogleMap map;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//FragmentManager fragmentManager = getFragmentManager();
		//FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		setContentView(R.layout.map);
		//Map mapFragment = new Map();
		//fragmentTransaction.replace(R.id.mapView, mapFragment);
		//fragmentTransaction.commit();
		
		mapView = (MapView) findViewById(R.id.mapView);
		mapView.onCreate(savedInstanceState);
 
		// Gets to GoogleMap from the MapView and does initialization stuff
		map = mapView.getMap();
		map.getUiSettings().setMyLocationButtonEnabled(false);
		map.setMyLocationEnabled(true);
 
		MapsInitializer.initialize(this);
		
		// Updates the location and zoom of the MapView
		CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(54.8, -3.2), 5);
		map.animateCamera(cameraUpdate);
		
		map.setOnMapClickListener(new OnMapClickListener(){
			@Override
			public void onMapClick(LatLng latLng) {
				map.clear();
				map.animateCamera(CameraUpdateFactory.newLatLng(latLng));
				map.addMarker(new MarkerOptions()
				.position(latLng));
				Intent data = new Intent();
				data.setData(Uri.parse(latLng.toString()));
				data.putExtra("lat", latLng.latitude);
				data.putExtra("lat", latLng.longitude);
				setResult(RESULT_OK, data);
				finish();
			}
		});
	}
	
	@Override
	public void onResume() {
		mapView.onResume();
		super.onResume();
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
}
