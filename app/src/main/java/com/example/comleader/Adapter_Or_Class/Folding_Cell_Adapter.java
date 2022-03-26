package com.example.comleader.Adapter_Or_Class;

import android.app.Dialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.comleader.HomeActivity;
import com.example.comleader.MapsActivity;
import com.example.comleader.Modal.Cus_Detail_Model;
import com.example.comleader.Modal.MemModel;
import com.example.comleader.Prevalant.Prevalant;
import com.example.comleader.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ramotion.foldingcell.FoldingCell;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class Folding_Cell_Adapter extends ArrayAdapter<MemModel> {

    private HashSet<Integer> unfoldedIndexes = new HashSet<>();
    private View.OnClickListener defaultRequestBtnClickListener;
    public View b;
    Context cc;
    ArrayList<MemModel> mod;

    private JsonObjectRequest jsonObjectRequest;

    final private String FCM_API = "https://fcm.googleapis.com/fcm/send";
    final private String serverKey = "key=" + "Your Firebase Server Key";

    final private String contentType = "application/json";
    final String TAG = "NOTIFICATION TAG";

    String NOTIFICATION_TITLE;
    String NOTIFICATION_MESSAGE;
    String TOPIC;

    EditText topic, title;
    Button sendN;

    private Dialog ialog;

    public Folding_Cell_Adapter(Context context, ArrayList<MemModel> objects) {
        super(context, 0, objects);

        cc = context;
        mod = objects;

        ialog = new Dialog(cc);
        ialog.setContentView(R.layout.new_a);
        ialog.setCancelable(true);
        title = ialog.findViewById(R.id.title_c);
        topic = ialog.findViewById(R.id.topic_c);
        sendN = ialog.findViewById(R.id.send_c);

    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        // get item for selected view
        MemModel item = getItem(position);
        // if cell is exists - reuse it, if not - create the new one from resource
        FoldingCell cell = (FoldingCell) convertView;
        ViewHolder viewHolder;

//        cell.fold(true);

        if (cell == null) {
            viewHolder = new ViewHolder();
            LayoutInflater vi = LayoutInflater.from(getContext());
            cell = (FoldingCell) vi.inflate(R.layout.shu_cus_layout, parent, false);



            viewHolder.title_name = cell.findViewById(R.id.title_name);
            viewHolder.title_no = cell.findViewById(R.id.title_no);
            viewHolder.title_pic = cell.findViewById(R.id.title_pic);
            viewHolder.title_call = cell.findViewById(R.id.title_OnCallTxt);
            viewHolder.title_on_off = cell.findViewById(R.id.title_OnOffTxt);
            viewHolder.title_callI = cell.findViewById(R.id.title_OnCallInd);
            viewHolder.title_on_offI = cell.findViewById(R.id.title_OnOffInd);

            viewHolder.body_name = cell.findViewById(R.id.body_cus_name);
            viewHolder.body_no = cell.findViewById(R.id.body_cus_num);
            viewHolder.body_address = cell.findViewById(R.id.body_cus_address);
            viewHolder.body_pic = cell.findViewById(R.id.body_cus_pic);
            viewHolder.body_a_time = cell.findViewById(R.id.body_cus_date);
            viewHolder.body_call = cell.findViewById(R.id.body_OnCallTxt);
            viewHolder.body_callI = cell.findViewById(R.id.body_OnCallInd);
            viewHolder.body_on_off = cell.findViewById(R.id.body_OnOffTxt);
            viewHolder.body_on_offI = cell.findViewById(R.id.body_OnOffInd);
            viewHolder.body_location = cell.findViewById(R.id.see_location);
            viewHolder.body_see_cus = cell.findViewById(R.id.see_cus);
            viewHolder.body_sendNoti = cell.findViewById(R.id.send_noti);
            viewHolder.body_sus = cell.findViewById(R.id.Suspend);

            cell.setTag(viewHolder);

            Picasso.get().load(mod.get(position).getImage()).centerCrop().fit().into(viewHolder.title_pic);
            Picasso.get().load(mod.get(position).getImage()).centerCrop().fit().into(viewHolder.body_pic);

            viewHolder.title_no.setText(mod.get(position).getName());
            viewHolder.title_name.setText(mod.get(position).getNumber());

            viewHolder.body_name.setText(mod.get(position).getName());
            viewHolder.body_no.setText(mod.get(position).getNumber());
            viewHolder.body_a_time.setText(mod.get(position).getAppDate());
            viewHolder.body_address.setText(mod.get(position).getAddress());

            viewHolder.body_on_off.setText(mod.get(position).getOnline());
            viewHolder.body_call.setText(mod.get(position).getOnCall());

            viewHolder.title_on_off.setText(mod.get(position).getOnline());
            viewHolder.title_call.setText(mod.get(position).getOnCall());

            if(mod.get(position).getOnline().equals("Online") && mod.get(position).getOnCall().equals("On Call")){
                viewHolder.body_on_offI.setImageResource(R.color.green);
                viewHolder.title_on_offI.setImageResource(R.color.green);

                viewHolder.body_callI.setImageResource(R.color.green);
                viewHolder.title_callI.setImageResource(R.color.green);
            } else if(!mod.get(position).getOnline().equals("Online") && mod.get(position).getOnCall().equals("On Call")){
                viewHolder.body_on_offI.setImageResource(R.color.red);
                viewHolder.title_on_offI.setImageResource(R.color.red);

                viewHolder.body_callI.setImageResource(R.color.green);
                viewHolder.title_callI.setImageResource(R.color.green);
            } else if(mod.get(position).getOnline().equals("Online") && !mod.get(position).getOnCall().equals("On Call")){
                viewHolder.body_on_offI.setImageResource(R.color.green);
                viewHolder.title_on_offI.setImageResource(R.color.green);

                viewHolder.body_callI.setImageResource(R.color.red);
                viewHolder.title_callI.setImageResource(R.color.red);
            } else if(!mod.get(position).getOnline().equals("Online") && !mod.get(position).getOnCall().equals("On Call")){
                viewHolder.body_on_offI.setImageResource(R.color.red);
                viewHolder.title_on_offI.setImageResource(R.color.red);

                viewHolder.body_callI.setImageResource(R.color.red);
                viewHolder.title_callI.setImageResource(R.color.red);
            }

            if(mod.get(position).getSuspend().equals("A")){

                viewHolder.body_see_cus.setVisibility(View.GONE);
                viewHolder.body_location.setVisibility(View.GONE);
                viewHolder.body_sus.setText("Approve");

            }

            viewHolder.body_see_cus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Paper.book().write(Prevalant.Mem_No, mod.get(position).getNumber());
                    ((HomeActivity)cc).setFrag("Cus");

                }
            });

            viewHolder.body_sendNoti.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ialog.show();

                    sendN.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            SendNotification(mod.get(position).getToken());

                        }
                    });

                }
            });

            sendN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    SendNotification(mod.get(position).getToken());

                    Toast.makeText(cc, "Lo", Toast.LENGTH_SHORT).show();

                }
            });

            viewHolder.body_location.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Paper.book().write(Prevalant.Mem_No, mod.get(position).getNumber());
