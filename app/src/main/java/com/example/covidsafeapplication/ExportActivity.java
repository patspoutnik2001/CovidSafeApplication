package com.example.covidsafeapplication;



import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;

import java.io.File;
import java.io.FileOutputStream;
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
            String stringParse = PdfTextExtractor.getTextFromPage(pdfReader,1).trim();
            int cpt_char=stringParse.length();
            pdfReader.close();
            //toast here
            Toast toast = new Toast(getApplicationContext());
            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setText("Creation success, wrote "+cpt_char+" characters");
            toast.show();
        }
        catch (Exception e){
            e.printStackTrace();
            Toast toast = new Toast(getApplicationContext());
            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setText("ERROR: the file might be damaged");
            toast.show();
        }
    }
}