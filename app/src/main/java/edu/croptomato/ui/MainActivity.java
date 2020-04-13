package edu.croptomato.ui;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.SurfaceTexture;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.TextureView.SurfaceTextureListener;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

import edu.croptomato.R;
import edu.croptomato.camera.CameraProcessor;
import edu.croptomato.classifier.Classifier;
import edu.croptomato.classifier.TensorFlowImageClassifier;
import edu.croptomato.config.Constants;

/**
 * Main activity of the application.
 */
public class MainActivity extends Activity {

    private static final String TAG = MainActivity.class.getName();

    private AutoFitTextureView textureView;
    private TextView percentageUnripe;
    private TextView percentageSemiRipe;
    private TextView percentageRipe;
    private ProgressBar progressBarUnripe;
    private ProgressBar progressBarSemiRipe;
    private ProgressBar progressBarRipe;

    private CameraProcessor cameraProcessor;
    private Classifier classifier;

    private final SurfaceTextureListener surfaceTextureListener = new SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture texture, int width, int height) {
            cameraProcessor.tryOpenCamera(width, height);
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture texture, int width, int height) {
        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture texture) {
            return true;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture texture) {
        }
    };

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.textureView = findViewById(R.id.texture);
        this.textureView.setSurfaceTextureListener(surfaceTextureListener);
        this.percentageUnripe = findViewById(R.id.percentageUnripe);
        this.percentageSemiRipe = findViewById(R.id.percentageSemiRipe);
        this.percentageRipe = findViewById(R.id.percentageRipe);
        this.progressBarUnripe = findViewById(R.id.progressBarUnripe);
        this.progressBarSemiRipe = findViewById(R.id.progressBarSemiRipe);
        this.progressBarRipe = findViewById(R.id.progressBarRipe);

        this.cameraProcessor = new CameraProcessor(this);
        this.classifier = loadClassifier();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (cameraProcessor.getSurfaceSize() != null) {
            cameraProcessor.tryOpenCamera();
        } else {
            Log.e(TAG, "Restart failed due to invalid surface.");
            this.finish();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        cameraProcessor.closeCaptureSession();
        cameraProcessor.closeCameraDevice();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (classifier != null) {
            classifier.close();
        }
    }

    @Override
    public void onRequestPermissionsResult(final int requestCode,
                                           final @NonNull String[] permissions,
                                           final @NonNull int[] grantResults) {
        if (permissionGranted(permissions[0], grantResults[0])) {
            cameraProcessor.openCamera();
        } else {
            Toast.makeText(this, "Permission denied!", Toast.LENGTH_LONG).show();
            System.exit(0);
        }
    }

    /**
     * Checks if permissions for the camera were granted.
     *
     * @param permission  Permission
     * @param grantResult Grant result
     * @return True if permissions were granted, false otherwise
     */
    private boolean permissionGranted(final String permission, final int grantResult) {
        return Manifest.permission.CAMERA.equals(permission) && grantResult == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Loads a classifier instance.
     *
     * @return Classifier instance
     */
    private Classifier loadClassifier() {
        return TensorFlowImageClassifier.create(getAssets(), Constants.MODEL_FILE,
                Constants.LABEL_FILE, Constants.INPUT_SIZE, Constants.IMAGE_MEAN,
                Constants.IMAGE_STD, Constants.INPUT_NAME, Constants.OUTPUT_NAME);
    }

    /**
     * Updates the GUI elements with the provided percentage values.
     *
     * @param percentageUnripe   Percentage unripe
     * @param percentageSemiRipe Percentage semi-ripe
     * @param percentageRipe     Percentage ripe
     */
    public void updateGuiElements(final float percentageUnripe, final float percentageSemiRipe,
                                  final float percentageRipe) {
        this.percentageUnripe.setText(getPercentageString(percentageUnripe));
        this.percentageSemiRipe.setText(getPercentageString(percentageSemiRipe));
        this.percentageRipe.setText(getPercentageString(percentageRipe));

        this.progressBarUnripe.setProgress((int) (percentageUnripe * 100.0f));
        this.progressBarSemiRipe.setProgress((int) (percentageSemiRipe * 100.0f));
        this.progressBarRipe.setProgress((int) (percentageRipe * 100.0f));

        this.progressBarUnripe.setMax(100);
        this.progressBarSemiRipe.setMax(100);
        this.progressBarRipe.setMax(100);
    }

    /**
     * Gets the provided percentage value as formatted string.
     *
     * @param percentage Percentage value
     * @return Percentage value as formatted string
     */
    private String getPercentageString(final float percentage) {
        return String.format(Locale.UK, "%.2f%%", percentage * 100.0f);
    }

    /**
     * Gets the texture view.
     *
     * @return Texture view
     */
    public AutoFitTextureView getTextureView() {
        return textureView;
    }

    /**
     * Gets the classifier instance.
     *
     * @return Classifier instance
     */
    public Classifier getClassifier() {
        return classifier;
    }

}