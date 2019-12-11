package com.mindorks.tensorflowexample;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ListTopicActivity extends AppCompatActivity {
    ImageView ocean, home, person, animal, plant, vehicle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_topic);

//        ocean = findViewById(R.id.ocean);
//        home = findViewById(R.id.home);
//        person = findViewById(R.id.person);
//        animal = findViewById(R.id.animal);
//        plant = findViewById(R.id.plant);
//        vehicle = findViewById(R.id.vehicle);
//
//        ocean.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Intent intent = new Intent(ListTopicActivity.this, TestRecycleView.class);
//                startActivity(intent);
//            }
//        });
//        home.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(ListTopicActivity.this, ListHomeActivity.class);
//                startActivity(intent);
//            }
//        });
//        person.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(ListTopicActivity.this, ListPersonActivity.class);
//                startActivity(intent);
//            }
//        });
//        animal.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(ListTopicActivity.this, ListAnimalActivity.class);
//                startActivity(intent);
//            }
//        });
//        plant.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(ListTopicActivity.this, ListPlantActivity.class);
//                startActivity(intent);
//            }
//        });
//        vehicle.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(ListTopicActivity.this, ListVehicleActivity.class);
//                startActivity(intent);
//            }
//        });

        final ListView list = findViewById(R.id.listView);
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("Ocean");
        arrayList.add("Animal");
        arrayList.add("Plant");
        arrayList.add("Home");
        arrayList.add("Person");
        arrayList.add("Vehicle");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayList);
        list.setAdapter(arrayAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String clickedItem=(String) list.getItemAtPosition(position);
                Toast.makeText(ListTopicActivity.this,clickedItem,Toast.LENGTH_LONG).show();
                Intent intent = new Intent(ListTopicActivity.this, TestRecycleView.class);
                intent.putExtra("topic", clickedItem);
                startActivity(intent);
            }
        });
    }
}
