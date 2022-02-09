package com.example.comleader;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class ConfermOTP extends AppCompatActivity {

    private String verificationId;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    private EditText editText, editAddress;
    private boolean doubleclick = false, yy = false;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBack;
    Button SignIn;
    String code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conferm_otp);

        progressBar = findViewById(R.id.progressbar);
        editText = findViewById(R.id.editTextCode);
//        editAddress = findViewById(R.id.editTextAddress);

        SignIn = findViewById(R.id.buttonSignIn);

        try {
            mAuth = FirebaseAuth.getInstance();

            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                @Override
                public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                    Toast.makeText(ConfermOTP.this, "Code Send", Toast.LENGTH_LONG).show();
                    super.onCodeSent(s, forceResendingToken);
                    verificationId = s;
                }

                @Override
                public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                    code = phoneAuthCredential.getSmsCode();
                    final String phonenumber = getIntent().getStringExtra("Number");
                    final String name = getIntent().getStringExtra("Name");
                    final String passward = getIntent().getStringExtra("Passward");
//                    String T = editAddress.getText().toString();

                    if (code != null) {
                        editText.setText(code);
                        verifyCode(code, name, passward, phonenumber);
                    }
                }

                @Override
                public void onVerificationFailed(FirebaseException e) {
                    Toast.makeText(ConfermOTP.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            };

            final String phonenumber = getIntent().getStringExtra("Number");
            final String name = getIntent().getStringExtra("Name");
            final String passward = getIntent().getStringExtra("Passward");

            sendVerificationCode(phonenumber, mCallBack);

            SignIn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    code = editText.getText().toString().trim();
                    String r = editAddress.getText().toString();

                    if (code.isEmpty() || code.length() < 6) {

                        editText.setError("Enter code...");
                        editText.requestFocus();
                        return;

                    } else if(TextUtils.isEmpty(r)){

                        Toast.makeText(ConfermOTP.this, "!Please Enter Your Good Address!", Toast.LENGTH_SHORT).show();

                    }
                    ConfermOTP.this.verifyCode(code, name, passward, phonenumber);
                }
            });
        } catch (Exception e) {
            Toast.makeText(ConfermOTP.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void sendVerificationCode(String phonenumber, PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBack) {
        Toast.makeText(ConfermOTP.this, phonenumber, Toast.LENGTH_LONG).show();
        progressBar.setVisibility(View.VISIBLE);
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phonenumber,
                60,
                TimeUnit.SECONDS,
                ConfermOTP.this,
                mCallBack
        );
        //TaskExecutors.MAIN_THREAD
        Toast.makeText(ConfermOTP.this, "Send Verification Code", Toast.LENGTH_LONG).show();
    }

    private void verifyCode(String code, String Name, String Passward, String PhoneNumber) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        Toast.makeText(ConfermOTP.this, "verifyCode", Toast.LENGTH_LONG).show();
        signInWithCredential(credential, Name, PhoneNumber, Passward);
    }

    private void signInWithCredential(PhoneAuthCredential credential, final String NameF, final String PhoneNumberF, final String PasswardF) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            ConfermOTP.this.AddAccount(NameF, PhoneNumberF, PasswardF);
                        } else {
                            Toast.makeText(ConfermOTP.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void AddAccount(String nameF, final String phoneNumberF, String passwardF) {

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        HashMap<String, Object> userdata = new HashMap<>();
        userdata.put("Name", nameF);
        userdata.put("Number", phoneNumberF);
        userdata.put("Passward", passwardF);
//        userdata.put("Suspend", "A");
        userdata.put("Token", "A");
        userdata.put("Image", "A");


        RootRef.child("Leader").child(phoneNumberF).updateChildren(userdata).
                addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isComplete()) {

                            Toast.makeText(ConfermOTP.this, "Congratulation, Your Account is Created Successfully", Toast.LENGTH_LONG).show();
                            Intent i = new Intent(ConfermOTP.this, MainActivity.class);
                            ConfermOTP.this.startActivity(i);
                            Toast.makeText(ConfermOTP.this, "Please Login", Toast.LENGTH_LONG).show();

                        }
                    }
                });
    }

    @Override
    public void onBackPressed() {

        if (doubleclick) {
            Intent o = new Intent(ConfermOTP.this, MainActivity.class);
            o.putExtra("eeee", "CaA");
            startActivity(o);
            return;
        }

        this.doubleclick = true;
        Toast.makeText(ConfermOTP.this, "Press Wait", Toast.LENGTH_LONG).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleclick = false;
            }
        }, 2000);

    }
}

