package com.example.comleader.Fragment;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.comleader.HomeActivity;
import com.example.comleader.Prevalant.Prevalant;
import com.example.comleader.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.UUID;

import io.paperdb.Paper;

import static android.app.Activity.RESULT_OK;

public class EditCusFragment extends Fragment {

    private static final int RESULT_LOAD_IMAGE = 1;

    private StorageReference mStorage;

    private String Image, Cus_Pid, Pre_Im;
    private Uri UImage;

    private String Cus_Name, Cus_Number, Cus_Address, Cus_Pin, Cus_Plan, Cus_Months, KeyWard, storeCurruntDate, storeCurruntTime, Who;

    private EditText Cus_Name_T, Cus_Number_T, Cus_Address_T, Cus_Pin_T, Cus_Plan_T, Cus_Months_T;

    private ImageView PicPhoto, edit_C;
    private Button AddCustomer, AddPic;
    private DatabaseReference ProductRef;
    private String productRandomKey, downloadImageUri;
    private StorageReference ProductImageRef;
    int dd = 0, ii = 0;
    private Dialog LoadingBar;
    private int A = 0, C = 0;
    private static final int GallaryPic = 1;

    private StorageTask uploadTask;
    private StorageReference profilePictureRef;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_edit_cus, container, false);

        Paper.init(getActivity());

        PicPhoto = root.findViewById(R.id.PicPhoto_E);

        AddCustomer = root.findViewById(R.id.AddCustomer_E);
        AddPic = root.findViewById(R.id.PicButton_E);

        Cus_Name_T = root.findViewById(R.id.Cus_Name_E);
        Cus_Number_T = root.findViewById(R.id.Cus_Number_E);
        Cus_Address_T = root.findViewById(R.id.Cus_Address_E);
        Cus_Pin_T = root.findViewById(R.id.Cus_Pin_E);
        Cus_Plan_T = root.findViewById(R.id.Cus_Plan_E);
        Cus_Months_T = root.findViewById(R.id.Cus_Month_E);

        edit_C = root.findViewById(R.id.edit_close);

        Cus_Pid = Paper.book().read(Prevalant.Cus_PID);

        LoadingBar = new Dialog(getActivity());
        LoadingBar.setContentView(R.layout.loading_dialog);
        LoadingBar.setCancelable(false);

        profilePictureRef = FirebaseStorage.getInstance().getReference().child("Picture");

        GetData();

        AddPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(ActivityCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){

                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
                    return;
                }

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"), RESULT_LOAD_IMAGE);

            }
        });

        AddCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Cus_Name = Cus_Name_T.getText().toString();
                Cus_Address = Cus_Address_T.getText().toString();
                Cus_Number = Cus_Number_T.getText().toString();
                Cus_Pin = Cus_Pin_T.getText().toString();
                Cus_Plan = Cus_Plan_T.getText().toString();
                Cus_Months = Cus_Months_T.getText().toString();

                if(TextUtils.isEmpty(Cus_Name)){
                    Cus_Name_T.setError("Please Enter");
                }else if(TextUtils.isEmpty(Cus_Address)){
                    Cus_Address_T.setError("Please Enter");
                }else if(TextUtils.isEmpty(Cus_Number)){
                    Cus_Number_T.setError("Please Enter");
                }else if(TextUtils.isEmpty(Cus_Pin)){
                    Cus_Pin_T.setError("Please Enter");
                }else if(TextUtils.isEmpty(Cus_Plan)){
                    Cus_Plan_T.setError("Please Enter");
                }else if(TextUtils.isEmpty(Cus_Months)){
                    Cus_Months_T.setError("Please Enter");
                }else{

                    if(Image.equals(Pre_Im)){
                        Add_Customer();
                    }else{
                        uploadImage(UImage);
                    }
                }
            }
        });


        return root;

    }

    private void GetData() {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Cus").child(Cus_Pid);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()){

                    Cus_Name = snapshot.child("Cus_Name").getValue().toString();
                    Cus_Number = snapshot.child("Cus_Number").getValue().toString();
                    Cus_Plan = snapshot.child("Cus_Plan").getValue().toString();
                    Cus_Address = snapshot.child("Cus_Address").getValue().toString();
                    Cus_Pin = snapshot.child("Cus_Pin").getValue().toString();
                    Image = snapshot.child("Cus_Image").getValue().toString();
                    Cus_Months = snapshot.child("Cus_Month").getValue().toString();
                    KeyWard = snapshot.child("Keyward").getValue().toString();
                    storeCurruntDate = snapshot.child("Date").getValue().toString();
                    storeCurruntTime = snapshot.child("Time").getValue().toString();
                    Who = snapshot.child("Who").getValue().toString();

                    Pre_Im = snapshot.child("Cus_Image").getValue().toString();

                    Cus_Name_T.setText(Cus_Name, TextView.BufferType.EDITABLE);
                    Cus_Number_T.setText(Cus_Number, TextView.BufferType.EDITABLE);
                    Cus_Address_T.setText(Cus_Address, TextView.BufferType.EDITABLE);
                    Cus_Pin_T.setText(Cus_Pin, TextView.BufferType.EDITABLE);
                    Cus_Plan_T.setText(Cus_Plan, TextView.BufferType.EDITABLE);
                    Cus_Months_T.setText(Cus_Months, TextView.BufferType.EDITABLE);

                    Picasso.get().load(Image).fit().centerCrop().into(PicPhoto);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void uploadImage(Uri imageUri) {


        try{

            final StorageReference filePath = profilePictureRef.child(imageUri.getLastPathSegment().substring(0, 4) + UUID.randomUUID()
                    .toString().substring(0,4) + ".jpg");

            final UploadTask uploadTask = filePath.putFile(imageUri);

            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    String massage = e.toString();
                    Toast.makeText(getActivity(),"Error! " + massage,Toast.LENGTH_LONG).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(getActivity(),"Product Image Upload Successfully",Toast.LENGTH_LONG).show();

                    Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                LoadingBar.dismiss();
                                throw task.getException();
                            }

                            downloadImageUri = filePath.getDownloadUrl().toString();
                            return filePath.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if(task.isSuccessful()){

                                Image = task.getResult().toString();
                                Toast.makeText(getActivity(),"Getting Product Image 1 Uri Successfully",Toast.LENGTH_LONG).show();

                                Add_Customer();

                            }
                        }
                    });
                }
            });

        }catch(Exception e){
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void Add_Customer() {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Cus");

//        Calendar calendar = Calendar.getInstance();
//
//        SimpleDateFormat currentDate = new SimpleDateFormat("yyyy-MM-dd");
//        storeCurruntDate = currentDate.format(calendar.getTime());
//
//        SimpleDateFormat currentTime = new SimpleDateFormat(" hh:mm:ss a");
//        storeCurruntTime = currentTime.format(calendar.getTime());

        productRandomKey = Cus_Pid;

        HashMap<String, Object> cu = new HashMap<>();

        cu.put("Cus_Name", Cus_Name);
        cu.put("Cus_Number", Cus_Number);
        cu.put("Cus_Plan", Cus_Plan);
        cu.put("Cus_Address", Cus_Address);
        cu.put("Cus_Pin", Cus_Pin);
        cu.put("Cus_Image", Image);
        cu.put("Cus_Month", Cus_Months);
        cu.put("Keyward", KeyWard);
        cu.put("Date", storeCurruntDate);
        cu.put("Time", storeCurruntTime);
        cu.put("PID", productRandomKey);
        cu.put("Who", Who);

        ref.child(productRandomKey).updateChildren(cu).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(getActivity(), "Add SuccessFully", Toast.LENGTH_SHORT).show();
                ((HomeActivity) getActivity()).setFrag("Mem");
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK) {

            UImage = data.getData();

            PicPhoto.setImageURI(UImage);

            AddCustomer.setVisibility(View.VISIBLE);

        } else {
            Toast.makeText(getActivity(), "Error try Again....", Toast.LENGTH_SHORT).show();
        }

    }

}
