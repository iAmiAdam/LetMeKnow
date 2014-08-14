package info.adamjsmith.letmeknow;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class MessageListFragment extends ListFragment {
	private ArrayList<Message> mMessages;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		
		getActivity().setTitle(R.string.messages);
		mMessages = InstanceHolder.get(getActivity()).getMessages();
		
		MessageAdapter adapter = new MessageAdapter(mMessages);
		setListAdapter(adapter);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		((MessageAdapter)getListAdapter()).notifyDataSetChanged();
	}
	
	private class MessageAdapter extends ArrayAdapter<Message> {
		
		public MessageAdapter(ArrayList<Message> messages) {
			super(getActivity(), 0, messages);
		}
		
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = getActivity().getLayoutInflater()
						.inflate(R.layout.list_item_message, null);
			}
			
			Message m = getItem(position);
			
			TextView content = (TextView) convertView.findViewById(R.id.message_list_content);
			content.setText(m.getText());
			
			return convertView;
		}
	}
}
