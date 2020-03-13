package edu.example.ssf.mma.userInterface;

import android.app.AlertDialog;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.VideoView;

import java.util.List;

import edu.example.ssf.mma.R;
import edu.example.ssf.mma.data.DataList;
import edu.example.ssf.mma.data.CurrentTickData;
import edu.example.ssf.mma.dataAnalyzer.DataAnalyzer;
import edu.example.ssf.mma.hardwareAdapter.HardwareFactory;
import edu.example.ssf.mma.timer.StateMachineHandler;

/**
 * Created by MatzeG on 09.04.2018.
 */

public class RecognizeActivity extends AppCompatActivity {

    // Init HW-Factory
    HardwareFactory hw;
    /**
     * Declaration of the state machine.
     */
    private StateMachineHandler stateMachineHandler;

    private Button startButton;
    private TextView textView;

    private List<Double> dataList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recognize);
        final String value = getIntent().getStringExtra("clickedItem");

        hw = new HardwareFactory(this);
        stateMachineHandler = new StateMachineHandler(RecognizeActivity.this);

        textView = findViewById(R.id.txt_recognize);
        startButton = findViewById(R.id.bt_start);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.setText("");
                startButton.setEnabled(false);
                startButton.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        playSound();
                        HardwareFactory.hwAcc.start();
                        stateMachineHandler.startStateMachine();
                        DataList.createDatalis();
                    }
                }, 1000);
                startButton.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        playSound();
                        CurrentTickData.resetValues();
                        dataList = DataList.getDatalist();
                        stateMachineHandler.stopStateMachine();
                        HardwareFactory.hwAcc.stop();
                        startButton.setEnabled(true);
                        DataAnalyzer dataAnalyzer = new DataAnalyzer();
                        textView.setText(dataAnalyzer.compareData(dataList, value));
                    }
                }, 5000);
            }
        });
        playAnimation(value);
    }

    private void playSound() {
        ToneGenerator toneGenerator = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
        toneGenerator.startTone(ToneGenerator.TONE_CDMA_PIP, 150);
    }

    private Uri getUri(int value) {
        return Uri.parse("android.resource://" + getPackageName() + "/" + value);
    }

    private void playAnimation(String value) {
        if (value != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(RecognizeActivity.this);
            VideoView view = new VideoView(RecognizeActivity.this);
            builder.setTitle("How to write the letter?");
            switch (value) {
                case "A":
                    view.setVideoURI(getUri(R.raw.a_animation));
                    break;
                case "B":
                    view.setVideoURI(getUri(R.raw.b_animation));
                    break;
                case "C":
                    view.setVideoURI(getUri(R.raw.c_animation));
                    break;
                case "O":
                    view.setVideoURI(getUri(R.raw.o_animation));
                    break;
            }
            builder.setView(view);
            builder.setNeutralButton("OK", null);
            builder.create().show();
            view.start();
        }
    }
}
