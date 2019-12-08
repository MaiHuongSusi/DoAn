package com.mindorks.tensorflowexample;

import android.Manifest;
import android.content.pm.PackageManager;
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
import androidx.core.app.ActivityCompat;

import com.google.ar.core.Anchor;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.MaterialFactory;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.ShapeFactory;
import com.google.ar.sceneform.rendering.ViewRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.BaseArFragment;
import com.google.ar.sceneform.ux.TransformableNode;

import static android.graphics.Color.TRANSPARENT;
import static android.graphics.Color.parseColor;

public class ListOceanActivity extends AppCompatActivity {

    private ArFragment arFragment;
    private ModelRenderable crabRenderable, crayfishRenderable, dolphinRenderable,
            jellyfishRenderable, octopusRenderable, seahorseRenderable, sharkRenderable,
            squidRenderable, starfishRenderable, turtleRenderable;
    ImageView crab, crayfish, dolphin, jellyfish, octopus, seahorse, shark, squid, starfish, turtle;
    View arrayView[];
    ViewRenderable name_object;
    int selected = 1;
    private int i = 0;
    private VideoRecorder videoRecorder;
    Button record;

    @Override
    @SuppressWarnings({"AndroidApiChecker", "FutureReturnValueIgnored"})
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_ocean);

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
                MaterialFactory.makeOpaqueWithColor(ListOceanActivity.this, new com.google.ar.sceneform.rendering.Color(Color.RED))
                        .thenAccept(material -> {
                            ModelRenderable renderable = ShapeFactory
                                    .makeSphere(0.3f, new Vector3(0f, 0.3f, 0f), material);

                            Anchor anchor = hitResult.createAnchor();
                            AnchorNode anchorNode = new AnchorNode(anchor);
                            anchorNode.setParent(arFragment.getArSceneView().getScene());

                            createModel(anchorNode, selected);
                        });
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
                    Toast.makeText(ListOceanActivity.this, "Recording", Toast.LENGTH_SHORT).show();
                } else {
                    record.setBackgroundResource(R.drawable.btn_record);
                    Toast.makeText(ListOceanActivity.this, "Saved video to gallery.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(ListOceanActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
    }

    private void setupModel() {
        ViewRenderable.builder()
                .setView(this, R.layout.name_object)
                .build()
                .thenAccept(viewRenderable -> name_object = viewRenderable);
        ModelRenderable.builder()
                .setSource(this, R.raw.crab)
                .build().thenAccept(modelRenderable -> crabRenderable = modelRenderable)
                .exceptionally(throwable -> {
                    Toast.makeText(this, "Unable to load crab model", Toast.LENGTH_SHORT).show();
                    return null;
                });
        ModelRenderable.builder()
                .setSource(this, R.raw.crayfish)
                .build().thenAccept(modelRenderable -> crayfishRenderable = modelRenderable)
                .exceptionally(throwable -> {
                    Toast.makeText(this, "Unable to load crayfish model", Toast.LENGTH_SHORT).show();
                    return null;
                });
        ModelRenderable.builder()
                .setSource(this, R.raw.dolphin)
                .build().thenAccept(modelRenderable -> dolphinRenderable = modelRenderable)
                .exceptionally(throwable -> {
                    Toast.makeText(this, "Unable to load dolphin model", Toast.LENGTH_SHORT).show();
                    return null;
                });
        ModelRenderable.builder()
                .setSource(this, R.raw.jellyfish)
                .build().thenAccept(modelRenderable -> jellyfishRenderable = modelRenderable)
                .exceptionally(throwable -> {
                    Toast.makeText(this, "Unable to load jellyfish model", Toast.LENGTH_SHORT).show();
                    return null;
                });
        ModelRenderable.builder()
                .setSource(this, R.raw.octopus)
                .build().thenAccept(modelRenderable -> octopusRenderable = modelRenderable)
                .exceptionally(throwable -> {
                    Toast.makeText(this, "Unable to load octopus model", Toast.LENGTH_SHORT).show();
                    return null;
                });
        ModelRenderable.builder()
                .setSource(this, R.raw.seahorse)
                .build().thenAccept(modelRenderable -> seahorseRenderable = modelRenderable)
                .exceptionally(throwable -> {
                    Toast.makeText(this, "Unable to load seahorse model", Toast.LENGTH_SHORT).show();
                    return null;
                });
        ModelRenderable.builder()
                .setSource(this, R.raw.shark)
                .build().thenAccept(modelRenderable -> sharkRenderable = modelRenderable)
                .exceptionally(throwable -> {
                    Toast.makeText(this, "Unable to load shark model", Toast.LENGTH_SHORT).show();
                    return null;
                });
        ModelRenderable.builder()
                .setSource(this, R.raw.squid)
                .build().thenAccept(modelRenderable -> squidRenderable = modelRenderable)
                .exceptionally(throwable -> {
                    Toast.makeText(this, "Unable to load squid model", Toast.LENGTH_SHORT).show();
                    return null;
                });
        ModelRenderable.builder()
                .setSource(this, R.raw.starfish)
                .build().thenAccept(modelRenderable -> starfishRenderable = modelRenderable)
                .exceptionally(throwable -> {
                    Toast.makeText(this, "Unable to load starfish model", Toast.LENGTH_SHORT).show();
                    return null;
                });
        ModelRenderable.builder()
                .setSource(this, R.raw.turtle)
                .build().thenAccept(modelRenderable -> turtleRenderable = modelRenderable)
                .exceptionally(throwable -> {
                    Toast.makeText(this, "Unable to load turtle model", Toast.LENGTH_SHORT).show();
                    return null;
                });
    }

    private void map() {
        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.arFragment);
        crab = findViewById(R.id.crab);
        crayfish = findViewById(R.id.crayfish);
        dolphin = findViewById(R.id.dolphin);
        jellyfish = findViewById(R.id.jellyfish);
        octopus = findViewById(R.id.octopus);
        seahorse = findViewById(R.id.seahorse);
        shark = findViewById(R.id.shark);
        squid = findViewById(R.id.squid);
        starfish = findViewById(R.id.starfish);
        turtle = findViewById(R.id.turtle);
    }

    private void setArrayView() {
        arrayView = new View[]{
                crab, crayfish, dolphin, jellyfish, octopus, seahorse, shark, squid, starfish, turtle
        };
    }

    private void setClickListener() {
        for (int i = 0; i < arrayView.length; i++) {
            arrayView[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (v.getId() == R.id.crab) {
                        selected = 1;
                        setBackground(v.getId());
                    } else if (v.getId() == R.id.crayfish) {
                        selected = 2;
                        setBackground(v.getId());
                    } else if (v.getId() == R.id.dolphin) {
                        selected = 3;
                        setBackground(v.getId());
                    } else if (v.getId() == R.id.jellyfish) {
                        selected = 4;
                        setBackground(v.getId());
                    } else if (v.getId() == R.id.octopus) {
                        selected = 5;
                        setBackground(v.getId());
                    } else if (v.getId() == R.id.seahorse) {
                        selected = 6;
                        setBackground(v.getId());
                    } else if (v.getId() == R.id.shark) {
                        selected = 7;
                        setBackground(v.getId());
                    } else if (v.getId() == R.id.squid) {
                        selected = 8;
                        setBackground(v.getId());
                    } else if (v.getId() == R.id.starfish) {
                        selected = 9;
                        setBackground(v.getId());
                    } else if (v.getId() == R.id.turtle) {
                        selected = 10;
                        setBackground(v.getId());
                    }
                }
            });
        }
    }

    private void createModel(AnchorNode anchorNode, int selected) {
        if (selected == 1) {
            TransformableNode crab = new TransformableNode(arFragment.getTransformationSystem());
            crab.setParent(anchorNode);
            crab.setRenderable(crabRenderable);
            crab.select();
            addName(anchorNode, crab, "Crab");
        }
        if (selected == 2) {
            TransformableNode crayfish = new TransformableNode(arFragment.getTransformationSystem());
            crayfish.setParent(anchorNode);
            crayfish.setRenderable(crayfishRenderable);
            crayfish.select();
            addName(anchorNode, crayfish, "Crayfish");
        }
        if (selected == 3) {
            TransformableNode dolphin = new TransformableNode(arFragment.getTransformationSystem());
            dolphin.setParent(anchorNode);
            dolphin.setRenderable(dolphinRenderable);
            dolphin.select();
            addName(anchorNode, dolphin, "Dolphin");
        }
        if (selected == 4) {
            TransformableNode jellyfish = new TransformableNode(arFragment.getTransformationSystem());
            jellyfish.setParent(anchorNode);
            jellyfish.setRenderable(jellyfishRenderable);
            jellyfish.select();
            addName(anchorNode, jellyfish, "Jellyfish");
        }
        if (selected == 5) {
            TransformableNode octopus = new TransformableNode(arFragment.getTransformationSystem());
            octopus.setParent(anchorNode);
            octopus.setRenderable(octopusRenderable);
            octopus.select();
            addName(anchorNode, octopus, "Octopus");
        }
        if (selected == 6) {
            TransformableNode seahorse = new TransformableNode(arFragment.getTransformationSystem());
            seahorse.setParent(anchorNode);
            seahorse.setRenderable(seahorseRenderable);
            seahorse.select();
            addName(anchorNode, seahorse, "Seahorse");
        }
        if (selected == 7) {
            TransformableNode shark = new TransformableNode(arFragment.getTransformationSystem());
            shark.setParent(anchorNode);
            shark.setRenderable(sharkRenderable);
            shark.select();
            addName(anchorNode, shark, "Shark");
        }
        if (selected == 8) {
            TransformableNode squid = new TransformableNode(arFragment.getTransformationSystem());
            squid.setParent(anchorNode);
            squid.setRenderable(squidRenderable);
            squid.select();
            addName(anchorNode, squid, "Squid");
        }
        if (selected == 9) {
            TransformableNode starfish = new TransformableNode(arFragment.getTransformationSystem());
            starfish.setParent(anchorNode);
            starfish.setRenderable(starfishRenderable);
            starfish.select();
            addName(anchorNode, starfish, "Starfish");
        }
        if (selected == 10) {
            TransformableNode turtle = new TransformableNode(arFragment.getTransformationSystem());
            turtle.setParent(anchorNode);
            turtle.setRenderable(turtleRenderable);
            turtle.select();
            addName(anchorNode, turtle, "Turtle");
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
                arrayView[i].setBackgroundColor(parseColor("#80333639"));
            } else {
                arrayView[i].setBackgroundColor(TRANSPARENT);
            }
        }
    }
}