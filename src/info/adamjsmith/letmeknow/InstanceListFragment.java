package info.adamjsmith.letmeknow;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class InstanceListFragment extends ListFragment {
	private ArrayList<Instance> mInstances;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		
		getActivity().setTitle("Let Me Know");
		mInstances = InstanceHolder.get(getActivity()).getInstances();
		
		InstanceAdapter adapter = new InstanceAdapter(mInstances);
		setListAdapter(adapter);
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.fragment_message_list, menu);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		((InstanceAdapter)getListAdapter()).notifyDataSetChanged();
	}
	
	private class InstanceAdapter extends ArrayAdapter<Instance> {
		
		public InstanceAdapter(ArrayList<Instance> instances){
			super(getActivity(), 0, instances);
		}
		
		public View getView(int position, View convertView, ViewGroup parent) {
			if(convertView == null) {
				convertView = getActivity().getLayoutInflater()
						.inflate(R.layout.list_item_instance, null);
			}
			
			Instance i = getItem(position);
			
			TextView contactName = (TextView) convertView.findViewById(R.id.instance_list_contact_name);
			contactName.setText("Test");
			
			TextView message = (TextView) convertView.findViewById(R.id.instance_list_message_content);
			message.setText(i.getMessage().getText());
			
			return convertView;
		}
	}
}
