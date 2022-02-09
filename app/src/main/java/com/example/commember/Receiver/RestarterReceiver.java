package com.example.commember.Receiver;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.commember.HomeActivity;
import com.example.commember.Prevalant.Prevalant;
import com.example.commember.R;
import com.example.commember.Service.HameshaRunService;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import io.paperdb.Paper;

import static com.example.commember.Adapter_Or_Class.Noti.CHANNEL_1_ID;
import static com.example.commember.R.drawable.are_online_vactor;
import static com.example.commember.R.drawable.nut_vactor;

public class RestarterReceiver extends BroadcastReceiver {

    private static int lastState = TelephonyManager.CALL_STATE_IDLE;
    private static Date callStartTime;
    private static boolean isIncoming;
    private static String savedNumber;
    String Hour, Minute, Day, Month, Year, FHour, FEHour, FMinute, FDay, FMonth, FYear, AMPM, storeCurruntTime, storeCurruntDate;

    Context cc;
    NotificationManagerCompat notificationManagerCompat;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("Broadcast Listened", "Service tried to stop");
//        Toast.makeText(context, "Service restarted", Toast.LENGTH_SHORT).show();

        try
        {
            cc = context;
            notificationManagerCompat = NotificationManagerCompat.from(context);

            if(intent.getAction().equals("android.intent.action.TIME_TICK")){

                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Time");

                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if(snapshot.exists()){

                            FHour = Hour = snapshot.child("Hour").getValue().toString();
                            FDay = Day = snapshot.child("Day").getValue().toString();
                            FMinute = Minute = snapshot.child("Minute").getValue().toString();
                            FMonth = Month = snapshot.child("Month").getValue().toString();
                            FYear = Year = snapshot.child("Year").getValue().toString();

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

                            if(storeCurruntTime.equals( " " + Hour + ":" + Minute + " " + AMPM)){
                                sendNoti("Good Morning", "Today's Session Will Starts in 5 Minutes");
                            }

                            if(storeCurruntTime.equals(" " + FHour + ":" + FMinute + " " + AMPM)){

                                Toast.makeText(context, "Session Online", Toast.LENGTH_SHORT).show();

                                ((HomeActivity) cc).See_Session.setText("Session is Online");
                                ((HomeActivity) cc).See_Session.setBackgroundResource(are_online_vactor);

                            }

                            RunningSession();

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }

            else if (intent.getAction().equals("android.intent.action.NEW_OUTGOING_CALL"))
            {
                savedNumber = intent.getExtras().getString("android.intent.extra.PHONE_NUMBER");
            } else if(intent.getAction().equals("android.intent.action.SCREEN_ON")){

                DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("User").child(Paper.book().read(Prevalant.UserNumberA));

                HashMap<String, Object> l = new HashMap<>();

                l.put("Online", "Online");

                reference.updateChildren(l);

            } else if (intent.getAction().equals("android.intent.action.SCREEN_OFF")){

                DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("User").child(Paper.book().read(Prevalant.UserNumberA));

                HashMap<String, Object>  l = new HashMap<>();

                l.put("Online", "Offline");

                reference.updateChildren(l);

            }
            String stateStr = intent.getExtras().getString(TelephonyManager.EXTRA_STATE);
            String number = intent.getExtras().getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
            int state = 0;
            if(stateStr.equals(TelephonyManager.EXTRA_STATE_IDLE))
            {
                state = TelephonyManager.CALL_STATE_IDLE;
            }
            else if(stateStr.equals(TelephonyManager.EXTRA_STATE_OFFHOOK))
            {
                state = TelephonyManager.CALL_STATE_OFFHOOK;
            }
            else if(stateStr.equals(TelephonyManager.EXTRA_STATE_RINGING))
            {
                state = TelephonyManager.CALL_STATE_RINGING;
            }

            onCallStateChanged(context, state, number);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(new Intent(context, HameshaRunService.class));
        } else {
            context.startService(new Intent(context, HameshaRunService.class));
        }
    }

    private void RunningSession() {

        int d = Integer.parseInt(FHour);
        d += 6;
        FEHour = String.valueOf(d);

        if(FEHour.length() == 1){
            FEHour = "0" + FEHour;
        }

        if(storeCurruntTime.equals(" " + FEHour + ":" + Minute + ":" + AMPM)){

            sendNoti("Session End", "Today's Session End! Well Done");

            ((HomeActivity) cc).See_Session.setText("Session End");
            ((HomeActivity) cc).See_Session.setBackgroundResource(nut_vactor);

        }

    }

    //Derived classes should override these to respond to specific events of interest
    protected void onIncomingCallStarted(Context ctx, String number, Date start){}
    protected void onIncomingCallEnded(Context ctx, String number, Date start, Date end){}

    public void onCallStateChanged(Context context, int state, String number)
    {
//        if(lastState == state)
//        {
//            Toast.makeText(context, "ret", Toast.LENGTH_SHORT).show();
//            //No change, debounce extras
//            return;
//        }
        switch (state)
        {
            case TelephonyManager.CALL_STATE_RINGING:
                isIncoming = true;
                callStartTime = new Date();
                savedNumber = number;
                ((HomeActivity)context).getLastLocation();
//                onIncomingCallStarted(context, number, callStartTime);
                break;

            case TelephonyManager.CALL_STATE_OFFHOOK:
                OnCall();
                ((HomeActivity)context).getLastLocation();
                onIncomingCallEnded(context,savedNumber,callStartTime,new Date());
                break;

            case TelephonyManager.CALL_STATE_IDLE:
                NoCall();
                ((HomeActivity)context).getLastLocation();
                onIncomingCallEnded(context, savedNumber, callStartTime, new Date());
                break;

            default:
                NoCall();

        }
        lastState = state;
    }

    private void OnCall() {

        DatabaseReference oo = FirebaseDatabase.getInstance().getReference().child("User").child(Paper.book().read(Prevalant.UserNumberA));

        HashMap<String, Object> l = new HashMap<>();

        l.put("OnCall", "On Call");

        oo.updateChildren(l);

    }

    private void NoCall() {

        DatabaseReference oo = FirebaseDatabase.getInstance().getReference().child("User").child(Paper.book().read(Prevalant.UserNumberA));

        HashMap<String, Object> l = new HashMap<>();

        l.put("OnCall", "False");

        oo.updateChildren(l);

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