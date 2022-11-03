package com.example.apstalvez.view;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.apstalvez.R;
import com.example.apstalvez.adapter.MarkerInfoAdapter;
import com.example.apstalvez.databinding.ActivityMainBinding;
import com.example.apstalvez.model.Place;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    private FirebaseAuth mAuth;


    GoogleMap map;
    private final List<Place> placeList = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.ivUser.setOnClickListener(view -> {
            startActivity(new Intent(this, AccountActivity.class));
        });
        getDataFirebase();

    }

    @Override
    protected void onStart() {
        super.onStart();
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        if(currentUser != null){
//        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void getDataFirebase() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("Places")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot ds : task.getResult()) {
                            Place place = new Place("", new LatLng(0, 0), "");
                            place.setName(ds.getString("name"));
                            place.setAddress(ds.getString("address"));

                            Object latLng = ds.get("latLng");
                            String[] splitLatLng = latLng.toString().split(",");
                            String latitude = splitLatLng[0].replaceAll("[\\\\[\\\\](){}]latitude=", "");
                            String longitude = splitLatLng[1].replaceAll("longitude=", "");
                            longitude = longitude.replaceAll("[\\\\[\\\\](){}]", "");

                            place.setLatLng(new LatLng(Double.parseDouble(latitude),
                                    Double.parseDouble(longitude)));

                            placeList.add(place);
                        }
                        addToMaps();
                    }
                });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void addToMaps() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_fragment);

        mapFragment.getMapAsync(googleMap -> {
            addMark(googleMap);

            googleMap.setInfoWindowAdapter(new MarkerInfoAdapter(this));
            googleMap.setOnMapLoadedCallback(() -> {
                LatLngBounds.Builder bounds = LatLngBounds.builder();
                placeList.forEach(place -> bounds.include(place.getLatLng()));
                googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds.build(), 100));
//                googleMap.setMyLocationEnabled(true);
            });
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void addMark(GoogleMap googleMap) {
        placeList.forEach(place -> {
            Marker marker = googleMap.addMarker(
                    new MarkerOptions()
                            .title(place.getName())
                            .snippet(place.getAddress())
                            .position(place.getLatLng())
            );
            marker.setTag(place);
        });
    }

    private void firebaseAuth(){
        mAuth = FirebaseAuth.getInstance();

    }
}
