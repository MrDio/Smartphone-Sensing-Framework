package edu.croptomato.helper;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.media.Image;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

import edu.croptomato.config.Constants;

/**
 * Helper class for image conversion.
 */
public class ImageConverter {

    /**
     * Converts the given image into a rescaled bitmap.
     *
     * @param image Image to be converted
     * @return Image as bitmap
     */
    public static Bitmap convertImageToBitmap(final Image image) {
        try {
            // Create bitmap from the given image.
            byte[] jpegBytes = getJpegBytesFromImage(image);
            Bitmap bitmap = BitmapFactory.decodeByteArray(jpegBytes, 0, jpegBytes.length);

            // Rescale and return bitmap.
            return rescaleBitmap(bitmap, Constants.INPUT_SIZE);
        } finally {
            // Cleanup.
            image.close();
        }
    }

    /**
     * Gets the JPEG bytes from the given image.
     *
     * @param image Image to be processed
     * @return Byte array with JPEG bytes
     */
    private static byte[] getJpegBytesFromImage(final Image image) {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();

        // First get YUV bytes to create an YUV image.
        byte[] yuvBytes = getYuvBytesFromImage(image);
        YuvImage yuvImage = new YuvImage(yuvBytes, ImageFormat.NV21, image.getWidth(), image.getHeight(), null);
        // Then compress the YUV image to JPEG and return the JPEG bytes.
        yuvImage.compressToJpeg(new Rect(0, 0, image.getWidth(), image.getHeight()), 100, outStream);

        return outStream.toByteArray();
    }

    /**
     * Gets the YUV bytes from the given image.
     *
     * @param image Image to be processed
     * @return Byte array with YUV bytes
     */
    private static byte[] getYuvBytesFromImage(final Image image) {
        // Get the YUV byte buffers from the image.
        ByteBuffer yBuffer = image.getPlanes()[0].getBuffer();
        ByteBuffer uBuffer = image.getPlanes()[1].getBuffer();
        ByteBuffer vBuffer = image.getPlanes()[2].getBuffer();

        // Get the remaining buffer sizes.
        int ySize = yBuffer.remaining();
        int uSize = uBuffer.remaining();
        int vSize = vBuffer.remaining();

        // Copy all buffers into byte array.
        byte[] yuvBytes = new byte[ySize + uSize + vSize];
        yBuffer.get(yuvBytes, 0, ySize);
        vBuffer.get(yuvBytes, ySize, vSize);
        uBuffer.get(yuvBytes, ySize + vSize, uSize);

        return yuvBytes;
    }

    /**
     * Rescales the given bitmap depending on the given size.
     *
     * @param bitmap Bitmap to be scaled
     * @param size   Size for scaling
     * @return Rescaled bitmap
     */
    private static Bitmap rescaleBitmap(final Bitmap bitmap, final int size) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        // Calculate X and Y scale for the matrix.
        float scaleX = ((float) size) / width;
        float scaleY = ((float) size) / height;

        Matrix matrix = new Matrix();
        matrix.postScale(scaleX, scaleY);

        // Create and return rescaled bitmap.
        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
    }
}
