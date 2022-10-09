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

    //  private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private ArrayList<Batiment> batimentArrayList = new ArrayList<>();
    // public ArrayList<Marker> markers = new ArrayList<>();
    // hashmap of makers and their GoogleMap
    public HashMap<Marker, Batiment> markerMap = new HashMap<>();


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

                                    Marker marker = googleMap.addMarker(new MarkerOptions().position(latLng).title(batiment.nomBatiment));
                                    markerMap.put(marker, batiment);

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

                                                System.out.println("batiment id: " + batiment.idBatiment+ "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
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
                        }
                    }
                });
    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        return false;
    }
}

