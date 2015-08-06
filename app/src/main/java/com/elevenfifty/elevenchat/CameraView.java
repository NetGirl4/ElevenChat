package com.elevenfifty.elevenchat;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by kathy on 7/13/2015.
 */
public class CameraView extends RelativeLayout {
    public final static String APP_NAME = "ElevenChat";
    public final static String IMAGE_NAME = "IMG.jpg";
    private FrameLayout previewLayout;
    private Camera camera;
    private CameraPreview preview;
    private int cameraIndex;
    private static final String TAG = "CameraView";

    public CameraView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        cameraIndex = Camera.getNumberOfCameras() - 1;
        previewLayout = (FrameLayout)findViewById(R.id.camera_preview);
        startCamera();

        Button switchCameraButton = (Button) findViewById(R.id.switch_camera_button);
        switchCameraButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                switchCamera();
            }
        });

        Button takePictureButton = (Button) findViewById(R.id.take_picture_button);
        takePictureButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                camera.takePicture(null, null, picture);
            }
        });
    }

    @Override
    protected void onDetachedFromWindow() {
        try {
            camera.release();
        } catch (Exception e) {
            Log.d(TAG,"Error releasing camera: " + e.getMessage());
        }
        super.onDetachedFromWindow();
    }

    private static Camera getCameraInstance(int index) {
        Camera cam = null;

        try {
            cam = Camera.open(index);
        } catch (Exception e) {
            Log.d(TAG,"Error getting camera " + e.getMessage());
        }
        return cam;
    }

    public void startCamera() {
        try {
            if (camera == null) {
                camera = getCameraInstance(cameraIndex);
            }
            preview = new CameraPreview(getContext(), camera);
            previewLayout.addView(preview);
            camera.startPreview();
        } catch (Exception e) {
            Log.d(TAG,"Error starting camera in StartCamera");
        }
    }

    public void stopCamera() {
        try {
            camera.stopPreview();
            previewLayout.removeView(preview);
            camera.release();
            camera = null;
        } catch (Exception e) {
            Log.d(TAG,"Error stopping camera in StopCamera: "
                    + e.getMessage());
        }
    }

    void switchCamera() {
        try {
            cameraIndex = (cameraIndex + 1) % Camera.getNumberOfCameras();
            camera.stopPreview();
            camera.release();
            previewLayout.removeView(preview);
            camera = getCameraInstance(cameraIndex);
            preview = new CameraPreview(getContext(), camera);
            previewLayout.addView(preview);
            camera.startPreview();
        } catch (Exception e) {
            Log.d(TAG,"Error switching camera: " + e.getMessage());
        }
    }

    private final Camera.PictureCallback picture = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            File pictureFile = new File(getContext().getCacheDir() + File.separator +
                    IMAGE_NAME);
            if (pictureFile == null){
                Log.d(TAG, "Error creating media file");
                return;
            }

            try {
                Bitmap image = BitmapFactory.decodeByteArray(data, 0, data.length);
                Matrix matrix = new Matrix();
                if (cameraIndex == 0) {
                    matrix.postRotate(90);
                } else {
                    matrix.postRotate(-90);
                }
                Bitmap rotatedImage = Bitmap.createBitmap(image, 0, 0, image.getWidth(), image.getHeight(), matrix, true);
                FileOutputStream fos = new FileOutputStream(pictureFile);
                    if (rotatedImage.getWidth() > 2048 || rotatedImage.getHeight() > 2048) {
                        Bitmap resizedImage = ImageHelper.resizeImage(rotatedImage, 2048, 2048);
                        resizedImage.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                    }
                rotatedImage.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.close();

                try {
                    camera.stopPreview();
                } catch (Exception ignored) {

                }

                Intent intent = new Intent(getContext(), ConfirmImageActivity.class);
                getContext().startActivity(intent);

                // start new activity here
            } catch (FileNotFoundException e) {
                Log.d(TAG, "File not found: " + e.getMessage());
            } catch (IOException e) {
                Log.d(TAG, "Error accessing file: " + e.getMessage());
            }
        }
    };
}

class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
    private Camera camera;
    private static final String TAG = "CameraPreview";

    public CameraPreview(Context context) {
        super(context);
    }

    public CameraPreview(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CameraPreview(Context context, Camera camera) {
        super(context);
        this.camera = camera;

        SurfaceHolder holder = getHolder();
        holder.addCallback(this);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        try {
            camera.stopPreview();
        } catch (Exception e) {
            Log.d(TAG,"error stopping camera preview: " + e.getMessage());
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            camera.setPreviewDisplay(holder);
            camera.startPreview();
        } catch (IOException e) {
            Log.d(TAG,"Error setting camera preview: " + e.getMessage());
        } catch (Exception e) {
            Log.d(TAG,"Exception setting camera preview: " + e.getMessage());
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if (holder.getSurface() == null) {
            return;
        }

        try {
            camera.stopPreview();
        } catch (Exception e) {
            Log.d(TAG,"Error re-stopping camera preview: " + e.getMessage());
        }
        try {
            camera.setDisplayOrientation(90);
            camera.setPreviewDisplay(holder);
            camera.startPreview();
            Log.d(TAG, "restarted camera preview");
        } catch (Exception e) {
            Log.d(TAG,"Error restarting camera preview: " + e.getMessage());
        }
    }
}