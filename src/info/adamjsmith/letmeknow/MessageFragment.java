package info.adamjsmith.letmeknow;

import java.util.UUID;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class MessageFragment extends Fragment {
	public static final String EXTRA_MESSAGE_ID = "info.adamjsmith.letmeknow.message_id";
	
	private Message mMessage;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		UUID messageId = (UUID)getArguments().getSerializable(EXTRA_MESSAGE_ID);
		mMessage = InstanceHolder.get(getActivity()).getMessage(messageId);
		
		setHasOptionsMenu(true);
		setTitle();
	}
	
	@Override
	public void onPause() {
		super.onPause();
		InstanceHolder.get(getActivity()).saveMessages();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		setTitle();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_message, parent, false);
		if(NavUtils.getParentActivityName(getActivity()) != null) {
			getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
		}
		
		EditText content = (EditText)v.findViewById(R.id.content);
		content.setText(mMessage.getText());
		
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
	
	public static Fragment newInstance(UUID messageId) {
		Bundle args = new Bundle();
		args.putSerializable(EXTRA_MESSAGE_ID, messageId);
		
		Fragment fragment = new MessageFragment();
		fragment.setArguments(args);
		
		return fragment;
	}
	
	private void setTitle() {
		if(mMessage.getText() != null) {
			String text = mMessage.getText();
			if (text.length() > 10) {
				String slice = text.substring(0, 9);
				slice += "...";
				getActivity().setTitle(slice);
			} else {
				getActivity().setTitle(text);
			}
		} else {
			getActivity().setTitle("New Message");
		}
	}
	
}
