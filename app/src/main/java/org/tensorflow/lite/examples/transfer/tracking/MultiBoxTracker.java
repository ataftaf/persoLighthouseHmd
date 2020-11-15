/* Copyright 2019 The TensorFlow Authors. All Rights Reserved.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
==============================================================================*/

package org.tensorflow.lite.examples.transfer.tracking;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.VibrationEffect;
import android.speech.tts.TextToSpeech;
import android.util.Pair;
import android.util.TypedValue;

import org.tensorflow.lite.examples.transfer.DetectorActivity;
import org.tensorflow.lite.examples.transfer.LegacyCameraConnectionFragment;
import org.tensorflow.lite.examples.transfer.MainActivity;
import org.tensorflow.lite.examples.transfer.env.BorderedText;
import org.tensorflow.lite.examples.transfer.env.ImageUtils;
import org.tensorflow.lite.examples.transfer.env.Logger;
import org.tensorflow.lite.examples.transfer.tflite.Classifier;

import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import static java.lang.Math.abs;

/** A tracker that handles non-max suppression and matches existing objects to new detections. */
public class MultiBoxTracker {
  private static final float TEXT_SIZE_DIP = 18;
  private static final float MIN_SIZE = 16.0f;
  private static final int[] COLORS = {
    Color.BLUE,
    Color.RED,
    Color.GREEN,
    Color.YELLOW,
    Color.CYAN,
    Color.MAGENTA,
    Color.WHITE,
    Color.parseColor("#55FF55"),
    Color.parseColor("#FFA500"),
    Color.parseColor("#FF8888"),
    Color.parseColor("#AAAAFF"),
    Color.parseColor("#FFFFAA"),
    Color.parseColor("#55AAAA"),
    Color.parseColor("#AA33AA"),
    Color.parseColor("#0D0068")
  };
  final List<Pair<Float, RectF>> screenRects = new LinkedList<Pair<Float, RectF>>();
  private final Logger logger = new Logger();
  private final Queue<Integer> availableColors = new LinkedList<Integer>();
  private final List<TrackedRecognition> trackedObjects = new LinkedList<TrackedRecognition>();
  private final Paint boxPaint = new Paint();
  private final float textSizePx;
  private final BorderedText borderedText;
  private Matrix frameToCanvasMatrix;
  private int frameWidth;
  private int frameHeight;
  private int sensorOrientation;

  private boolean first=false;
  private boolean second=false;

  private boolean firstS=false;
  private boolean secondS=false;
  private boolean speechH=false;

  private String speech=null;
  private String speechO=null;
  private String speechS=null;
  private String speechSO=null;
  private boolean modeVibrate= true;
  private boolean modeSphere=true;
  private ToneGenerator toneG;
  private float widthObj=0;
  private float widthObjO=0;
  private int freq=100;
  private long timeN;
  private long timeO;

  static public double focalW;
  static public double focalL;
  static public double bottleRealWidth= 11;
  static public double bottleRealHeight= 32;
  static public double cupRealWidth= 11;
  static public double cupRealHeight= 9;
  static public double mouseRealWidth= 7;
  static public double mouseRealHeight= 13;
  static public double keyboardRealWidth= 45;
  static public double keyboardRealHeight= 14;
  static public double remoteRealWidth= 5.5;
  static public double remoteRealHeight= 17;
  static public double bolRealWidth= 14;
  static public double bolRealHeight= 8;
  static public double spoonRealWidth= 4;
  static public double spoonRealHeight= 20;
  static public double watchRealWidth= 5;
  static public double laptopRealWidth= 42;
  static public double objectRealWidth=63;
  static public double objectRealHeight=163;
  static public double calibrateDistance=20;
  static public double boundingBoxWidth;
  static public double boundingBoxHeight;
  static public double mouseBoundingBoxWidth;
  static public double mouseboundingBoxHeight;


  private  TrackedRecognition neighbour;
  RectF neighbourTrackedPos;
  DecimalFormat ThreeDForm = new DecimalFormat("#.###");


