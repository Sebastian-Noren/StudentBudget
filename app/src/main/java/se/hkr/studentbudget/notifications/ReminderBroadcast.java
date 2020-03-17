package se.hkr.studentbudget.notifications;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import se.hkr.studentbudget.LoginActivity;
import se.hkr.studentbudget.R;

import static android.content.Context.ALARM_SERVICE;


public class ReminderBroadcast extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        //Daily notification
        NotificationCompat.Builder builderDaily = new NotificationCompat.Builder(context, "daily")
                .setSmallIcon(R.drawable.icons_salary)
                .setContentTitle("Student Budget")
                .setContentText("Don't forget to add your expenses!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        NotificationManagerCompat notificationManagerDaily = NotificationManagerCompat.from(context);
        notificationManagerDaily.notify(200, builderDaily.build());


    }

    public static void setupRepeatingNotifications(Activity activity, long triggerAtMillis, long intervalMillis){
        Intent intent = new Intent(activity, ReminderBroadcast.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(activity,0,intent,0);

        AlarmManager alarmManager =(AlarmManager) activity.getSystemService(ALARM_SERVICE);



        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,triggerAtMillis,intervalMillis,pendingIntent);
    }

    public static void createNotificationChannel(Activity activity, String channelID, CharSequence channelName) {

        String description = "Student Budget channel!";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(channelID, channelName, importance );
        channel.setDescription(description);

        NotificationManager notificationManager = activity.getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);

    }
}
