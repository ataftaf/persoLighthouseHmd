/*
 * Copyright 2019 The TensorFlow Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.tensorflow.lite.examples.transfer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.media.ImageReader.OnImageAvailableListener;
import android.os.SystemClock;
import android.os.Vibrator;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.util.Size;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.widget.Toast;

import org.tensorflow.lite.examples.transfer.customview.OverlayView;
import org.tensorflow.lite.examples.transfer.env.BorderedText;
import org.tensorflow.lite.examples.transfer.env.Logger;
import org.tensorflow.lite.examples.transfer.env.ImageUtils;
import org.tensorflow.lite.examples.transfer.tflite.Classifier;
import org.tensorflow.lite.examples.transfer.tflite.TFLiteObjectDetectionAPIModel;
import org.tensorflow.lite.examples.transfer.tracking.MultiBoxTracker;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import static android.widget.Toast.makeText;

/**
 * An activity that uses a TensorFlowMultiBoxDetector and ObjectTracker to detect and then track
 * objects.
 */
public class DetectorActivity extends CameraActivity implements OnImageAvailableListener {
  private static final Logger LOGGER = new Logger();

  // Configuration values for the prepackaged SSD model.
  private static final int TF_OD_API_INPUT_SIZE = 300;
  private static final boolean TF_OD_API_IS_QUANTIZED = false;
  private static final String TF_OD_API_MODEL_FILE = "detect.tflite";
  public static final String TF_OD_API_LABELS_FILE = "file:///android_asset/model/labelmap.txt";
  private static final DetectorMode MODE = DetectorMode.TF_OD_API;
  // Minimum detection confidence to track a detection.
  private static final float MINIMUM_CONFIDENCE_TF_OD_API = 0.5f;
  private static final boolean MAINTAIN_ASPECT = false;
  private static final Size DESIRED_PREVIEW_SIZE = new Size(640, 480);
  private static final boolean SAVE_PREVIEW_BITMAP = false;
  private static final float TEXT_SIZE_DIP = 10;
  OverlayView trackingOverlay;
  public static Integer sensorOrientation;

  private Classifier detector;


  private long lastProcessingTimeMs;
  private Bitmap rgbFrameBitmap = null;
  private Bitmap croppedBitmap = null;
  private Bitmap cropCopyBitmap = null;

  private boolean computingDetection = false;

  static private long timestamp = 0;

  private Matrix frameToCropTransform;
  private Matrix cropToFrameTransform;

  private MultiBoxTracker tracker;

  private BorderedText borderedText;
  //static public String OBJECT_TO_BE_DETECTED; //Ala
  static public TextToSpeech TEXT_TO_SPEECH;
  static public String MODE_TO_BE_ACTIVATED;
  static public Vibrator Vib;

