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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.ar.core.Anchor;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.animation.ModelAnimator;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.AnimationData;
import com.google.ar.sceneform.rendering.MaterialFactory;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.ShapeFactory;
import com.google.ar.sceneform.rendering.ViewRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.BaseArFragment;
import com.google.ar.sceneform.ux.TransformableNode;

import static android.graphics.Color.TRANSPARENT;
import static android.graphics.Color.parseColor;

public class ListOtherAnimationActivity extends AppCompatActivity {

    private ArFragment arFragment;
    private ModelRenderable astronautRenderable, cow1Renderable, fishRenderable, hornetRenderable, mouseRenderable, skeletonRenderable;
    ImageView astronaut, cow1, fish, hornet, mouse, skeleton;
    View arrayView[];
    ViewRenderable name_object;
    int selected = 1;
    private VideoRecorder videoRecorder;
    private ModelAnimator modelAnimator;
    private int i = 0;
    Button record;
    LinearLayout btnAnimation, btnLoop;

    @Override
    @SuppressWarnings({"AndroidApiChecker", "FutureReturnValueIgnored"})
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_other_animation);

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
                MaterialFactory.makeOpaqueWithColor(ListOtherAnimationActivity.this, new com.google.ar.sceneform.rendering.Color(Color.RED))
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
                    record.setBackgroundResource(R.drawable.btn_record);
                    Toast.makeText(ListOtherAnimationActivity.this, "Recording", Toast.LENGTH_SHORT).show();
                } else {
                    record.setBackgroundResource(R.drawable.btn_stop);
                    Toast.makeText(ListOtherAnimationActivity.this, "Saved video to gallery.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        LinearLayout animation = findViewById(R.id.btnAnimation);
        animation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (selected) {
                    case 1:
                        animationModel(astronautRenderable);
                        break;
                    case 2:
                        animationModel(cow1Renderable);
                        break;
                    case 3:
                        animationModel(fishRenderable);
                        break;
                    case 4:
                        animationModel(hornetRenderable);
                        break;
                    case 5:
                        animationModel(mouseRenderable);
                        break;
                    case 6:
                        animationModel(skeletonRenderable);
                        break;
                }
            }
        });

        LinearLayout loop = findViewById(R.id.btnLoop);
        loop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (modelAnimator.isRunning()) {
                    modelAnimator.setRepeatCount(100);
                }
            }
        });
    }

    private void animationModel(ModelRenderable modelRenderable) {
        if (modelAnimator != null && modelAnimator.isRunning()) {
            modelAnimator.end();
        }
        int animationCount = modelRenderable.getAnimationDataCount();
        if (i == animationCount) {
            i = 0;
        }
        AnimationData animationData = modelRenderable.getAnimationData(i);
        modelAnimator = new ModelAnimator(animationData, modelRenderable);
        modelAnimator.start();
        i++;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(ListOtherAnimationActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
    }

    private void setupModel() {
        ViewRenderable.builder()
                .setView(this, R.layout.name_object)
                .build()
                .thenAccept(viewRenderable -> name_object = viewRenderable);

        ModelRenderable.builder()
                .setSource(this, R.raw.astronaut)
                .build().thenAccept(modelRenderable -> astronautRenderable = modelRenderable)
                .exceptionally(throwable -> {
                    Toast.makeText(this, "Unable to load astronaut model", Toast.LENGTH_SHORT).show();
                    return null;
                });
        ModelRenderable.builder()
                .setSource(this, R.raw.cow1)
                .build().thenAccept(modelRenderable -> cow1Renderable = modelRenderable)
                .exceptionally(throwable -> {
                    Toast.makeText(this, "Unable to load cow model", Toast.LENGTH_SHORT).show();
                    return null;
                });
        ModelRenderable.builder()
                .setSource(this, R.raw.fish)
                .build().thenAccept(modelRenderable -> fishRenderable = modelRenderable)
                .exceptionally(throwable -> {
                    Toast.makeText(this, "Unable to load fish model", Toast.LENGTH_SHORT).show();
                    return null;
                });
        ModelRenderable.builder()
                .setSource(this, R.raw.hornet)
                .build().thenAccept(modelRenderable -> hornetRenderable= modelRenderable)
                .exceptionally(throwable -> {
                    Toast.makeText(this, "Unable to load hornet model", Toast.LENGTH_SHORT).show();
                    return null;
                });
        ModelRenderable.builder()
                .setSource(this, R.raw.rata)
                .build().thenAccept(modelRenderable -> mouseRenderable = modelRenderable)
                .exceptionally(throwable -> {
                    Toast.makeText(this, "Unable to load mouse model", Toast.LENGTH_SHORT).show();
                    return null;
                });
        ModelRenderable.builder()
                .setSource(this, R.raw.skeleton)
                .build().thenAccept(modelRenderable -> skeletonRenderable = modelRenderable)
                .exceptionally(throwable -> {
                    Toast.makeText(this, "Unable to load skeleton model", Toast.LENGTH_SHORT).show();
                    return null;
                });
    }

    private void map() {
        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.arFragment);
        astronaut = findViewById(R.id.astronaut);
        cow1 = findViewById(R.id.cow1);
        fish = findViewById(R.id.fish);
        hornet = findViewById(R.id.hornet);
        mouse = findViewById(R.id.mouse);
        skeleton = findViewById(R.id.skeleton);
    }

    private void setArrayView() {
        arrayView = new View[]{
                astronaut, cow1, fish, hornet, mouse, skeleton
        };
    }

    private void setClickListener() {
        for (int i = 0; i < arrayView.length; i++) {
            arrayView[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (v.getId() == R.id.astronaut) {
                        selected = 1;
                        setBackground(v.getId());
                    } else if (v.getId() == R.id.cow1) {
                        selected = 2;
                        setBackground(v.getId());
                    } else if (v.getId() == R.id.fish) {
                        selected = 3;
                        setBackground(v.getId());
                    } else if (v.getId() == R.id.hornet) {
                        selected = 4;
                        setBackground(v.getId());
                    } else if (v.getId() == R.id.mouse) {
                        selected = 5;
                        setBackground(v.getId());
                    } else if (v.getId() == R.id.skeleton) {
                        selected = 6;
                        setBackground(v.getId());
                    } 
                }
            });
        }
    }

    private void createModel(AnchorNode anchorNode, int selected) {
        if (selected == 1) {
            TransformableNode astronaut = new TransformableNode(arFragment.getTransformationSystem());
            astronaut.setParent(anchorNode);
            astronaut.setRenderable(astronautRenderable);
            astronaut.select();
            addName(anchorNode, astronaut, "astronaut");
        }
        if (selected == 2) {
            TransformableNode cow1 = new TransformableNode(arFragment.getTransformationSystem());
            cow1.setParent(anchorNode);
            cow1.setRenderable(cow1Renderable);
            cow1.select();
            addName(anchorNode, cow1, "cow");
        }
        if (selected == 3) {
            TransformableNode fish = new TransformableNode(arFragment.getTransformationSystem());
            fish.setParent(anchorNode);
            fish.setRenderable(fishRenderable);
            fish.select();
            addName(anchorNode, fish, "fish");
        }
        if (selected == 4) {
            TransformableNode hornet = new TransformableNode(arFragment.getTransformationSystem());
            hornet.setParent(anchorNode);
            hornet.setRenderable(hornetRenderable);
            hornet.select();
            addName(anchorNode, hornet, "hornet");
        }
        if (selected == 5) {
            TransformableNode mouse = new TransformableNode(arFragment.getTransformationSystem());
            mouse.setParent(anchorNode);
            mouse.setRenderable(mouseRenderable);
            mouse.select();
            addName(anchorNode, mouse, "mouse");
        }
        if (selected == 6) {
            TransformableNode skeleton = new TransformableNode(arFragment.getTransformationSystem());
            skeleton.setParent(anchorNode);
            skeleton.setRenderable(skeletonRenderable);
            skeleton.select();
            addName(anchorNode, skeleton, "skeleton");
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