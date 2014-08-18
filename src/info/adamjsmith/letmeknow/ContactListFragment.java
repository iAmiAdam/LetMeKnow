package info.adamjsmith.letmeknow;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ContactListFragment extends ListFragment {
	private ArrayList<Contact> mContacts;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mContacts = InstanceHolder.get(getActivity()).getContacts();
		
		ContactAdapter adapter = new ContactAdapter(mContacts);
		setListAdapter(adapter);
	}
	
	class ContactAdapter extends ArrayAdapter<Contact> {
		
		public ContactAdapter(ArrayList<Contact> contacts) {
			super(getActivity(), 0, contacts);
		}
		
		public View getView(int position, View convertView, ViewGroup parent) {
			if(convertView == null) {
				convertView = getActivity().getLayoutInflater()
						.inflate(R.layout.list_item_contact, null);
			}
			
			Contact c = getItem(position);
			String[] phones = c.getNumbers();
			String phonesText = "";
			Integer i = 1;
			
			for (String s : phones) {
				phonesText += s;
				if(i != phones.length) {
					phonesText += "\n";
				}
				i++;
			}
			
			
			TextView name = (TextView) convertView.findViewById(R.id.contact_list_name);
			name.setText(c.getName());
			
			TextView numbers = (TextView) convertView.findViewById(R.id.contact_list_numbers);
			numbers.setText(phonesText);
			
			
			return convertView;
		}
	}
}
