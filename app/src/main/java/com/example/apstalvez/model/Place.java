package com.example.apstalvez.model;

import com.google.android.gms.maps.model.LatLng;

public class Place {
    private String name;
    private LatLng latLng;
    private String address;

    public Place(String name, LatLng latLng, String address) {
        this.name = name;
        this.latLng = latLng;
        this.address = address;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}
