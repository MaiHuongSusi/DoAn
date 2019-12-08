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

public class ListPlantActivity extends AppCompatActivity {

    private ArFragment arFragment;
    private ModelRenderable mushroomRenderable, chiliRenderable, flowerRenderable, appleRenderable,
            cactusRenderable, carrotRenderable, strawberryRenderable,
            tomatoRenderable, watermelonRenderable;
    ImageView mushroom, chili, flower, apple, cactus, carrot, strawberry, tomato, watermelon;
    View arrayView[];
    ViewRenderable name_object;
    int selected = 1;
    private VideoRecorder videoRecorder;
    Button record;

    @Override
    @SuppressWarnings({"AndroidApiChecker", "FutureReturnValueIgnored"})
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_plant);

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
                    Toast.makeText(ListPlantActivity.this, "Recording", Toast.LENGTH_SHORT).show();
                } else {
                    record.setBackgroundResource(R.drawable.btn_record);
                    Toast.makeText(ListPlantActivity.this, "Saved video to gallery.", Toast.LENGTH_SHORT).show();
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
                .setSource(this, R.raw.mushroom)
                .build().thenAccept(modelRenderable -> mushroomRenderable = modelRenderable)
                .exceptionally(throwable -> {
                    Toast.makeText(this, "Unable to load mushroom model", Toast.LENGTH_SHORT).show();
                    return null;
                });
        ModelRenderable.builder()
                .setSource(this, R.raw.chili)
                .build().thenAccept(modelRenderable -> chiliRenderable = modelRenderable)
                .exceptionally(throwable -> {
                    Toast.makeText(this, "Unable to load chili model", Toast.LENGTH_SHORT).show();
                    return null;
                });
        ModelRenderable.builder()
                .setSource(this, R.raw.flower)
                .build().thenAccept(modelRenderable -> flowerRenderable = modelRenderable)
                .exceptionally(throwable -> {
                    Toast.makeText(this, "Unable to load flower model", Toast.LENGTH_SHORT).show();
                    return null;
                });
        ModelRenderable.builder()
                .setSource(this, R.raw.apple)
                .build().thenAccept(modelRenderable -> appleRenderable = modelRenderable)
                .exceptionally(throwable -> {
                    Toast.makeText(this, "Unable to load apple model", Toast.LENGTH_SHORT).show();
                    return null;
                });
        ModelRenderable.builder()
                .setSource(this, R.raw.cactus)
                .build().thenAccept(modelRenderable -> cactusRenderable = modelRenderable)
                .exceptionally(throwable -> {
                    Toast.makeText(this, "Unable to load cactus model", Toast.LENGTH_SHORT).show();
                    return null;
                });
        ModelRenderable.builder()
                .setSource(this, R.raw.carrot)
                .build().thenAccept(modelRenderable -> carrotRenderable = modelRenderable)
                .exceptionally(throwable -> {
                    Toast.makeText(this, "Unable to load carrot model", Toast.LENGTH_SHORT).show();
                    return null;
                });
        ModelRenderable.builder()
                .setSource(this, R.raw.strawberry)
                .build().thenAccept(modelRenderable -> strawberryRenderable = modelRenderable)
                .exceptionally(throwable -> {
                    Toast.makeText(this, "Unable to load strawberry model", Toast.LENGTH_SHORT).show();
                    return null;
                });
        ModelRenderable.builder()
                .setSource(this, R.raw.tomato)
                .build().thenAccept(modelRenderable -> tomatoRenderable = modelRenderable)
                .exceptionally(throwable -> {
                    Toast.makeText(this, "Unable to load tomato model", Toast.LENGTH_SHORT).show();
                    return null;
                });
        ModelRenderable.builder()
                .setSource(this, R.raw.watermelon)
                .build().thenAccept(modelRenderable -> watermelonRenderable = modelRenderable)
                .exceptionally(throwable -> {
                    Toast.makeText(this, "Unable to load watermelon model", Toast.LENGTH_SHORT).show();
                    return null;
                });
    }

    private void map() {
        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.arFragment);
        mushroom = findViewById(R.id.mushroom);
        chili = findViewById(R.id.chili);
        flower = findViewById(R.id.flower);
        apple = findViewById(R.id.apple);
        cactus = findViewById(R.id.cactus);
        carrot = findViewById(R.id.carrot);
        strawberry = findViewById(R.id.strawberry);
        tomato = findViewById(R.id.tomato);
        watermelon = findViewById(R.id.watermelon);
    }

    private void setArrayView() {
        arrayView = new View[]{
                mushroom, chili, flower, apple, cactus, carrot, strawberry, tomato, watermelon
        };
    }

    private void setClickListener() {
        for (int i = 0; i < arrayView.length; i++) {
            arrayView[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (v.getId() == R.id.mushroom) {
                        selected = 1;
                        setBackground(v.getId());
                    } else if (v.getId() == R.id.chili) {
                        selected = 2;
                        setBackground(v.getId());
                    } else if (v.getId() == R.id.flower) {
                        selected = 3;
                        setBackground(v.getId());
                    } else if (v.getId() == R.id.apple) {
                        selected = 4;
                        setBackground(v.getId());
                    } else if (v.getId() == R.id.cactus) {
                        selected = 5;
                        setBackground(v.getId());
                    } else if (v.getId() == R.id.carrot) {
                        selected = 6;
                        setBackground(v.getId());
                    } else if (v.getId() == R.id.strawberry) {
                        selected = 7;
                        setBackground(v.getId());
                    } else if (v.getId() == R.id.tomato) {
                        selected = 8;
                        setBackground(v.getId());
                    } else {
                        selected = 9;
                        setBackground(v.getId());
                    }
                }
            });
        }
    }

    private void createModel(AnchorNode anchorNode, int selected) {
        if (selected == 1) {
            TransformableNode mushroom = new TransformableNode(arFragment.getTransformationSystem());
            mushroom.setParent(anchorNode);
            mushroom.setRenderable(mushroomRenderable);
            mushroom.select();
            addName(anchorNode, mushroom, "Mushroom");
        }
        if (selected == 2) {
            TransformableNode chili = new TransformableNode(arFragment.getTransformationSystem());
            chili.setParent(anchorNode);
            chili.setRenderable(chiliRenderable);
            chili.select();
            addName(anchorNode, chili, "Chili");
        }
        if (selected == 3) {
            TransformableNode flower = new TransformableNode(arFragment.getTransformationSystem());
            flower.setParent(anchorNode);
            flower.setRenderable(flowerRenderable);
            flower.select();
            addName(anchorNode, flower, "Flower");
        }
        if (selected == 4) {
            TransformableNode apple = new TransformableNode(arFragment.getTransformationSystem());
            apple.setParent(anchorNode);
            apple.setRenderable(appleRenderable);
            apple.select();
            addName(anchorNode, apple, "Apple");
        }
        if (selected == 5) {
            TransformableNode cactus = new TransformableNode(arFragment.getTransformationSystem());
            cactus.setParent(anchorNode);
            cactus.setRenderable(cactusRenderable);
            cactus.select();
            addName(anchorNode, cactus, "Cactus");
        }
        if (selected == 6) {
            TransformableNode carrot = new TransformableNode(arFragment.getTransformationSystem());
            carrot.setParent(anchorNode);
            carrot.setRenderable(carrotRenderable);
            carrot.select();
            addName(anchorNode, carrot, "Carrot");
        }
        if (selected == 7) {
            TransformableNode strawberry = new TransformableNode(arFragment.getTransformationSystem());
            strawberry.setParent(anchorNode);
            strawberry.setRenderable(strawberryRenderable);
            strawberry.select();
            addName(anchorNode, strawberry, "Strawberry");
        }
        if (selected == 8) {
            TransformableNode tomato = new TransformableNode(arFragment.getTransformationSystem());
            tomato.setParent(anchorNode);
            tomato.setRenderable(tomatoRenderable);
            tomato.select();
            addName(anchorNode, tomato, "Tomato");
        }
        if (selected == 9) {
            TransformableNode watermelon = new TransformableNode(arFragment.getTransformationSystem());
            watermelon.setParent(anchorNode);
            watermelon.setRenderable(watermelonRenderable);
            watermelon.select();
            addName(anchorNode, watermelon, "Watermelon");
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



