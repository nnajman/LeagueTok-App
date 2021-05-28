package com.example.leaguetok.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.example.leaguetok.MainActivity;
import com.example.leaguetok.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FcmMessageService extends FirebaseMessagingService {
    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);

        //TODO: send new token to server
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        if (remoteMessage.getNotification() != null) {
            // Set custom notification layout
            RemoteViews remoteViews = new RemoteViews(getApplicationContext().getPackageName(), R.layout.notification);
            remoteViews.setTextViewText(R.id.notification_title, remoteMessage.getNotification().getTitle());
            remoteViews.setTextViewText(R.id.notification_text, remoteMessage.getNotification().getBody());

            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

            String channel_id = "notification_channel";
            NotificationCompat.Builder builder = new NotificationCompat
                    .Builder(getApplicationContext(), channel_id)
                    .setAutoCancel(true)
                    .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                    .setOnlyAlertOnce(true)
                    .setContentIntent(pendingIntent)
                    .setContent(remoteViews);

            ((NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE)).notify(0, builder.build());

        }
    }
}
