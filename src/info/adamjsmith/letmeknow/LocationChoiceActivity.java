package info.adamjsmith.letmeknow;

import android.support.v4.app.Fragment;

public class LocationChoiceActivity extends SingleFragmentActivity {

	@Override
	protected Fragment createFragment() {
		return new LocationChoiceListFragment();
	}

}
