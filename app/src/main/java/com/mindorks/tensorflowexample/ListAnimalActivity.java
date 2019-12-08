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

public class ListAnimalActivity extends AppCompatActivity {

    private ArFragment arFragment;

    private ModelRenderable foxRenderable, catRenderable, dogRenderable,
            duckRenderable, horseRenderable, zebraRenderable,
            sheepRenderable, chickenRenderable, kangarooRenderable, cowRenderable;
    ImageView fox, cat, dog, duck, horse, zebra, sheep, chicken, kangaroo, cow;
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
        setContentView(R.layout.list_animal);

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
                    Toast.makeText(ListAnimalActivity.this, "Recording", Toast.LENGTH_SHORT).show();
                } else {
                    record.setBackgroundResource(R.drawable.btn_record);
                    Toast.makeText(ListAnimalActivity.this, "Saved video to gallery.", Toast.LENGTH_SHORT).show();
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
                .setSource(this, R.raw.fox)
                .build().thenAccept(modelRenderable -> foxRenderable = modelRenderable)
                .exceptionally(throwable -> {
                    Toast.makeText(this, "Unable to load fox model", Toast.LENGTH_SHORT).show();
                    return null;
                });
        ModelRenderable.builder()
                .setSource(this, R.raw.cat)
                .build().thenAccept(modelRenderable -> catRenderable = modelRenderable)
                .exceptionally(throwable -> {
                    Toast.makeText(this, "Unable to load cat model", Toast.LENGTH_SHORT).show();
                    return null;
                });
        ModelRenderable.builder()
                .setSource(this, R.raw.dog)
                .build().thenAccept(modelRenderable -> dogRenderable = modelRenderable)
                .exceptionally(throwable -> {
                    Toast.makeText(this, "Unable to load dog model", Toast.LENGTH_SHORT).show();
                    return null;
                });
        ModelRenderable.builder()
                .setSource(this, R.raw.duck)
                .build().thenAccept(modelRenderable -> duckRenderable = modelRenderable)
                .exceptionally(throwable -> {
                    Toast.makeText(this, "Unable to load duck model", Toast.LENGTH_SHORT).show();
                    return null;
                });
        ModelRenderable.builder()
                .setSource(this, R.raw.horse_01)
                .build().thenAccept(modelRenderable -> horseRenderable = modelRenderable)
                .exceptionally(throwable -> {
                    Toast.makeText(this, "Unable to load horse model", Toast.LENGTH_SHORT).show();
                    return null;
                });
        ModelRenderable.builder()
                .setSource(this, R.raw.zebra)
                .build().thenAccept(modelRenderable -> zebraRenderable = modelRenderable)
                .exceptionally(throwable -> {
                    Toast.makeText(this, "Unable to load zebra model", Toast.LENGTH_SHORT).show();
                    return null;
                });
        ModelRenderable.builder()
                .setSource(this, R.raw.sheep)
                .build().thenAccept(modelRenderable -> sheepRenderable = modelRenderable)
                .exceptionally(throwable -> {
                    Toast.makeText(this, "Unable to load sheep model", Toast.LENGTH_SHORT).show();
                    return null;
                });
        ModelRenderable.builder()
                .setSource(this, R.raw.chicken)
                .build().thenAccept(modelRenderable -> chickenRenderable = modelRenderable)
                .exceptionally(throwable -> {
                    Toast.makeText(this, "Unable to load chicken model", Toast.LENGTH_SHORT).show();
                    return null;
                });
        ModelRenderable.builder()
                .setSource(this, R.raw.kangaroo)
                .build().thenAccept(modelRenderable -> kangarooRenderable = modelRenderable)
                .exceptionally(throwable -> {
                    Toast.makeText(this, "Unable to load kangaroo model", Toast.LENGTH_SHORT).show();
                    return null;
                });
        ModelRenderable.builder()
                .setSource(this, R.raw.cow)
                .build().thenAccept(modelRenderable -> cowRenderable = modelRenderable)
                .exceptionally(throwable -> {
                    Toast.makeText(this, "Unable to load cow model", Toast.LENGTH_SHORT).show();
                    return null;
                });
    }

    private void map() {
        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.arFragment);
        fox = findViewById(R.id.fox);
        cat = findViewById(R.id.cat);
        dog = findViewById(R.id.dog);
        duck = findViewById(R.id.duck);
        horse = findViewById(R.id.horse);
        zebra = findViewById(R.id.zebra);
        sheep = findViewById(R.id.sheep);
        chicken = findViewById(R.id.chicken);
        kangaroo = findViewById(R.id.kangaroo);
        cow = findViewById(R.id.cow);
    }

    private void setArrayView() {
        arrayView = new View[]{
                fox, cat, dog, duck, horse, zebra, sheep, chicken, kangaroo, cow
        };
    }

    private void setClickListener() {
        for (int i = 0; i < arrayView.length; i++) {
            arrayView[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (v.getId() == R.id.fox) {
                        selected = 1;
                        setBackground(v.getId());
                    } else if (v.getId() == R.id.cat) {
                        selected = 2;
                        setBackground(v.getId());
                    } else if (v.getId() == R.id.dog) {
                        selected = 3;
                        setBackground(v.getId());
                    } else if (v.getId() == R.id.duck) {
                        selected = 4;
                        setBackground(v.getId());
                    } else if (v.getId() == R.id.horse) {
                        selected = 5;
                        setBackground(v.getId());
                    } else if (v.getId() == R.id.zebra) {
                        selected = 6;
                        setBackground(v.getId());
                    } else if (v.getId() == R.id.sheep) {
                        selected = 7;
                        setBackground(v.getId());
                    } else if (v.getId() == R.id.chicken) {
                        selected = 8;
                        setBackground(v.getId());
                    } else if (v.getId() == R.id.kangaroo) {
                        selected = 9;
                        setBackground(v.getId());
                    } else if (v.getId() == R.id.cow) {
                        selected = 10;
                        setBackground(v.getId());
                    }
                }
            });
        }
    }

    private void createModel(AnchorNode anchorNode, int selected) {
        if (selected == 1) {
            TransformableNode fox = new TransformableNode(arFragment.getTransformationSystem());
            fox.setParent(anchorNode);
            fox.setRenderable(foxRenderable);
            fox.select();
            addName(anchorNode, fox, "Fox");
        }
        if (selected == 2) {
            TransformableNode cat = new TransformableNode(arFragment.getTransformationSystem());
            cat.setParent(anchorNode);
            cat.setRenderable(catRenderable);
            cat.select();
            addName(anchorNode, cat, "Cat");
        }
        if (selected == 3) {
            TransformableNode dog = new TransformableNode(arFragment.getTransformationSystem());
            dog.setParent(anchorNode);
            dog.setRenderable(dogRenderable);
            dog.select();
            addName(anchorNode, dog, "Dog");
        }
        if (selected == 4) {
            TransformableNode duck = new TransformableNode(arFragment.getTransformationSystem());
            duck.setParent(anchorNode);
            duck.setRenderable(duckRenderable);
            duck.select();
            addName(anchorNode, duck, "Duck");
        }
        if (selected == 5) {
            TransformableNode horse = new TransformableNode(arFragment.getTransformationSystem());
            horse.setParent(anchorNode);
            horse.setRenderable(horseRenderable);
            horse.select();
            addName(anchorNode, horse, "Horse");
        }
        if (selected == 6) {
            TransformableNode zebra = new TransformableNode(arFragment.getTransformationSystem());
            zebra.setParent(anchorNode);
            zebra.setRenderable(zebraRenderable);
            zebra.select();
            addName(anchorNode, zebra, "Zebra");
        }
        if (selected == 7) {
            TransformableNode sheep = new TransformableNode(arFragment.getTransformationSystem());
            sheep.setParent(anchorNode);
            sheep.setRenderable(sheepRenderable);
            sheep.select();
            addName(anchorNode, sheep, "Sheep");
        }
        if (selected == 8) {
            TransformableNode chicken = new TransformableNode(arFragment.getTransformationSystem());
            chicken.setParent(anchorNode);
            chicken.setRenderable(chickenRenderable);
            chicken.select();
            addName(anchorNode, chicken, "Chicken");
        }
        if (selected == 9) {
            TransformableNode kangaroo = new TransformableNode(arFragment.getTransformationSystem());
            kangaroo.setParent(anchorNode);
            kangaroo.setRenderable(kangarooRenderable);
            kangaroo.select();
            addName(anchorNode, kangaroo, "Kangaroo");
        }
        if (selected == 10) {
            TransformableNode cow = new TransformableNode(arFragment.getTransformationSystem());
            cow.setParent(anchorNode);
            cow.setRenderable(cowRenderable);
            cow.select();
            addName(anchorNode, cow, "Cow");
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


