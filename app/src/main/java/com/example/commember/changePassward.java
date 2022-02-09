package com.example.commember;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class changePassward extends AppCompatActivity {

    private String verificationId, Uid, num;
    private FirebaseAuth mAuth;
    private EditText newPassward, confermPassward, otpEnter;
    private Button changePassward, confermOTPButton;
    private boolean doubleclick = false, yy = false;
    private Dialog LoadingBar;
    private ProgressBar progressBar;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_passward);

        try {
            num = getIntent().getStringExtra("PhoneNumber");
            Uid = getIntent().getStringExtra("UserId");

            newPassward = findViewById(R.id.YNP);
            confermPassward = findViewById(R.id.CP);
            otpEnter = findViewById(R.id.PutOTP);

            changePassward = findViewById(R.id.changePassward);
            confermOTPButton = findViewById(R.id.confermOTPButton);

            LoadingBar = new Dialog(changePassward.this);
            LoadingBar.setContentView(R.layout.loading_dialog);
            LoadingBar.setCancelable(false);

            progressBar = findViewById(R.id.pB);

            mAuth = FirebaseAuth.getInstance();

            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                @Override
                public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                    Toast.makeText(changePassward.this, "Code Send", Toast.LENGTH_LONG).show();
                    super.onCodeSent(s, forceResendingToken);
                    verificationId = s;
                }

                @Override
                public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                    String code = phoneAuthCredential.getSmsCode();
                    final String phonenumber = getIntent().getStringExtra("PhoneNumber");
                    if (code != null) {
                        otpEnter.setText(code);
                        verifyCode(code, phonenumber);
                    }
                }

                @Override
                public void onVerificationFailed(@NonNull FirebaseException e) {
                    Toast.makeText(changePassward.this, "1-" + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            };

            sendVerificationCode(num, mCallBack);

            confermOTPButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String code = otpEnter.getText().toString().trim();
                    String PhoneN = com.example.commember.changePassward.this.getIntent().getStringExtra("PhoneNumber");

                    if (code.isEmpty() || code.length() < 6) {

                        otpEnter.setError("Enter code...");
                        otpEnter.requestFocus();
                        return;
                    }
                    com.example.commember.changePassward.this.verifyCode(code, PhoneN);
                }
            });

            changePassward.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String New_Passward = newPassward.getText().toString();
                    String Conferm_Passward = confermPassward.getText().toString();

                    if (New_Passward.isEmpty() && Conferm_Passward.isEmpty()) {
                        newPassward.setError("Enter New Password");
                        newPassward.requestFocus();
                    } else if (New_Passward.length() < 8 && Conferm_Passward.length() < 8) {
                        newPassward.setError("Minimum 8 Characters Required");
                        confermPassward.setError("Minimum 8 Characters Required");
                    } else if (New_Passward.equals(Conferm_Passward)) {
                        LoadingBar.show();
                        com.example.commember.changePassward.this.changePasswardN(New_Passward, num);
                    } else {
                        confermPassward.setError("Please Match the password");
                        confermPassward.requestFocus();
                    }
                }
            });
        }
        catch (Exception e){
            Toast.makeText(changePassward.this, "2-" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void sendVerificationCode(String num, PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBack) {
        Toast.makeText(changePassward.this,num,Toast.LENGTH_LONG).show();
        progressBar.setVisibility(View.VISIBLE);
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                num,
                60,
                TimeUnit.SECONDS,
                changePassward.this,
                mCallBack
        );
    }

    private void changePasswardN(String newPassward, String num) {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("User");

        HashMap<String, Object> map = new HashMap<>();

        map.put("Passward", newPassward);

        ref.child(Uid).updateChildren(map);

        LoadingBar.dismiss();

        Intent i = new Intent(changePassward.this, MainActivity.class);
        startActivity(i);
        Toast.makeText(changePassward.this,"Password Updated Successfully",Toast.LENGTH_LONG).show();
        Toast.makeText(changePassward.this,"Please Login",Toast.LENGTH_LONG).show();
        finish();

    }

    private void verifyCode(String code, String PhoneNumber) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        Toast.makeText(changePassward.this,"verifyCode",Toast.LENGTH_LONG).show();
        signInWithCredential(credential, PhoneNumber);
    }

    private void signInWithCredential(PhoneAuthCredential credential, final String PhoneNumberF) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            newPassward.setVisibility(View.VISIBLE);
                            confermPassward.setVisibility(View.VISIBLE);
                            changePassward.setVisibility(View.VISIBLE);

                        } else {
                            Toast.makeText(com.example.commember.changePassward.this, "3-" + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    @Override
    public void onBackPressed() {

        if (doubleclick) {
            Intent o = new Intent(changePassward.this, MainActivity.class);
            o.putExtra("eeee", "CaA");
            startActivity(o);
            return;
        }

        this.doubleclick = true;
        Toast.makeText(changePassward.this, "Press Wait", Toast.LENGTH_LONG).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleclick = false;
            }
        }, 2000);

    }

}