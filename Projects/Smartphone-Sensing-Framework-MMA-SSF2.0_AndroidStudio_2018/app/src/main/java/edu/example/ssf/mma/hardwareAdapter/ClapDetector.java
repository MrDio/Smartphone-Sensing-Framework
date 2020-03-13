package edu.example.ssf.mma.hardwareAdapter;

import android.util.Log;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.AudioProcessor;
import be.tarsos.dsp.io.android.AudioDispatcherFactory;
import be.tarsos.dsp.onsets.OnsetDetector;
import be.tarsos.dsp.onsets.OnsetHandler;
import be.tarsos.dsp.util.fft.FFT;


public class ClapDetector implements AudioProcessor, OnsetDetector {

    private final int lowPassFilter;
    private final int highPassFilter;
    private float sampleRate;
    private OnsetHandler handler;
    private float[] currentMagnitudes;
    private float[] priorMagnitudes;
    private double sensitivity;
    private FFT fft = null;
    private double threshold = 0;
    private long processedSamples;
    private float peakCounter1;
    private float peakCounter2;

    public ClapDetector(OnsetHandler claphandler) {
        AudioDispatcher mDispatcher = AudioDispatcherFactory.fromDefaultMicrophone(22050, 1024, 0);
        this.lowPassFilter = 0;
        this.highPassFilter = 22000;
        this.threshold = 8;
        this.sensitivity = 40;
        this.sampleRate = 22050;
        this.handler = claphandler;

        setupFftAndBuffers(1024);

        mDispatcher.addAudioProcessor(this);
        Thread t = new Thread(mDispatcher);
        t.start();
    }

    private void setupFftAndBuffers(int bufferSize) {
        this.fft = new FFT(bufferSize / 2);
        this.priorMagnitudes = new float[bufferSize / 2];
        this.currentMagnitudes = new float[bufferSize / 2];
    }

    private int toHz(int indexInBuffer){
        return (int) fft.binToHz(indexInBuffer, this.sampleRate);
    }

    private boolean isInDefinedFrequenceSpectrum(int hz){
        if(hz > lowPassFilter && hz < highPassFilter)
            return true;
        return false;
    }

    public boolean process(AudioEvent audioEvent) {
        float[] audioInputFloats = audioEvent.getFloatBuffer();
        this.processedSamples += (long) audioInputFloats.length;
        this.processedSamples -= (long) audioEvent.getOverlap();
        this.fft.forwardTransform(audioInputFloats);
        this.fft.modulus(audioInputFloats, this.currentMagnitudes);

        int peakCounter = 0;
        for (int i = 0; i < this.currentMagnitudes.length; ++i) {
            if (this.priorMagnitudes[i] > 0.0F) {
                int hz = toHz(i);
                if(! isInDefinedFrequenceSpectrum(hz)) continue;
                double decibel = 10.0D * Math.log10((double) (this.currentMagnitudes[i] / this.priorMagnitudes[i]));

                if (decibel >= this.threshold) {
                   //Log.d("Sound", "Frequ: " + hz + " decibel:" + decibel);
                    ++peakCounter;
                }
            }
            this.priorMagnitudes[i] = this.currentMagnitudes[i];
        }

        //Log.d("Sound", "Peakcounter: " + peakCounter);

        double peakCountThreshold = (100.0D - this.sensitivity) * (double) audioInputFloats.length / 200.0D;
        if (this.peakCounter2 < this.peakCounter1
                && this.peakCounter1 >= (float) peakCounter
                && (double) this.peakCounter1 > peakCountThreshold) {
            float time = (float) this.processedSamples / this.sampleRate;
            this.handler.handleOnset((double) time, -1.0D);
        }

        this.peakCounter2 = this.peakCounter1;
        this.peakCounter1 = (float) peakCounter;
        return true;
    }

    public void processingFinished() {
    }

    public void setHandler(OnsetHandler var1) {
        this.handler = var1;
    }

}
