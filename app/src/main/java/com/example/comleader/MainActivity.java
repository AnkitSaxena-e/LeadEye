package com.example.comleader;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.comleader.Adapter_Or_Class.CounteryData;
import com.example.comleader.Adapter_Or_Class.OnSweepListner;
import com.example.comleader.Modal.Users;
import com.example.comleader.Prevalant.Prevalant;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;
import java.util.Map;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {

    private Dialog LoadingBar;
    private String parantName = "Leader";
    private static final int ASK_MULTIPLE_PERMISSION_REQUEST_CODE = 12345;
    Button Join, Login;


    private MaterialEditText Phone_Login, Passward_Login;
    private TextView Forgot_Passward;
    private Spinner spinner_Login;
    private ImageButton Show_Pass_Login;
    private String IIMM = "AS", Sus = "No";

    private MaterialEditText Name_Join, Number_Join, Passward_Join, Conferm_Passward_Join;
    private Spinner spinner_Join;
    private ImageButton Show_Pass_Join;
    int i = 0;

    private MotionLayout motionMain;
    private TextView Open_Login, Open_Join;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Paper.init(MainActivity.this);

        motionMain = findViewById(R.id.Main_Motion);

        Open_Login = findViewById(R.id.open_Login);
        Open_Join = findViewById(R.id.open_Join);

        MainFunction();
        LoginFunction();
        JoinFunction();

        Open_Join.setOnTouchListener(new OnSweepListner(MainActivity.this) {

            @Override
            public void onClick() {
                super.onClick();

                Toast.makeText(MainActivity.this, "join", Toast.LENGTH_SHORT).show();

                motionMain.setTransitionDuration(500);
                motionMain.transitionToState(R.id.join);

            }

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                Toast.makeText(MainActivity.this, "join", Toast.LENGTH_SHORT).show();

                motionMain.setTransitionDuration(500);
                motionMain.transitionToState(R.id.join);

                return super.onTouch(v, event);

            }
        });

        Open_Login.setOnTouchListener(new OnSweepListner(MainActivity.this) {

            @Override
            public void onClick() {
                super.onClick();

                Toast.makeText(MainActivity.this, "login", Toast.LENGTH_SHORT).show();

                motionMain.setTransitionDuration(500);
                motionMain.transitionToState(R.id.login);

            }

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                Toast.makeText(MainActivity.this, "login", Toast.LENGTH_SHORT).show();

                motionMain.setTransitionDuration(500);
                motionMain.transitionToState(R.id.login);

                return super.onTouch(v, event);

            }
        });

    }

    //////////////////////////////////////////////////////Login///////////////////////////////////////////////////////////

    private void LoginFunction() {

        Phone_Login = findViewById(R.id.Number_Login);
        Passward_Login = findViewById(R.id.Passward_Login);
        Show_Pass_Login = findViewById(R.id.Show);

        Login = findViewById(R.id.Join_login);
        Forgot_Passward = findViewById(R.id.ForgotP);

        Prevalant.SuspendUser = new ArrayList<>();

        spinner_Login = findViewById(R.id.spinnerCountriesL);
        spinner_Login.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, CounteryData.countryNames));

        String defaul = "India";
        ArrayAdapter myAdapter = (ArrayAdapter) spinner_Login.getAdapter();
        int position = myAdapter.getPosition(defaul);
        spinner_Login.setSelection(position);

        TakeUser();

        Passward_Login.setTransformationMethod(PasswordTransformationMethod.getInstance());

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.this.LoginAccount();
            }
        });

        Show_Pass_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (i == 0) {
                    Passward_Login.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    Show_Pass_Login.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_visibility));
                    i++;
                } else {
                    Passward_Login.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    Show_Pass_Login.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_visibility_off));
                    i = 0;
                }
            }
        });

        Forgot_Passward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String Number = Phone_Login.getText().toString();
                String code = CounteryData.countryAreaCodes[spinner_Login.getSelectedItemPosition()];
                String PhoneNum = "+" + code + Number;

                if (PhoneNum.isEmpty()) {
                    Phone_Login.setError("Please Enter Your UserId");
                } else {

                    ForgotPassward(PhoneNum);

                }
            }
        });

        Paper.book().write(Prevalant.CheckH, "A");

        String UserPhoneKey = Paper.book().read(Prevalant.userPhoneKey);
        String UserPassward = Paper.book().read(Prevalant.userPasswardKey);

        if(UserPhoneKey != "" && UserPassward != ""){
            if(!TextUtils.isEmpty(UserPhoneKey) && !TextUtils.isEmpty(UserPassward)){

                LoadingBar.show();

                Toast.makeText(this, UserPhoneKey + "" + UserPassward, Toast.LENGTH_SHORT).show();

//                CheckFireBase(UserPhoneKey, UserPassward);
            }
        }
    }

    private void LoginAccount() {

        try {

            String Number = Phone_Login.getText().toString();
            String code = CounteryData.countryAreaCodes[spinner_Login.getSelectedItemPosition()];
            String phoneNumber = "+" + code + Number;
            String passward = Passward_Login.getText().toString();

            if (TextUtils.isEmpty(phoneNumber)) {
                Phone_Login.setError("Please Enter User Id");
            } else if (TextUtils.isEmpty(passward)) {
                Passward_Login.setError("Please Enter Your Phone Password");
            } else if(passward.length() < 8) {
                Passward_Login.setError("Minimum 8 Characters Required");
            } else {

                LoadingBar.show();
                CheckFireBase(phoneNumber, passward);

            }
        }
        catch (Exception e){
            Toast.makeText(MainActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

    private void ForgotPassward(final String phoneNum) {

        DatabaseReference trf = FirebaseDatabase.getInstance().getReference().child("User").child(phoneNum);

        trf.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()){

                    String Nuum = snapshot.child("Number").getValue().toString();

                    Intent i = new Intent(MainActivity.this, changePassward.class);
                    i.putExtra("PhoneNumber", Nuum);
                    i.putExtra("UserId", phoneNum);
                    startActivity(i);

                }
                else{

                    Toast.makeText(MainActivity.this, "This Account Does Not Exist..", Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void TakeUser() {

        DatabaseReference fh = FirebaseDatabase.getInstance().getReference().child("User");

        fh.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {

                    try {

                        CId((Map<String, Object>) snapshot.getValue());

                    } catch (Exception e) {

                        Toast.makeText(MainActivity.this, "gg" + e.getMessage(), Toast.LENGTH_SHORT).show();

                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void CId(Map<String, Object> snapshot) {

        for (Map.Entry<String, Object> en : snapshot.entrySet()) {

            Map SPro = (Map) en.getValue();

            String oo = String.valueOf(SPro.get("Suspend"));

            if (oo.equals("Suspended")) {

                Sus = "Susp";
            }
        }
    }

    //////////////////////////////////////////////////////Join/////////////////////////////////////////////////////////////

    private void JoinFunction() {

        Join = findViewById(R.id.Join_button);
        Name_Join = findViewById(R.id.Name_Join);
        Number_Join = findViewById(R.id.Number_Join);
        Passward_Join = findViewById(R.id.Passward_Join);
        Conferm_Passward_Join = findViewById(R.id.Passward_Conferm_Join);

        Show_Pass_Join = findViewById(R.id.Show_Join);

        Passward_Join.setTransformationMethod(PasswordTransformationMethod.getInstance());
        Conferm_Passward_Join.setTransformationMethod(PasswordTransformationMethod.getInstance());

        spinner_Join = findViewById(R.id.spinnerCountries);
        spinner_Join.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, CounteryData.countryNames));

        String defaul = "India";
        ArrayAdapter myAdapter = (ArrayAdapter) spinner_Join.getAdapter();
        int position = myAdapter.getPosition(defaul);
        spinner_Join.setSelection(position);

        Name_Join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Name_Join.setBackgroundResource(R.drawable.edit_text_back);
                spinner_Join.setBackgroundResource(R.drawable.lineline);
                Number_Join.setBackgroundResource(R.drawable.lineline);
                Passward_Join.setBackgroundResource(R.drawable.lineline);
                Conferm_Passward_Join.setBackgroundResource(R.drawable.lineline);
                Show_Pass_Join.setBackgroundResource(R.drawable.lineline);

            }
        });

        Number_Join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Name_Join.setBackgroundResource(R.drawable.lineline);
                spinner_Join.setBackgroundResource(R.drawable.edit_text_back);
                Number_Join.setBackgroundResource(R.drawable.edit_text_back);
                Passward_Join.setBackgroundResource(R.drawable.lineline);
                Conferm_Passward_Join.setBackgroundResource(R.drawable.lineline);
                Show_Pass_Join.setBackgroundResource(R.drawable.lineline);

            }
        });

        Passward_Join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Name_Join.setBackgroundResource(R.drawable.lineline);
                spinner_Join.setBackgroundResource(R.drawable.lineline);
                Number_Join.setBackgroundResource(R.drawable.lineline);
                Passward_Join.setBackgroundResource(R.drawable.edit_text_back);
                Conferm_Passward_Join.setBackgroundResource(R.drawable.lineline);
                Show_Pass_Join.setBackgroundResource(R.drawable.edit_text_back);

            }
        });

        Conferm_Passward_Join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Name_Join.setBackgroundResource(R.drawable.lineline);
                spinner_Join.setBackgroundResource(R.drawable.lineline);
                Number_Join.setBackgroundResource(R.drawable.lineline);
                Passward_Join.setBackgroundResource(R.drawable.lineline);
                Conferm_Passward_Join.setBackgroundResource(R.drawable.edit_text_back);
                Show_Pass_Join.setBackgroundResource(R.drawable.edit_text_back);

            }
        });

        Show_Pass_Join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Name_Join.setBackgroundResource(R.drawable.lineline);
                spinner_Join.setBackgroundResource(R.drawable.lineline);
                Number_Join.setBackgroundResource(R.drawable.lineline);
                Passward_Join.setBackgroundResource(R.drawable.edit_text_back);
                Conferm_Passward_Join.setBackgroundResource(R.drawable.edit_text_back);
                Show_Pass_Join.setBackgroundResource(R.drawable.edit_text_back);

                if (i == 0) {
                    Passward_Join.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    Conferm_Passward_Join.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    Show_Pass_Join.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_visibility));
                    i++;
                } else {
                    Passward_Join.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    Conferm_Passward_Join.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    Show_Pass_Join.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_visibility_off));
                    i = 0;
                }
            }
        });

        Join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.this.CreateAccount();
            }
        });

    }

    private void CreateAccount() {

        String name = Name_Join.getText().toString();
        String passward = Passward_Join.getText().toString();
        String conferm_passward = Conferm_Passward_Join.getText().toString();
        String code = CounteryData.countryAreaCodes[spinner_Join.getSelectedItemPosition()];
        String number = Number_Join.getText().toString().trim();

        if(TextUtils.isEmpty(name)){
            Name_Join.setError("Please Enter Your Name");
        }

        else if (number.isEmpty() || number.length() < 10) {
            Number_Join.setError("Valid number is required");
            Number_Join.requestFocus();
        }

        else if(passward.length() < 8){
            Passward_Join.setError("Minimum 8 Characters Required");
        }

        else if(TextUtils.isEmpty(code)){
            ((TextView)spinner_Join.getSelectedView()).setError("Please Select Your Countery Code");
        }

        else if(TextUtils.isEmpty(passward)){
            Passward_Join.setError("Please set your Password");
        }

        else if(TextUtils.isEmpty(conferm_passward)) {
            Conferm_Passward_Join.setError("Please Conform Your Password");
        }

        else if(passward.equals(conferm_passward)) {
            LoadingBar.show();
            String phoneNumber = "+" + code + number;
            ValidatingAccount(name,phoneNumber,passward);
        }

        else{
            Passward_Join.setError("Please Match the Password");
            Conferm_Passward_Join.setError("Please Match the Password");
        }
    }

    private void ValidatingAccount(final String name, final String number, final String passward) {

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(!(dataSnapshot.child(parantName).child(number).exists())){
                    LoadingBar.dismiss();
                    Intent i = new Intent(MainActivity.this, ConfermOTP.class);
                    i.putExtra("Name",name);
                    i.putExtra("Passward",passward);
                    i.putExtra("Number",number);
                    startActivity(i);
                }

                else{
                    Toast.makeText(MainActivity.this,"This" + number + "is already exist please try with a different number",Toast.LENGTH_LONG).show();
                    LoadingBar.dismiss();
                    Intent i = new Intent(MainActivity.this,MainActivity.class);
                    startActivity(i);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //////////////////////////////////////////////////////Main/////////////////////////////////////////////////////////////

    private void MainFunction() {

        LoadingBar = new Dialog(MainActivity.this);
        LoadingBar.setContentView(R.layout.loading_dialog);
        LoadingBar.setCancelable(false);
        Paper.init(MainActivity.this);

        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.INTERNET)
                + ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_NETWORK_STATE)
                + ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_SMS)
                + ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.RECEIVE_SMS)
                + ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.SEND_SMS)
                + ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA)
                + ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                + ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                + ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.SYSTEM_ALERT_WINDOW)
                != PackageManager.PERMISSION_GRANTED){

            if(ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.INTERNET)
                    || ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.ACCESS_NETWORK_STATE)
                    || ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.READ_SMS)
                    || ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.RECEIVE_SMS)
                    || ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.SEND_SMS)
                    || ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.CAMERA)
                    || ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    || ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    || ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.SYSTEM_ALERT_WINDOW))
            {

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                builder.setTitle("Grant Those Permissions");
                builder.setMessage("Internet, Read and Write Storage and Send SMS");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(MainActivity.this,
                                new String[]{
                                        Manifest.permission.INTERNET,
                                        Manifest.permission.ACCESS_NETWORK_STATE,
                                        Manifest.permission.READ_SMS,
                                        Manifest.permission.RECEIVE_SMS,
                                        Manifest.permission.SEND_SMS,
                                        Manifest.permission.CAMERA,
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                        Manifest.permission.READ_EXTERNAL_STORAGE,
                                        Manifest.permission.SYSTEM_ALERT_WINDOW
                                }, ASK_MULTIPLE_PERMISSION_REQUEST_CODE);
                    }
                });

                builder.setNegativeButton("Cancel", null);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }else{
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{
                                Manifest.permission.INTERNET,
                                Manifest.permission.ACCESS_NETWORK_STATE,
                                Manifest.permission.READ_SMS,
                                Manifest.permission.RECEIVE_SMS,
                                Manifest.permission.SEND_SMS,
                                Manifest.permission.CAMERA,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.SYSTEM_ALERT_WINDOW
                        },ASK_MULTIPLE_PERMISSION_REQUEST_CODE);
            }
        }else{
            Toast.makeText(getApplicationContext(), "Permission is already Granted", Toast.LENGTH_SHORT).show();
        }

        String UserPhoneKey = Paper.book().read(Prevalant.userPhoneKey);
        String UserPassward = Paper.book().read(Prevalant.userPasswardKey);

        if(!TextUtils.isEmpty(UserPhoneKey) && !TextUtils.isEmpty(UserPassward)){

            LoadingBar.show();

            Toast.makeText(this, UserPhoneKey + " " + UserPassward, Toast.LENGTH_SHORT).show();

            CheckFireBase(UserPhoneKey, UserPassward);
        }

    }

    private void CheckFireBase(final String Number, final String passward){

        try{

            final DatabaseReference RootRef = FirebaseDatabase.getInstance().getReference();

            RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (dataSnapshot.child(parantName).exists()) {

                        Users userdata = dataSnapshot.child(parantName).child(Number).getValue(Users.class);

                        String NNAA = dataSnapshot.child(parantName).child(Number).child("Name").getValue().toString();
                        String NNUU = dataSnapshot.child(parantName).child(Number).child("Number").getValue().toString();

                        if(dataSnapshot.child(parantName).child(Number).child("Image").exists()){
                            IIMM = dataSnapshot.child(parantName).child(Number).child("Image").getValue().toString();
                        }

                        if (userdata.getNumber().equals(Number)) {

                            if (userdata.getPassward().equals(passward)) {
                                    try {
                                            Toast.makeText(MainActivity.this, "Logged in Successfully", Toast.LENGTH_LONG).show();
                                            LoadingBar.dismiss();
                                            Paper.book().write(Prevalant.UserNameA, NNAA);
                                            Paper.book().write(Prevalant.UserImageA, IIMM);
                                            Paper.book().write(Prevalant.UserNumberA, NNUU);
                                            Paper.book().write(Prevalant.userPhoneKey, NNUU);
                                            Paper.book().write(Prevalant.userPasswardKey, passward);
                                            Intent i = new Intent(MainActivity.this, HomeActivity.class);
                                            Prevalant.currentOnlineUser = userdata;
                                            Paper.book().write(Prevalant.userName, userdata);
                                            i.putExtra("eeee", "LA");
                                            startActivity(i);

                                    } catch (Exception e) {
                                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                            } else {
                                Toast.makeText(MainActivity.this, "Wrong Password", Toast.LENGTH_LONG).show();
                                LoadingBar.dismiss();
                                Toast.makeText(MainActivity.this, "Please Try Again", Toast.LENGTH_LONG).show();
                            }
                        }else {
                            Toast.makeText(MainActivity.this, "Account with this " + Number + " id does not exsit", Toast.LENGTH_LONG).show();
                            LoadingBar.dismiss();
                            Toast.makeText(MainActivity.this, "Please Try Again", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "Account with this " + Number + " id does not exsit", Toast.LENGTH_LONG).show();
                        LoadingBar.dismiss();
                        Toast.makeText(MainActivity.this, "Please Create a new Account", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        catch (Exception e){
            Toast.makeText(MainActivity.this,e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else {
                Toast.makeText(MainActivity.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        moveTaskToBack(true);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

}