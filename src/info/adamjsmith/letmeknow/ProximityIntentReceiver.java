package info.adamjsmith.letmeknow;

import java.util.UUID;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

public class ProximityIntentReceiver extends BroadcastReceiver {
	Context mContext;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		this.mContext = context;
		UUID instanceId = (UUID) intent.getSerializableExtra(InstanceFragment.EXTRA_INSTANCE_ID);
		Instance i = InstanceHolder.get(mContext).getInstance(instanceId);
		pushNotification(i);
		InstanceHolder.get(context).deleteInstance(i);
	}
	
	public void pushNotification(Instance i) {
		NotificationManager nm = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
		String name = InstanceHolder.get(mContext).getContact(i.getContact()).getName();
		final Intent intent = new Intent();
		PendingIntent pendingIntent = PendingIntent.getActivity(mContext,  0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		NotificationCompat.Builder notifBuilder = new NotificationCompat.Builder(mContext)
		.setSmallIcon(R.drawable.notification)
		.setContentTitle("Text Message Sent")
		.setContentText("We've let " + name + " know you're safe")
		.setAutoCancel(true)
		.setContentIntent(pendingIntent);
		nm.notify(1, notifBuilder.build());
	}

}
