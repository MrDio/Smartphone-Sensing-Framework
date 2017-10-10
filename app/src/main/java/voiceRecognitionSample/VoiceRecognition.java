/*
 *  This file is part of the Multimodal Mobility Analyser(MMA), based
 *  on the Smartphone Sensing Framework (SSF)

    MMA (also SSF) is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    MMA (also SSF) is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with MMA.  If not, see <http://www.gnu.org/licenses/>.
 */
package voiceRecognitionSample;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.widget.Toast;
import de.tae.coach.R;
import de.tae.coach.userInterface.activeState.ActiveStateUIActivity;


/**
 * This is a sample file which is NOT yet a part of the SSF. It is just a working and correct sample
 * how voice-recognition works with android.
 * @author Kavivarman Sivarasah and Dionysios Satikidis (dionysios.satikidis@gmail.com)
 * @version 0.0
 */

public class VoiceRecognition {

	private SpeechRecognizer sr;
	protected static MediaPlayer voice;

	private final String TAG = "VoiceRecognition";

	private ActiveStateUIActivity activeStateUIActivity;

	public VoiceRecognition(ActiveStateUIActivity activeStateUIActivity) {
		this.activeStateUIActivity = activeStateUIActivity;
	}

	public boolean start() {
		if (SpeechRecognizer.isRecognitionAvailable(activeStateUIActivity)) {
			sr = SpeechRecognizer.createSpeechRecognizer(activeStateUIActivity);
			sr.setRecognitionListener(new listener());
			startVoiceRecognition();
			
			Toast.makeText(activeStateUIActivity.getApplicationContext(), "Use 'Go', 'Stop' and the exercise names as commands.", Toast.LENGTH_LONG).show();
			return true;
		} else {
			Intent browserIntent = new Intent(
					Intent.ACTION_VIEW,
					Uri.parse("https://market.android.com/details?id=com.google.android.voicesearch"));
			activeStateUIActivity.startActivity(browserIntent);
			return false;
		}
	}

	public void stop() {
		sr.destroy();
	}

	public void startVoiceRecognition() {

		Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
				RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
		intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,
				"voice.recognition.test");

		intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 5);
		Log.i("start", "listening");
		sr.startListening(intent);

	}

	@SuppressLint("DefaultLocale")
	class listener implements RecognitionListener {
		public void onReadyForSpeech(Bundle params) {
			// Log.d(TAG, "onReadyForSpeech");
		}

		public void onBeginningOfSpeech() {
			// Log.d(TAG, "onBeginningOfSpeech");
		}

		public void onRmsChanged(float rmsdB) {
			// Log.d(TAG, "onRmsChanged");
		}

		public void onBufferReceived(byte[] buffer) {
			// Log.d(TAG, "onBufferReceived");
		}

		public void onEndOfSpeech() {
			// Log.d(TAG, "onEndofSpeech");

		}

		public void onError(int error) {
			// Log.d(TAG, "error " + error);
			startVoiceRecognition();

		}

		public void onResults(Bundle results) {

			ArrayList<String> data = results
					.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
			for (String match : data) {
				String[] words = match.split(" ");
				for (String word : words) {
					Log.i(TAG, word);
					if (activeStateUIActivity.isExerciseStartet()) {
						if (word.toLowerCase().equals("stop")
								|| word.toLowerCase().equals("top")) {
							activeStateUIActivity
									.buttonActStateStartStopClicked();
							break;
						}
					} else {
						if (word.toLowerCase().equals("go")
								|| word.toLowerCase().equals("start")) {
							activeStateUIActivity
									.buttonActStateStartStopClicked();
							break;
						} else if (word.toLowerCase().equals("pull")
								|| word.toLowerCase().equals("pullup")
								|| word.toLowerCase().equals("pullups")) {
							activeStateUIActivity.activateExercise("pullups");
							playMedia(R.raw.pullups);
						} else if (word.toLowerCase().equals("push")
								|| word.toLowerCase().equals("pushup")
								|| word.toLowerCase().equals("pushups")
								|| word.toLowerCase().equals("paula")) {
							activeStateUIActivity.activateExercise("pushups");
							playMedia(R.raw.pushups);
						} else if (word.toLowerCase().equals("squad")
								|| word.toLowerCase().equals("squat")
								|| word.toLowerCase().equals("squats")) {
							activeStateUIActivity.activateExercise("squats");
							playMedia(R.raw.squats);

						} else if (word.toLowerCase().equals("sit")
								|| word.toLowerCase().equals("situp")
								|| word.toLowerCase().equals("situps")) {

							activeStateUIActivity.activateExercise("situps");
							playMedia(R.raw.sitpus);
						}
					}
				}

			}

			startVoiceRecognition();

		}

		public void onPartialResults(Bundle partialResults) {
			Log.d(TAG, "onPartialResults");
		}

		public void onEvent(int eventType, Bundle params) {
			Log.d(TAG, "onEvent " + eventType);
		}

	}

	public static void updateVolumeSetting() {
		AudioManager audMgr = (AudioManager) ActiveStateUIActivity.currentContext
				.getSystemService(Context.AUDIO_SERVICE);
		int volume = audMgr.getStreamVolume(AudioManager.STREAM_MUSIC);
		voice.setVolume(volume, volume);
	}

	protected void playMedia(int resourceId) {
		if (voice != null) {
			if (voice.isPlaying())
				voice.stop();
			voice.release();
		}
		voice = MediaPlayer.create(ActiveStateUIActivity.currentContext,
				resourceId);
		updateVolumeSetting();
		voice.start();
	}
}
