package info.adamjsmith.letmeknow;

import java.util.UUID;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsManager;

public class NotificationTools extends BroadcastReceiver {
	Context mContext;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		try {
			this.mContext = context.getApplicationContext();
			
			UUID instanceId = (UUID) intent.getExtras().get(InstanceFragment.EXTRA_INSTANCE_ID);
			Instance i = InstanceHolder.get(mContext).getInstance(instanceId);
			sentNotification(i);
			sendMessage(i);
			if (!i.persist()) {
				InstanceHolder.get(mContext).deleteInstance(i);
			} else {
				i.setAlert();
				i.setActive(false);
				InstanceHolder.get(mContext).saveInstances();
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private void sendMessage(Instance i) {
		String phoneNumber = i.getNumber();
		String message = InstanceHolder.get(mContext).getMessage(i.getMessage()).getText();
		SmsManager sms = SmsManager.getDefault();
		sms.sendTextMessage(phoneNumber, null, message, null, null);
	}
	
	private void sentNotification(Instance i) {
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
