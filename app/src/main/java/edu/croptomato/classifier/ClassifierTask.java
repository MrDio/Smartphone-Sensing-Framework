package edu.croptomato.classifier;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.AsyncTask;

import java.util.List;

import edu.croptomato.classifier.Classifier.Recognition;
import edu.croptomato.ui.MainActivity;
import edu.croptomato.helper.ImageConverter;

/**
 * Classifier task to be executed asynchronously.
 */
public class ClassifierTask extends AsyncTask<Image, Void, List<Recognition>> {

    private static boolean running = false;

    @SuppressLint("StaticFieldLeak")
    private MainActivity activity;

    public ClassifierTask(final MainActivity activity) {
        this.activity = activity;
    }

    @Override
    protected void onPreExecute() {
        running = true;
        super.onPreExecute();
    }

    @Override
    protected List<Recognition> doInBackground(final Image... images) {
        // Convert image to bitmap because the provided classifier requires bitmap input.
        Bitmap convertedBitmap = ImageConverter.convertImageToBitmap(images[0]);
        return activity.getClassifier().recognizeImage(convertedBitmap);
    }

    @Override
    protected void onPostExecute(final List<Recognition> recognitions) {
        super.onPostExecute(recognitions);

        float percentageUnripe = 0.0f;
        float percentageSemiRipe = 0.0f;
        float percentageRipe = 0.0f;

        // Process all recognitions and their confidence values.
        for (Recognition recognition : recognitions) {
            switch (recognition.getTitle()) {
                case "unripe":
                    percentageUnripe = recognition.getConfidence();
                    break;
                case "halfripe":
                    percentageSemiRipe = recognition.getConfidence();
                    break;
                case "ripe":
                    percentageRipe = recognition.getConfidence();
                    break;
            }
        }

        // Update the GUI main activity.
        activity.updateGuiElements(percentageUnripe, percentageSemiRipe, percentageRipe);
        running = false;
    }

    public static boolean isRunning() {
        return running;
    }

}