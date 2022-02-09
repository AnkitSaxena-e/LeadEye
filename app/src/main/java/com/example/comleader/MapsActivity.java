package com.example.comleader;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.widget.Toast;

import com.example.comleader.Prevalant.Prevalant;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    private float lati = -34, longit = 151;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("User").child(Paper.book().read(Prevalant.Mem_No));

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {

                    try {
                        lati = Float.parseFloat(snapshot.child("Latitude").getValue().toString());
                        longit = Float.parseFloat(snapshot.child("Longitude").getValue().toString());

                        Geocoder geocoder = new Geocoder(MapsActivity.this, Locale.getDefault());
                        List<Address> addressList = geocoder.getFromLocation(lati, longit, 1);
                        if(addressList.isEmpty()){
                            LatLng sydney = new LatLng(lati, longit);
                            mMap.addMarker(new MarkerOptions().position(sydney).title("Waiting Location"));
                            float zoom = 16.0f;
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, zoom));
                        } else{
                            LatLng sydney = new LatLng(lati, longit);
                            mMap.addMarker(new MarkerOptions().position(sydney).title(addressList.get(0).getFeatureName() + " " + addressList.get(0).getLocality() + " " + addressList.get(0).getAdminArea() + ", " + addressList.get(0).getCountryName()));
                            float zoom = 16.0f;
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, zoom));
                        }
                    } catch (Exception e){
                        Toast.makeText(MapsActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }



                    //                    mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // Add a marker in Sydney and move the camera
    }

}