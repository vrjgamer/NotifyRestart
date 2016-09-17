package com.vrjco.v.notifyrestart;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

/**
 * Created by VRUSHABH on 16-09-2016.
 */
public class RestartBroadcastListener extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //check if broadcast is about boot complete
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            //check switch from prefs
            if (MainActivity.getPrefsNotify(context)) {
                showNotification(context);
            }
        }
    }

    //show in-app notification
    private void showNotification(Context context) {
        Log.d("boot", "finished " + MainActivity.getPrefsNotify(context));
        // Set Notification Title
        String strtitle = MainActivity.getPrefsTitle(context);
        // Set Notification Text
        String strtext = MainActivity.getPrefsText(context);

        Log.d("boot", "notification " + strtitle + " "+ strtext);

        //Create Notification using NotificationCompat.Builder
        Uri uri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                // Set Icon
                .setSmallIcon(R.mipmap.ic_launcher)
                // Set Ticker Message
                .setTicker("Restarted!")
                // Set Title
                .setContentTitle(strtitle)
                // Set Text
                .setContentText(strtext)
                //Set Sound
                .setSound(uri)
                //Auto Dismiss Notification
                .setAutoCancel(true);

        // Create Notification Manager
        NotificationManager notificationmanager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        // Build Notification with Notification Manager
        notificationmanager.notify(0, builder.build());
    }

}
