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
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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

public class CaptureActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {
    private static final int CAPTURE = 123;
    private static final int INPUT_SIZE = 224;
    private static final int IMAGE_MEAN = 117;
    private static final float IMAGE_STD = 1;
    private static final String INPUT_NAME = "input";
    private static final String OUTPUT_NAME = "output";
    private static final String MODEL_FILE = "file:///android_asset/tensorflow_inception_graph.pb";
    private static final String LABEL_FILE =
            "file:///android_asset/imagenet_comp_graph_label_strings.txt";
    Uri imageUri;
    ImageView imageView;
    Bitmap bitmap;
    TextView nameResult;
    private Classifier classifier;
    String detail;
    GridLayout idGrid;
    private TextToSpeech tts;
    private Executor executor = Executors.newSingleThreadExecutor();
    private LinearLayout imageSpeaker, imageDetail, imageVideo;
    private LinearLayout btnIdentify, btnChoose;
    private Dialog dialog;
    private Button btnCapturePic;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture);

        imageView = findViewById(R.id.imageView);
        nameResult = (TextView) findViewById(R.id.nameResult);
        nameResult.setMovementMethod(new ScrollingMovementMethod());
        idGrid = (GridLayout) findViewById(R.id.idGrid);
        imageSpeaker = findViewById(R.id.imageSpeaker);
        imageDetail = findViewById(R.id.imageDetail);
        imageVideo = findViewById(R.id.imageVideo);
        btnCapturePic = findViewById(R.id.btnCapturePic);
        btnIdentify = findViewById(R.id.btnIdentify);
        btnChoose = findViewById(R.id.btnChoose);

        btnIdentify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent identify = new Intent(CaptureActivity.this, InstantDetectActivity.class);
                startActivity(identify);
            }
        });

        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent choose = new Intent(CaptureActivity.this, ChooseImageActivity.class);
                startActivity(choose);
            }
        });

        capturePic();

        initTensorFlowAndLoadModel();
        tts = new TextToSpeech(this, this);
        btnCapturePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                capturePic();
            }
        });

        imageSpeaker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speakOut();
            }
        });
        dialog = new Dialog(this);
        imageDetail = findViewById(R.id.imageDetail);
        imageDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDetail();
            }
        });
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                tts.stop();
            }
        });
    }

    private void capturePic() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAPTURE);
    }

    @Override
    public void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
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
                dialog.dismiss();
                if (tts.isSpeaking())
                    tts.stop();
            }
        });

        ImageView imgBtnSpeaker;
        imgBtnSpeaker = dialog.findViewById(R.id.imgBtnSpeaker);

        imgBtnSpeaker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = textDes.getText().toString();
                tts.setSpeechRate((float) 0.7);
                tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
            }
        });
    }

    private void speakOut() {
        String text = nameResult.getText().toString();
        tts.setSpeechRate((float) 0.7);
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == CAPTURE) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(bitmap);
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            String path = MediaStore.Images.Media.insertImage(this.getContentResolver(), bitmap, "Title", null);
            imageUri = Uri.parse(path);
            detectImage();
        }
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
}
