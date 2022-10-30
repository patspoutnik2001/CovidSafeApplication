package com.example.covidsafeapplication;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Picture;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfReader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Random;

public class LocalActivity extends AppCompatActivity {

    ImageButton exportPDF;
    TextView display_name, display_mesures;
    ArrayList<JSONObject> mesures_list;
    ArrayList<Mesure> mes_temp;
    ArrayList<Mesure> mes_hum;
    ArrayList<Mesure> mes_co2;
    Mesure[] last_mes;
    Local current_local;
    private RequestQueue mQueue;
    private String strForExport="";
    public ArrayList<BarEntry> barArraylist = new ArrayList<BarEntry>();
    BarData barData;
    BarChart barChart;
    BarDataSet barDataSet;
    Spinner filter_spinner;

    boolean found_last_mes,found_all_mes=false;

    private String stringFilePath;
    private File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local);
        mesures_list = new ArrayList<>();
        mes_temp = new ArrayList<>();
        mes_hum = new ArrayList<>();
        mes_co2 = new ArrayList<>();
        last_mes = new Mesure[3];
        mQueue = Volley.newRequestQueue(this);
        filter_spinner = findViewById(R.id.filter_spinner);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int id = extras.getInt("idLocal");

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("local")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    int bat = Integer.parseInt(document.get("idBatiment").toString());
                                    int idLocal = Integer.parseInt(document.get("idLocal").toString());
                                    String name_local = document.getString("nom");

                                    if (idLocal == id) {
                                        current_local = new Local(bat, idLocal, name_local);
                                        display_name.setText(current_local.name);
                                        initMesures();
                                        //initLastMesures();

                                    }
                                }
                                if (current_local == null) {
                                    goToListBat();
                                }
                            } else {
                                Log.w(TAG, "Error getting documents.", task.getException());
                            }
                        }
                    });
        }

        exportPDF = findViewById(R.id.exportPDF);
        display_name = findViewById(R.id.tv_display_local_name);
        display_mesures = findViewById(R.id.display_all_mesures);
        display_mesures.setMovementMethod(new ScrollingMovementMethod());
        barChart = findViewById(R.id.barChart);



        exportPDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToExport();
            }
        });



        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);


        filter_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (found_all_mes)
                    displayMesures(filter_spinner.getSelectedItem().toString());
                if (found_last_mes)
                    displayLastMesure(filter_spinner.getSelectedItem().toString());
                //TODO: quand je decomante ca bug :<
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


    private void initLastMesures() {
        String url = "https://patryk.alwaysdata.net/CovidSafeRoom/api2.php";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("mesures");
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss", Locale.ENGLISH);
                            System.out.println(jsonArray.length());
                            last_mes=new Mesure[3];
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject mes = jsonArray.getJSONObject(i);
                                //String dateStr = mes.getString("date_");
                                Calendar date = Calendar.getInstance();

                                date.setTime(sdf.parse(mes.getString("max(date_)")));
                                String idMesure = mes.getString("idMesure").trim();
                                //System.out.println("id mesure: "+idMesure);
                                //int id=Integer.getInteger(idMesure);
                                String tauxMesure = mes.getString("taux").trim();
                                //int taux= Integer.getInteger(tauxMesure);
                                String typeMesure = mes.getString("typeData").trim();
                                //int type= Integer.getInteger(typeMesure);
                                String idL = mes.getString("idLocal").trim();
                                //int iL = Integer.getInteger(idL);
                                if (idL.equals(current_local.id + "")) {
                                    //mesures_list.add(mes);
                                    Mesure mesure = new Mesure(idMesure,tauxMesure,typeMesure,idL,date);

                                    if (mesure.typeData.equals("1"))
                                        last_mes[1]=mesure;
                                    else if (mesure.typeData.equals("2"))
                                        last_mes[2]=mesure;
                                    else if (mesure.typeData.equals("3"))
                                        last_mes[0]=mesure;

                                    //display_mesures.append(idMesure + ", " + tauxMesure + ", " + getTypeMesure(typeMesure) + ", " + date.toString() + ", " + idL + "\n\n");
                                }
                            }
                            //Toast.makeText(getApplicationContext(), "All data fetched", Toast.LENGTH_SHORT).show();
                            //display_mesures.setText(mesures_list.toString());
                            found_last_mes=true;
                            displayLastMesure(filter_spinner.getSelectedItem().toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        mQueue.add(request);
    }

    private void displayLastMesure(String _type) {
        String tempStr ="Couldnt find the last mesure-> "+getTypeMesure(_type) +"\r\n";

        if (_type.equals("Co2") && last_mes[0]!=null){
            tempStr="Last one: "+last_mes[0].taux+" at "+last_mes[0].date.getTime()+"\r\n";
        }
        if (_type.equals("Temperature") && last_mes[1]!=null) {
            tempStr="Last one: "+last_mes[1].taux+" at "+last_mes[1].date.getTime()+"\r\n";

        }
        if (_type.equals(getString(R.string.hum_string)) &&last_mes[2]!=null ) {
            tempStr="Last one: "+last_mes[2].taux+" at "+last_mes[2].date.getTime()+"\r\n";

        }

        display_mesures.append(tempStr);
        System.out.println(tempStr);
        //
    }

    private void initMesures() {
        String url = "https://patryk.alwaysdata.net/CovidSafeRoom/api.php?idLocal="+current_local.id;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("mesures");
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss", Locale.ENGLISH);
                            System.out.println(jsonArray.length());
                            mes_co2.clear();
                            mes_hum.clear();
                            mes_temp.clear();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject mes = jsonArray.getJSONObject(i);
                                //String dateStr = mes.getString("date_");
                                Calendar date = Calendar.getInstance();

                                date.setTime(sdf.parse(mes.getString("date_")));
                                String idMesure = mes.getString("idMesure").trim();
                                //System.out.println("id mesure: "+idMesure);
                                //int id=Integer.getInteger(idMesure);
                                String tauxMesure = mes.getString("taux").trim();
                                //int taux= Integer.getInteger(tauxMesure);
                                String typeMesure = mes.getString("typeData").trim();
                                //int type= Integer.getInteger(typeMesure);
                                String idL = mes.getString("idLocal").trim();
                                //int iL = Integer.getInteger(idL);
                                if (idL.equals(current_local.id + "")) {
                                    //mesures_list.add(mes);
                                    Mesure mesure = new Mesure(idMesure,tauxMesure,typeMesure,idL,date);

                                    if (mesure.typeData.equals("1"))
                                        mes_co2.add(mesure);
                                    else if (mesure.typeData.equals("2"))
                                        mes_temp.add(mesure);
                                    else if (mesure.typeData.equals("3"))
                                        mes_hum.add(mesure);

                                    //display_mesures.append(idMesure + ", " + tauxMesure + ", " + getTypeMesure(typeMesure) + ", " + date.toString() + ", " + idL + "\n\n");
                                }
                            }
                            //Toast.makeText(getApplicationContext(), "All data fetched", Toast.LENGTH_SHORT).show();
                            //display_mesures.setText(mesures_list.toString());
                            found_all_mes=true;
                            displayMesures(filter_spinner.getSelectedItem().toString());
                            initLastMesures();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        mQueue.add(request);



    }

    private void makeChart() {
        barDataSet = new BarDataSet(barArraylist,"");
        barData = new BarData(barDataSet);
        //barChart.setData(barData);
        //color bar data set
        barChart.setData(barData);
        barDataSet.setColors(ColorTemplate.PASTEL_COLORS);
        //text color
        barDataSet.setValueTextColor(R.color.textColor);
        //settting text size
        barDataSet.setValueTextSize(16f);
        barChart.getDescription().setEnabled(true);
    }

    private void displayMesures(String _type) {

        if (_type.equals("Co2"))
            _type="1";
        if (_type.equals("Temperature"))
            _type="2";
        if (_type.equals(getString(R.string.hum_string)))
            _type="3";

        System.out.println(_type);
        if (barData!=null) {
            barDataSet.clear();
            barData.clearValues();
        }
        //strForExport="";
        barChart.clear();
        display_mesures.setText("");
        displayOne(_type);
        makeChart();

    }

    private void displayOne(String _t) {
        for (int i = 0; i < 7; i++) {
            int cpt=0;
            int current_day_sum=0;
            for (Mesure item:getMesureList(_t)) {
                if (Calendar.getInstance().get(Calendar.DATE)-i==item.date.get(Calendar.DATE)) {
                    current_day_sum+= Integer.parseInt(item.taux);
                    cpt++;
                }
            }

            if (cpt!=0) {
                current_day_sum = current_day_sum / cpt;
            }
            String tempStr =Calendar.getInstance().get(Calendar.DAY_OF_MONTH)-i+"/"+Calendar.getInstance().get(Calendar.MONTH)+": Avarage "+getTypeMesure(_t)+ ": " + current_day_sum+ "\r\n";
            //strForExport+=(tempStr);
            display_mesures.append(tempStr);
            barArraylist.add(new BarEntry((float) i,(float)current_day_sum));
        }
    }

    private ArrayList<Mesure> getMesureList(String t) {
        if (t=="1")
            return mes_co2;
        if (t=="2")
            return mes_temp;
        if (t=="3")
            return mes_hum;

        return new ArrayList<>();
    }

    private void goToListBat() {
        Intent intent = new Intent(this, ListeActivity.class);
        startActivity(intent);
    }
    private String getTypeMesure(String t){
        if (t.equals("1"))
            return "CO2";
        if (t.equals("2"))
            return "Temp";
        if (t.equals("3"))
            return getString(R.string.hum_string);
        return getString(R.string.taux_error);
    }

    private void goToExport() {
        int number = new Random().nextInt(100000);
        stringFilePath=Environment.getExternalStorageDirectory().getPath() + "/Download/CovidDocs/"+current_local.name;
        File FPath = new File(stringFilePath);
        if (!FPath.exists()) {
            if (!FPath.mkdir()) {
                FPath.mkdirs();
            }
        }
        stringFilePath+="/"+current_local.name+"-"+number+".pdf";
        file= new File(stringFilePath);

        PdfDocument pdfDocument = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(300, 600, 1).create();
        PdfDocument.Page page = pdfDocument.startPage(pageInfo);

        Bitmap bitmap;
        barChart.setDrawingCacheEnabled(true);
        barDataSet.setValueTextColor(Color.WHITE);
        bitmap = Bitmap.createBitmap(barChart.getDrawingCache());
        barDataSet.setValueTextColor(Color.BLACK);
        barChart.setDrawingCacheEnabled(false);

        Paint paint = new Paint();
        //40 char max sur une ligne?
        int x = 10, y = 25;
        StringBuilder _sb = new StringBuilder(getResources().getString(R.string.export_local_string)+current_local.name+"\r\n"+display_mesures.getText().toString());

        for (String line:_sb.toString().split("\r\n")){//take each line from string
            page.getCanvas().drawText(line,x,y, paint);
            y+=paint.descent()-paint.ascent();
        }

        Picture pic_chart = new Picture();
        Canvas canvas = pic_chart.beginRecording(bitmap.getWidth(), bitmap.getHeight());
        canvas.drawBitmap(bitmap, null, new RectF(0f, 200f,  300f,  500f), null);
        pic_chart.endRecording();

        page.getCanvas().drawPicture(pic_chart);


        pdfDocument.finishPage(page);//fin de la page
        try {
            pdfDocument.writeTo(new FileOutputStream(file)); // writing the file into the memory
        }
        catch (Exception e){
            e.printStackTrace();
        }
        pdfDocument.close();//closing the file
        ReadPDF();
        //exportChart();
    }


    public void ReadPDF(){
        try {
            PdfReader pdfReader = new PdfReader(file.getPath());
            pdfReader.close();
            //notif here
            sendNotificationToUser("Success","PDF has been created with success",stringFilePath,"application/pdf");
        }
        catch (Exception e){
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error while creating PDF", Toast.LENGTH_SHORT).show();
        }
    }
    private void sendNotificationToUser(String _n, String _d,String _filePath,String _type) {
        createNotificationChannel();

        Intent intent = new Intent();
        intent.setAction(android.content.Intent.ACTION_VIEW);

        File file = new File(_filePath); // set your image path

        Uri uri = FileProvider.getUriForFile(this, this.getApplicationContext().getPackageName() + ".provider", file);
        intent.setDataAndType(uri, _type);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

        Bitmap bm = BitmapFactory.decodeFile(_filePath);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "notif1")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(_n)
                .setContentText(_d)
                .setContentIntent(pIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);


        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(0, builder.build());




    }
    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "PDF Creation";
            String description = "normal";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("notif1", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }



    }
}