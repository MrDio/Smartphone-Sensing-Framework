package edu.example.ssf.mma.hardwareAdapter;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.io.android.AudioDispatcherFactory;
import be.tarsos.dsp.onsets.OnsetHandler;
import be.tarsos.dsp.onsets.PercussionOnsetDetector;


public class ClapDetector {

    public ClapDetector(OnsetHandler claphandler) {
        AudioDispatcher mDispatcher = AudioDispatcherFactory.fromDefaultMicrophone(22050, 1024, 0);
        double threshold = 8;
        double sensitivity = 40;
        PercussionOnsetDetector mPercussionDetector = new PercussionOnsetDetector(22050, 1024,
              claphandler, sensitivity, threshold);
        mDispatcher.addAudioProcessor(mPercussionDetector);
        Thread t = new Thread(mDispatcher);
        t.start();
    }
}
