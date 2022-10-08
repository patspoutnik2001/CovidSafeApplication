package com.example.covidsafeapplication;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class LocalListActivity extends AppCompatActivity {


    ListView local_listView;
    TextView display_tv;
    Batiment batiment;
    ArrayList<Local> locals=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_list);

        local_listView=findViewById(R.id.local_list);
        display_tv=findViewById(R.id.local_name_display);

        Bundle extras = getIntent().getExtras();
        if (extras!=null){
            int id = extras.getInt("batid");
            String name = extras.getString("batName");
            batiment= new Batiment(id,name);
        }







        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("local")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                int bat=Integer.parseInt( document.get("idBatiment").toString());
                                int idLocal=Integer.parseInt( document.get("idLocal").toString());

                                if (bat==batiment.idBatiment)
                                    locals.add(new Local(bat,idLocal,document.getString("nom")));


                            }
                            System.out.println("locals: "+locals.size());
                            LocalListAdapter adapter= new LocalListAdapter(getApplicationContext(),locals);
                            local_listView.setAdapter(adapter);
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
        display_tv.setText(batiment.nomBatiment);

        local_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(LocalListActivity.this, LocalActivity.class);
                intent.putExtra("localId", locals.get(i).id);
                intent.putExtra("localName", locals.get(i).name);
                intent.putExtra("idBat", locals.get(i).idBat);
                startActivity(intent);
            }
        });
    }
}