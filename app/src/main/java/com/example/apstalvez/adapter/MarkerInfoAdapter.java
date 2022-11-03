package com.example.apstalvez.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.apstalvez.R;
import com.example.apstalvez.model.Place;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class MarkerInfoAdapter implements GoogleMap.InfoWindowAdapter {

    private Context context;
    public MarkerInfoAdapter(Context context) {
        this.context = context;
    }

    @Nullable
    @Override
    public View getInfoContents(@NonNull Marker marker) {
       View view =  LayoutInflater.from(context).inflate(R.layout.custom_marker_info, null);

        Place place = (Place) marker.getTag();

       TextView tvTitle = view.findViewById(R.id.tvTitle);
       TextView tvAddress = view.findViewById(R.id.tvAddress);

       tvTitle.setText(place.getName());
       tvAddress.setText(place.getAddress());
       return view;
    }

    @Nullable
    @Override
    public View getInfoWindow(@NonNull Marker marker) {
        return null;
    }
}
