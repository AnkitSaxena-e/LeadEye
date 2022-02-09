package com.example.commember.Receiver;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.commember.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static com.example.commember.Adapter_Or_Class.Noti.CHANNEL_1_ID;

public class TickTimeReceiver extends BroadcastReceiver {

    String Hour, Minute, Day, Month, Year, AMPM, storeCurruntTime, storeCurruntDate;

    Context cc;
    NotificationManagerCompat notificationManagerCompat;

    @Override
    public void onReceive(Context context, Intent intent) {

        cc = context;
        notificationManagerCompat = NotificationManagerCompat.from(context);

        if(intent.getAction().equals("android.intent.action.TIME_TICK")){

            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Time");

            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if(snapshot.exists()){

                        Hour = snapshot.child("Hour").getValue().toString();
                        Day = snapshot.child("Day").getValue().toString();
                        Minute = snapshot.child("Minute").getValue().toString();
                        Month = snapshot.child("Month").getValue().toString();
                        Year = snapshot.child("Year").getValue().toString();

                        Calendar calendar = Calendar.getInstance();

                        SimpleDateFormat currentDate = new SimpleDateFormat("yyyy-MM-dd");
                        storeCurruntDate = currentDate.format(calendar.getTime());

                        SimpleDateFormat currentTime = new SimpleDateFormat(" hh:mm a");
                        storeCurruntTime = currentTime.format(calendar.getTime());

                        if(Integer.parseInt(Hour) >= 12){

                            AMPM = "PM";
                            int a = Integer.parseInt(Hour);
                            a -= 12;
                            Hour = String.valueOf(a);

                        } else {
                            AMPM = "AM";
                        }

                        if(Hour.length() == 1){

                            Hour = "0" + Hour;

                        }

                        if(Minute.length() == 1){

                            Minute = "0" + Minute;

                        }

                        if(Integer.parseInt(Minute) < 5){
                            int l = Integer.parseInt(Minute);
                            l += 55;

                            int y = Integer.parseInt(Hour);
                            y -= 1;

                            Hour = String.valueOf(y);
                            Minute = String.valueOf(l);
                        }else {
                            Minute = String.valueOf(Integer.parseInt(Minute) - 5);
                        }

                        Toast.makeText(context, "Time Tick", Toast.LENGTH_SHORT).show();

                        if(storeCurruntTime.equals( " " + Hour + ":" + Minute + " " + AMPM)){
                            sendNoti("Good Morning", "Today's Session Will Starts in 5 Minutes");
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }

    }

    public void sendNoti(String title, String massage) {

        Notification notification = new NotificationCompat.Builder(cc, CHANNEL_1_ID)
                .setSmallIcon(R.drawable.homeicon)
                .setContentTitle(title)
                .setContentText(massage)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();

        notificationManagerCompat.notify(1, notification);

    }

}

