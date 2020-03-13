package edu.example.ssf.mma.imagedetection;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.graphics.YuvImage;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import edu.example.ssf.mma.R;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class ImageDetection extends Activity implements View.OnClickListener {


    private static final int INPUT_SIZE = 224;
    private static final int IMAGE_MEAN = 117;
    private static final float IMAGE_STD = 1;
    private static final String INPUT_NAME = "input";
    private static final String OUTPUT_NAME = "output";
    private static final String MODEL_FILE = "file:///android_asset/tensorflow_inception_graph.pb";
    private static final String LABEL_FILE = "file:///android_asset/imagenet_comp_graph_label_strings.txt";

    private Classifier classifier;

    private TextView textView;

    private boolean isRunning = false;

    //------------------------------------------------------------

    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();
    private static final String TAG = "ImageDetection";

    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }

    private AutoFitTextureView textureView;

    private String mCameraId = "0";

    private CameraDevice cameraDevice;

    private Size previewSize;
    private CaptureRequest.Builder previewRequestBuilder;

    private CaptureRequest previewRequest;

    private CameraCaptureSession captureSession;
    private ImageReader imageReader;
    private ImageReader previewReader;
    private final TextureView.SurfaceTextureListener mSurfaceTextureListener
            = new TextureView.SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture texture
                , int width, int height) {

            openCamera(width, height);
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture texture
                , int width, int height) {
        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture texture) {
            return true;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture texture) {
        }
    };
    private final CameraDevice.StateCallback stateCallback = new CameraDevice.StateCallback() {

        @Override
        public void onOpened(CameraDevice cameraDevice) {
            ImageDetection.this.cameraDevice = cameraDevice;

            createCameraPreviewSession();
        }


        @Override
        public void onDisconnected(CameraDevice cameraDevice) {
            cameraDevice.close();
            ImageDetection.this.cameraDevice = null;
        }


        @Override
        public void onError(CameraDevice cameraDevice, int error) {
            cameraDevice.close();
            ImageDetection.this.cameraDevice = null;
            ImageDetection.this.finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
          setContentView(R.layout.activity_imagedetection);
        if (Build.VERSION.SDK_INT>22){
            if (ContextCompat.checkSelfPermission(ImageDetection.this,
                    Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(ImageDetection.this,
                        new String[]{Manifest.permission.CAMERA},1);

            }
        }
        if (classifier == null) {
            classifier = TensorFlowImageClassifier.create(ImageDetection.this.getAssets(),
                    MODEL_FILE, LABEL_FILE, INPUT_SIZE, IMAGE_MEAN, IMAGE_STD, INPUT_NAME, OUTPUT_NAME);

        }
        //=================================
        textView =  findViewById(R.id.textview);
        textureView =  findViewById(R.id.texture);

        textureView.setSurfaceTextureListener(mSurfaceTextureListener);
        findViewById(R.id.capture).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        captureStillPicture();
    }

    private void captureStillPicture() {
        try {
            if (cameraDevice == null) {
                return;
            }

            final CaptureRequest.Builder captureRequestBuilder =
                    cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);

            captureRequestBuilder.addTarget(imageReader.getSurface());

            captureRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE,
                    CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);

            captureRequestBuilder.set(CaptureRequest.CONTROL_AE_MODE,
                    CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);

            int rotation = getWindowManager().getDefaultDisplay().getRotation();

            captureRequestBuilder.set(CaptureRequest.JPEG_ORIENTATION
                    , ORIENTATIONS.get(rotation));

            captureSession.stopRepeating();

            captureSession.capture(captureRequestBuilder.build()
                    , new CameraCaptureSession.CaptureCallback()
                    {

                        @Override
                        public void onCaptureCompleted(CameraCaptureSession session
                                , CaptureRequest request, TotalCaptureResult result) {
                            try {

                                previewRequestBuilder.set(CaptureRequest.CONTROL_AF_TRIGGER,
                                        CameraMetadata.CONTROL_AF_TRIGGER_CANCEL);

                                previewRequestBuilder.set(CaptureRequest.CONTROL_AE_MODE,
                                        CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);

                                captureSession.setRepeatingRequest(previewRequest, null,
                                        null);
                            } catch (CameraAccessException e) {
                                e.printStackTrace();
                            }
                        }
                    }, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }


    private void openCamera(int width, int height) {
        setUpCameraOutputs(width, height);
        CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            manager.openCamera(mCameraId, stateCallback, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void createCameraPreviewSession() {
        try {
            SurfaceTexture texture = textureView.getSurfaceTexture();
            texture.setDefaultBufferSize(previewSize.getWidth(), previewSize.getHeight());
            Surface surface = new Surface(texture);

            previewRequestBuilder = cameraDevice
                    .createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);

            previewRequestBuilder.addTarget(surface);
            previewRequestBuilder.addTarget(previewReader.getSurface());


            cameraDevice.createCaptureSession(Arrays.asList(surface
                    , imageReader.getSurface(), previewReader.getSurface()), new CameraCaptureSession.StateCallback()
                    {
                        @Override
                        public void onConfigured(CameraCaptureSession cameraCaptureSession) {

                            if (null == cameraDevice) {
                                return;
                            }


                            captureSession = cameraCaptureSession;
                            try {

                                previewRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE,
                                        CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);

                                previewRequestBuilder.set(CaptureRequest.CONTROL_AE_MODE,
                                        CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);

                                previewRequest = previewRequestBuilder.build();

                                captureSession.setRepeatingRequest(previewRequest,null, null);
                            } catch (CameraAccessException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onConfigureFailed(CameraCaptureSession cameraCaptureSession) {
                            Toast.makeText(ImageDetection.this, "Config Failed!"
                                    , Toast.LENGTH_SHORT).show();
                        }
                    }, null
            );
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void setUpCameraOutputs(int width, int height) {
        CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {

            CameraCharacteristics characteristics
                    = manager.getCameraCharacteristics(mCameraId);

            StreamConfigurationMap map = characteristics.get(
                    CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);


            Size largest = Collections.max(
                    Arrays.asList(map.getOutputSizes(ImageFormat.JPEG)),
                    new CompareSizesByArea());
            previewReader = ImageReader.newInstance(largest.getWidth(), largest.getHeight(),
                    ImageFormat.YUV_420_888, 2);
            previewReader.setOnImageAvailableListener(new ImageReader.OnImageAvailableListener() {
                @Override
                public void onImageAvailable(ImageReader reader) {
                    Image image = reader.acquireLatestImage();

                    if(!isRunning){
                        new MyTask().execute(image);
                    }else{
                        image.close();
                    }

                }
            }, null);

            imageReader = ImageReader.newInstance(largest.getWidth(), largest.getHeight(),
                    ImageFormat.JPEG, 2);
            imageReader.setOnImageAvailableListener(
                    new ImageReader.OnImageAvailableListener() {

                        @Override
                        public void onImageAvailable(ImageReader reader) {

                            Image image = reader.acquireNextImage();
                            ByteBuffer buffer = image.getPlanes()[0].getBuffer();
                            byte[] bytes = new byte[buffer.remaining()];

                            File file = new File(getExternalFilesDir(null), "pic.jpg");
                            buffer.get(bytes);
                            try (
                                    FileOutputStream output = new FileOutputStream(file)) {
                                output.write(bytes);
                                Toast.makeText(ImageDetection.this, "Path: " + file, Toast.LENGTH_LONG).show();
                            } catch (Exception e) {
                                e.printStackTrace();
                            } finally {
                                image.close();
                            }
                        }
                    }, null);


            previewSize = chooseOptimalSize(map.getOutputSizes(
                    SurfaceTexture.class), width, height, largest);

            int orientation = getResources().getConfiguration().orientation;
            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                textureView.setAspectRatio(
                        previewSize.getWidth(), previewSize.getHeight());
            } else {
                textureView.setAspectRatio(
                        previewSize.getHeight(), previewSize.getWidth());
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            Log.d(TAG, "NullPointer");
        }
    }

    private static Size chooseOptimalSize(Size[] choices
            , int width, int height, Size aspectRatio) {

        List<Size> bigEnough = new ArrayList<>();
        int w = aspectRatio.getWidth();
        int h = aspectRatio.getHeight();
        for (Size option : choices) {
            if (option.getHeight() == option.getWidth() * h / w &&
                    option.getWidth() >= width && option.getHeight() >= height) {
                bigEnough.add(option);
            }
        }

        if (bigEnough.size() > 0) {
            return Collections.min(bigEnough, new CompareSizesByArea());
        } else {
            System.out.println("Size Error !!!");
            return choices[0];
        }
    }


    static class CompareSizesByArea implements Comparator<Size> {
        @Override
        public int compare(Size lhs, Size rhs) {

            return Long.signum((long) lhs.getWidth() * lhs.getHeight() -
                    (long) rhs.getWidth() * rhs.getHeight());
        }
    }


    class MyTask extends AsyncTask<Image, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            isRunning = true;
        }

        @Override
        protected String doInBackground(Image... images) {
            Image image = images[0];
            final YuvImage yuvImage = new YuvImage(ImageUtil.getDataFromImage(image, ImageUtil.COLOR_FormatNV21), ImageFormat.NV21, image.getWidth(),image.getHeight(), null);
            ByteArrayOutputStream outBitmap = new ByteArrayOutputStream();

            yuvImage.compressToJpeg(new Rect(0, 0,image.getWidth(), image.getHeight()), 95, outBitmap);
            byte[] bytes = outBitmap.toByteArray();

            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, null);

            Bitmap croppedBitmap = null;
            try {
                croppedBitmap = ImageUtil.getScaleBitmap(bitmap, INPUT_SIZE);
                croppedBitmap = ImageUtil.rotateBimap(90, croppedBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

            final List<Classifier.Recognition> results = classifier.recognizeImage(croppedBitmap);

            image.close();
            return results.toString();
        }

        @Override
        protected void onPostExecute(String string) {
            super.onPostExecute(string);
            textView.setText(string);
            isRunning = false;
        }
    }

}