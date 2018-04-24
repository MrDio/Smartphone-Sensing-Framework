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
package edu.example.ssf.mma.hardwareAdapter.microphone;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import edu.example.ssf.mma.hardwareAdapter.IMicrophone;

/**
 * Initialising the Microphone of the Smartphone and get the data output form
 * the sensor.
 *
 * @author D. Lagamtzis
 * @version 2.0
 */
public class microphone extends Activity implements IMicrophone{

    /**
     * setting the variable for responsible for recording to false.
     */
    private Boolean isRecording = true;

    /**
     * Reference to the audio file.
     */
    private File audiofile = null;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_hh_mm_ss");

    protected Double maxAmplitude = 0.0d;
    private byte[] buffer;
    static final int SAMPLE_RATE = 8000;
    private int fftsize = 8192;

    /**
     * create the new media recorder in order to record the audio.
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
    }

    public void runAgain() {

    }

    @Override
    public void start() {
        try {
            File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/RapidDisruption");
            if (!dir.exists()) {
                dir.mkdirs();
            }
            audiofile = new File(dir, "Recording" + sdf.format(new Date(System.currentTimeMillis())) + ".3gpp");
        } catch (Exception e) {
            Log.w("creating file error", e.toString());
        }

        if (!isRecording)
            runAgain();

        try {
            isRecording = true;
            threadCreator().start();


        } catch (IllegalStateException e) {
            // start:it is called before prepare()
            // prepare: it is called after start() or before setOutputFormat()
            e.printStackTrace();
        }
    }

    public Thread threadCreator() {

        return new Thread(new Runnable() {
            @Override
            public void run() {
//                recorder.startRecording();
//
//                int bufferReadResult = recorder.read(buffer, 0,
//                        buffer.length);
//                maxAmplitude = MathCalculations.getDB(bufferReadResult);
//                CurrentTickData.micMaxAmpl = maxAmplitude;

//                AudioDispatcher dispatcher = AudioDispatcherFactory.fromDefaultMicrophone(22050, 1024, 0);
//                PitchDetectionHandler pdh = new PitchDetectionHandler() {
//                    @Override
//                    public void handlePitch(PitchDetectionResult result, AudioEvent e) {
//                        final float pitchInHz = result.getPitch();
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                Log.d("d", pitchInHz + "");
//                            }
//                        });
//                    }
//                };
//                AudioProcessor p = new PitchProcessor(PitchProcessor.PitchEstimationAlgorithm.YIN, 22050, 1024, pdh);
//                dispatcher.addAudioProcessor(p);
//                new Thread(dispatcher, "Audio Dispatcher").start();

//                PipedAudioStream f = new PipedAudioStream(audiofile.getName());
//                TarsosDSPAudioInputStream stream = f.getMonoStream(SAMPLE_RATE, 0);
//                Log.d("d", buffer[0] + buffer[1] + buffer[2] + buffer[3] + "");
//                int overlap = fftsize - 4096;
//                final SpectralPeakProcessor spectralPeakFollower = new SpectralPeakProcessor(fftsize, overlap, SAMPLE_RATE);
//                dispatcher = new AudioDispatcher(stream, fftsize, overlap);
//
//                dispatcher.addAudioProcessor(new AudioProcessor() {
//                    int frameCounter = 0;
//
//                    @Override
//                    public void processingFinished() {
//
//                    }
//
//                    @Override
//                    public boolean process(AudioEvent audioEvent) {
//                        Log.d("d",spectralPeakFollower.getMagnitudes().toString());
//                        Log.d("d",spectralPeakFollower.getFrequencyEstimates().toString());
//
//
//                        return true;
//                    }
//                });
}
            //   if(isRecording){
            //       maxAmplitude = MathCalculations.getDB(mediaRecorder.getMaxAmplitude());
            //       CurrentTickData.micMaxAmpl = maxAmplitude;
            //       Log.d("AmpliMax", CurrentTickData.micMaxAmpl+ "");
            //   }

        });

    }

    @Override
    public void stop() {
        try {
            isRecording = false;
        } catch (IllegalStateException e) {
            //  it is called before start()
            e.printStackTrace();
        } catch (RuntimeException e) {
            // no valid audio/video data has been received
            e.printStackTrace();
        }


    }

    @Override
    public Double getMaxAmplitude() {
        return maxAmplitude;
    }
}