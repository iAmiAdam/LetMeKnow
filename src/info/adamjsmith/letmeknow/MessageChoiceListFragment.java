package info.adamjsmith.letmeknow;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.ListView;

public class MessageChoiceListFragment extends MessageListFragment {
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		Message m = ((MessageAdapter)getListAdapter()).getItem(position);

		Intent i = new Intent();
		i.putExtra(MessageFragment.EXTRA_MESSAGE_ID, m.getId());
		getActivity().setResult(Activity.RESULT_OK, i);
		getActivity().finish();
	}
}
