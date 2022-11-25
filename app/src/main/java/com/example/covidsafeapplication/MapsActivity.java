package com.example.covidsafeapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.covidsafeapplication.databinding.ActivityMapsBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicReference;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    //  private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private ArrayList<Batiment> batimentArrayList = new ArrayList<>();
    // public ArrayList<Marker> markers = new ArrayList<>();
    // hashmap of makers and their GoogleMap
    public HashMap<Marker, Batiment> markerMap = new HashMap<>();
    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=this;

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.maps);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("batiment")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            LatLng l = new LatLng(10f,10f);
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                try {
                                    String address = document.get("address").toString();
                                    int idBatiment = Integer.parseInt(document.get("idBatiment").toString());
                                    String nomBatiment = document.getString("nomBatiment");
                                    double lat = Double.parseDouble(document.get("latitude").toString());
                                    double lng = Double.parseDouble(document.get("longitude").toString());
                                    Batiment batiment = new Batiment(idBatiment, nomBatiment, address, lat, lng);
                                    batimentArrayList.add(batiment);

                                    // add marker on the map
                                    LatLng latLngMark = new LatLng(lat, lng);
                                    // the zoom will be on last one added
                                    String url = "https://api.openweathermap.org/data/2.5/weather?lat="+lat + "&lon="+ lng + "&appid=5bdff022d351650cee55cec49c82c67b&units=metric";
                                    l=latLngMark;
                                    Ion.with(context)
                                            .load(url)
                                            .asJsonObject()
                                            .setCallback((e, result) -> {
                                                if (e != null) {
                                                    Toast.makeText(context, "Error "+e.toString(), Toast.LENGTH_SHORT).show();
                                                    return;
                                                }
                                                if (result != null) {
                                                    System.out.println(url);
                                                    String temp = result.get("main").getAsJsonObject().get("temp").getAsString();
                                                    // transform the temp from kelvin to celsius
                                                    //float celsius = Float.parseFloat(temp) - 283.15f + 1;
                                                    //tvTemp.setText("Temperature: " + temp + "°C");
                                                    Marker marker = googleMap.addMarker(new MarkerOptions().position(latLngMark).title(batiment.nomBatiment).snippet(temp+"°C"));
                                                    markerMap.put(marker, batiment);
                                                }
                                            });



                                    // if market is clicked, go to the next activity

                                    googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                                        @Override
                                        public boolean onMarkerClick(Marker marker) {
                                            // intent
                                            Intent intent = new Intent(MapsActivity.this, LocalListActivity.class);

                                            // if maker  is equal to the marker in the hashmap
                                            if (markerMap.containsKey(marker)) {
                                                // get the batiment object
                                                Batiment batiment = markerMap.get(marker);

                                                System.out.println("batiment id: " + batiment.idBatiment);
                                                System.out.println("batiment name: " + batiment.nomBatiment);


                                                intent.putExtra("batid", batiment.idBatiment);
                                                intent.putExtra("batName", batiment.nomBatiment);
                                                startActivity(intent);
                                                return false;
                                            }
                                            return false;
                                        }
                                    });
                                } catch (NumberFormatException e) {
                                    e.printStackTrace();
                                }
                            }
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(l,10f));
                        }
                    }
                });
    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        return false;
    }
}

