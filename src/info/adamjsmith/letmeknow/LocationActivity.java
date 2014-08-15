package info.adamjsmith.letmeknow;

import java.util.UUID;

import android.support.v4.app.Fragment;

public class LocationActivity extends SingleFragmentActivity {

	@Override
	protected Fragment createFragment() {
		UUID locationId = (UUID)getIntent().getSerializableExtra(LocationFragment.EXTRA_LOCATION_ID);
		return new LocationFragment(locationId);
	}

}
