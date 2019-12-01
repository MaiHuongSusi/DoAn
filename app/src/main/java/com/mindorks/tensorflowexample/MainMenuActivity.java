package com.mindorks.tensorflowexample;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);
    }

    public void show3DObject(View view) {
        Intent intent = new Intent(MainMenuActivity.this, CameraActivity.class);
        startActivity(intent);
    }

    public void identifyObjects(View view) {
        Intent intent = new Intent(MainMenuActivity.this, CameraActivity.class);
        startActivity(intent);
    }
}
