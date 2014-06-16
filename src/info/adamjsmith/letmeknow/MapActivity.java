package info.adamjsmith.letmeknow;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

public class MapActivity extends Activity {
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		setContentView(R.layout.map);
		Map mapFragment = new Map();
		fragmentTransaction.replace(R.id.mapView, mapFragment);
		fragmentTransaction.commit();
	}
}
