package com.example.apstalvez;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    GoogleMap map;
    private ActivityMainBinding binding;
    private final List<Place> placeList = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getDataFireBase();

    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    private void getDataFireBase(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        placeList.forEach(place -> {
//            db.collection("Places")
//                    .add(place)
//                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                        @Override
//                        public void onSuccess(DocumentReference documentReference) {
//                            Log.d("Tag", "onSuccess: " + "success");
//                            Toast.makeText(MainActivity.this, "success", Toast.LENGTH_SHORT).show();
//                        }
//                    })
//                    .addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            Log.d("Tag", "onSuccess: " + e);
//                        }
//                    });
//        });


        db.collection("Places")
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        for(QueryDocumentSnapshot ds : task.getResult()){
                            Place place = new Place("", new LatLng(0,0), "");
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
    private void addToMaps(){
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
    private void addMark(GoogleMap googleMap){
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
}
