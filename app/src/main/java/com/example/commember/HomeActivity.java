package com.example.commember;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationManagerCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;
import android.provider.Settings;
import android.support.v4.media.session.MediaSessionCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.commember.Receiver.RestarterReceiver;
import com.example.commember.Fragment.AddCusFragment;
import com.example.commember.Fragment.EditCusFragment;
import com.example.commember.Fragment.SeeCusFragment;
import com.example.commember.Prevalant.Prevalant;
import com.example.commember.Service.HameshaRunService;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import io.paperdb.Paper;

public class HomeActivity extends AppCompatActivity implements ServiceConnection {

    public TextView Add_Cus, See_Cus, See_Ins, See_Session, Check_Location;
    FusedLocationProviderClient mFusedLocationClient;
    int PERMISSION_ID = 44;
    String Token = "A", Start_Time = "A", End_Time = "A";
    NotificationManagerCompat notificationManagerCompat;

    boolean gps = false, natwork = false;

    MediaSessionCompat mediaSession;

    public MotionLayout motionLayout_Home;
    ConstraintLayout Short_Pro, Full_Pro;

    public ImageView Pro_Pic, Pro_Full_Pic;
    public TextView Pro_Name, Pro_Full_Name, Pro_Full_Address, Pro_Full_No;

    public String War;

    RestarterReceiver tickTimeReceiver;

    ////////////////////////////////////////////////////////////////////////////

    Intent mServiceIntent;
    private HameshaRunService hameshaRunService;

    ////////////////////////////////////////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Paper.init(HomeActivity.this);

        notificationManagerCompat = NotificationManagerCompat.from(this);

        hameshaRunService = new HameshaRunService();
        mServiceIntent = new Intent(this, hameshaRunService.getClass());

        Add_Cus = findViewById(R.id.add_cus);
        See_Cus = findViewById(R.id.see_cus);
        See_Ins = findViewById(R.id.see_ins);
        See_Session = findViewById(R.id.see_session);
        Check_Location = findViewById(R.id.che_loc);

        Short_Pro = findViewById(R.id.profile_Short);
        Full_Pro = findViewById(R.id.profile_Full);

        Pro_Pic = findViewById(R.id.pro_Image);
        Pro_Full_Pic = findViewById(R.id.pro_Image_Full);

        Pro_Name = findViewById(R.id.pro_Name);
        Pro_Full_Name = findViewById(R.id.pro_Name_Full);
        Pro_Full_Address = findViewById(R.id.pro_Address_Full);
        Pro_Full_No = findViewById(R.id.pro_Number_Full);

        motionLayout_Home = findViewById(R.id.Home_Motion);

