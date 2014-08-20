package info.adamjsmith.letmeknow;

import java.util.UUID;

import android.app.IntentService;
import android.content.Intent;

public class NotificationTools extends IntentService {
	
	private static final String DEBUG_TAG = "NotificationTools";

	public NotificationTools() {
		super(DEBUG_TAG);
	}
	
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
