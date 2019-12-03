package com.mindorks.tensorflowexample;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.speech.tts.TextToSpeech;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import de.hdodenhof.circleimageview.CircleImageView;


public class ChooseImageActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {
    private static final int INPUT_SIZE = 224;
    private static final int IMAGE_MEAN = 117;
    private static final float IMAGE_STD = 1;
    private static final String INPUT_NAME = "input";
    private static final String OUTPUT_NAME = "output";
    private static final String MODEL_FILE = "file:///android_asset/tensorflow_inception_graph.pb";
    private static final String LABEL_FILE =
            "file:///android_asset/imagenet_comp_graph_label_strings.txt";


    private Classifier classifier;
    private Executor executor = Executors.newSingleThreadExecutor();
    private TextToSpeech tts;

    ImageView imageView;
    TextView nameResult;
    ImageView btnDes;
    Button btnDetect, btnChooseImg;
    private LinearLayout imageSpeaker, imageDetail, imageVideo;
    Uri imageUri;
    GridLayout idGrid;
    private static final int PICK_IMAGE = 100;
    Bitmap bitmap;
    Dialog dialog;
    String detail;
    private LinearLayout btnIdentify, btnCapture;
    private StorageReference mStorageRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        idGrid = (GridLayout) findViewById(R.id.idGrid);
        imageView = (ImageView) findViewById(R.id.imageView);
        nameResult = (TextView) findViewById(R.id.nameResult);
        imageSpeaker = findViewById(R.id.imageSpeaker);
        imageDetail = findViewById(R.id.imageDetail);
        imageVideo = findViewById(R.id.imageVideo);
        nameResult = (TextView) findViewById(R.id.nameResult);
        btnDetect = (Button) findViewById(R.id.btnDetect);
        btnChooseImg = (Button) findViewById(R.id.btnChooseImg);
        btnIdentify = findViewById(R.id.btnIdentify);
        btnCapture = findViewById(R.id.btnCapture);

        mStorageRef = FirebaseStorage.getInstance().getReference();

        btnIdentify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent identify = new Intent(ChooseImageActivity.this, InstantDetectActivity.class);
                startActivity(identify);
            }
        });
        btnCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent capture = new Intent(ChooseImageActivity.this, CaptureActivity.class);
                startActivity(capture);
            }
        });

        nameResult.setMovementMethod(new ScrollingMovementMethod());
        idGrid.setVisibility(idGrid.INVISIBLE);

        btnChooseImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameResult.setText(null);
                openGallary();
            }
        });

        btnDetect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imageUri != null)
                    detectImage();
                else
                    Toast.makeText(ChooseImageActivity.this, "None of the image be selected to recognizing", Toast.LENGTH_SHORT).show();
            }
        });

        initTensorFlowAndLoadModel();
        tts = new TextToSpeech(this, this);
        imageSpeaker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                speakOut();
            }
        });
        dialog = new Dialog(this);
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                tts.stop();
            }
        });
        imageDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDetail();
            }
        });
        imageVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (detail != "[Not Classify]" && detail != "Not Classify") {
                    String object = detail;
                    String codeObject;
                    if (detail.contains(" ")) {
                        codeObject = detail.substring(1, detail.indexOf(" ")).toLowerCase().concat("_").concat(detail.substring(detail.indexOf(" ") + 1, detail.indexOf("]")).toLowerCase());
                    } else {
                        codeObject = detail.substring(1, detail.indexOf("]")).toLowerCase();
                    }
                    mStorageRef.child(codeObject + "/" + codeObject + ".mp4").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Intent intent = new Intent(ChooseImageActivity.this, PlayVideoInArScene.class);
                            intent.putExtra("object", object);
                            intent.putExtra("pathVideo", uri.toString());
                            startActivity(intent);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ChooseImageActivity.this, "Fail to load video.", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
    }

    private void showDetail() {
        dialog.setContentView(R.layout.dialog_description);
        dialog.show();
        final TextView textDes;
        textDes = (TextView) dialog.findViewById(R.id.textDes);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(
                    new InputStreamReader(getAssets().open("imagenet_comp_graph_label_strings1.txt"), "UTF-8"));
            // do reading, usually loop until end of file reading
            String mLine;
            while ((mLine = reader.readLine()) != null) {
                if (mLine.contains(detail)) {
                    textDes.setText(mLine);
                    break;
                } else {
                    textDes.setText(" Don't have Description!");
                }
            }
        } catch (IOException e) {
            //log the exception
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    //log the exception
                }
            }
        }

        TextView txtClose;
        txtClose = (TextView) dialog.findViewById(R.id.txtClose);
        CircleImageView imageView1 = (CircleImageView) dialog.findViewById(R.id.imageView);
        imageView1.setImageURI(imageUri);
        txtClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tts.isSpeaking())
                    tts.stop();
                dialog.dismiss();
            }
        });

        ImageView imgBtnSpeaker;
        imgBtnSpeaker = (ImageView) dialog.findViewById(R.id.imgBtnSpeaker);

        imgBtnSpeaker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = textDes.getText().toString();
                tts.setSpeechRate((float) 0.7);
                tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
            }
        });
    }

    @Override
    public void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {

            int result = tts.setLanguage(Locale.ENGLISH);

            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This Language is not supported");
            } else {
                imageSpeaker.setEnabled(true);
            }

        } else {
            Log.e("TTS", "Initilization Failed!");
        }
    }

    private void detectImage() {
        try {
            bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        bitmap = Bitmap.createScaledBitmap(bitmap, 224, 224, false);
        final List<Classifier.Recognition> results = classifier.recognizeImage(bitmap);
        nameResult.setText(results.toString());
        detail = results.toString();
        idGrid.setVisibility(idGrid.VISIBLE);
    }

    private void openGallary() {
        Intent gallary = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(gallary, PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            imageUri = data.getData();
            imageView.setImageURI(imageUri);
        } else if (resultCode == RESULT_OK && requestCode == 123) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(bitmap);
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            String path = MediaStore.Images.Media.insertImage(this.getContentResolver(), bitmap, "Title", null);
            imageUri = Uri.parse(path);
        }
    }

    private void initTensorFlowAndLoadModel() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    classifier = TensorFlowImageClassifier.create(
                            getAssets(),
                            MODEL_FILE,
                            LABEL_FILE,
                            INPUT_SIZE,
                            IMAGE_MEAN,
                            IMAGE_STD,
                            INPUT_NAME,
                            OUTPUT_NAME);
                } catch (final Exception e) {
                    throw new RuntimeException("Error initializing TensorFlow!", e);
                }
            }
        });
    }

    private void speakOut() {
        String text = nameResult.getText().toString();
        tts.setSpeechRate((float) 0.7);
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }
}