   /*
  This code comes from here, to be improved
  https://stackoverflow.com/questions/2874743/android-volume-buttons-used-in-my-application
   */

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        int action = event.getAction();
        int keyCode = event.getKeyCode();
        int zoomPourcent;
        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_UP:
                if (action == KeyEvent.ACTION_DOWN) {
                    if(LegacyCameraConnectionFragment.currentZoomLevel != 99)
                        LegacyCameraConnectionFragment.currentZoomLevel=LegacyCameraConnectionFragment.currentZoomLevel+33;
                        speak("zoomer a "+LegacyCameraConnectionFragment.currentZoomLevel+ "pour cent", "KEY UP");

                    this.setFragment();
                    recreate(); // restart activity

                }
                return true;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                if (action == KeyEvent.ACTION_DOWN) {
                    if(LegacyCameraConnectionFragment.currentZoomLevel >= 33)
                        LegacyCameraConnectionFragment.currentZoomLevel=LegacyCameraConnectionFragment.currentZoomLevel - 33;
                    else
                        LegacyCameraConnectionFragment.currentZoomLevel=0;

                    speak("dézoomer a "+LegacyCameraConnectionFragment.currentZoomLevel+ "pour cent", "KEY DOWN");
                    this.setFragment();
                    recreate(); // restart activity
                }
                return true;
            default:
                return super.dispatchKeyEvent(event);
        }
    }

  @Override
  public void onPreviewSizeChosen(final Size size, final int rotation) {
    final float textSizePx =
        TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, TEXT_SIZE_DIP, getResources().getDisplayMetrics());

    Intent intent = getIntent();
    //OBJECT_TO_BE_DETECTED= intent.getStringExtra("objectToBeDetected"); //Ala
    MODE_TO_BE_ACTIVATED= intent.getStringExtra("mode"); //Ala

    TEXT_TO_SPEECH = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
      @Override
      public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
          int ttsLang = TEXT_TO_SPEECH.setLanguage(Locale.FRANCE);
            if (ttsLang == TextToSpeech.LANG_MISSING_DATA
                    || ttsLang == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "The Language is not supported!");
            } else {
                Log.i("TTS", "Language Supported.");
            }
            Log.i("TTS", "Initialization success.");

        } else {
            makeText(getApplicationContext(), "TTS Initialization failed!", Toast.LENGTH_SHORT).show();
        }
      }
    });

    Vib  = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE); // Ala mode vibration

    borderedText = new BorderedText(textSizePx);
    borderedText.setTypeface(Typeface.MONOSPACE);
    tracker = new MultiBoxTracker(this);
    int cropSize = TF_OD_API_INPUT_SIZE;

    try {
      detector =
          TFLiteObjectDetectionAPIModel.create(
              getAssets(),
              TF_OD_API_MODEL_FILE,
              TF_OD_API_LABELS_FILE,
              TF_OD_API_INPUT_SIZE,
              TF_OD_API_IS_QUANTIZED);
      cropSize = TF_OD_API_INPUT_SIZE;
    } catch (final IOException e) {
      e.printStackTrace();
      LOGGER.e(e, "Exception initializing classifier!");
      Toast toast =
          Toast.makeText(
              getApplicationContext(), "Classifier could not be initialized", Toast.LENGTH_SHORT);
      toast.show();
      finish();
    }

    previewWidth = size.getWidth();
    previewHeight = size.getHeight();//size 640*480 , rotaion: 90

    sensorOrientation = rotation - getScreenOrientation();
    LOGGER.i("Camera orientation relative to screen canvas: %d", sensorOrientation);

    LOGGER.i("Initializing at size %dx%d", previewWidth, previewHeight);
    rgbFrameBitmap = Bitmap.createBitmap(previewWidth, previewHeight, Config.ARGB_8888);
    croppedBitmap = Bitmap.createBitmap(cropSize, cropSize, Config.ARGB_8888);

    frameToCropTransform =
        ImageUtils.getTransformationMatrix(
            previewWidth, previewHeight,
            cropSize, cropSize,
            sensorOrientation, MAINTAIN_ASPECT);

    cropToFrameTransform = new Matrix();
    frameToCropTransform.invert(cropToFrameTransform);

    trackingOverlay = (OverlayView) findViewById(R.id.tracking_overlay);
    trackingOverlay.addCallback(
        new OverlayView.DrawCallback() {
          @Override
          public void drawCallback(final Canvas canvas) {
            tracker.draw(canvas); //Ala here we call to draw rectangles
            if (isDebug()) {
              tracker.drawDebug(canvas);
            }
          }
        });

    tracker.setFrameConfiguration(previewWidth, previewHeight, sensorOrientation);
  }
 public static long getTimeStamp(){
    return timestamp;
 }
  @Override
  protected void processImage() {
    ++timestamp;
    final long currTimestamp = timestamp;
    trackingOverlay.postInvalidate();

    // No mutex needed as this method is not reentrant.
    if (computingDetection) {
      readyForNextImage();
      return;
    }
    computingDetection = true;
    LOGGER.i("Preparing image " + currTimestamp + " for detection in bg thread.");

    rgbFrameBitmap.setPixels(getRgbBytes(), 0, previewWidth, 0, 0, previewWidth, previewHeight);

    readyForNextImage();

    final Canvas canvas = new Canvas(croppedBitmap);
    canvas.drawBitmap(rgbFrameBitmap, frameToCropTransform, null);
    // For examining the actual TF input.
    if (SAVE_PREVIEW_BITMAP) {
      ImageUtils.saveBitmap(croppedBitmap);
    }

    runInBackground(
        new Runnable() {
          @Override
          public void run() {
            LOGGER.i("Running detection on image " + currTimestamp);
            final long startTime = SystemClock.uptimeMillis();
            final List<Classifier.Recognition> results = detector.recognizeImage(croppedBitmap);
            lastProcessingTimeMs = SystemClock.uptimeMillis() - startTime;

            cropCopyBitmap = Bitmap.createBitmap(croppedBitmap);
            final Canvas canvas = new Canvas(cropCopyBitmap);
            final Paint paint = new Paint();
            paint.setColor(Color.RED);
            paint.setStyle(Style.STROKE);
            paint.setStrokeWidth(2.0f);

            float minimumConfidence = MINIMUM_CONFIDENCE_TF_OD_API;
            switch (MODE) {
              case TF_OD_API:
                minimumConfidence = MINIMUM_CONFIDENCE_TF_OD_API;
                break;
            }

            final List<Classifier.Recognition> mappedRecognitions =
                new LinkedList<Classifier.Recognition>();

            for (final Classifier.Recognition result : results) {
              final RectF location = result.getLocation();
              if (location != null && result.getConfidence() >= minimumConfidence) {
                canvas.drawRect(location, paint);

                cropToFrameTransform.mapRect(location);

                result.setLocation(location);
                mappedRecognitions.add(result);
              }
            }

            tracker.trackResults(mappedRecognitions, currTimestamp);
            trackingOverlay.postInvalidate();

            computingDetection = false;

            runOnUiThread(
                new Runnable() {
                  @Override
                  public void run() {
                    showFrameInfo(previewWidth + "x" + previewHeight);
                    showCropInfo(cropCopyBitmap.getWidth() + "x" + cropCopyBitmap.getHeight());
                    showInference(lastProcessingTimeMs + "ms");
                  }
                });
          }
        });
  }

  @Override
  protected int getLayoutId() {
    return R.layout.camera_connection_fragment_tracking;
  }

  @Override
  protected Size getDesiredPreviewFrameSize() {
    return DESIRED_PREVIEW_SIZE;
  }


    // Which detection model to use: by default uses Tensorflow Object Detection API frozen
  // checkpoints.
  private enum DetectorMode {
    TF_OD_API;
  }

  public void onPause(){
    super.onPause();

  }



  @Override
  protected void setUseNNAPI(final boolean isChecked) {
    runInBackground(() -> detector.setUseNNAPI(isChecked));
  }

  @Override
  protected void setNumThreads(final int numThreads) {
    runInBackground(() -> detector.setNumThreads(numThreads));
  }

}
