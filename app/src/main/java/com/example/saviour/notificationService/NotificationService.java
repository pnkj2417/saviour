package com.example.saviour.notificationService;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.Context;
import android.os.Build;
import androidx.annotation.RequiresApi;
import androidx.core.app.TaskStackBuilder;
import com.example.saviour.MainActivity;
import com.example.saviour.R;
import java.util.Date;

public class NotificationService extends IntentService {

    private static final String NOTIFICATION_CHANNEL_ID = NotificationService.class.getSimpleName();
    public NotificationService() {
        super("NotificationService");
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onHandleIntent(Intent intent) {
        NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID,
                NOTIFICATION_CHANNEL_ID + "_name",
                NotificationManager.IMPORTANCE_HIGH);
        NotificationManager notifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notifyManager.createNotificationChannel(channel);
        Notification.Builder notification = new Notification.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(getString(R.string.app_name))
                .setOngoing(true)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setPriority(Notification.PRIORITY_HIGH)
                .setWhen(getTimeStamp())
                .setContentIntent(getIntent())
                .setShowWhen(true)
                .setCategory(Notification.CATEGORY_SYSTEM);
        notifyManager.notify(1, notification.build());
    }

    private long getTimeStamp() {
        Date date = new Date();
        return date.getTime();
    }

    private PendingIntent getIntent() {
        Intent resultIntent = new Intent(this, MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntentWithParentStack(resultIntent);
        return stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
    }
}
