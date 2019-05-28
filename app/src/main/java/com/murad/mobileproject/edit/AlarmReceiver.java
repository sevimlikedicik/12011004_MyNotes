package com.murad.mobileproject.edit;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.murad.mobileproject.R;
import com.murad.mobileproject.main.MainActivity;
import com.murad.mobileproject.models.Note;
import com.murad.mobileproject.util.SharedPreference;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AlarmReceiver  extends BroadcastReceiver {

    private Note note;
    private List<Note> noteList;
    @Override
    public void onReceive(Context context, Intent intent) {

        Calendar calendar = Calendar.getInstance();
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        String hour = "" + hourOfDay;
        if (hourOfDay < 10)
            hour = "0" + hourOfDay;
        String minuteString = "";
        if (minute < 10)
            minuteString = "0" + minute;
        else {
            minuteString = "" + minute;
        }

        try {
            noteList = Note.parse(context, new JSONArray(SharedPreference.getInstance(context).getNotes()));
        } catch (JSONException e) {
            noteList = new ArrayList<>();
        }

        for (int i = 0; i < noteList.size() ; i++) {

            if (noteList.get(i).getReminder() != null && noteList.get(i).getReminder().getTime().equals(hour + ":" + minuteString)){
                note = noteList.get(i);
            }
        }

        if (note != null){

            intent = new Intent(context, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("Notification", "1");

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            int notificationId = 1;
            String channelId = "channel-01";
            String channelName = "My Notes";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                NotificationChannel mChannel = new NotificationChannel(
                        channelId, channelName, importance);
                notificationManager.createNotificationChannel(mChannel);
            }
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, channelId)
                    .setSmallIcon(R.drawable.info_popup)
                    .setContentTitle(note.getTitle())
                    .setContentText(note.getContent());
            Bitmap icon = BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.info_popup);
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mBuilder.setLargeIcon(icon);
                mBuilder.setSmallIcon(R.drawable.info_popup);
            } else {
                mBuilder.setSmallIcon(R.drawable.info_popup);
            }
            mBuilder.setColor(context.getResources().getColor(R.color.colorAccent));

            PendingIntent pendingIntent = PendingIntent.getActivity(context.getApplicationContext(), 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

            mBuilder.setContentIntent(pendingIntent);
            mBuilder.setAutoCancel(true);

            notificationManager.notify(notificationId, mBuilder.build());
        }
    }

}
