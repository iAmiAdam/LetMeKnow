package info.adamjsmith.letmeknow;

import java.util.UUID;

import android.app.IntentService;
import android.content.Intent;

public class NotificationTools extends IntentService {

	public NotificationTools(String name) {
		super(name);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		UUID instanceId = (UUID) intent.getSerializableExtra(InstanceFragment.EXTRA_INSTANCE_ID);
		Instance i = InstanceHolder.get(getApplicationContext()).getInstance(instanceId);
		
		InstanceHolder.get(getApplicationContext()).deleteInstance(i);
	}

}
