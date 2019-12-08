package com.mindorks.tensorflowexample;

import android.graphics.Color;
import android.media.CamcorderProfile;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.ar.core.Anchor;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.ViewRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.BaseArFragment;
import com.google.ar.sceneform.ux.TransformableNode;

public class ListVehicleActivity extends AppCompatActivity {

    private ArFragment arFragment;

    private ModelRenderable bikeRenderable, trainRenderable, carRenderable, firetruckRenderable,
            helicopterRenderable, planeRenderable, boatRenderable, sailboatRenderable;
    ImageView bike, train, car, firetruck, helicopter, plane, boat, sailboat;
    View arrayView[];
    ViewRenderable name_object;
    int selected = 1;
    private VideoRecorder videoRecorder;
    Button record;

    @Override
    @SuppressWarnings({"AndroidApiChecker", "FutureReturnValueIgnored"})
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_vehicle);

        //Anh xa
        map();

        //Set ArrayView
        setArrayView();

        //click each item
        setClickListener();

        //Setup 3D model
        setupModel();

        arFragment.setOnTapArPlaneListener(new BaseArFragment.OnTapArPlaneListener() {
            @Override
            public void onTapPlane(HitResult hitResult, Plane plane, MotionEvent motionEvent) {
                Anchor anchor = hitResult.createAnchor();
                AnchorNode anchorNode = new AnchorNode(anchor);
                anchorNode.setParent(arFragment.getArSceneView().getScene());

                createModel(anchorNode, selected);
            }
        });
        Button record = findViewById(R.id.record);
        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (videoRecorder == null) {
                    videoRecorder = new VideoRecorder();
                    videoRecorder.setSceneView(arFragment.getArSceneView());
                    int orientation = getResources().getConfiguration().orientation;
                    videoRecorder.setVideoQuality(CamcorderProfile.QUALITY_HIGH, orientation);
                }
                boolean isRecording = videoRecorder.onToggleRecord();
                if (isRecording) {
                    record.setBackgroundResource(R.drawable.btn_stop);
                    Toast.makeText(ListVehicleActivity.this, "Recording", Toast.LENGTH_SHORT).show();
                } else {
                    record.setBackgroundResource(R.drawable.btn_record);
                    Toast.makeText(ListVehicleActivity.this, "Saved video to gallery.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setupModel() {
        ViewRenderable.builder()
                .setView(this, R.layout.name_object)
                .build()
                .thenAccept(viewRenderable -> name_object = viewRenderable);
        ModelRenderable.builder()
                .setSource(this, R.raw.bike)
                .build().thenAccept(modelRenderable -> bikeRenderable = modelRenderable)
                .exceptionally(throwable -> {
                    Toast.makeText(this, "Unable to load bike model", Toast.LENGTH_SHORT).show();
                    return null;
                });
        ModelRenderable.builder()
                .setSource(this, R.raw.train)
                .build().thenAccept(modelRenderable -> trainRenderable = modelRenderable)
                .exceptionally(throwable -> {
                    Toast.makeText(this, "Unable to load train model", Toast.LENGTH_SHORT).show();
                    return null;
                });
        ModelRenderable.builder()
                .setSource(this, R.raw.car)
                .build().thenAccept(modelRenderable -> carRenderable = modelRenderable)
                .exceptionally(throwable -> {
                    Toast.makeText(this, "Unable to load car model", Toast.LENGTH_SHORT).show();
                    return null;
                });
        ModelRenderable.builder()
                .setSource(this, R.raw.firetruck)
                .build().thenAccept(modelRenderable -> firetruckRenderable = modelRenderable)
                .exceptionally(throwable -> {
                    Toast.makeText(this, "Unable to load firetruck model", Toast.LENGTH_SHORT).show();
                    return null;
                });
        ModelRenderable.builder()
                .setSource(this, R.raw.helicopter)
                .build().thenAccept(modelRenderable -> helicopterRenderable = modelRenderable)
                .exceptionally(throwable -> {
                    Toast.makeText(this, "Unable to load helicopter model", Toast.LENGTH_SHORT).show();
                    return null;
                });
        ModelRenderable.builder()
                .setSource(this, R.raw.plane)
                .build().thenAccept(modelRenderable -> planeRenderable = modelRenderable)
                .exceptionally(throwable -> {
                    Toast.makeText(this, "Unable to load plane model", Toast.LENGTH_SHORT).show();
                    return null;
                });
        ModelRenderable.builder()
                .setSource(this, R.raw.boat)
                .build().thenAccept(modelRenderable -> boatRenderable = modelRenderable)
                .exceptionally(throwable -> {
                    Toast.makeText(this, "Unable to load boat model", Toast.LENGTH_SHORT).show();
                    return null;
                });
        ModelRenderable.builder()
                .setSource(this, R.raw.sailboat)
                .build().thenAccept(modelRenderable -> sailboatRenderable = modelRenderable)
                .exceptionally(throwable -> {
                    Toast.makeText(this, "Unable to load sailboat model", Toast.LENGTH_SHORT).show();
                    return null;
                });
    }

    private void map() {
        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.arFragment);
        bike = findViewById(R.id.bike);
        train = findViewById(R.id.train);
        car = findViewById(R.id.car);
        firetruck = findViewById(R.id.firetruck);
        helicopter = findViewById(R.id.helicopter);
        plane = findViewById(R.id.plane);
        boat = findViewById(R.id.boat);
        sailboat = findViewById(R.id.sailboat);
    }

    private void setArrayView() {
        arrayView = new View[]{
                bike, train, car, firetruck, helicopter, plane, boat, sailboat
        };
    }

    private void setClickListener() {
        for (int i = 0; i < arrayView.length; i++) {
            arrayView[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (v.getId() == R.id.bike) {
                        selected = 1;
                        setBackground(v.getId());
                    } else if (v.getId() == R.id.train) {
                        selected = 2;
                        setBackground(v.getId());
                    } else if (v.getId() == R.id.car) {
                        selected = 3;
                        setBackground(v.getId());
                    }  else if (v.getId() == R.id.firetruck) {
                        selected = 4;
                        setBackground(v.getId());
                    } else if (v.getId() == R.id.helicopter) {
                        selected = 5;
                        setBackground(v.getId());
                    } else if (v.getId() == R.id.plane) {
                        selected = 6;
                        setBackground(v.getId());
                    } else if (v.getId() == R.id.boat) {
                        selected = 7;
                        setBackground(v.getId());
                    } else {
                        selected = 8;
                        setBackground(v.getId());
                    }
                }
            });
        }
    }

    private void createModel(AnchorNode anchorNode, int selected) {
        if (selected == 1) {
            TransformableNode bike = new TransformableNode(arFragment.getTransformationSystem());
            bike.setParent(anchorNode);
            bike.setRenderable(bikeRenderable);
            bike.select();
            addName(anchorNode, bike, "Bike");
        }
        if (selected == 2) {
            TransformableNode train = new TransformableNode(arFragment.getTransformationSystem());
            train.setParent(anchorNode);
            train.setRenderable(trainRenderable);
            train.select();
            addName(anchorNode, train, "Train");
        }
        if (selected == 3) {
            TransformableNode car = new TransformableNode(arFragment.getTransformationSystem());
            car.setParent(anchorNode);
            car.setRenderable(carRenderable);
            car.select();
            addName(anchorNode, car, "Car");
        }
        if (selected == 4) {
            TransformableNode firetruck = new TransformableNode(arFragment.getTransformationSystem());
            firetruck.setParent(anchorNode);
            firetruck.setRenderable(firetruckRenderable);
            firetruck.select();
            addName(anchorNode, firetruck, "firetruck");
        }
        if (selected == 5) {
            TransformableNode helicopter = new TransformableNode(arFragment.getTransformationSystem());
            helicopter.setParent(anchorNode);
            helicopter.setRenderable(helicopterRenderable);
            helicopter.select();
            addName(anchorNode, helicopter, "helicopter");
        }
        if (selected == 6) {
            TransformableNode plane = new TransformableNode(arFragment.getTransformationSystem());
            plane.setParent(anchorNode);
            plane.setRenderable(planeRenderable);
            plane.select();
            addName(anchorNode, plane, "plane");
        }
        if (selected == 7) {
            TransformableNode boat = new TransformableNode(arFragment.getTransformationSystem());
            boat.setParent(anchorNode);
            boat.setRenderable(boatRenderable);
            boat.select();
            addName(anchorNode, boat, "boat");
        }
        if (selected == 8) {
            TransformableNode sailboat = new TransformableNode(arFragment.getTransformationSystem());
            sailboat.setParent(anchorNode);
            sailboat.setRenderable(sailboatRenderable);
            sailboat.select();
            addName(anchorNode, sailboat, "sailboat");
        }
    }

    private void addName(AnchorNode anchorNode, TransformableNode model, String name) {
        ViewRenderable.builder()
                .setView(this, R.layout.name_object)
                .build()
                .thenAccept(viewRenderable -> {
                    TransformableNode nameView = new TransformableNode(arFragment.getTransformationSystem());
                    nameView.setLocalPosition(new Vector3(0f, model.getLocalPosition().y + 0.5f, 0));
                    nameView.setParent(anchorNode);
                    nameView.setRenderable(viewRenderable);
                    nameView.select();

                    //set text
                    TextView textView = (TextView) viewRenderable.getView();
                    textView.setText(name);
                    textView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            anchorNode.setParent(null);
                        }
                    });
                });

    }

    private void setBackground(int id) {
        for (int i = 0; i < arrayView.length; i++) {
            if (arrayView[i].getId() == id) {
                arrayView[i].setBackgroundColor(Color.parseColor("#80333639"));
            } else {
                arrayView[i].setBackgroundColor(Color.TRANSPARENT);
            }
        }
    }
}



