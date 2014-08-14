package info.adamjsmith.letmeknow;

import android.support.v4.app.Fragment;

public class InstanceListActivity extends SingleFragmentActivity {

	@Override
	protected Fragment createFragment() {
		return new InstanceListFragment();
	}

}