        tickTimeReceiver = new RestarterReceiver();

        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_TIME_TICK);
        registerReceiver(tickTimeReceiver, intentFilter);

        IntentFilter screenF = new IntentFilter();
        screenF.addAction(Intent.ACTION_SCREEN_ON);
        screenF.addAction(Intent.ACTION_SCREEN_OFF);
        registerReceiver(tickTimeReceiver, screenF);

        getSupportFragmentManager().beginTransaction().replace(R.id.frame_Layout_Home, new AddCusFragment()).commit();

        ////////////////////////////////////////////////////////////////////////////////////

        if (!isMyServiceRunning(hameshaRunService.getClass())) {
            startService(mServiceIntent);
        }

        ////////////////////////////////////////////////////////////////////////////////////


        Add_Cus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                motionLayout_Home.setTransitionDuration(500);
                motionLayout_Home.transitionToState(R.id.frag);
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_Layout_Home, new AddCusFragment()).commit();

            }
        });

        See_Cus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                motionLayout_Home.setTransitionDuration(500);
                motionLayout_Home.transitionToState(R.id.frag);
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_Layout_Home, new SeeCusFragment()).commit();

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

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        mediaSession = new MediaSessionCompat(this, "Plaaaay");

        checkPermissions();

        checkLocation();

        updateToken();

        getLastLocation();

        CheckOffline();

        TakeSessionTime();

        SetDetail();

    }

    /////////////////////////////////////////////////

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i ("Service status", "Running");
                return true;
            }
        }
        Log.i ("Service status", "Not running");
        return false;
    }

    /////////////////////////////////////////////////

    private void SetDetail() {

        DatabaseReference rr = FirebaseDatabase.getInstance().getReference().child("User").child(Paper.book().read(Prevalant.UserNumberA));

        rr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()){

                    Picasso.get().load(snapshot.child("Image").getValue().toString()).fit().centerCrop().into(Pro_Pic);
                    Picasso.get().load(snapshot.child("Image").getValue().toString()).fit().centerCrop().into(Pro_Full_Pic);

                    Pro_Name.setText(snapshot.child("Name").getValue().toString());
                    Pro_Full_Name.setText(snapshot.child("Name").getValue().toString());
                    Pro_Full_Address.setText(snapshot.child("Address").getValue().toString());
                    Pro_Full_No.setText(snapshot.child("Number").getValue().toString());

                    War = snapshot.child("Warning").getValue().toString();

                    if(War.equals("Off")){

                        Check_Location.setText("All is Good");
                        Check_Location.setBackgroundResource(R.drawable.add_cus_vactor);

                    } else {

                        Check_Location.setText(War);
                        Check_Location.setBackgroundResource(R.drawable.warning_vactor);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    ///////////////////////////////////////////////////////////Service///////////////////////////////////////////////////

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {

        if (!isMyServiceRunning(hameshaRunService.getClass())) {
            startService(mServiceIntent);
        }

    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

        stopService(mServiceIntent);
        Toast.makeText(HomeActivity.this, "Ser Dis", Toast.LENGTH_SHORT).show();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("User").child(Paper.book().read(Prevalant.UserNumberA));

        HashMap<String, Object>  l = new HashMap<>();

        l.put("Online", "Offline");

        reference.updateChildren(l);

    }

    ///////////////////////////////////////////////////////////Service///////////////////////////////////////////////////

    public void TakeSessionTime() {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Admin");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()){

                    Start_Time = snapshot.child("Start_Time").getValue().toString();
                    End_Time = snapshot.child("End_Time").getValue().toString();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void getToken() {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("User").child(Paper.book().read(Prevalant.UserNumberA));

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()){

                    Token = snapshot.child("Token").getValue().toString();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void getBack(){

        motionLayout_Home.setTransitionDuration(500);
        motionLayout_Home.transitionToState(R.id.start);

    }

    public void getEditCus(){
        motionLayout_Home.setTransitionDuration(500);
        motionLayout_Home.transitionToState(R.id.frag);
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_Layout_Home, new EditCusFragment()).commit();
    }

    public void updateToken() {

        String refreshToken= FirebaseInstanceId.getInstance().getToken();

        DatabaseReference rtgv = FirebaseDatabase.getInstance().getReference().child("User").child(Paper.book().read(Prevalant.UserNumberA));

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("Token", refreshToken);
        rtgv.updateChildren(hashMap);

    }

    ///////////////////////////////////////////////////Check_Offline//////////////////////////////////////////////////////////

    public void CheckOffline() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("User").child(Paper.book().read(Prevalant.UserNumberA));

        HashMap<String, Object>  l = new HashMap<>();

        l.put("Online", "Online");

        reference.updateChildren(l);

    }

    ///////////////////////////////////////////////////Check_Offline//////////////////////////////////////////////////////////

//    ///////////////////////////////////////////////////////////Notification////////////////////////////////////////////
//
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
//
//    ///////////////////////////////////////////////////////////Notification////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////Location////////////////////////////////////////////////////////////////////

    public void checkLocation() {

        LocationManager locationManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);

        try{
            gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception e){
            Toast.makeText(this, "error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        try{

            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

            if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED){
                natwork = true;
            } else {
                natwork = false;
            }

        } catch (Exception e){
            Toast.makeText(this, "error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        if(!natwork){

            new AlertDialog.Builder(this)
                    .setMessage("Network is Not Enable")
                    .setPositiveButton("Open Setting", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(Settings.ACTION_NETWORK_OPERATOR_SETTINGS));
                        }
                    })
                    .setNegativeButton("Cancel",null)
                    .show();

        }

    }

    @SuppressLint("MissingPermission")
    public void getLastLocation() {
        if (checkPermissions()) {

            if (isLocationEnabled()) {

                mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        Location location = task.getResult();
                        if (location == null) {
                            requestNewLocationData();
                        } else {
                            DatabaseReference RootRef = FirebaseDatabase.getInstance().getReference();

                            HashMap<String, Object> userdata = new HashMap<>();
                            userdata.put("Latitude", String.valueOf(location.getLatitude()));
                            userdata.put("Longitude", String.valueOf(location.getLongitude()));

                            RootRef.child("User").child(Paper.book().read(Prevalant.UserNumberA)).updateChildren(userdata);
                        }
                    }
                });
            } else {
                Toast.makeText(this, "Please turn on" + " your location...", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            requestPermissions();
        }
    }

    @SuppressLint("MissingPermission")
    public void requestNewLocationData() {

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(5);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
    }

    public LocationCallback mLocationCallback = new LocationCallback() {

        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();

            DatabaseReference RootRef = FirebaseDatabase.getInstance().getReference();

            HashMap<String, Object> userdata = new HashMap<>();
            userdata.put("Latitude", String.valueOf(mLastLocation.getLatitude()));
            userdata.put("Longitude", String.valueOf(mLastLocation.getLongitude()));


            RootRef.child("User").child(Paper.book().read(Prevalant.UserNumberA)).updateChildren(userdata);
        }
    };

    public boolean checkPermissions() {

        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;

    }

    public void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ID);
    }

    public boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            }
        }
    }

    /////////////////////////////////////////////////////////////////Location//////////////////////////////////////////////////////////////////////

    @Override
    public void onResume() {
        super.onResume();
        if (checkPermissions()) {
            getLastLocation();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("restartservice");
        broadcastIntent.setClass(this, RestarterReceiver.class);
        this.sendBroadcast(broadcastIntent);

    }
}