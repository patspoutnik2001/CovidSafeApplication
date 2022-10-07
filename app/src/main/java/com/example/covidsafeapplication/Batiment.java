package com.example.covidsafeapplication;

import android.location.Geocoder;

public class Batiment {
    int idBatiment;
    String nomBatiment;
    Geocoder geocoder;

    Batiment(int i, String n){
        idBatiment=i;
        nomBatiment=n;
        geocoder.getFromLocationName(nomBatiment,1);
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

        addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

        String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
        String city = addresses.get(0).getLocality();
        String state = addresses.get(0).getAdminArea();
        String country = addresses.get(0).getCountryName();
        String postalCode = addresses.get(0).getPostalCode();
        String knownName = addresses.get(0).getFeatureName(); // Onl

    }



}
