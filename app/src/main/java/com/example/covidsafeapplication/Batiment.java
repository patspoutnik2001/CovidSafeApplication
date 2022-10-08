package com.example.covidsafeapplication;

import android.location.Geocoder;

public class Batiment {
    int idBatiment;
    String nomBatiment;
    String adress;
    Double latitude;
    Double longitude;


    Batiment(int id, String n, String a, Double lat, Double lng){
        idBatiment=id;
        nomBatiment=n;
        adress=a;
        latitude=lat;
        longitude=lng;

    }

    Batiment(int id, String n){
        idBatiment=id;
        nomBatiment=n;
    }



}
