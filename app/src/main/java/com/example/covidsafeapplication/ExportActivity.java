package com.example.covidsafeapplication;



import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.channels.Channels;
import java.nio.charset.Charset;
import java.security.Timestamp;
import java.time.Instant;
import java.util.Random;

public class ExportActivity extends AppCompatActivity {


    //change file name
    private String stringFilePath;
    private File file;
    String exportStr="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export);
        exportStr=getResources().getString(R.string.export_local_string);

        String temp_localname="";
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            temp_localname=extras.getString("localStr");
            exportStr+= temp_localname+"\r\n";
            exportStr += extras.getString("exportStr");
        }
        //creation of random int for file name
        int number = new Random().nextInt(100000);

        stringFilePath=Environment.getExternalStorageDirectory().getPath() + "/Download/"+temp_localname+"-"+number+".pdf";
         file= new File(stringFilePath);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);
    }

    public void buttonCreatePDF(View view){

        PdfDocument pdfDocument = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(300, 600, 1).create();
        PdfDocument.Page page = pdfDocument.startPage(pageInfo);

        Paint paint = new Paint();
        //40 char max sur une ligne?
        int x = 10, y = 25;

        for (String line:exportStr.split("\r\n")){//take each line from string
            page.getCanvas().drawText(line,x,y, paint);
            y+=paint.descent()-paint.ascent();
        }
        pdfDocument.finishPage(page);//fin de la page
        try {
            pdfDocument.writeTo(new FileOutputStream(file)); // writing the file into the memory
        }
        catch (Exception e){
            e.printStackTrace();
        }
        pdfDocument.close();//closing the file
        ReadPDF();
    }

    public void ReadPDF(){
        try {
            PdfReader pdfReader = new PdfReader(file.getPath());
            pdfReader.close();
            //notif here
            sendNotificationToUser("Success","PDF has been created with success\n\r"+stringFilePath);

        }
        catch (Exception e){
            e.printStackTrace();
            //add notification
            sendNotificationToUser("Error PDF","PDF file might be damaged");

        }
    }

    private void sendNotificationToUser(String _n, String _d) {
        createNotificationChannel();

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "notif1")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(_n)
                .setContentText(_d)
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