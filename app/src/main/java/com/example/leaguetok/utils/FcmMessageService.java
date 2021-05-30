package com.example.leaguetok.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.navigation.NavController;
import androidx.navigation.NavDeepLink;
import androidx.navigation.NavDeepLinkBuilder;
import androidx.navigation.Navigation;

import com.example.leaguetok.MainActivity;
import com.example.leaguetok.R;
import com.example.leaguetok.model.Model;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.HashMap;
import java.util.Map;

public class FcmMessageService extends FirebaseMessagingService {
    public static void sendDeviceToken() {
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (!task.isSuccessful()) {
                    Log.w("TAG", "Fetching FCM registration token failed", task.getException());
                    return;
                }

                // Get new FCM registration token
                String token = task.getResult();
                Log.d("TAG", "fcm token: " + token);

                // Send device token to server
                Model.instance.sendDeviceToken(FirebaseAuth.getInstance().getCurrentUser().getUid(), token, new Model.AsyncListener() {
                    @Override
                    public void onComplete(Object data) {}

                    @Override
                    public void onError(Object error) {}
                });
            }
        });
    }

    @Override
    public void onNewToken(@NonNull String token) {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            // Send device token to server
            Model.instance.sendDeviceToken(FirebaseAuth.getInstance().getCurrentUser().getUid(), token, new Model.AsyncListener() {
                @Override
                public void onComplete(Object data) {
                }

                @Override
                public void onError(Object error) {
                }
            });
        }
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        if (remoteMessage.getData().size() > 0) {
            Map<String, String> data = remoteMessage.getData();

            // Set custom notification layout
            RemoteViews remoteViews = new RemoteViews(getApplicationContext().getPackageName(), R.layout.notification);
            remoteViews.setTextViewText(R.id.notification_title, data.get("title"));
            remoteViews.setTextViewText(R.id.notification_text, data.get("message"));

            Bundle bundle = new Bundle();
            bundle.putString("ImitVideoResult", data.get("score"));
            bundle.putString("OriginalVideoID", data.get("sourceId"));
            PendingIntent pendingIntent = new NavDeepLinkBuilder(this)
                    .setComponentName(MainActivity.class)
                    .setGraph(R.navigation.mobile_navigation)
                    .setDestination(R.id.uploadResultFragment2)
                    .setArguments(bundle)
                    .createPendingIntent();
            //Intent intent = new Intent(this, MainActivity.class);
            //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            //PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

            String channel_id = "notification_channel";
            NotificationCompat.Builder builder = new NotificationCompat
                    .Builder(getApplicationContext(), channel_id)
                    .setAutoCancel(true)
                    .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                    .setSmallIcon(R.drawable.ic_notification)
                    .setOnlyAlertOnce(true)
                    .setContentIntent(pendingIntent);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                builder.setCustomContentView(remoteViews);
            }
            else {
                builder = builder.setContentTitle(remoteMessage.getNotification().getTitle())
                        .setContentText(remoteMessage.getNotification().getBody())
                        .setSmallIcon(R.drawable.ic_notification);
            }

            NotificationManager notificationManager= (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
                NotificationChannel notificationChannel=new NotificationChannel(channel_id,"notification",NotificationManager.IMPORTANCE_HIGH);
                notificationManager.createNotificationChannel(notificationChannel);
            }

            notificationManager.notify(0, builder.build());
        }
    }
}