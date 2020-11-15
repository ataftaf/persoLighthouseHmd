package org.tensorflow.lite.examples.transfer;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.AssetManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

// implement interfaces for the service text to speech

public class MainActivity extends Activity implements TextToSpeech.OnInitListener, TextToSpeech.OnUtteranceCompletedListener {


    String bonjour="Bonjour, Que cherchez vous ?";
    String reBonjour= "Que cherchez vous cette fois?";
    String messageIntent=null;
    static public String objectName;
    private String mode;
    private boolean foundObject= false;
    private SensorManager mSensorManager;
    private Sensor mLightSensor;
    private int mLightQuantity;
    private Sensor mLight;


    private TextToSpeech textToSpeech;
    private static final int REQUEST_OBJECT = 100;
    private Boolean lightCheckOn= false;
    private Boolean lightCheckOff= false;
    private SensorEventListener listener;
    private ImageButton addObject;

    // Service text to speech: no wait until the speech is finished

    private void speakWithoutWait(String text) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            textToSpeech.speak(text, TextToSpeech.QUEUE_ADD, null, null);

        } else {
            textToSpeech.speak(text, TextToSpeech.QUEUE_ADD, null);
        }
    }

    // Service text to speech: wait until the speech is finished

    private void speak(String text, String specification){
        if(text != null) {
            HashMap<String, String> myHashAlarm = new HashMap<String, String>();
            myHashAlarm.put(TextToSpeech.Engine.KEY_PARAM_STREAM, String.valueOf(AudioManager.STREAM_ALARM));
            myHashAlarm.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, specification);
                textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, myHashAlarm);
        }
    }

    // When the activity is created, we call for the service text to speech

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        mLightSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        textToSpeech = new TextToSpeech(this, this);
        addObject= findViewById(R.id.addObject);
        addObject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent intent =new Intent(MainActivity.this, MainActivity2.class);
               startActivity(intent);
            }
        });
    }





   // Service text to speech to ask the user the object he is looking for, wait until the speech is finished

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS)
        {
            textToSpeech.setOnUtteranceCompletedListener(this);
            int result = textToSpeech.setLanguage(Locale.FRANCE);

            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This Language is not supported");
            } else {
                Intent intent = getIntent();
                messageIntent= intent.getStringExtra("return"); 
                if(messageIntent ==null){


                    // Obtain references to the SensorManager and the Light Sensor // to be improved
                    // Implement a listener to receive updates
                     listener = new SensorEventListener() {
                        @Override
                        public void onSensorChanged(SensorEvent sensorEvent) {
                            mLightQuantity = (int) sensorEvent.values[0];
                            if (mLightQuantity>3 && !lightCheckOn) {
                                speak(bonjour, "FINISHED QUESTION OBJECT");
                                lightCheckOn=true;

                            }else if (mLightQuantity<=3 && !lightCheckOff) {
                                speak("Veuillez allumer la lumière s'il vous plait degree "+ mLightQuantity, "LIGHT OPEN"); // in the case where it is not switch on
                                lightCheckOff=true;

                            }
                        }

                        @Override
                        public void onAccuracyChanged(Sensor sensor, int i) {

                        }
                    };

                    // Register the listener with the light sensor -- choosing
                    // one of the SensorManager.SENSOR_DELAY_* constants.
                    mSensorManager.registerListener(
                            listener, mLightSensor, SensorManager.SENSOR_DELAY_UI);



                    //speak(bonjour, "FINISHED QUESTION OBJECT");
                }
                else if (messageIntent.contains("anotherResearch")){
                    speak(reBonjour, "FINISHED QUESTION OBJECT");
                }else {  // if (messageIntent.contains("kill"))
                    this.finish();
                    Intent intent1 = new Intent(Intent.ACTION_MAIN);
                    intent1.addCategory(Intent.CATEGORY_HOME);
                    intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent1);
                }
            }
        } else {
            Log.e("TTS", "Initilization Failed!");
        }
    }

    // Service text to speech, when lighthouse finish speaking

    @Override
    public void onUtteranceCompleted(String utteranceId) {
        if(utteranceId.equals("FINISHED QUESTION OBJECT"))   // when the question about the object has finished
        {
            listen(REQUEST_OBJECT);
            Log.i("TTS", "Called");   //never called
        }
    }



    /** Service Speech to text **/

   /* Service speech to text, listen to the user  // To handle error, I have to custimize the service by implementing the class RecognitionListener,
     the problem with customizing is the fact that there os no startActivityForResult anymore, please see this code: https://github.com/sdsb8432/SpeechToText-Android
     I downloaded it into the folder: BessmellahDistance, Speech-tp-text
    */

    private void listen(int request){
        Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

        try {
            startActivityForResult(i, request);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(MainActivity.this, "Your device doesn't support Speech Recognition", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     *  Service speech to text, when the user finish his request
     * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {

            case REQUEST_OBJECT: {
                if (resultCode == RESULT_OK && null != data ) {
                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    String text = result.get(0);

                    if(text.contains("ajouter")){
                        speakWithoutWait("Veuillez s'il vous plait cliquer au milieu de l'ecran");
                    }else {

                        if (text.contains("je cherche")) {
                            objectName = text.split("cherche")[1];
                        } else {
                            objectName = text;
                        }


                        // check if the object exists in the labelmap file

                        final AssetManager assetManager = getAssets();
                        InputStream labelsInput = null;
                        String actualFilename = DetectorActivity.TF_OD_API_LABELS_FILE.split("file:///android_asset/")[1];
                        try {
                            labelsInput = assetManager.open(actualFilename);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        BufferedReader br = new BufferedReader(new InputStreamReader(labelsInput));
                        String line;
                        try {
                            while (((line = br.readLine()) != null) && (!foundObject)) {
                                if (objectName.contains(line)) {
                                    foundObject = true;
                                    objectName = line;
                                    //return;
                                }
                            }
                            br.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        if (foundObject) {
                            speakWithoutWait("vous cherchez bien " + objectName);
                            Intent myIntent = new Intent(MainActivity.this, DetectorActivity.class); // when the user precises the mode, start searching for the object
                            // myIntent.putExtra("objectToBeDetected", objectName); //Optional parameters
                            myIntent.putExtra("mode", "contexte"); //Optional parameters
                            lightCheckOff = true;
                            lightCheckOn = true;
                            startActivity(myIntent);

                        } else {   // when the user choses an object that does not exist in the labelmap file

                            speak(objectName + " n'existe pas dans ma base de données, veuillez choisir un autre objet ou bien ajouter " + objectName + " à ma base", "FINISHED QUESTION OBJECT");
                        }
                    }

                } else{
                    speak("Veuillez repeter l'objet s'il vous plait","FINISHED QUESTION OBJECT" );
                }

                break;
            }


        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
    }

  /*  @Override
    protected void onResume() {
        super.onResume();
        if (mLight != null) {
            mSensorManager.registerListener(listener, mLight,
                    SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mLight != null) {
            mSensorManager.unregisterListener(listener);
        }
    } */

}
