package com.example.ldg8fe.rapiddisruption.hardwareAdapter.microphone;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.TextView;

import com.example.ldg8fe.rapiddisruption.data.CurrentTickData;
import com.example.ldg8fe.rapiddisruption.data.MathCalculations;
import com.example.ldg8fe.rapiddisruption.hardwareAdapter.IMicrophone;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.media.MediaRecorder;



public class microphone extends Activity implements IMicrophone {

    /** Reference to the media recorder. */
    private MediaRecorder mediaRecorder = null;

    /** setting the variable for responsible for recording to false. */
    private Boolean isRecording = true;

    /** Reference to the audio file. */
    private File audiofile = null;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_hh_mm_ss");

    /**
     * 	create the new media recorder in order to record the audio.
     */
    public microphone(Context context) {
        initMicrophone(context);
    }



    /**
     * Instantiate the microphone and create a new sound-file,  in order to record the audio into a file and save it on the smartphone.
     */

    public void initMicrophone(Context context) {

            if ((ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) ||
                    (ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED)) {

                Log.e("mic", "error");
            }
            mediaRecorder = new MediaRecorder();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

    }

    public void runAgain(){
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
    }

    @Override
    public void start() {
        try {
            File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/RapidDisruption");
            if (!dir.exists()) {
                dir.mkdirs();
            }
            audiofile = new File( dir,"Recording" + sdf.format( new Date(System.currentTimeMillis())) + ".3gpp");
        }
        catch(Exception e){
            Log.w("creating file error", e.toString());
        }

        if (!isRecording)
            runAgain();

        mediaRecorder.setOutputFile(audiofile.getAbsolutePath());
        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
            isRecording = true;
            threadCreator().start();


        } catch (IllegalStateException e) {
            // start:it is called before prepare()
            // prepare: it is called after start() or before setOutputFormat()
            e.printStackTrace();
        } catch (IOException e) {
            // prepare() fails
            e.printStackTrace();
        }

    }
    public Thread threadCreator(){
       return  new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(isRecording){
                                CurrentTickData.micMaxAmpl = MathCalculations.getDB(mediaRecorder.getMaxAmplitude());
                                Log.d("AmpliMax", CurrentTickData.micMaxAmpl+ "");
                            }
                        }
                    });
                    try {
                        Thread.sleep(1000);
                    } catch (Exception e) {

                    }
                }
            }
        });

    }

    @Override
    public void stop() {
        try {
            isRecording = false;
            mediaRecorder.stop();
            mediaRecorder.release();

            //mediaRecorder  = null;

            // t.cancel(true);
        } catch (IllegalStateException e) {
            //  it is called before start()
            e.printStackTrace();
        } catch (RuntimeException e) {
            // no valid audio/video data has been received
            e.printStackTrace();
        }


    }

    @Override
    public void micUI(int v1, TextView... tvs) {
        /** TODO with function getMaxAmplitude
         *
         * */
        tvs[0].setText("Max Amplitude : " + String.format("%.2f", v1));
    }

    @Override
    public Double getMaxAmplitude() {
        return null;
    }

}