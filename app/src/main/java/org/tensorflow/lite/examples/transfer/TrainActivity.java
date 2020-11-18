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

package org.tensorflow.lite.examples.transfer;

import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import java.util.HashMap;
import java.util.Locale;

/**
 * Main activity of the classifier demo org.tensorflow.lite.app.
 */
public class TrainActivity extends FragmentActivity implements  TextToSpeech.OnInitListener, TextToSpeech.OnUtteranceCompletedListener {
  public static TransferLearningModelWrapper tlModel;
  public static CameraFragmentViewModel viewModel;
  static public Vibrator Vib;
  CameraFragment cameraFragment;
  private TextToSpeech textToSpeech;


  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_train);
    textToSpeech = new TextToSpeech(this, this);


    ///////Added by Ala
    if (tlModel == null) {
      tlModel = new TransferLearningModelWrapper(TrainActivity.this);
    }
    Vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE); // Ala mode vibration


    // If we're being restored from a previous state,
    // then we don't need to do anything and should return or else
    // we could end up with overlapping fragments.
    if (savedInstanceState != null) {
      return;
    }

    PermissionsFragment firstFragment = new PermissionsFragment();

    getSupportFragmentManager()
            .beginTransaction()
            .add(R.id.fragment_container, firstFragment)
            .commit();
  }

  @Override
  public void onAttachFragment(Fragment fragment) {

    if (fragment instanceof PermissionsFragment) {
      ((PermissionsFragment) fragment).setOnPermissionsAcquiredListener(() -> {
        if (cameraFragment == null) {
          cameraFragment = new CameraFragment();
        }
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, cameraFragment)
                .commit();
      });

    }
  }

  @Override
  public boolean dispatchKeyEvent(KeyEvent event) {
    int action = event.getAction();
    int keyCode = event.getKeyCode();
    switch (keyCode) {
      case KeyEvent.KEYCODE_VOLUME_UP:
        if (action == KeyEvent.ACTION_DOWN) {
          cameraFragment.addExamples("1");
          MainActivity.speakWithoutWait("Merci. Veuillez maintenant orienter la caméra vers un autre objet et cliquer sur le bouton volume moins");
        }
        return true;

      case KeyEvent.KEYCODE_VOLUME_DOWN:
        if (action == KeyEvent.ACTION_DOWN) {
          cameraFragment.addExamples("2");
          speak("Veuillez patienter quelques secondes pendant que l'application se met à jour ", "UPDATE");

        }

        return true;
      default:
        return super.dispatchKeyEvent(event);
    }

  }

  private void speak(String text, String specification) {
    if (text != null) {
      HashMap<String, String> myHashAlarm = new HashMap<String, String>();
      myHashAlarm.put(TextToSpeech.Engine.KEY_PARAM_STREAM, String.valueOf(AudioManager.STREAM_ALARM));
      myHashAlarm.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, specification);
      textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, myHashAlarm);
    }
  }


  @Override
  public void onUtteranceCompleted(String utteranceId) {
    if(utteranceId.equals("UPDATE"))   // when the question about the object has finished
    {
        if (viewModel.getTrainingState().getValue() == CameraFragmentViewModel.TrainingState.NOT_STARTED) {
        viewModel.setTrainingState(CameraFragmentViewModel.TrainingState.STARTED);
        findViewById(R.id.trainButton).setVisibility(View.INVISIBLE);
        findViewById(R.id.pauseButton).setVisibility(View.VISIBLE);
      }
    }
  }

  @Override
  public void onInit(int status) {
    //speak("Veuillez s'il vous plait placer l'objet que vous voulez ajouter devant la caméra et pressez le bouton volume plus.", "ADD_OBJECT");
    if (status == TextToSpeech.SUCCESS) {
      textToSpeech.setOnUtteranceCompletedListener(this);
      int result = textToSpeech.setLanguage(Locale.FRANCE);

      if (result == TextToSpeech.LANG_MISSING_DATA
              || result == TextToSpeech.LANG_NOT_SUPPORTED) {
        Log.e("TTS", "This Language is not supported");
      }
    } else {
      Log.e("TTS", "Initilization Failed!");
    }
  }
}
