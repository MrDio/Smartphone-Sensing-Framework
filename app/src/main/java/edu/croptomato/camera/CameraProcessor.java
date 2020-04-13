package edu.croptomato.camera;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.util.Size;
import android.view.Surface;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import edu.croptomato.classifier.ClassifierTask;
import edu.croptomato.config.Constants;
import edu.croptomato.ui.MainActivity;

/**
 * Camera processor.
 */
public class CameraProcessor {

    private static final int PERMISSIONS_MULTIPLE_REQUEST = 123;
    private static final String CAMERA_ID = "0";

    private MainActivity activity;

    private CameraDevice cameraDevice;
    private CameraCaptureSession captureSession;
    private ImageReader previewReader;

    private Size previewSize;
    private Size surfaceSize;

    public CameraProcessor(final MainActivity activity) {
        this.activity = activity;
    }

    private final CameraDevice.StateCallback stateCallback = new CameraDevice.StateCallback() {

        @Override
        public void onOpened(@NonNull CameraDevice cameraDevice) {
            setCameraDevice(cameraDevice);
            createCaptureSession();
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice cameraDevice) {
            cameraDevice.close();
            setCameraDevice(null);
        }

        @Override
        public void onError(@NonNull CameraDevice cameraDevice, int error) {
            cameraDevice.close();
            setCameraDevice(null);
            activity.finish();
        }
    };

    /**
     * Creates a camera capture session.
     */
    private void createCaptureSession() {
        try {
            SurfaceTexture texture = activity.getTextureView().getSurfaceTexture();
            texture.setDefaultBufferSize(previewSize.getWidth(), previewSize.getHeight());
            Surface surface = new Surface(texture);

            final CaptureRequest.Builder captureRequest = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            captureRequest.addTarget(surface);
            captureRequest.addTarget(previewReader.getSurface());

            cameraDevice.createCaptureSession(Arrays.asList(surface, previewReader.getSurface()),
                    new CameraCaptureSession.StateCallback() {
                        @Override
                        public void onConfigured(
                                @NonNull CameraCaptureSession cameraCaptureSession) {
                            if (cameraDevice != null) {
                                captureSession = cameraCaptureSession;
                                try {
                                    captureRequest.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
                                    captureRequest.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
                                    captureRequest.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);

                                    captureSession.setRepeatingRequest(captureRequest.build(), null, null);
                                } catch (CameraAccessException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onConfigureFailed(
                                @NonNull CameraCaptureSession cameraCaptureSession) {
                            Toast.makeText(activity, "Configuration failed!", Toast.LENGTH_SHORT).show();
                        }
                    }, null
            );
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void setUpCameraOutputs(@NonNull CameraManager manager, Size surfaceSize) {
        try {
            StreamConfigurationMap map = manager.getCameraCharacteristics(CAMERA_ID)
                                                .get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);

            Size largest = Collections.max(Arrays.asList(map.getOutputSizes(ImageFormat.YUV_420_888)),
                    new CompareSizesByArea());
            largest = new Size(Math.min(largest.getWidth(), Constants.INPUT_SIZE * 2),
                    Math.min(largest.getHeight(), Constants.INPUT_SIZE * 2));

            previewReader = ImageReader.newInstance(largest.getWidth(), largest.getHeight(), ImageFormat.YUV_420_888, 5);
            previewReader.setOnImageAvailableListener(new ImageReader.OnImageAvailableListener() {
                @Override
                public void onImageAvailable(ImageReader reader) {
                    Image image = reader.acquireNextImage();

                    if (!ClassifierTask.isRunning() && activity.getClassifier() != null) {
                        new ClassifierTask(activity).execute(image);
                    } else {
                        image.close();
                    }
                }
            }, null);

            previewSize = getPreviewSize(map.getOutputSizes(SurfaceTexture.class), surfaceSize, largest);
            activity.getTextureView().setAspectRatio(previewSize.getHeight(), previewSize.getWidth());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Size getPreviewSize(final Size[] choices, final Size surfaceSize,
                                       final Size aspectRatio) {

        List<Size> bigEnough = new ArrayList<>();
        for (Size option : choices) {
            if (option.getHeight() == option.getWidth() * aspectRatio.getHeight() / aspectRatio.getWidth()
                    && option.getWidth() <= surfaceSize.getWidth() && option.getHeight() <= surfaceSize.getHeight()) {
                bigEnough.add(option);
            }
        }

        if (bigEnough.size() > 0) {
            return Collections.min(bigEnough, new CompareSizesByArea());
        } else {
            return choices[0];
        }
    }

    /**
     * Opens the connection to the camera device.
     */
    public void openCamera() {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            CameraManager manager = (CameraManager) activity.getSystemService(Context.CAMERA_SERVICE);
            if (manager != null) {
                try {
                    setUpCameraOutputs(manager, surfaceSize);
                    manager.openCamera(CAMERA_ID, stateCallback, null);
                } catch (CameraAccessException e) {
                    Toast.makeText(activity, e.getMessage(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(activity, "Failed to get CameraManager", Toast.LENGTH_LONG).show();
                Log.e("camera", "Failed to get CameraManager");
            }
        } else {
            Toast.makeText(activity, "Missing permissions to open the camera", Toast.LENGTH_LONG).show();
            Log.e("camera", "Missing permissions to open the camera");
        }
    }

    /**
     * Request camera permissions.
     */
    public void requestCameraPermissions() {
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA},
                PERMISSIONS_MULTIPLE_REQUEST);
    }

    /**
     * Closes the connection to the camera device.
     */
    public void closeCameraDevice() {
        if (cameraDevice != null) {
            cameraDevice.close();
        }
    }

    /**
     * Sets the camera device.
     *
     * @param cameraDevice Camera device
     */
    private void setCameraDevice(final CameraDevice cameraDevice) {
        this.cameraDevice = cameraDevice;
    }

    /**
     * Closes the camera capture session.
     */
    public void closeCaptureSession() {
        if (captureSession != null) {
            captureSession.close();
        }
    }

    /**
     * Gets the size of the surface.
     *
     * @return Surface size
     */
    public Size getSurfaceSize() {
        return surfaceSize;
    }

    /**
     * Sets the size of the surface.
     *
     * @param width  Surface width
     * @param height Surface height
     */
    public void setSurfaceSize(final int width, final int height) {
        this.surfaceSize = new Size(width, height);
    }

    /**
     * Comparator implementation.
     */
    public static class CompareSizesByArea implements Comparator<Size> {
        @Override
        public int compare(final Size lhs, final Size rhs) {
            return Long.signum((long) lhs.getWidth() * lhs.getHeight() - (long) rhs.getWidth() * rhs.getHeight());
        }
    }

}
