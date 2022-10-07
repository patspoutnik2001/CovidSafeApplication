package com.example.covidsafeapplication;



import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;

import java.io.File;
import java.io.FileOutputStream;

public class ExportActivity extends AppCompatActivity {

    private EditText editText;
    private TextView textView;

    private String stringFilePath = Environment.getExternalStorageDirectory().getPath() + "/Download/ProgrammerWorld.pdf";
    private File file = new File(stringFilePath);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export);

        editText = findViewById(R.id.editText);
        textView = findViewById(R.id.textView);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);
    }

    public void buttonCreatePDF(View view){

        PdfDocument pdfDocument = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(300, 600, 1).create();
        PdfDocument.Page page = pdfDocument.startPage(pageInfo);

        Paint paint = new Paint();
        String stringPDF = editText.getText().toString();

        int x = 10, y = 25;

        for (String line:stringPDF.split("\n")){
            page.getCanvas().drawText(line,x,y, paint);

            y+=paint.descent()-paint.ascent();
        }
        pdfDocument.finishPage(page);
        try {
            pdfDocument.writeTo(new FileOutputStream(file));
        }
        catch (Exception e){
            e.printStackTrace();
            textView.setText("Error in Creating");
        }
        pdfDocument.close();
    }

    public void buttonReadPDF(View view){
        try {
            PdfReader pdfReader = new PdfReader(file.getPath());
            String stringParse = PdfTextExtractor.getTextFromPage(pdfReader,1).trim();
            pdfReader.close();
            textView.setText(stringParse);
        }
        catch (Exception e){
            e.printStackTrace();
            textView.setText("Error in Reading");
        }
    }
}