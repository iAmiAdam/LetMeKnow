package info.adamjsmith.letmeknow;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.ListView;

public class LocationChoiceListFragment extends LocationListFragment {
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		Location loc = ((LocationAdapter)getListAdapter()).getItem(position);

		Intent i = new Intent();
		i.putExtra(LocationFragment.EXTRA_LOCATION_ID, loc.getId());
		getActivity().setResult(Activity.RESULT_OK, i);
		getActivity().finish();
	}
}
