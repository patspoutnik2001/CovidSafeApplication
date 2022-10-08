package com.example.covidsafeapplication;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.covidsafeapplication.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private ArrayList<Batiment> batimentArrayList = new ArrayList<>();
    private ArrayList<Marker> markers = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.maps);
        mapFragment.getMapAsync(this);
    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {

        System.out.println(marker.getId()+ "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");

        return false;
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
                                    LatLng latLng = new LatLng(lat, lng);
                                    // print long and lat on the map
                                    addMarker(googleMap, latLng, batiment);


                                } catch (NumberFormatException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                });
    }

    private void addMarker(GoogleMap googleMap, LatLng latlng, Batiment batiment) {
        Marker marker = googleMap.addMarker(new MarkerOptions().position(latlng).title(batiment.nomBatiment));
        markers.add(marker);
        mMap = googleMap;

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                // on marker click we are getting the title of our marker
                // which is clicked and displaying it in a toast message.
                String markerName = marker.getTitle();
                Toast.makeText(MapsActivity.this, "Clicked location is " + markerName, Toast.LENGTH_SHORT).show();

                // intent
                Intent intent = new Intent(MapsActivity.this, ListeActivity.class);
                intent.putExtra("idBatiment", batiment.idBatiment);
                startActivity(intent);



                return false;
            }
        });
    }
}

