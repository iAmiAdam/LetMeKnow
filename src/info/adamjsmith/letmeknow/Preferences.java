package info.adamjsmith.letmeknow;

import android.app.Activity;
import android.os.Bundle;

public class Preferences extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);
		
		getFragmentManager().beginTransaction()
		.replace(R.id.settingsView, new SettingsFragment())
		.commit();
	}
}
