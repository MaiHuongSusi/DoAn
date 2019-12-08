package com.mindorks.tensorflowexample;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MainMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);
    }

    public void show3DObject(View view) {
        Intent intent = new Intent(MainMenuActivity.this, ThreeDObjectActivity.class);
        startActivity(intent);
    }

    public void identifyObjects(View view) {
        Intent intent = new Intent(MainMenuActivity.this, InstantDetectActivity.class);
        startActivity(intent);
    }
}
