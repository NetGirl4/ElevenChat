package com.elevenfifty.elevenchat;

import android.graphics.Bitmap;

/**
 * Created by kathy on 7/13/2015.
 */
public class ImageHelper {
    final static int MAX_SIZE = 2048;
    public static Bitmap resizeImage(Bitmap image, int targetWidth, int targetHeight) {

        float ratio = (float)image.getWidth() / (float)image.getHeight();
        if (ratio > 1) {
            targetWidth = 2048;
            targetHeight = (int) (1024 / ratio);
        } else {
            targetWidth = (int) (MAX_SIZE / ratio);
            targetHeight = MAX_SIZE;

        }
        return Bitmap.createScaledBitmap(image, targetWidth, targetHeight, true);
    }
}