//                    ((HomeActivity)cc).setFrag("Loc");
                    Intent i = new Intent(cc, MapsActivity.class);
                    cc.startActivity(i);

                }
            });

            viewHolder.body_sus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(mod.get(position).getSuspend().equals("Approved")){

                        Suspend(mod.get(position).getNumber());

                    } else {

                        UnSuspend(mod.get(position).getNumber());

                    }

                }
            });

            cell.fold(true);

        } else {
            // for existing cell set valid valid state(without animation)
            if (unfoldedIndexes.contains(position)) {
                cell.unfold(true);
            } else {
                cell.fold(true);
            }
            viewHolder = (ViewHolder) cell.getTag();
        }

        if (null == item)
            return cell;

        return cell;
    }

    private void SendNotification(String To) {

        String Title_V = title.getText().toString();
        String TopiC_V = topic.getText().toString();

        try{
            Toast.makeText(cc, Title_V + " " + TopiC_V, Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Toast.makeText(cc, "e "+ e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        if(TextUtils.isEmpty(Title_V)){
            title.setError("Please Enter");
            Toast.makeText(cc, "Title E", Toast.LENGTH_SHORT).show();
            Log.i(TAG, "Didn't work");
        } else if(TextUtils.isEmpty(TopiC_V)){
            topic.setError("Please Enter");
            Toast.makeText(cc, "Topic E", Toast.LENGTH_SHORT).show();
            Log.i(TAG, "Didn't work");
        } else {
            Toast.makeText(cc, "Lo", Toast.LENGTH_SHORT).show();
            sendNotificationss(To, Title_V, TopiC_V);
        }

    }

    private void sendNotificationss(String to, String ti, String me) {

        TOPIC = to; //topic must match with what the receiver subscribed to
        NOTIFICATION_TITLE = ti;
        NOTIFICATION_MESSAGE = me;

        JSONObject notification = new JSONObject();
        JSONObject notifcationBody = new JSONObject();
        try {
            notifcationBody.put("title", NOTIFICATION_TITLE);
            notifcationBody.put("message", NOTIFICATION_MESSAGE);

            notification.put("to", TOPIC);
            notification.put("data", notifcationBody);
        } catch (JSONException e) {
            Toast.makeText(cc, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        sendNotification(notification);

    }

    private void sendNotification(JSONObject notification) {

        jsonObjectRequest = new JsonObjectRequest(FCM_API, notification,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i(TAG, "onResponse: " + response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(cc, "Request error", Toast.LENGTH_LONG).show();
                        Log.i(TAG, "onErrorResponse: Didn't work");
                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", serverKey);
                params.put("Content-Type", contentType);
                return params;
            }
        };

        MySingleton.getInstance(cc).addToRequestQueue(jsonObjectRequest);

        Toast.makeText(cc, "Send Successfully", Toast.LENGTH_SHORT).show();

        ialog.dismiss();

    }

    // simple methods for register cell state changes
    public void registerToggle(int position) {
        if (unfoldedIndexes.contains(position))
            registerFold(position);
        else
            registerUnfold(position);
    }

    public void registerFold(int position) {
        unfoldedIndexes.remove(position);
    }

    public void registerUnfold(int position) {
        unfoldedIndexes.add(position);
    }

    public View.OnClickListener getDefaultRequestBtnClickListener() {
        return defaultRequestBtnClickListener;
    }

    public void setDefaultRequestBtnClickListener(View.OnClickListener defaultRequestBtnClickListener) {
        this.defaultRequestBtnClickListener = defaultRequestBtnClickListener;
    }

    // View lookup cache
    private static class ViewHolder {
        TextView title_name, title_no, title_on_off, title_call;
        ImageView title_pic;
        TextView body_name, body_no, body_a_time, body_address, body_on_off, body_call, body_sus, body_location, body_sendNoti, body_see_cus;
        ImageView body_pic;
        CircleImageView body_on_offI, body_callI, title_on_offI, title_callI;
    }

    public void Suspend(String h){

        AlertDialog.Builder aD = new AlertDialog.Builder(cc);
                    aD.setMessage("Are You Sure, Your Wanna Suspend this User..");
                    aD.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            DatabaseReference RootRef = FirebaseDatabase.getInstance().getReference();

                            HashMap<String, Object> userdata = new HashMap<>();
                            userdata.put("Suspend", "Suspend");

                            RootRef.child("User").child(h).updateChildren(userdata).
                            addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isComplete()) {

                                        Toast.makeText(cc, "Done", Toast.LENGTH_LONG).show();

                                    }
                                }
                            });

                        }
                    });
                    aD.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    AlertDialog alertDialog = aD.create();
                    alertDialog.show();

    }

    public void UnSuspend(String h){

        AlertDialog.Builder aD = new AlertDialog.Builder(cc);
                    aD.setMessage("Your Wanna UnSuspend this User..");
                    aD.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            DatabaseReference RootRef = FirebaseDatabase.getInstance().getReference();

                            HashMap<String, Object> userdata = new HashMap<>();
                            userdata.put("Suspend", "Approved");

                            RootRef.child("User").child(h).updateChildren(userdata).
                            addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isComplete()) {

                                        Toast.makeText(cc, "Done", Toast.LENGTH_LONG).show();

                                    }
                                }
                            });

                        }
                    });
                    aD.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    AlertDialog alertDialog = aD.create();
                    alertDialog.show();

    }

}
