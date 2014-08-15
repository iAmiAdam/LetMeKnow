package info.adamjsmith.letmeknow;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MessageListFragment extends ListFragment {
	private ArrayList<Message> mMessages;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		
		mMessages = InstanceHolder.get(getActivity()).getMessages();
		
		MessageAdapter adapter = new MessageAdapter(mMessages);
		setListAdapter(adapter);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		((MessageAdapter)getListAdapter()).notifyDataSetChanged();
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		ListView listView = getListView();
		listView.setBackgroundColor(getResources().getColor(R.color.background));
		listView.setDividerHeight(0);
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.fragment_message_list, menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_item_new_message:
			Message message = new Message();
			InstanceHolder.get(getActivity()).addMessage(message);
			Intent i = new Intent(getActivity(), MessagePagerActivity.class);
			i.putExtra(MessageFragment.EXTRA_MESSAGE_ID, message.getId());
			startActivityForResult(i, 0);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		Message m = ((MessageAdapter)getListAdapter()).getItem(position);

		Intent i = new Intent(getActivity(), MessagePagerActivity.class);
		i.putExtra(MessageFragment.EXTRA_MESSAGE_ID, m.getId());
		startActivity(i);
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
