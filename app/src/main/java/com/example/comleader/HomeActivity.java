package com.example.comleader;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.constraintlayout.utils.widget.MotionLabel;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Notification;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;
import android.provider.Settings;
import android.support.v4.media.session.MediaSessionCompat;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.comleader.Adapter_Or_Class.MySingleton;
import com.example.comleader.Fragment.ApprovelFragment;
import com.example.comleader.Fragment.EditCusFragment;
import com.example.comleader.Fragment.MemberFragment;
import com.example.comleader.Fragment.SeeCusFragment;
import com.example.comleader.Fragment.SeeLocationFragment;
import com.example.comleader.Prevalant.Prevalant;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.squareup.picasso.Picasso;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import io.paperdb.Paper;

import static android.content.ContentValues.TAG;
import static com.example.comleader.Adapter_Or_Class.Noti.CHANNEL_1_ID;

public class HomeActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private TextView Set_Time, Set_Holly;
    ImageView Pro_Pic, Pro_Full_Pic;
    TextView Pro_Name, Pro_Full_Name, Pro_Full_No;
    MotionLayout motionLayout_Home;
    ConstraintLayout Short_Pro, Full_Pro;
    BottomNavigationView navView;
    private int Day, Month, Year, Hour, Minute, AMPM, myAMPM, myDay, myMonth, myYear, myHour, myMinute;

    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Paper.init(HomeActivity.this);

        navView = findViewById(R.id.nav_view);

        Set_Time = findViewById(R.id.set_Time);
        Set_Holly = findViewById(R.id.set_Holly);

        Short_Pro = findViewById(R.id.profile_Short);
        Full_Pro = findViewById(R.id.profile_Full);

        Pro_Pic = findViewById(R.id.pro_Image);
        Pro_Full_Pic = findViewById(R.id.pro_Image_Full);

        Pro_Name = findViewById(R.id.pro_Name);
        Pro_Full_Name = findViewById(R.id.pro_Name_Full);
        Pro_Full_No = findViewById(R.id.pro_Number_Full);

        motionLayout_Home = findViewById(R.id.Home_Motion);

        navView.setOnNavigationItemSelectedListener(onNavi);

        getSupportFragmentManager().beginTransaction().replace(R.id.frame_Layout_Home, new MemberFragment()).commit();

        Set_Time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SetTime();

            }
        });

        Set_Holly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SetDate();

            }
        });

        Short_Pro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                motionLayout_Home.setTransitionDuration(500);
                motionLayout_Home.transitionToState(R.id.end);

            }
        });

        Full_Pro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                motionLayout_Home.setTransitionDuration(500);
                motionLayout_Home.transitionToState(R.id.start);

            }
        });

        SetDetail();

    }

    private void SetDate() {

        Calendar c = Calendar.getInstance();
        Year = c.get(Calendar.YEAR);
        Month = c.get(Calendar.MONTH);
        Day = c.get(Calendar.DAY_OF_MONTH);

        datePickerDialog = DatePickerDialog.newInstance(HomeActivity.this, Year, Month, Day);
        datePickerDialog.show(getSupportFragmentManager(), "Select Hollyday");

    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

        DatabaseReference rf = FirebaseDatabase.getInstance().getReference().child("Time");

        HashMap<String, Object> date = new HashMap<>();

        date.put("Year", year);
        date.put("Month", monthOfYear);
        date.put("Day", dayOfMonth);

        rf.updateChildren(date).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(HomeActivity.this, "HollyDay Updated", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {

        DatabaseReference rf = FirebaseDatabase.getInstance().getReference().child("Time");

        HashMap<String, Object> date = new HashMap<>();

        date.put("Hour", hourOfDay);
        date.put("Minute", minute);

        rf.updateChildren(date).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(HomeActivity.this, "Time Updated", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void SetTime() {

        Calendar c = Calendar.getInstance();
        Hour = c.get(Calendar.HOUR);
        Minute = c.get(Calendar.MONTH);
        AMPM = c.get(Calendar.AM_PM);

        timePickerDialog = TimePickerDialog.newInstance(HomeActivity.this, Hour, Minute, true);
        timePickerDialog.show(getSupportFragmentManager(), "Select Time");

    }

    public void setFrag(String Lo){

        if(Lo.equals("Cus")){
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_Layout_Home, new SeeCusFragment()).commit();
        } else if(Lo.equals("Loc")){
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_Layout_Home, new SeeLocationFragment()).commit();
        } else if(Lo.equals("ECus")){
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_Layout_Home, new EditCusFragment()).commit();
        } else if(Lo.equals("Mem")){
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_Layout_Home, new MemberFragment()).commit();
        } else if(Lo.equals("Appr")){
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_Layout_Home, new ApprovelFragment()).commit();
        }

    }

    BottomNavigationView.OnNavigationItemSelectedListener onNavi = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

            Fragment fragment = null;

            if (menuItem.getItemId() == R.id.navigation_member) {
                fragment = new MemberFragment();
            } else if (menuItem.getItemId() == R.id.navigation_approvel) {
                fragment = new ApprovelFragment();
            }

            getSupportFragmentManager().beginTransaction().replace(R.id.frame_Layout_Home, fragment).commit();

            return true;
        }
    };

    private void SetDetail() {

        DatabaseReference rr = FirebaseDatabase.getInstance().getReference().child("Leader").child(Paper.book().read(Prevalant.UserNumberA));

        rr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()){

                    Picasso.get().load(snapshot.child("Image").getValue().toString()).fit().centerCrop().into(Pro_Pic);
                    Picasso.get().load(snapshot.child("Image").getValue().toString()).fit().centerCrop().into(Pro_Full_Pic);

                    Pro_Name.setText(snapshot.child("Name").getValue().toString());
                    Pro_Full_Name.setText(snapshot.child("Name").getValue().toString());
//                    Pro_Full_Address.setText(snapshot.child("Address").getValue().toString());
                    Pro_Full_No.setText(snapshot.child("Number").getValue().toString());

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    ///////////////////////////////////////////////////////////Notification////////////////////////////////////////////

//    public void sendNoti(String title, String massage) {
//
//        Notification notification = new NotificationCompat.Builder(this, CHANNEL_1_ID)
//                .setSmallIcon(R.drawable.homeicon)
//                .setContentTitle(title)
//                .setContentText(massage)
//                .setPriority(NotificationCompat.PRIORITY_HIGH)
//                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
//                .build();
//
//        notificationManagerCompat.notify(1, notification);
//
//    }
//
//    private void sendNotificationss(String to, String ti, String ta) {
//
//        Toast.makeText(this, "Geela " + to, Toast.LENGTH_SHORT).show();
//
//        String TOPIC = to; //topic must match with what the receiver subscribed to
//        String NOTIFICATION_TITLE = ti;
//        String NOTIFICATION_MESSAGE = ta;
//
//        JSONObject notification = new JSONObject();
//        JSONObject notifcationBody = new JSONObject();
//        try {
//            notifcationBody.put("title", NOTIFICATION_TITLE);
//            notifcationBody.put("message", NOTIFICATION_MESSAGE);
//
//            notification.put("to", TOPIC);
//            notification.put("data", notifcationBody);
//        } catch (JSONException e) {
//            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
//        }
//        sendNotification(notification);
//
//    }
//
//    private void sendNotification(JSONObject notification) {
//
//        jsonObjectRequest = new JsonObjectRequest(FCM_API, notification,
//                response -> {
//                    Log.i(TAG, "onResponse: " + response.toString());
//
//                    try {
//                        Toast.makeText(HomeActivity.this, "Rsponce " + response.get("success").toString(), Toast.LENGTH_SHORT).show();
//
//                        String jj = response.get("success").toString();
//
//                    } catch (JSONException e) {
//                        Toast.makeText(HomeActivity.this, "2 " + e.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//
//                },
//                error -> {
//                    Toast.makeText(HomeActivity.this, "Request error", Toast.LENGTH_LONG).show();
//                    Log.i(TAG, "onErrorResponse: Didn't work");
//                }) {
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> params = new HashMap<>();
//                params.put("Authorization", serverKey);
//                params.put("Content-Type", contentType);
//                return params;
//            }
//        };
//
//        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
//
//    }

    ///////////////////////////////////////////////////////////Notification////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////Location////////////////////////////////////////////////////////////////////


}