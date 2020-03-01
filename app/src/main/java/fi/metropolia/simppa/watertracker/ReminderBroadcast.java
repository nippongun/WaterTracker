package fi.metropolia.simppa.watertracker;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class ReminderBroadcast extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        //Intent for when notification is tapped on
        Intent intentToMain = new Intent(context, MainActivity.class);
        intentToMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent toMainIntent = PendingIntent.getActivity(context, 0, intentToMain, 0);

        //Get progress from shared preferences
        SharedPreferences prefGet = context.getSharedPreferences("Progress", Activity.MODE_PRIVATE);
        int progress = prefGet.getInt("Progress", 0);


        //Notification itself

        if (progress > 20) {// if by 14:00
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "notifyUser")
                 .setSmallIcon(R.drawable.logo_background)
                .setContentTitle("Today you drank " + progress + " % of your daily goal.")
                .setContentText("You are on a good track!") //one liner
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setCategory(NotificationCompat.CATEGORY_REMINDER)
                .setContentIntent(toMainIntent)
                .setAutoCancel(true);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(200, builder.build());}

        else {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "notifyUser")
                    .setSmallIcon(R.drawable.logo_background)
                    .setContentTitle("You drank " + progress + " % of your daily goal today.")
                    .setContentText("Make a new record. Remember to take your water bottle everywhere with you") //one liner
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setCategory(NotificationCompat.CATEGORY_REMINDER)
                    .setContentIntent(toMainIntent)
                    .setAutoCancel(true);
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            notificationManager.notify(200, builder.build());}
        }
    }

    //sources: https://www.youtube.com/watch?v=nl-dheVpt8o
    //sources: https://developer.android.com/training/notify-user/build-notification
    //sources: https://developer.android.com/training/scheduling/alarms#java
