package com.a4dotsinc.chopchop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;


import com.a4dotsinc.chopchop.services.ChopChopService;
import com.squareup.seismic.ShakeDetector;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ContextCompat.startForegroundService(this, new Intent(this, ChopChopService.class).putExtra("text", "ChopChop"));
        finish();
    }


}
