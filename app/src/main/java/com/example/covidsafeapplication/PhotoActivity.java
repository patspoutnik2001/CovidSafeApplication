package com.example.covidsafeapplication;

import android.content.Intent;
import android.graphics.Camera;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class PhotoActivity extends AppCompatActivity {

    TextView testView;

    Camera camera;
    SurfaceView surfaceView;
    SurfaceHolder surfaceHolder;

    private final String tag = "VideoServer";

    Button start, stop;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

//        Button loginBtn = (Button) findViewById(R.id.login);
//        loginBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                goToLocal();
//            }
//        });

        start = (Button)findViewById(R.id.qrCodePhoto);
        start.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View arg0) {
                start_camera();
            }
        });

//        stop = (Button)findViewById(R.id.btn_stop);
//        stop.setOnClickListener(new Button.OnClickListener() {
//            public void onClick(View arg0) {
//                stop_camera();
//            }
//        });

//        surfaceView = (SurfaceView)findViewById(R.id.surfaceView1);
//        surfaceHolder = surfaceView.getHolder();
//        surfaceHolder.addCallback(this);
//        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

    }


    private void goToLocal() {
        Intent intent = new Intent(this, LocalActivity.class);
        startActivity(intent);
    }

    private void start_camera() {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        startActivity(intent);
    }




}