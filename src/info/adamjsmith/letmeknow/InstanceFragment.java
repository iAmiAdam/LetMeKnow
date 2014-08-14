package info.adamjsmith.letmeknow;

import java.util.UUID;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

public class InstanceFragment extends Fragment {
	public static final String EXTRA_INSTANCE_ID = "info.adamjsmith.letmeknow.instance_id";

	private Instance mInstance;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		UUID instanceId = (UUID)getArguments().getSerializable(EXTRA_INSTANCE_ID);
		mInstance = InstanceHolder.get(getActivity()).getInstance(instanceId);
		
		setHasOptionsMenu(true);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_instance, parent, false);
		if(NavUtils.getParentActivityName(getActivity()) != null) {
			getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
		}
		
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
	
	public static Fragment newInstance(UUID instanceId) {
		Bundle args = new Bundle();
		args.putSerializable(EXTRA_INSTANCE_ID, instanceId);
		
		Fragment fragment = new InstanceFragment();
		fragment.setArguments(args);
		
		return fragment;
	}
	
}
