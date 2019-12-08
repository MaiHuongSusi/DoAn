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

public class ListHomeActivity extends AppCompatActivity {

    private ArFragment arFragment;
    private ModelRenderable houseRenderable, mordernhouseRenderable, livingroomRenderable,
            couchRenderable, tvRenderable, remotecontrolRenderable, tableRenderable,
            kitchenRenderable, kitchen1Renderable, bedroomRenderable, bathroomRenderable;
    ImageView house, mordernhouse, livingroom, couch, tv, remotecontrol, table,
            kitchen, kitchen1, bedroom, bathroom;
    View arrayView[];
    ViewRenderable name_object;
    int selected = 1;
    private VideoRecorder videoRecorder;
    Button record;

    @Override
    @SuppressWarnings({"AndroidApiChecker", "FutureReturnValueIgnored"})
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_home);

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
                    Toast.makeText(ListHomeActivity.this, "Recording", Toast.LENGTH_SHORT).show();
                } else {
                    record.setBackgroundResource(R.drawable.btn_record);
                    Toast.makeText(ListHomeActivity.this, "Saved video to gallery.", Toast.LENGTH_SHORT).show();
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
                .setSource(this, R.raw.house)
                .build().thenAccept(modelRenderable -> houseRenderable = modelRenderable)
                .exceptionally(throwable -> {
                    Toast.makeText(this, "Unable to load house model", Toast.LENGTH_SHORT).show();
                    return null;
                });
        ModelRenderable.builder()
                .setSource(this, R.raw.modernhouse)
                .build().thenAccept(modelRenderable -> mordernhouseRenderable = modelRenderable)
                .exceptionally(throwable -> {
                    Toast.makeText(this, "Unable to load modern house model", Toast.LENGTH_SHORT).show();
                    return null;
                });
        ModelRenderable.builder()
                .setSource(this, R.raw.livingroom)
                .build().thenAccept(modelRenderable -> livingroomRenderable = modelRenderable)
                .exceptionally(throwable -> {
                    Toast.makeText(this, "Unable to load living room model", Toast.LENGTH_SHORT).show();
                    return null;
                });
        ModelRenderable.builder()
                .setSource(this, R.raw.couch)
                .build().thenAccept(modelRenderable -> couchRenderable = modelRenderable)
                .exceptionally(throwable -> {
                    Toast.makeText(this, "Unable to load couch model", Toast.LENGTH_SHORT).show();
                    return null;
                });
        ModelRenderable.builder()
                .setSource(this, R.raw.tv)
                .build().thenAccept(modelRenderable -> tvRenderable = modelRenderable)
                .exceptionally(throwable -> {
                    Toast.makeText(this, "Unable to load television model", Toast.LENGTH_SHORT).show();
                    return null;
                });
        ModelRenderable.builder()
                .setSource(this, R.raw.remotecontrol)
                .build().thenAccept(modelRenderable -> remotecontrolRenderable = modelRenderable)
                .exceptionally(throwable -> {
                    Toast.makeText(this, "Unable to load remote control model", Toast.LENGTH_SHORT).show();
                    return null;
                });
        ModelRenderable.builder()
                .setSource(this, R.raw.table)
                .build().thenAccept(modelRenderable -> tableRenderable = modelRenderable)
                .exceptionally(throwable -> {
                    Toast.makeText(this, "Unable to load table model", Toast.LENGTH_SHORT).show();
                    return null;
                });
        ModelRenderable.builder()
                .setSource(this, R.raw.kitchen)
                .build().thenAccept(modelRenderable -> kitchenRenderable = modelRenderable)
                .exceptionally(throwable -> {
                    Toast.makeText(this, "Unable to load kitchen model", Toast.LENGTH_SHORT).show();
                    return null;
                });
        ModelRenderable.builder()
                .setSource(this, R.raw.kitchen1)
                .build().thenAccept(modelRenderable -> kitchen1Renderable = modelRenderable)
                .exceptionally(throwable -> {
                    Toast.makeText(this, "Unable to load kitchen model", Toast.LENGTH_SHORT).show();
                    return null;
                });
        ModelRenderable.builder()
                .setSource(this, R.raw.bedroom)
                .build().thenAccept(modelRenderable -> bedroomRenderable = modelRenderable)
                .exceptionally(throwable -> {
                    Toast.makeText(this, "Unable to load bedroom model", Toast.LENGTH_SHORT).show();
                    return null;
                });
        ModelRenderable.builder()
                .setSource(this, R.raw.bathroom)
                .build().thenAccept(modelRenderable -> bathroomRenderable = modelRenderable)
                .exceptionally(throwable -> {
                    Toast.makeText(this, "Unable to load bathroom model", Toast.LENGTH_SHORT).show();
                    return null;
                });
    }

    private void map() {
        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.arFragment);
        house = findViewById(R.id.house);
        mordernhouse = findViewById(R.id.modernhouse);
        livingroom = findViewById(R.id.livingroom);
        couch = findViewById(R.id.couch);
        tv = findViewById(R.id.tv);
        remotecontrol = findViewById(R.id.remotecontrol);
        table = findViewById(R.id.table);
        kitchen = findViewById(R.id.kitchen);
        kitchen1 = findViewById(R.id.kitchen1);
        bedroom = findViewById(R.id.bedroom);
        bathroom = findViewById(R.id.bathroom);
    }

    private void setArrayView() {
        arrayView = new View[]{
                house, mordernhouse, livingroom, couch, tv, remotecontrol, table,
                kitchen, kitchen1, bedroom, bathroom
        };
    }

    private void setClickListener() {
        for (int i = 0; i < arrayView.length; i++) {
            arrayView[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (v.getId() == R.id.house) {
                        selected = 1;
                        setBackground(v.getId());
                    } else if (v.getId() == R.id.modernhouse) {
                        selected = 2;
                        setBackground(v.getId());
                    } else if (v.getId() == R.id.livingroom) {
                        selected = 3;
                        setBackground(v.getId());
                    } else if (v.getId() == R.id.couch) {
                        selected = 4;
                        setBackground(v.getId());
                    } else if (v.getId() == R.id.tv) {
                        selected = 5;
                        setBackground(v.getId());
                    } else if (v.getId() == R.id.remotecontrol) {
                        selected = 6;
                        setBackground(v.getId());
                    } else if (v.getId() == R.id.table) {
                        selected = 7;
                        setBackground(v.getId());

                    } else if (v.getId() == R.id.kitchen) {
                        selected = 8;
                        setBackground(v.getId());
                    } else if (v.getId() == R.id.kitchen1) {
                        selected = 9;
                        setBackground(v.getId());
                    } else if (v.getId() == R.id.bedroom) {
                        selected = 10;
                        setBackground(v.getId());
                    } else {
                        selected = 11;
                        setBackground(v.getId());
                    }
                }
            });
        }
    }

    private void createModel(AnchorNode anchorNode, int selected) {
        if (selected == 1) {
            TransformableNode house = new TransformableNode(arFragment.getTransformationSystem());
            house.setParent(anchorNode);
            house.setRenderable(houseRenderable);
            house.select();
            addName(anchorNode, house, "House");
        }
        if (selected == 2) {
            TransformableNode modernhouse = new TransformableNode(arFragment.getTransformationSystem());
            modernhouse.setParent(anchorNode);
            modernhouse.setRenderable(mordernhouseRenderable);
            modernhouse.select();
            addName(anchorNode, modernhouse, "Modern House");
        }
        if (selected == 3) {
            TransformableNode livingroom = new TransformableNode(arFragment.getTransformationSystem());
            livingroom.setParent(anchorNode);
            livingroom.setRenderable(livingroomRenderable);
            livingroom.select();
            addName(anchorNode, livingroom, "Living Room");
        }
        if (selected == 4) {
            TransformableNode couch = new TransformableNode(arFragment.getTransformationSystem());
            couch.setParent(anchorNode);
            couch.setRenderable(couchRenderable);
            couch.select();
            addName(anchorNode, couch, "Couch");
        }
        if (selected == 5) {
            TransformableNode tv = new TransformableNode(arFragment.getTransformationSystem());
            tv.setParent(anchorNode);
            tv.setRenderable(tvRenderable);
            tv.select();
            addName(anchorNode, tv, "Television");
        }
        if (selected == 6) {
            TransformableNode remotecontrol = new TransformableNode(arFragment.getTransformationSystem());
            remotecontrol.setParent(anchorNode);
            remotecontrol.setRenderable(remotecontrolRenderable);
            remotecontrol.select();
            addName(anchorNode, remotecontrol, "Remote control");
        }
        if (selected == 7) {
            TransformableNode table = new TransformableNode(arFragment.getTransformationSystem());
            table.setParent(anchorNode);
            table.setRenderable(tableRenderable);
            table.select();
            addName(anchorNode, table, "Table");
        }
        if (selected == 8) {
            TransformableNode kitchen = new TransformableNode(arFragment.getTransformationSystem());
            kitchen.setParent(anchorNode);
            kitchen.setRenderable(kitchenRenderable);
            kitchen.select();
            addName(anchorNode, kitchen, "Kitchen");
        }
        if (selected == 9) {
            TransformableNode kitchen1 = new TransformableNode(arFragment.getTransformationSystem());
            kitchen1.setParent(anchorNode);
            kitchen1.setRenderable(kitchen1Renderable);
            kitchen1.select();
            addName(anchorNode, kitchen1, "Kitchen");
        }
        if (selected == 10) {
            TransformableNode bedroom = new TransformableNode(arFragment.getTransformationSystem());
            bedroom.setParent(anchorNode);
            bedroom.setRenderable(bedroomRenderable);
            bedroom.select();
            addName(anchorNode, bedroom, "Bedroom");
        }
        if (selected == 11) {
            TransformableNode bathroom = new TransformableNode(arFragment.getTransformationSystem());
            bathroom.setParent(anchorNode);
            bathroom.setRenderable(bathroomRenderable);
            bathroom.select();
            addName(anchorNode, bathroom, "Bathroom");
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



