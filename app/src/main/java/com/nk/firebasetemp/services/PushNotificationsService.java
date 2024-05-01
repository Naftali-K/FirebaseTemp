package com.nk.firebasetemp.services;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.nk.firebasetemp.MainActivity;
import com.nk.firebasetemp.R;


/**
 * @Author: naftalikomarovski
 * @Date: 2024/04/29
 */

/**
 * https://youtu.be/m8vUFO5mFIM?si=oTyZqLOQuYjyUyMh - video lesson
 * https://youtu.be/M7z2MFoI6MQ?si=9eQE9ApQMA6gWPV4 - video lesson
 */
public class PushNotificationsService extends FirebaseMessagingService {
    private static final String TAG = "Test_code";

    private final int PERMISSION_REQ_CODE = 100;

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {

        // Second video --------
        // Open application by pushing to notification.
        Log.d(TAG, "From: " + message.getFrom());
        if (message.getNotification() != null) {
            Log.d(TAG, "Notification Body: " + message.getNotification().getBody());
//            sentNotification(message.getFrom(), message.getNotification().getBody());
        }
        if (message.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + message.getData());

//            if (/* Check if data needs to be processed by long running job */ true) {
//                // For long-running tasks (10 seconds or more) use WorkManager.
//                scheduleJob();
//            } else {
//                // Handle message within 10 seconds
//                handleNow();
//            }
        }
        sentToastNotification(message.getFrom(), message.getNotification().getBody());
        sentNotification(message.getNotification().getTitle(), message.getNotification().getBody());
        // --------------------

        // First video -------
        // Show notifications, without action to open app by pushing to note.
        String title = message.getNotification().getTitle();
        String text = message.getNotification().getBody();
//        sendNote(title, text);
        // -------------------

        super.onMessageReceived(message);
    }

    private void sentToastNotification(String from, String body) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(PushNotificationsService.this, from + " -> " + body, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sentNotification(String title, String messageBody) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_MUTABLE);

        String channelID = "My channel ID";
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, channelID)
                .setSmallIcon(R.drawable.icon_speaker_notes)
                .setContentTitle(title)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelID, "Channel human readable title", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0, notificationBuilder.build());
    }

    private void sendNote(String title, String text) {
        final String CHANNEL_ID = "HEADS_UP_NOTIFICATION";
        NotificationChannel channel = new NotificationChannel(
                CHANNEL_ID,
                "Header Up Notification",
                NotificationManager.IMPORTANCE_HIGH);
        getSystemService(NotificationManager.class).createNotificationChannel(channel);
        Notification.Builder notification = new Notification.Builder(this, CHANNEL_ID)
                .setContentTitle(title + " Edit from Code")
                .setContentText(text + " Edit from Code")
                .setSmallIcon(R.drawable.icon_notification_important)
                .setAutoCancel(true);

//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            ActivityCompat.requestPermissions("mainActivity", new String[] {Manifest.permission.POST_NOTIFICATIONS}, PERMISSION_REQ_CODE);
//            return;
//        }

        NotificationManagerCompat.from(this).notify(1, notification.build());
    }
}
