package com.example.commember.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;

import com.example.commember.HomeActivity;
import com.example.commember.Prevalant.Prevalant;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import io.paperdb.Paper;

public class PhoneStateBR extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent)
    {
        try
        {
            if(intent.getAction().equals("android.intent.action.SCREEN_ON")){

                DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("User").child(Paper.book().read(Prevalant.UserNumberA));

                HashMap<String, Object>  l = new HashMap<>();

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
                    NoCall();
                    ((HomeActivity)context).getLastLocation();
                }
                else if(stateStr.equals(TelephonyManager.EXTRA_STATE_OFFHOOK))
                {
                    OnCall();
                    ((HomeActivity)context).getLastLocation();
                }
                else if(stateStr.equals(TelephonyManager.EXTRA_STATE_RINGING))
                {
                    ((HomeActivity)context).getLastLocation();
                }
                else{
                    NoCall();
                }
            
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
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

}
