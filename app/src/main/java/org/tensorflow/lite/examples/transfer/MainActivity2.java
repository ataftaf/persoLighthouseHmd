package org.tensorflow.lite.examples.transfer;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

import static android.widget.Toast.makeText;

public class MainActivity2 extends AppCompatActivity {

    Button inferenceButton;
    Button trainButton;
    static public TextToSpeech TEXT_TO_SPEECH;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
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
        inferenceButton = findViewById(R.id.inference);
        trainButton=findViewById(R.id.train);

        inferenceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CameraFragment.trainStatus=false;
                Intent intent= new Intent(MainActivity2.this, TrainActivity.class);
                startActivity(intent);
            }
        });

        trainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // speak("Veuillez s'il vous plait placer l'objet que vous voulez ajouter devant la caméra et pressez le bouton volume plus.");
                CameraFragment.trainStatus=true;
                Intent intent= new Intent(MainActivity2.this, TrainActivity.class);
                startActivity(intent);
            }
        });
    }
    public static void speak(String text){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            TEXT_TO_SPEECH.speak(text, TextToSpeech.QUEUE_ADD, null, null);

        }else{
            TEXT_TO_SPEECH.speak(text, TextToSpeech.QUEUE_ADD, null);
        }
    }

}