  public MultiBoxTracker(final Context context) {
    for (final int color : COLORS) {
      availableColors.add(color);
    }

    boxPaint.setColor(Color.RED);
    boxPaint.setStyle(Style.STROKE);
    boxPaint.setStrokeWidth(10.0f);
    boxPaint.setStrokeCap(Cap.ROUND);
    boxPaint.setStrokeJoin(Join.ROUND);
    boxPaint.setStrokeMiter(100);

    textSizePx =
        TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, TEXT_SIZE_DIP, context.getResources().getDisplayMetrics());
    borderedText = new BorderedText(textSizePx);
  }

  private void speak(String text){
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
     DetectorActivity.TEXT_TO_SPEECH.speak(text, TextToSpeech.QUEUE_ADD, null, null);

    }else{
      DetectorActivity.TEXT_TO_SPEECH.speak(text, TextToSpeech.QUEUE_ADD, null);
    }
  }


  public synchronized void setFrameConfiguration(
      final int width, final int height, final int sensorOrientation) {
    frameWidth = width;
    frameHeight = height;
    this.sensorOrientation = sensorOrientation;
  }

  public synchronized void drawDebug(final Canvas canvas) {
    final Paint textPaint = new Paint();
    textPaint.setColor(Color.WHITE);
    textPaint.setTextSize(60.0f);

    final Paint boxPaint = new Paint();
    boxPaint.setColor(Color.RED);
    boxPaint.setAlpha(200);
    boxPaint.setStyle(Style.STROKE);

    for (final Pair<Float, RectF> detection : screenRects) {
      final RectF rect = detection.second;
      canvas.drawRect(rect, boxPaint);
      canvas.drawText("" + detection.first, rect.left, rect.top, textPaint);
      borderedText.drawText(canvas, rect.centerX(), rect.centerY(), "" + detection.first);
    }
  }

  public synchronized void trackResults(final List<Classifier.Recognition> results, final long timestamp) {
    logger.i("Processing %d results from %d", results.size(), timestamp);
    processResults(results);
  }

  private Matrix getFrameToCanvasMatrix() {
    return frameToCanvasMatrix;
  }

  public synchronized void draw(final Canvas canvas) {
    final boolean rotated = sensorOrientation % 180 == 90;
    final float multiplier =
        Math.min(
            canvas.getHeight() / (float) (rotated ? frameWidth : frameHeight),
            canvas.getWidth() / (float) (rotated ? frameHeight : frameWidth));
    frameToCanvasMatrix =
        ImageUtils.getTransformationMatrix(
            frameWidth,
            frameHeight,
            (int) (multiplier * (rotated ? frameHeight : frameWidth)),
            (int) (multiplier * (rotated ? frameWidth : frameHeight)),
            sensorOrientation,
            false);

    for (final TrackedRecognition recognition : trackedObjects) {

      final RectF trackedPos = new RectF(recognition.location);
        getFrameToCanvasMatrix().mapRect(trackedPos);
        boxPaint.setColor(recognition.color);

        float cornerSize = Math.min(trackedPos.width(), trackedPos.height()) / 8.0f;
        canvas.drawRoundRect(trackedPos, cornerSize, cornerSize, boxPaint); //Here we draw the final rectangles


        /*final String labelString =
              !TextUtils.isEmpty(recognition.title)
                      ? String.format("%s %.2f", recognition.title, (100 * recognition.detectionConfidence))
                      : String.format("%.2f", (100 * recognition.detectionConfidence));*/

      final String labelString = recognition.title + " "+ (int) 100 * recognition.detectionConfidence+"%" ;
        borderedText.drawText(                    //Here we draw the final texts, labelString contains the object and confidence
                canvas, trackedPos.left + cornerSize, trackedPos.top, labelString , boxPaint);

      if (!MainActivity.objectName.contains(recognition.title)) { // the last neigbour of the object we are looking for
          neighbourTrackedPos = trackedPos;
          neighbour=recognition;
          timeN= DetectorActivity.getTimeStamp();
       }
      else // the object we are looking for
      {
        if (DetectorActivity.MODE_TO_BE_ACTIVATED.contains(("horloge"))){

          playModeHorloge( canvas,  trackedPos, recognition);

         } else {
           /**
            * Create a new rectangle with the specified coordinates. Note: no range
            * checking is performed, so the caller must ensure that left <= right and
            * top <= bottom.
            *
            * @param left   The X coordinate of the left side of the rectangle
            * @param top    The Y coordinate of the top of the rectangle
            * @param right  The X coordinate of the right side of the rectangle
            * @param bottom The Y coordinate of the bottom of the rectangle
            *
            *  https://stackoverflow.com/questions/306316/determine-if-two-rectangles-overlap-each-other
            *               to detect of a rectangle is right another
            */
           timeO = DetectorActivity.getTimeStamp();

           if ((neighbour != null) && (neighbourTrackedPos != null) && (abs(timeO - timeN)) < 3) {
             if (trackedPos.top > neighbourTrackedPos.bottom) {  // here the rectangle do not overlap
               speech = recognition.title + " est devant" + neighbour.title;
             } else if (trackedPos.bottom < neighbourTrackedPos.top) {

               if(!neighbour.title.contains("table basse")&& (!neighbour.title.contains("table à manger")) && (!neighbour.title.contains("buffet"))&& (!neighbour.title.contains("canapé"))) {
                 speech = recognition.title + " est derrière" + neighbour.title;
               }else {
                 speech = recognition.title + " est sur" + neighbour.title;
               }
             } else if (trackedPos.left > neighbourTrackedPos.right) {
               speech = recognition.title + " est à droite de" + neighbour.title;
             } else if (trackedPos.right < neighbourTrackedPos.left) {
               speech = recognition.title + " est à gauche de" + neighbour.title;

             } else if (neighbourTrackedPos.contains(trackedPos)) {
              // if (!neighbour.title.contains("ordinateur")) {
                 speech = recognition.title + " est sur" + neighbour.title;
              // }

             } else { // here the rectangles overlaps
               int difference = (int) abs((trackedPos.top - neighbourTrackedPos.bottom));
               int differenceT = (int) abs((neighbourTrackedPos.top - trackedPos.bottom));
               int heightN = (int) neighbourTrackedPos.height();
               int heightO = (int) trackedPos.height();

                //when the vertical overlap is not too big
               if ((trackedPos.bottom > neighbourTrackedPos.bottom) && (difference < heightO / 3)) {
                 speech = recognition.title + " legèrement  devant" + neighbour.title;

               } else if ((neighbourTrackedPos.top > trackedPos.top) && (differenceT < heightN / 3)) {

                   if(!neighbour.title.contains("table basse")&& (!neighbour.title.contains("table à manger")) && (!neighbour.title.contains("buffet"))&& (!neighbour.title.contains("canapé"))) {
                     speech = recognition.title + " legèrement  derrière" + neighbour.title;

                   }else{
                      speech = recognition.title + " est sur" + neighbour.title;
                   }



                 //when the vertical overlap is big
               } else if (trackedPos.right > neighbourTrackedPos.right) {
                 speech = recognition.title + " est legèrement à droite de " + neighbour.title;
               } else if (trackedPos.left < neighbourTrackedPos.left) {
                 speech = recognition.title + " est legèrement à gauche de " + neighbour.title;
                 // when the object is partially inside the neighbour
               } else if ((trackedPos.left > neighbourTrackedPos.left) && (trackedPos.right < neighbourTrackedPos.right)){
                 speech = recognition.title + " est legèrement devant " + neighbour.title;
               } else { // other options, when the objet is totally inside his neighbour // it is missing the case "legerement derriere"
                 speech = recognition.title + " est sur" + neighbour.title;
               }
             }


           } else {
             if(!speechH) {
               speech = playModeHorloge(canvas, trackedPos, recognition);
               speechH = true;
             }

             //speech = recognition.title + "trouvé, devant vous";

           }

           if ((!first) && (speech != null)) {
             speak(speech);
             first = true;
             speechO = speech;
           }
           if ((first && !second) && (speechO != null) && (!speech.equals(speechO))) {

             speak("précisément" + speech);
             second = true;
           }
             vibratePhone(400);
             playTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 500);

         }

       }

    }
  }

  //https://stackoverflow.com/questions/44385723/drawing-on-an-android-canvas-angle-is-not-correct
  //To add the zooming options, this post is interesting: https://stackoverflow.com/questions/9278647/zoom-camera-in-surfaceview-preview

  private String playModeHorloge(Canvas canvas, RectF trackedPos, TrackedRecognition recognition){
    int cx= LegacyCameraConnectionFragment.widthCircle /2; //improve this with CameraConnectionFragment when version supports api2
    int cy=LegacyCameraConnectionFragment.heightCircle/2;

    int radius = Math.min(cx, cy);
    final Paint textPaintC = new Paint();
    textPaintC.setColor(Color.WHITE);
    textPaintC.setTextSize(200.0f);

    textPaintC.setStyle(Style.STROKE);
    textPaintC.setStrokeWidth(3f);
    canvas.drawCircle(cx, cy, radius, textPaintC);
    int noOfJoints=12;
    float pointAngle = 360 / noOfJoints;

    double minimum=10000;
    float difference;
    int count=0;
    int countP=0;
    for (float angle = 0; angle < 361; angle = angle + pointAngle)
    {   //move round the circle to each point
      float displacedAngle = angle - 90;
      Math.toRadians(displacedAngle);
      float x = cx + ((float) Math.cos(Math.toRadians(displacedAngle)) * radius); //convert angle to radians for x and y coordinates
      float y = cy + ((float) Math.sin(Math.toRadians(displacedAngle)) * radius);
      float xD= x-trackedPos.centerX();
      float yD=y-trackedPos.centerY();
      difference= (float) Math.sqrt((Math.pow(xD,2))+ (Math.pow(yD,2)));
      if (difference<minimum){
        minimum=difference;
        countP=count;
      }
      count++;
      canvas.drawLine(cx, cy, x, y, textPaintC); //draw a line from center point back to the point
    }
    if (countP==0){
      countP=12;
    }

    speechS=recognition.title + " est a " + countP+ "  heures devant vous";

    //speechS=recognition.title + " est a " + countP;
    /*
    if(countP==12){
      speechS= speechS + " devant vous ";
    }else if(countP <=3){
      speechS= speechS + " heures a droite ";
    } else if((countP >3) && (countP < 6)){
      speechS= speechS+ " heures en bas a droite ";
    }else if(countP==6){
      speechS= speechS + " en bas ";
    }
    else if((countP >6) && (countP < 9)){
      speechS= speechS+ " heures en bas a gauche ";
    } else{
      speechS= speechS+ " heures a gauche ";
    }*/

    /*if ((!firstS) && (speechS != null)) {
      speak(speechS);
      firstS = true;
      speechSO = speechS;
    }*/

    final Paint textPaintR = new Paint();
    textPaintR.setColor(Color.WHITE);
    textPaintR.setTextSize(60.0f);
    canvas.drawCircle(cx, cy, 10, textPaintR);

    final Paint textPaint = new Paint();
    textPaint.setColor(Color.RED);
    textPaint.setTextSize(60.0f);
    canvas.drawCircle(trackedPos.centerX(), trackedPos.centerY(), 10, textPaint);
    return speechS;

  }

  private final void vibratePhone(int freq) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      DetectorActivity.Vib.vibrate(VibrationEffect.createOneShot(freq, VibrationEffect.DEFAULT_AMPLITUDE));
    } else {
      //deprecated in API 26
      DetectorActivity.Vib.vibrate(freq);
    }
  }

  private void playTone(int mediaFileRawId, int freq) {
    try {
      if (toneG == null) {
        // send the tone to the "alarm" stream (classic beeps go there) with 100% volume
        toneG = new ToneGenerator(AudioManager.STREAM_ALARM, 100);
      }
      toneG.startTone(mediaFileRawId, freq);  // 200 is duration in ms
      Handler handler = new Handler(Looper.getMainLooper());
      handler.postDelayed(new Runnable() {
        @Override
        public void run() {
          if (toneG != null) {
            toneG.release();
            toneG = null;
          }
        }

      }, freq);
    } catch (Exception e) {
      //Log.d(TAG, "Exception while playing sound:" + e);
    }
  }

  private void processResults(final List<Classifier.Recognition> results) {
    final List<Pair<Float, Classifier.Recognition>> rectsToTrack = new LinkedList<Pair<Float, Classifier.Recognition>>();

    screenRects.clear();
    final Matrix rgbFrameToScreen = new Matrix(getFrameToCanvasMatrix());

    for (final Classifier.Recognition result : results) {
      if (result.getLocation() == null) {
        continue;
      }
      final RectF detectionFrameRect = new RectF(result.getLocation());

      final RectF detectionScreenRect = new RectF();
      rgbFrameToScreen.mapRect(detectionScreenRect, detectionFrameRect);

      logger.v(
          "Result! Frame: " + result.getLocation() + " mapped to screen:" + detectionScreenRect);

      screenRects.add(new Pair<Float, RectF>(result.getConfidence(), detectionScreenRect));

      if (detectionFrameRect.width() < MIN_SIZE || detectionFrameRect.height() < MIN_SIZE) {
        logger.w("Degenerate rectangle! " + detectionFrameRect);
        continue;
      }

      rectsToTrack.add(new Pair<Float, Classifier.Recognition>(result.getConfidence(), result));
    }

    trackedObjects.clear();
    if (rectsToTrack.isEmpty()) {
      logger.v("Nothing to track, aborting.");
      return;
    }

    for (final Pair<Float, Classifier.Recognition> potential : rectsToTrack) {
      final TrackedRecognition trackedRecognition = new TrackedRecognition();
      trackedRecognition.detectionConfidence = potential.first;
      trackedRecognition.location = new RectF(potential.second.getLocation());
      trackedRecognition.title = potential.second.getTitle();
      trackedRecognition.color = COLORS[trackedObjects.size()];
      trackedObjects.add(trackedRecognition);

      if (trackedObjects.size() >= COLORS.length) {
        break;
      }
    }
  }

  private static class TrackedRecognition {
    RectF location;
    float detectionConfidence;
    int color;
    String title;
  }
}
