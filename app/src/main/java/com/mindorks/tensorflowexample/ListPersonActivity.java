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

public class ListPersonActivity extends AppCompatActivity {

    private ArFragment arFragment;
    private ModelRenderable breifcaseRenderable, capRenderable, flipflopsRenderable,
            headphonesRenderable, purseRenderable, sandalsRenderable, sneakersRenderable,
            stilettosRenderable, sunglassesRenderable, tshirtRenderable;
    ImageView breifcase, cap, flipflops, headphones, purse, sandals, sneakers, stilettos, sunglasses, tshirt;
    View arrayView[];
    ViewRenderable name_object;
    int selected = 1;
    private VideoRecorder videoRecorder;
    Button record;

    @Override
    @SuppressWarnings({"AndroidApiChecker", "FutureReturnValueIgnored"})
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_person);

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
                    Toast.makeText(ListPersonActivity.this, "Recording", Toast.LENGTH_SHORT).show();
                } else {
                    record.setBackgroundResource(R.drawable.btn_record);
                    Toast.makeText(ListPersonActivity.this, "Saved video to gallery.", Toast.LENGTH_SHORT).show();
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
                .setSource(this, R.raw.breifcase)
                .build().thenAccept(modelRenderable -> breifcaseRenderable = modelRenderable)
                .exceptionally(throwable -> {
                    Toast.makeText(this, "Unable to load breifcase model", Toast.LENGTH_SHORT).show();
                    return null;
                });
        ModelRenderable.builder()
                .setSource(this, R.raw.cap)
                .build().thenAccept(modelRenderable -> capRenderable = modelRenderable)
                .exceptionally(throwable -> {
                    Toast.makeText(this, "Unable to load cap model", Toast.LENGTH_SHORT).show();
                    return null;
                });
        ModelRenderable.builder()
                .setSource(this, R.raw.flipflops)
                .build().thenAccept(modelRenderable -> flipflopsRenderable = modelRenderable)
                .exceptionally(throwable -> {
                    Toast.makeText(this, "Unable to load flipflops model", Toast.LENGTH_SHORT).show();
                    return null;
                });
        ModelRenderable.builder()
                .setSource(this, R.raw.headphones)
                .build().thenAccept(modelRenderable -> headphonesRenderable = modelRenderable)
                .exceptionally(throwable -> {
                    Toast.makeText(this, "Unable to load headphones model", Toast.LENGTH_SHORT).show();
                    return null;
                });
        ModelRenderable.builder()
                .setSource(this, R.raw.purse)
                .build().thenAccept(modelRenderable -> purseRenderable = modelRenderable)
                .exceptionally(throwable -> {
                    Toast.makeText(this, "Unable to load purse model", Toast.LENGTH_SHORT).show();
                    return null;
                });
        ModelRenderable.builder()
                .setSource(this, R.raw.sandals)
                .build().thenAccept(modelRenderable -> sandalsRenderable = modelRenderable)
                .exceptionally(throwable -> {
                    Toast.makeText(this, "Unable to load sandals model", Toast.LENGTH_SHORT).show();
                    return null;
                });
        ModelRenderable.builder()
                .setSource(this, R.raw.sneakers)
                .build().thenAccept(modelRenderable -> sneakersRenderable = modelRenderable)
                .exceptionally(throwable -> {
                    Toast.makeText(this, "Unable to load sneakers model", Toast.LENGTH_SHORT).show();
                    return null;
                });
        ModelRenderable.builder()
                .setSource(this, R.raw.stillettos)
                .build().thenAccept(modelRenderable -> stilettosRenderable = modelRenderable)
                .exceptionally(throwable -> {
                    Toast.makeText(this, "Unable to load stilettos model", Toast.LENGTH_SHORT).show();
                    return null;
                });
        ModelRenderable.builder()
                .setSource(this, R.raw.sunglasses)
                .build().thenAccept(modelRenderable -> sunglassesRenderable = modelRenderable)
                .exceptionally(throwable -> {
                    Toast.makeText(this, "Unable to load sunglasses model", Toast.LENGTH_SHORT).show();
                    return null;
                });
        ModelRenderable.builder()
                .setSource(this, R.raw.tshirt)
                .build().thenAccept(modelRenderable -> tshirtRenderable = modelRenderable)
                .exceptionally(throwable -> {
                    Toast.makeText(this, "Unable to load T-shirt model", Toast.LENGTH_SHORT).show();
                    return null;
                });
    }

    private void map() {
        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.arFragment);
        breifcase = findViewById(R.id.breifcap);
        cap = findViewById(R.id.cap);
        flipflops = findViewById(R.id.flipflops);
        headphones = findViewById(R.id.headphones);
        purse = findViewById(R.id.purse);
        sandals = findViewById(R.id.sandals);
        sneakers = findViewById(R.id.sneakers);
        stilettos = findViewById(R.id.stillettos);
        sunglasses = findViewById(R.id.sunglasses);
        tshirt = findViewById(R.id.tshirt);
    }

    private void setArrayView() {
        arrayView = new View[]{
                breifcase, cap, flipflops, headphones, purse, sandals, sneakers, stilettos, sunglasses, tshirt
        };
    }

    private void setClickListener() {
        for (int i = 0; i < arrayView.length; i++) {
            arrayView[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (v.getId() == R.id.breifcap) {
                        selected = 1;
                        setBackground(v.getId());
                    } else if (v.getId() == R.id.cap) {
                        selected = 2;
                        setBackground(v.getId());
                    } else if (v.getId() == R.id.flipflops) {
                        selected = 3;
                        setBackground(v.getId());
                    } else if (v.getId() == R.id.headphones) {
                        selected = 4;
                        setBackground(v.getId());
                    } else if (v.getId() == R.id.purse) {
                        selected = 5;
                        setBackground(v.getId());
                    } else if (v.getId() == R.id.sandals) {
                        selected = 6;
                        setBackground(v.getId());
                    } else if (v.getId() == R.id.sneakers) {
                        selected = 7;
                        setBackground(v.getId());
                    } else if (v.getId() == R.id.stillettos) {
                        selected = 8;
                        setBackground(v.getId());
                    } else if (v.getId() == R.id.sunglasses) {
                        selected = 9;
                        setBackground(v.getId());
                    } else {
                        selected = 10;
                        setBackground(v.getId());
                    }
                }
            });
        }
    }

    private void createModel(AnchorNode anchorNode, int selected) {
        if (selected == 1) {
            TransformableNode breifcase = new TransformableNode(arFragment.getTransformationSystem());
            breifcase.setParent(anchorNode);
            breifcase.setRenderable(breifcaseRenderable);
            breifcase.select();
            addName(anchorNode, breifcase, "breifcase");
        }
        if (selected == 2) {
            TransformableNode cap = new TransformableNode(arFragment.getTransformationSystem());
            cap.setParent(anchorNode);
            cap.setRenderable(capRenderable);
            cap.select();
            addName(anchorNode, cap, "cap");
        }
        if (selected == 3) {
            TransformableNode flipflops = new TransformableNode(arFragment.getTransformationSystem());
            flipflops.setParent(anchorNode);
            flipflops.setRenderable(flipflopsRenderable);
            flipflops.select();
            addName(anchorNode, flipflops, "flipflops");
        }
        if (selected == 4) {
            TransformableNode headphones = new TransformableNode(arFragment.getTransformationSystem());
            headphones.setParent(anchorNode);
            headphones.setRenderable(headphonesRenderable);
            headphones.select();
            addName(anchorNode, headphones, "headphones");
        }
        if (selected == 5) {
            TransformableNode purse = new TransformableNode(arFragment.getTransformationSystem());
            purse.setParent(anchorNode);
            purse.setRenderable( purseRenderable);
            purse.select();
            addName(anchorNode,  purse, "purse");
        }
        if (selected == 6) {
            TransformableNode sandals = new TransformableNode(arFragment.getTransformationSystem());
            sandals.setParent(anchorNode);
            sandals.setRenderable(sandalsRenderable);
            sandals.select();
            addName(anchorNode, sandals, "sandals");
        }
        if (selected == 7) {
            TransformableNode sneakers = new TransformableNode(arFragment.getTransformationSystem());
            sneakers.setParent(anchorNode);
            sneakers.setRenderable(sneakersRenderable);
            sneakers.select();
            addName(anchorNode, sneakers, "sneakers");
        }
        if (selected == 8) {
            TransformableNode stilettos = new TransformableNode(arFragment.getTransformationSystem());
            stilettos.setParent(anchorNode);
            stilettos.setRenderable(stilettosRenderable);
            stilettos.select();
            addName(anchorNode, stilettos, "stilettos");
        }
        if (selected == 9) {
            TransformableNode sunglasses = new TransformableNode(arFragment.getTransformationSystem());
            sunglasses.setParent(anchorNode);
            sunglasses.setRenderable(sunglassesRenderable);
            sunglasses.select();
            addName(anchorNode, sunglasses, "sunglasses");
        }
        if (selected == 10) {
            TransformableNode tshirt = new TransformableNode(arFragment.getTransformationSystem());
            tshirt.setParent(anchorNode);
            tshirt.setRenderable(tshirtRenderable);
            tshirt.select();
            addName(anchorNode, tshirt, "T-shirt");
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



