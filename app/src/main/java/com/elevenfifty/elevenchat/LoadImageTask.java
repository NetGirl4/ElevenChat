package com.elevenfifty.elevenchat;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.lang.ref.WeakReference;

/**
 * Created by kathy on 7/13/2015.
 */
public class LoadImageTask extends AsyncTask<String, Void, Drawable> {
    private final WeakReference<ImageView> imageViewWeakReference;

    public LoadImageTask(ImageView imageView) {
        imageViewWeakReference = new WeakReference<>(imageView);

    }

    @Override
    protected Drawable doInBackground(String... params) {
        String imagePath = params[0];
        return Drawable.createFromPath(imagePath);
    }

    @Override
    protected void onPostExecute(Drawable drawable) {
        if (imageViewWeakReference != null && drawable != null) {
            final ImageView imageView = imageViewWeakReference.get();
            if (imageView != null) {
                imageView.setImageDrawable(drawable);
            }
        }
    }
}
