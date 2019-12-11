package com.mindorks.tensorflowexample;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import java.util.ArrayList;
import java.util.List;

public class TestRecycleView extends AppCompatActivity {
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<Integer> mImages = new ArrayList<>();
    String topic;
    private ArFragment arFragment;
    int selected = 0;
    ViewRenderable name_object;
    private ModelRenderable ren1, ren2;
    String result;
    private RecyclerView.LayoutManager layoutManager;
    private List<String> listData = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_recycle_view);
        topic = getIntent().getStringExtra("topic");

        initImageBitmap();
        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.arFragment);
        arFragment.setOnTapArPlaneListener(new BaseArFragment.OnTapArPlaneListener() {
            @Override
            public void onTapPlane(HitResult hitResult, Plane plane, MotionEvent motionEvent) {
                MaterialFactory.makeOpaqueWithColor(TestRecycleView.this, new com.google.ar.sceneform.rendering.Color(Color.RED))
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
    }

    private void createModel(AnchorNode anchorNode, int selected) {
        ViewRenderable.builder()
                .setView(this, R.layout.name_object)
                .build()
                .thenAccept(viewRenderable -> name_object = viewRenderable);
        TransformableNode node = new TransformableNode(arFragment.getTransformationSystem());

        switch (selected) {
            case 1:
                ModelRenderable.builder()
                        .setSource(this, mImages.get(selected))
                        .build().thenAccept(modelRenderable -> ren1 = modelRenderable)
                        .exceptionally(throwable -> {
                            Toast.makeText(this, "Unable to load " + mNames.get(selected) + " model", Toast.LENGTH_SHORT).show();
                            return null;
                        });
                createModel(node, anchorNode, ren1);
                break;
            case 2:
                ModelRenderable.builder()
                        .setSource(this, mImages.get(selected))
                        .build().thenAccept(modelRenderable -> ren2 = modelRenderable)
                        .exceptionally(throwable -> {
                            Toast.makeText(this, "Unable to load " + mNames.get(selected) + " model", Toast.LENGTH_SHORT).show();
                            return null;
                        });
                createModel(node, anchorNode, ren2);
                break;
            default:
//                Toast.makeText(this, "Please choose a model.", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void createModel(TransformableNode node, AnchorNode anchorNode, ModelRenderable ren) {
        node.setParent(anchorNode);
        node.setRenderable(ren);
        node.select();
        addName(anchorNode, node, mNames.get(selected));
    }

    public void initImageBitmap() {
        switch (topic) {
            case "Ocean":
                mImages.add(R.drawable.apple);
                mNames.add("apple");

                mImages.add(R.drawable.bike);
                mNames.add("bike");

                break;
            case "Animal":
                mImages.add(R.drawable.apple);
                mNames.add("apple1");

                mImages.add(R.drawable.bike);
                mNames.add("bike1");

                break;
            case "Plant":
                mImages.add(R.drawable.apple);
                mNames.add("apple2");

                mImages.add(R.drawable.bike);
                mNames.add("bike2");

                break;
            case "Home":
                mImages.add(R.drawable.apple);
                mNames.add("apple3");

                mImages.add(R.drawable.bike);
                mNames.add("bike3");

                break;
            case "Person":
                mImages.add(R.drawable.apple);
                mNames.add("apple4");

                mImages.add(R.drawable.bike);
                mNames.add("bike4");
                break;
            case "Vehicle":
                mImages.add(R.drawable.apple);
                mNames.add("apple5");

                mImages.add(R.drawable.bike);
                mNames.add("bike5");
                break;
        }

        initRecycleView();
    }

    private void initRecycleView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(layoutManager);
        RecycleViewAdapter recycleViewAdapter = new RecycleViewAdapter(this, mImages, mNames);
        recycleViewAdapter.setOnItemClick(new RecycleViewAdapter.IOnItemClick() {
            @Override
            public void onClick(int pos) {
                // Pos here
                Toast.makeText(TestRecycleView.this, "Clicked position: " + pos, Toast.LENGTH_SHORT).show();
            }
        });
//        recycleViewAdapter.setOnItemClickListener(new RecycleViewAdapter.IOnItemClickListener() {
//            @Override
//            public void onClick() {
//                selected = recycleViewAdapter.selected;
//                Toast.makeText(TestRecycleView.this, ""+selected, Toast.LENGTH_SHORT).show();
//            }
//        });
        recyclerView.setAdapter(recycleViewAdapter);
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
}
