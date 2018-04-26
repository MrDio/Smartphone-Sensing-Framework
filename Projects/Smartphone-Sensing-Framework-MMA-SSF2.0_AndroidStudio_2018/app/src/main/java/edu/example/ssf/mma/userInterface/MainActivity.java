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

package edu.example.ssf.mma.userInterface;

/*
  This class provides the Main Activity of the application
  Its only activ responsibility is the management of the UI.
  Everything else is a passiv responsibility and is management by typical OOP or callbacks

  @author D. Lagamtzis
 * @version 2.0
 */

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.AudioProcessor;
import be.tarsos.dsp.io.android.AudioDispatcherFactory;
import be.tarsos.dsp.util.fft.FFT;
import edu.example.ssf.mma.R;
import edu.example.ssf.mma.Tracker.Trackings;
import edu.example.ssf.mma.data.CsvFileWriter;
import edu.example.ssf.mma.statemachine.IStateMachine;
import edu.example.ssf.mma.statemachine.StateMachine;

public class MainActivity extends AppCompatActivity {

    //Permissions Android
    public static final int PERMISSIONS_MULTIPLE_REQUEST = 123;


    private TextView textViewLogs;
    private Button startTimerButton;
    private static int sample_rate = 22050;
    private static AudioDispatcher audioCsvDispatcher;
    private Button recordtoCsvButton;
    private Button clearLogButton;
    private boolean isRecordingToCsv = false;
    private int bufferSizeMic = sample_rate * 3;
    private int bufferSizeFFT = sample_rate;
    private IStateMachine stateMachine;
    private ScrollView scrollViewLog;


    @SuppressLint("RestrictedApi")
    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //GoogleAccessor.actvityResultHanlder(requestCode, resultCode, data);
    }


    @SuppressLint("RestrictedApi")
    @Override
    protected void onStart() {
        super.onStart();
    }

    private void inAppLog(final String info) {
        runOnUiThread(() -> {
            CharSequence text = textViewLogs.getText();

            textViewLogs.setText(String.format("%s%s\n", text, info));
            scrollViewLog.smoothScrollTo(0, 99999);


        });
    }

    private void onTrackingActivityStopped(long delta) {

        runOnUiThread(() -> {
            startTimerButton.setText("Clap to start\nActivity");
            startTimerButton.setBackgroundColor(Color.parseColor("#FF00CD90"));
            int seconds = (int) delta / 1000;
            int hours = seconds / 3600;
            int minutes = (seconds % 3600) / 60;
            seconds = (seconds % 3600) % 60;
            String info = "Time tracked - hrs: " + hours + " min: " + minutes + " sec: " + seconds;
            inAppLog(info);
        });
    }


    private void record10SecondsFFTtoCsv() {
        if (isRecordingToCsv) return;
        try {
            isRecordingToCsv = true;
            recordtoCsvButton.setText("Recording ...");

            CsvFileWriter.crtFile();

            audioCsvDispatcher.addAudioProcessor(new AudioProcessor() {
                FFT fft = new FFT(bufferSizeFFT);
                final float[] amplitudes = new float[bufferSizeFFT];

                @Override
                public boolean process(AudioEvent audioEvent) {
                    float[] audioBuffer = audioEvent.getFloatBuffer();
                    fft.forwardTransform(audioBuffer);
                    fft.modulus(audioBuffer, amplitudes);

                    // log and display feedback in UI
                    Log.d("d", "got audioBuffer");
                    runOnUiThread(() -> recordtoCsvButton.setText("Writting ... "));

                    CsvFileWriter.writeLine(fft, amplitudes, sample_rate);
                    CsvFileWriter.closeFile();

                    // reset text of button
                    runOnUiThread(() -> {
                        isRecordingToCsv = false; // only UI thread accesses that boolean
                        recordtoCsvButton.setText("record to csv");
                    });

                    // causes that the audioprocessor will not get called again by the audiodispatcher
                    audioCsvDispatcher.removeAudioProcessor(this);
                    return true;
                }

                @Override
                public void processingFinished() {

                }
            });

        } catch (Exception ex) {
            isRecordingToCsv = false;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE},
                PERMISSIONS_MULTIPLE_REQUEST);


        initView();


//       setupAudioToCsv();
//       GoogleAccessor = new GoogleAccessor(this);
//       GoogleAccessor.signIn();

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[]grantResults) {
        switch (requestCode) {
            case PERMISSIONS_MULTIPLE_REQUEST:
                //
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Trackings.onNewDelta = this::onTrackingActivityStopped;
                    Trackings.onStartNewTracking = this::onActivityStarted;

                    this.stateMachine = new StateMachine();
                    this.stateMachine.setLogger(this::inAppLog);
                } else {
                    // Do Nothing
                }
        }
    }

    private void initView() {
        textViewLogs = findViewById(R.id.textviewLogs);
        textViewLogs.setMovementMethod(new ScrollingMovementMethod());

        this.startTimerButton = findViewById(R.id.startButton2);
        scrollViewLog = findViewById(R.id.scrollViewLog);


        recordtoCsvButton = findViewById(R.id.recordToCsvButton);
        recordtoCsvButton.setOnClickListener(view -> record10SecondsFFTtoCsv());

        clearLogButton = findViewById(R.id.clearLogButton);
        clearLogButton.setOnClickListener(view -> clearLogs());
    }

    private void clearLogs() {
        textViewLogs.scrollTo(0, 0);
        textViewLogs.setText("");
    }

    private void setupAudioToCsv() {
        audioCsvDispatcher = AudioDispatcherFactory.fromDefaultMicrophone(sample_rate, bufferSizeMic, 0);
        Thread tCsv = new Thread(audioCsvDispatcher);
        tCsv.start();
    }

    private void onActivityStarted() {
        runOnUiThread(() -> {
            startTimerButton.setText("End\nTracking");
            startTimerButton.setBackgroundColor(Color.parseColor("#ff00ff"));
        });

    }

}
